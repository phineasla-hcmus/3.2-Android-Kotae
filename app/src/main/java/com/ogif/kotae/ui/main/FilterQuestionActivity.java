package com.ogif.kotae.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Question;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FilterQuestionActivity extends AppCompatActivity {
    private TextView tvCancel, tvSubmit;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_question);

        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);

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

                // If list is empty => No filter => Get all
                List<String> lstCourses = getCourses();

                filterQuestions(sort, status, lstGrades, lstCourses);

//                Intent intent = new Intent();
//                intent.putExtra("SORT", sort);
//                intent.putExtra("FILTER_STATUS", status);
//                intent.putStringArrayListExtra("FILTER_GRADES", (ArrayList<String>) lstGrades);
//                intent.putStringArrayListExtra("FILTER_COURSES", (ArrayList<String>) lstCourses);
//                setResult(RESULT_OK, intent);
//
//                finish();
            }
        });
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

    private Timestamp getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new Timestamp(calendar.getTime());
    }

    private Timestamp getFirstDayOfMonth(Date date) {
        return new Timestamp(new Date(date.getYear(), date.getMonth(), 1));
    }

    private void filterQuestions(String sort, String status, List<String> lstGrades, List<String> lstCourses) {
        ArrayList<Question> filteredQuestions = new ArrayList<Question>();
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("questions").whereEqualTo("blocked", false)
                .orderBy("upvote", Query.Direction.DESCENDING);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Question question = documentSnapshot.toObject(Question.class);
                    filteredQuestions.add(question);
                }

                for (Question question : filteredQuestions) {
                    Log.d("AAA", question.getTitle());
                }

                // To Do
                // .....
                sortQuestionByUpvote(filteredQuestions, sort);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", e.toString());
                Toast.makeText(FilterQuestionActivity.this, "Query Failed...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        Question question = documentSnapshot.toObject(Question.class);
//                        filteredQuestions.add(question);
//                    }
//                } else {
//                    Log.d("AAA", "onComplete: FALSE GET DATA FIREBASE");
//                    Toast.makeText(FilterQuestionActivity.this, "Query Failed...", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//
//                // To do
//                // Test get questions
//                for (Question question : filteredQuestions) {
//                    Log.d("AAA", question.getTitle());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("AAA", "onComplete: FALSE GET DATA FIREBASE");
//                Log.d("AAA", e.toString());
//            }
//        });
    }

    private ArrayList<Question> sortQuestionByUpvote(ArrayList<Question> questions, String sort) {
        ArrayList<Question> newQuestions = new ArrayList<Question>();

        // ELSE => Get all (Do nothing)
        if (sort.equals("MOST_VIEW")) {
            // Default : Order by vote desc
            return questions;
        } else if (sort.equals("TOP_WEEK")) {
            Timestamp firstDayOfWeek = getFirstDayOfWeek(new Date());

            for (Question question : questions) {
                if (question.getPostTime().compareTo(firstDayOfWeek.toDate()) != -1) {
                    // Log.d("AAA", String.valueOf(question.getPostTime().toString()));
                    newQuestions.add(question);
                }
            }

            return newQuestions;
        } else if (sort.equals("TOP_MONTH")) {
            Timestamp firstDayOfMonth = getFirstDayOfMonth(new Date());

            for (Question question : questions) {
                if (question.getPostTime().compareTo(firstDayOfMonth.toDate()) != -1) {
                    // Log.d("AAA", String.valueOf(question.getPostTime().toString()));
                    newQuestions.add(question);
                }
            }

            return newQuestions;
        }
        return questions;
    }

    private ArrayList<Question> filterQuestionByStatus(ArrayList<Question> questions, String status) {
        // ELSE => Get all (Do nothing)
        if (status.equals("ANSWERED")) {

        } else if (status.equals("UNANSWERED")) {

        }
        return questions;
    }

    private ArrayList<Question> filterQuestionByGrade(ArrayList<Question> questions, List<String> lstGrades) {
        // size == 0 => Get all (Do nothing)
        if (lstGrades.size() != 0) {
            return questions;
        }

        return null;
    }

    private ArrayList<Question> filterQuestionBySubject(ArrayList<Question> questions, List<String> lstCourses) {
        // size == 0 => Get all (Do nothing)
        if (lstCourses.size() == 0) {
            return questions;
        }


        return null;
    }
}