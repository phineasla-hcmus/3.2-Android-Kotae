package com.ogif.kotae.ui.main;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.ogif.kotae.R;
import com.ogif.kotae.adapters.SearchRecyclerViewAdapter;
import com.ogif.kotae.adapters.model.SearchItem;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivitySearchBinding;
import com.ogif.kotae.utils.AnimationUtils;
import com.ogif.kotae.utils.DataProvider;
import com.ogif.kotae.utils.HeaderedRecyclerViewListener;
import com.ogif.kotae.utils.VerticalSpacingItemDecorator;
import com.paulrybitskyi.commons.ktx.NumberUtils;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;
import com.paulrybitskyi.persistentsearchview.utils.ViewUtils;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding binding;
    private DataProvider dataProvider = new DataProvider();
    private List<SearchItem> items = new ArrayList<>();
    private PersistentSearchView persistentSearchView;
    private OnSearchConfirmedListener mOnSearchConfirmedListener;
    private OnSearchQueryChangeListener mOnSearchQueryChangeListener;
    private OnSuggestionChangeListener mOnSuggestionChangeListener;
    private SearchRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        adapter = new SearchRecyclerViewAdapter(this, items);

//        onRestoreState(savedInstanceState)
        init();
    }

    private void init() {
        persistentSearchView = binding.persistentSearchView;

        initProgressBar();
        initSearchView();
        initEmptyView();
        initRecyclerView();
    }

    private void initProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void initSearchView() {
        persistentSearchView.setOnLeftBtnClickListener(this);
        persistentSearchView.setOnClearInputBtnClickListener(this);
        persistentSearchView.setOnRightBtnClickListener(this);
        persistentSearchView.showRightButton();
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));
        this.mOnSearchConfirmedListener = new OnSearchConfirmedListener() {
            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                Toast.makeText(SearchActivity.this, "Search confirmed", Toast.LENGTH_SHORT).show();
                SearchActivity.this.saveSearchQueryIfNecessary(query);
                searchView.collapse();

                SearchActivity.this.performSearch(query);
            }
        };
        persistentSearchView.setOnSearchConfirmedListener(this.mOnSearchConfirmedListener);
        this.mOnSearchQueryChangeListener = new OnSearchQueryChangeListener() {
            @Override
            public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
                if (newQuery.isEmpty()) {
                    setSuggestions(dataProvider.getInitialSearchQueries(), true);
                } else {
                    setSuggestions(dataProvider.getSuggestionsForQuery(newQuery), true);
                }
            }
        };
        persistentSearchView.setOnSearchQueryChangeListener(this.mOnSearchQueryChangeListener);
        this.mOnSuggestionChangeListener = new OnSuggestionChangeListener() {
            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                String query = suggestion.getItemModel().getText();
                saveSearchQueryIfNecessary(query);
                setSuggestions(dataProvider.getSuggestionsForQuery(query), false);
                performSearch(query);
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
                dataProvider.removeSearchQuery(suggestion.getItemModel().getText());
            }
        };
        persistentSearchView.setOnSuggestionChangeListener(this.mOnSuggestionChangeListener);
        persistentSearchView.setDismissOnTouchOutside(true);
        persistentSearchView.setDimBackground(true);
        persistentSearchView.setProgressBarEnabled(true);
        persistentSearchView.setVoiceInputButtonEnabled(true);
        persistentSearchView.setClearInputButtonEnabled(true);
        persistentSearchView.setQueryInputGravity(Gravity.START | Gravity.CENTER);
    }

    private void setSuggestions(List<String> queries, boolean expandIfNecessary) {
        List<SuggestionItem> suggestions = SuggestionCreationUtil.asRecentSearchSuggestions(queries);
        persistentSearchView.setSuggestions(suggestions, expandIfNecessary);
    }

    private void initEmptyView() {
        // if items are empty, set visibility to true
        binding.emptyViewLl.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager((RecyclerView.LayoutManager) this.initLayoutManager());
        binding.recyclerView.setAdapter(this.initAdapter());
        binding.recyclerView.addItemDecoration((RecyclerView.ItemDecoration) this.initVerticalSpacingItemDecorator());
        binding.recyclerView.addOnScrollListener((RecyclerView.OnScrollListener) this.initHeaderedRecyclerViewListener());
    }

    private final LinearLayoutManager initLayoutManager() {
        return new LinearLayoutManager((Context) this);
    }

    private final SearchRecyclerViewAdapter initAdapter() {
        SearchRecyclerViewAdapter var1 = new SearchRecyclerViewAdapter((Context) this, this.items);
        var1.setMOnItemClickListener(new OnItemClickListener<SearchItem>() {
            @Override
            public void onItemClicked(View view, SearchItem item, int position) {
                Toast.makeText(SearchActivity.this, "Username", Toast.LENGTH_SHORT).show();
            }
        });

        this.adapter = var1;

        return var1;
    }

    private final VerticalSpacingItemDecorator initVerticalSpacingItemDecorator() {
        return new VerticalSpacingItemDecorator(NumberUtils.dpToPx(2, (Context) this), NumberUtils.dpToPx(2, (Context) this));
    }

    private final HeaderedRecyclerViewListener initHeaderedRecyclerViewListener() {
        return (HeaderedRecyclerViewListener) (new HeaderedRecyclerViewListener((Context) this) {
            public void showHeader() {
                AnimationUtils.INSTANCE.showHeader(binding.persistentSearchView);
            }

            public void hideHeader() {
                AnimationUtils.INSTANCE.hideHeader(binding.persistentSearchView);
            }
        });
    }

    private final void saveSearchQueryIfNecessary(String query) {
        this.dataProvider.saveSearchQuery(query);
    }

    private final void performSearch(String query) {
        LinearLayout var18 = binding.emptyViewLl;
        ViewUtils.makeGone((View) var18);
        RecyclerView var19 = binding.recyclerView;
        var19.setAlpha(0.0F);
        ProgressBar var20 = binding.progressBar;
        ViewUtils.makeVisible((View) var20);
        SearchRecyclerViewAdapter var21 = this.adapter;
        var21.clear();

        Iterable $this$map$iv = (Iterable) this.dataProvider.generateQuestions(query, 100);
        Iterator var11 = $this$map$iv.iterator();

        ArrayList<SearchItem> destination$iv$iv = new ArrayList<>();

        while (var11.hasNext()) {
            Object item$iv$iv = var11.next();
            Question p1 = (Question) item$iv$iv;
            SearchItem var16 = new SearchItem(p1);
            destination$iv$iv.add(var16);
        }

        this.items = destination$iv$iv;
//        adapter.notifyDataSetChanged();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                persistentSearchView.hideProgressBar(false);
                persistentSearchView.showLeftButton();
                adapter.setItems(items);
                ProgressBar var10000 = binding.progressBar;
                ViewUtils.makeGone((View) var10000);
                binding.recyclerView.animate().alpha(1.0F).setInterpolator((TimeInterpolator) (new LinearInterpolator())).setDuration(300L).start();
            }
        };
        (new Handler()).postDelayed(runnable, 1000L);
        binding.persistentSearchView.hideLeftButton(false);
        binding.persistentSearchView.showProgressBar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftBtnIv:
                onLeftButtonClicked();
                break;
            case R.id.clearInputBtnIv:
                onClearInputButtonClicked();
                break;
            case R.id.rightBtnIv:
                onRightButtonClicked();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadInitialDataIfNecessary();

        if (shouldExpandSearchView()) {
            persistentSearchView.expand(false);
            getWindow().setSoftInputMode(SOFT_INPUT_STATE_VISIBLE | SOFT_INPUT_ADJUST_NOTHING);
        } else {
            getWindow().setSoftInputMode(SOFT_INPUT_STATE_HIDDEN | SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    private void loadInitialDataIfNecessary() {
        if (persistentSearchView.isInputQueryEmpty()) {
            setSuggestions(dataProvider.getInitialSearchQueries(), false);
        } else {
            setSuggestions(dataProvider.getSuggestionsForQuery(persistentSearchView.getInputQuery()), false);
        }
    }


    private boolean shouldExpandSearchView() {
        return ((persistentSearchView.isInputQueryEmpty() && (adapter.getItemCount() == 0)) || persistentSearchView.isExpanded());
    }


    public void onBackPressed() {
        if (binding.persistentSearchView.isExpanded()) {
            binding.persistentSearchView.collapse();
            return;
        }
        super.onBackPressed();
    }

    private void onLeftButtonClicked() {
        onBackPressed();
    }


    private void onClearInputButtonClicked() {
        //
    }


    private void onRightButtonClicked() {
        Toast.makeText(this, "Right button clicked", Toast.LENGTH_SHORT).show();
    }
}