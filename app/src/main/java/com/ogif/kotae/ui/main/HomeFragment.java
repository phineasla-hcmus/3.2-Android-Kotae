package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.ui.question.CreateQuestionActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  {


    // private View homeView;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;
    SwipeRefreshLayout swipeLayout;
    private FirebaseFirestore db;

    private HomeAdapter adapter;

    public HomeFragment() {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setUpRecyclerView();

        fabAddQuestion = (FloatingActionButton) binding.fabAddQuestion;

        fabAddQuestion.setOnClickListener(v -> {
            startCreateQuestionActivity();
        });
        swipeLayout = (SwipeRefreshLayout) binding.swipeContainer;
       // swipeLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }

//    @Override
//    public void onRefresh() {
//        // Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
//        //TODO: do refresh task
//    }

    private void startCreateQuestionActivity() {
        Intent intent = new Intent(requireActivity().getApplicationContext(), CreateQuestionActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) binding.rvHome;

        //adapter = new HomeAdapter(questionList(), this.getContext());
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("questions").whereEqualTo("blocked",false);

        FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query, Question.class)
                .build();
        adapter = new HomeAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));
        recyclerView.setAdapter(adapter);

    }

    //    private List<Question> questionList() {
//        List<Question> questionList = new ArrayList<>();
//        questionsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null){
//                    Log.e("Firestore error", error.getMessage() );
//                    return;
//                }
//                for (DocumentChange dc : value.getDocumentChanges()){
//                    if (dc.getType()== DocumentChange.Type.ADDED){
//                        questionList.add(dc.getDocument().toObject(Question.class));
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
//        return questionList;
//    }

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