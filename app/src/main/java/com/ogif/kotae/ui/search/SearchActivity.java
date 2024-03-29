package com.ogif.kotae.ui.search;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivitySearchBinding;
import com.ogif.kotae.fcm.Notification;
import com.ogif.kotae.ui.QuestionViewModel;
import com.ogif.kotae.ui.SearchViewModel;
import com.ogif.kotae.ui.main.FilterQuestionActivity;
import com.ogif.kotae.ui.search.adapter.SearchAdapter;
import com.ogif.kotae.utils.DataProvider;
import com.ogif.kotae.utils.ui.AnimationUtils;
import com.ogif.kotae.utils.ui.HeaderedRecyclerViewListener;
import com.ogif.kotae.utils.ui.VerticalSpacingItemDecorator;
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
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySearchBinding binding;
    private DataProvider dataProvider = new DataProvider();
    private final List<SearchItem> items = new ArrayList<>();
    private ArrayList<Question> results = new ArrayList<>();
    private PersistentSearchView persistentSearchView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private SearchAdapter adapter;
    private SearchViewModel viewModel;
    private QuestionViewModel questionViewModel;
    public static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        this.items.clear();
                        // There are no request codes
                        Intent data = result.getData();
                        ArrayList<Question> questions = data.getParcelableArrayListExtra("filteredQuestions");
                        for (int i = 0; i < questions.size(); i++) {
                            this.items.add(new SearchItem(questions.get(i)));
                        }
                        adapter.setItems(this.items);
                    }
                });

        this.viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        this.questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        init();

        adapter.setOnVoteChangeListener((position, voteView, previous, current) -> {
            Post holder = (Post) voteView.getHolder();
            if (holder == null) {
                Log.w(TAG, "Unidentified holder for VoteView, did you forget to setHolder()?");
                return;
            }
            Notification notification = new Notification();
            notification.pushUpvoteNotification(this, holder);
            questionViewModel.updateVote(holder, previous, current);
        });
    }

    private void startFilterActivity() {
        if (this.items.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.filter_empty_list), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), FilterQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "FILTER_SEARCH");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchResults", results);
        intent.putExtras(bundle);
        activityResultLauncher.launch(intent);
    }

    private void init() {
        persistentSearchView = binding.persistentSearchView;

        initProgressBar();
        initSearchView();
        initEmptyView();
        initRecyclerView();
    }

    private void initProgressBar() {
        binding.pbSearch.setVisibility(View.GONE);
    }

    private void initSearchView() {
        persistentSearchView.setOnLeftBtnClickListener(this);
        persistentSearchView.setOnClearInputBtnClickListener(this);
        persistentSearchView.setOnRightBtnClickListener(this);
        persistentSearchView.showRightButton();
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));
        OnSearchConfirmedListener mOnSearchConfirmedListener = (searchView, query) -> {
            SearchActivity.this.saveSearchQuery(query);
            searchView.collapse();

            SearchActivity.this.performSearch(query);
        };
        persistentSearchView.setOnSearchConfirmedListener(mOnSearchConfirmedListener);

        OnSearchQueryChangeListener mOnSearchQueryChangeListener = (searchView, oldQuery, newQuery) -> {
            if (newQuery.isEmpty()) {
                setSuggestions(dataProvider.getInitialSearchQueries(this), true);
            } else {
                setSuggestions(dataProvider.getSuggestionsForQuery(newQuery), true);
            }
        };
        persistentSearchView.setOnSearchQueryChangeListener(mOnSearchQueryChangeListener);

        OnSuggestionChangeListener mOnSuggestionChangeListener = new OnSuggestionChangeListener() {
            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                String query = suggestion.getItemModel().getText();
                saveSearchQuery(query);
                setSuggestions(dataProvider.getSuggestionsForQuery(query), false);
                performSearch(query);
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
                dataProvider.removeSearchQuery(suggestion.getItemModel().getText());
            }
        };
        persistentSearchView.setOnSuggestionChangeListener(mOnSuggestionChangeListener);
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
        binding.llSearchEmptyView.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        binding.rvSearch.setLayoutManager(this.initLayoutManager());
        binding.rvSearch.setAdapter(this.initAdapter());
        binding.rvSearch.addItemDecoration(this.initVerticalSpacingItemDecorator());
        binding.rvSearch.addOnScrollListener(this.initHeaderedRecyclerViewListener());
    }

    private LinearLayoutManager initLayoutManager() {
        return new LinearLayoutManager(this);
    }

    private SearchAdapter initAdapter() {
        SearchAdapter adapter = new SearchAdapter(this, this.items);
        this.adapter = adapter;

        return adapter;
    }

    private VerticalSpacingItemDecorator initVerticalSpacingItemDecorator() {
        return new VerticalSpacingItemDecorator(NumberUtils.dpToPx(2, this), NumberUtils.dpToPx(2, this));
    }

    private HeaderedRecyclerViewListener initHeaderedRecyclerViewListener() {
        return new HeaderedRecyclerViewListener(this) {
            public void showHeader() {
                AnimationUtils.INSTANCE.showHeader(binding.persistentSearchView);
            }

            public void hideHeader() {
                AnimationUtils.INSTANCE.hideHeader(binding.persistentSearchView);
            }
        };
    }

    private void saveSearchQuery(String query) {
        this.dataProvider.saveSearchQuery(query);
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/61841088/given-a-string-generate-all-2-consecutive-characters-3-consecutive-characters">
     */

    public void combo(List<String> combinations, String s) {
        int len = s.length();
        int gap = len - 1; // This determines the consecutive character size.
        for (int set = 0; set <= len; set++) {
            if ((set + gap) <= len) {
                String subString = s.substring(set, set + gap);
                combinations.add(subString);
            }
        }
        combinations.add(s);
    }

    private void performSearch(String query) {
        ViewUtils.makeGone(binding.llSearchEmptyView);
        binding.rvSearch.setAlpha(0.0F);
        ViewUtils.makeVisible(binding.pbSearch);
        this.adapter.clear();

        this.viewModel.getQuestions(query);

        List<String> combinations = new ArrayList<>();
        combo(combinations, query);

        this.viewModel.getQuestionLiveData().observe(this, questions -> {
            this.items.clear();
//            this.adapter.notifyDataSetChanged();
            this.results.clear();
            if (questions != null) {

                for (int i = 0; i < questions.size(); i++) {
                    boolean flag = false;
                    for (int j = 0; j < combinations.size(); j++) {
                        if (questions.get(i).getTitle().contains(combinations.get(j))) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        this.items.add(new SearchItem(questions.get(i)));
                        this.results.add(questions.get(i));
                    }
                }
            }

        });

        Runnable runnable = () -> {
            persistentSearchView.hideProgressBar(false);
            persistentSearchView.showLeftButton();
            adapter.setItems(items);
//            adapter.notifyItemRangeInserted(0, items.size());
            ViewUtils.makeGone(binding.pbSearch);
            binding.rvSearch.animate()
                    .alpha(1.0F)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(300L)
                    .start();
        };
        (new Handler()).postDelayed(runnable, 1000L);
        binding.persistentSearchView.hideLeftButton(false);
        binding.persistentSearchView.showProgressBar();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftBtnIv)
            onLeftButtonClicked();
        else if (id == R.id.clearInputBtnIv)
            onClearInputButtonClicked();
        else if (id == R.id.rightBtnIv)
            onRightButtonClicked();
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
            setSuggestions(dataProvider.getInitialSearchQueries(this), false);
        } else {
            setSuggestions(dataProvider.getSuggestionsForQuery(persistentSearchView.getInputQuery()), false);
        }
    }


    private boolean shouldExpandSearchView() {
        return ((persistentSearchView.isInputQueryEmpty() && (adapter.getItemCount() == 0)) || persistentSearchView
                .isExpanded());
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
        startFilterActivity();
    }
}