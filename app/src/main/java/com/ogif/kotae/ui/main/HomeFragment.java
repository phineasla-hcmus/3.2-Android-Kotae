package com.ogif.kotae.ui.main;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.FragmentHomeBinding;
import com.ogif.kotae.utils.QuestionAdapter;
import com.ogif.kotae.utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private View homeView;
    private FloatingActionButton fabAddQuestion;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setUpRecyclerView();

        fabAddQuestion = (FloatingActionButton) binding.fabAddQuestion;

        fabAddQuestion.setOnClickListener(v -> {
            startCreateQuestionActivity();
        });

        return binding.getRoot();
    }

    private void startCreateQuestionActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CreateQuestionActivity.class);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
//        RecyclerView recyclerView = (RecyclerView) homeView.findViewById(R.id.rv_home);
        RecyclerView recyclerView = (RecyclerView) binding.rvHome;
        //RecyclerAdapter adapter = new RecyclerAdapter(dummyStrings());

        QuestionAdapter adapter = new QuestionAdapter(questionList(),this.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));
        recyclerView.setAdapter(adapter);
//        adapter.setRecyclerClickListener(new RecyclerAdapter.RecyclerClickListener() {
//            @Override
//            public void onClick(int position) {
//                if (position == 0) {
//                    spaceNavigationView.showBadgeAtIndex(1, 54, ContextCompat.getColor(getActivity().getApplicationContext(), R.color.badge_background_color));
//                } else if (position == 1) {
//                    spaceNavigationView.hideBudgeAtIndex(1);
//                }
//            }
//        });
    }

    private List<String> dummyStrings() {
        List<String> colorList = new ArrayList<>();
        colorList.add("#354045");
        colorList.add("#20995E");
        colorList.add("#76FF03");
        colorList.add("#E26D1B");
        colorList.add("#911717");
        colorList.add("#9C27B0");
        colorList.add("#20995E");
        colorList.add("#76FF03");
        colorList.add("#20995E");
        colorList.add("#76FF03");
        colorList.add("#E26D1B");
        colorList.add("#911717");
        colorList.add("#9C27B0");
        colorList.add("#20995E");
        colorList.add("#76FF03");
        colorList.add("#E26D1B");
        colorList.add("#911717");
        colorList.add("#9C27B0");
        colorList.add("#FFC107");
        colorList.add("#01579B");
        return colorList;
    }
    private List<Question> questionList(){
        List<Question> questionList = new ArrayList<>();
        String date = "22/01/2022";
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        questionList.add(new Question
                ("Chau","Lớp 10", "Tiếng Anh",
                        "How old are you?",date));
        return questionList;
    }
}