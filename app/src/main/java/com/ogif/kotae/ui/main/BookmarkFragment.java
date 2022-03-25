package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.FragmentBookmarkBinding;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.ui.question.CreateQuestionActivity;


public class BookmarkFragment extends Fragment {

    // private View homeView;
    private FloatingActionButton fabAddQuestion;
    private @NonNull FragmentBookmarkBinding binding;
    SwipeRefreshLayout swipeLayout;
    private FirebaseFirestore db;

    private HomeAdapter adapter;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // homeView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
        setUpRecyclerView();


        swipeLayout = (SwipeRefreshLayout) binding.swipeContainer;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }



    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) binding.rvBookmark;

        //adapter = new HomeAdapter(questionList(), this.getContext());
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("questions").whereEqualTo("blocked",false);

        FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query, Question.class)
                .build();
        adapter = new HomeAdapter(options, this.getContext());
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}