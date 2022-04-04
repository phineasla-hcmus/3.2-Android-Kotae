package com.ogif.kotae.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.repository.QuestionRepository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class FilterQuestionActivity extends AppCompatActivity {
    private TextView tvCancel, tvSubmit, tvReset;
    private FirebaseFirestore db;
    private String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_question);

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            activity = "search";
        }

        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        tvReset = (TextView) findViewById(R.id.tv_reset);

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelected();
            }

        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sort = getSort();
                String status = getStatus();
                // If list is empty => No filter => Get all
                List<String> lstGrades = getGrades();
                List<String> lstCourses = getCourses();

                ArrayList<Question> filteredQuestions = new ArrayList<Question>();
                ArrayList<Answer> answers = new ArrayList<Answer>();

                QuestionRepository questionRepository = new QuestionRepository();
                questionRepository.getFilterQuestions(sort, status, lstGrades, lstCourses, new TaskListener.State<ArrayList<Question>>() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AAA", e.toString());
                    }

                    @Override
                    public void onSuccess(ArrayList<Question> result) {
                        printQuestions(result);
                        // Set Result
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("filteredQuestions", result);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
    }

    private void clearSelected() {
        ArrayList<RadioButton> radioButtonArrayList = new ArrayList<RadioButton>();
        radioButtonArrayList.add((RadioButton) findViewById(R.id.rad_question_sort_most_view));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.rad_question_sort_top_week));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.rad_question_sort_top_month));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.rad_question_status_answered));
        radioButtonArrayList.add((RadioButton) findViewById(R.id.rad_question_status_unanswered));


        ArrayList<CheckBox> checkBoxArrayList = new ArrayList<CheckBox>();
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_1));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_1));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_2));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_3));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_4));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_5));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_6));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_7));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_8));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_9));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_10));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_11));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_grade_12));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_math));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_physic));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_chemistry));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_english));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_literature));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_biology));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_history));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_geography));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_ethic));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_informatics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chk_technology));

        for (CheckBox chkItem : checkBoxArrayList) {
            chkItem.setChecked(false);
        }

        for (RadioButton radItem : radioButtonArrayList) {
            radItem.setChecked(false);
        }

        RadioButton radQuestionSortAll = (RadioButton) findViewById(R.id.rad_question_sort_all);
        RadioButton radQuestionStatusAll = (RadioButton) findViewById(R.id.rad_question_status_all);

        radQuestionSortAll.setChecked(true);
        radQuestionStatusAll.setChecked(true);
    }

    private String getSort() {
        RadioButton radMostView = (RadioButton) findViewById(R.id.rad_question_sort_most_view);
        RadioButton radTopWeek = (RadioButton) findViewById(R.id.rad_question_sort_top_week);
        RadioButton radTopMonth = (RadioButton) findViewById(R.id.rad_question_sort_top_month);

        if (radMostView.isChecked()) return "MOST_VIEW";
        if (radTopWeek.isChecked()) return "TOP_WEEK";
        if (radTopMonth.isChecked()) return "TOP_MONTH";

        return "ALL";
    }

    private String getStatus() {
        RadioButton radAnswered = (RadioButton) findViewById(R.id.rad_question_status_answered);
        RadioButton radUnanswered = (RadioButton) findViewById(R.id.rad_question_status_unanswered);

        if (radAnswered.isChecked()) return "ANSWERED";
        if (radUnanswered.isChecked()) return "UNANSWERED";

        return "ALL";
    }

    private List<String> getGrades() {
        CheckBox chkGrade1 = (CheckBox) findViewById(R.id.chk_grade_1);
        CheckBox chkGrade2 = (CheckBox) findViewById(R.id.chk_grade_2);
        CheckBox chkGrade3 = (CheckBox) findViewById(R.id.chk_grade_3);
        CheckBox chkGrade4 = (CheckBox) findViewById(R.id.chk_grade_4);
        CheckBox chkGrade5 = (CheckBox) findViewById(R.id.chk_grade_5);
        CheckBox chkGrade6 = (CheckBox) findViewById(R.id.chk_grade_6);
        CheckBox chkGrade7 = (CheckBox) findViewById(R.id.chk_grade_7);
        CheckBox chkGrade8 = (CheckBox) findViewById(R.id.chk_grade_8);
        CheckBox chkGrade9 = (CheckBox) findViewById(R.id.chk_grade_9);
        CheckBox chkGrade10 = (CheckBox) findViewById(R.id.chk_grade_10);
        CheckBox chkGrade11 = (CheckBox) findViewById(R.id.chk_grade_11);
        CheckBox chkGrade12 = (CheckBox) findViewById(R.id.chk_grade_12);

        List<String> lstGrades = new ArrayList<String>();

        if (chkGrade1.isChecked()) lstGrades.add("g01");
        if (chkGrade2.isChecked()) lstGrades.add("g02");
        if (chkGrade3.isChecked()) lstGrades.add("g03");
        if (chkGrade4.isChecked()) lstGrades.add("g04");
        if (chkGrade5.isChecked()) lstGrades.add("g05");
        if (chkGrade6.isChecked()) lstGrades.add("g06");
        if (chkGrade7.isChecked()) lstGrades.add("g07");
        if (chkGrade8.isChecked()) lstGrades.add("g08");
        if (chkGrade9.isChecked()) lstGrades.add("g09");
        if (chkGrade10.isChecked()) lstGrades.add("g10");
        if (chkGrade11.isChecked()) lstGrades.add("g11");
        if (chkGrade12.isChecked()) lstGrades.add("g12");

        return lstGrades;
    }

    private List<String> getCourses() {
        CheckBox chkMath = (CheckBox) findViewById(R.id.chk_math);
        CheckBox chkPhysic = (CheckBox) findViewById(R.id.chk_physic);
        CheckBox chkChemistry = (CheckBox) findViewById(R.id.chk_chemistry);
        CheckBox chkEnglish = (CheckBox) findViewById(R.id.chk_english);
        CheckBox chkLiterature = (CheckBox) findViewById(R.id.chk_literature);
        CheckBox chkBiology = (CheckBox) findViewById(R.id.chk_biology);
        CheckBox chkHistory = (CheckBox) findViewById(R.id.chk_history);
        CheckBox chkGeography = (CheckBox) findViewById(R.id.chk_geography);
        CheckBox chkEthic = (CheckBox) findViewById(R.id.chk_ethic);
        CheckBox chkInformatics = (CheckBox) findViewById(R.id.chk_informatics);
        CheckBox chkTechnology = (CheckBox) findViewById(R.id.chk_technology);

        List<String> lstCourses = new ArrayList<String>();

        if (chkMath.isChecked()) lstCourses.add("s01");
        if (chkPhysic.isChecked()) lstCourses.add("s02");
        if (chkChemistry.isChecked()) lstCourses.add("s03");
        if (chkEnglish.isChecked()) lstCourses.add("s04");
        if (chkLiterature.isChecked()) lstCourses.add("s05");
        if (chkBiology.isChecked()) lstCourses.add("s06");
        if (chkHistory.isChecked()) lstCourses.add("s07");
        if (chkGeography.isChecked()) lstCourses.add("s08");
        if (chkEthic.isChecked()) lstCourses.add("s09");
        if (chkInformatics.isChecked()) lstCourses.add("s10");
        if (chkTechnology.isChecked()) lstCourses.add("s11");

        return lstCourses;
    }

    // For Testing
    private void printQuestions(ArrayList<Question> questions) {
        for (Question question : questions) {
            Log.d("AAA", question.getTitle());
        }
    }
}