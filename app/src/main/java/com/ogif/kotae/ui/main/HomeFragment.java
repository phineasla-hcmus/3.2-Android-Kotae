package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.ui.createquestion.CreateQuestionActivity;

public class HomeFragment extends Fragment {


    // private View homeView;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;
    private SwipeRefreshLayout swipeLayout;
    private QuestionRepository questionRepository;
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

        questionRepository = new QuestionRepository();

        setUpRecyclerView();
        fabAddQuestion = (FloatingActionButton) binding.fabAddQuestion;

        fabAddQuestion.setOnClickListener(v -> {
            startCreateQuestionActivity();
        });
        swipeLayout = (SwipeRefreshLayout) binding.swipeContainer;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                        .setQuery(questionRepository.getHomeQuestions(), Question.class)
                        .build();
                adapter = new HomeAdapter(options, getActivity().getApplicationContext());
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }


    private void startCreateQuestionActivity() {
        Intent intent = new Intent(requireActivity().getApplicationContext(), CreateQuestionActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) binding.rvHome;

        //adapter = new HomeAdapter(questionList(), this.getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity().getApplicationContext(), 1));

        FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(questionRepository.getHomeQuestions(), Question.class)
                .build();
        adapter = new HomeAdapter(options, this.getContext());
        adapter.notifyDataSetChanged();
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