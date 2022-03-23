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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.R;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
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
                List<String> lstCourses = getCourses();

                ArrayList<Question> filteredQuestions = new ArrayList<Question>();
                ArrayList<Answer> answers = new ArrayList<Answer>();

                db = FirebaseFirestore.getInstance();
                Query queryQuestion = db.collection("questions").whereEqualTo("blocked", false)
                        .orderBy("upvote", Query.Direction.DESCENDING);
                queryQuestion.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshotsQuestions) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshotsQuestions) {
                            Question question = documentSnapshot.toObject(Question.class);
                            filteredQuestions.add(question);
                        }

                        Query queryAnswer = db.collection("answers");
                        queryAnswer.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshotsAnswers) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshotsAnswers) {
                                    Answer answer = documentSnapshot.toObject(Answer.class);
                                    answers.add(answer);
                                }

                                // To Do
                                filterQuestionsAndSetResult(sort, status, lstGrades, lstCourses, filteredQuestions, answers);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("AAA", e.toString());
                                Toast.makeText(FilterQuestionActivity.this, "Query Answers Failed...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AAA", e.toString());
                        Toast.makeText(FilterQuestionActivity.this, "Query Questions Failed...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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

    private void filterQuestionsAndSetResult(String sort, String status, List<String> lstGrades, List<String> lstCourses, ArrayList<Question> questions, ArrayList<Answer> answers) {
        questions = sortQuestionByUpvote(questions, sort);
        questions = filterQuestionByStatus(questions, answers, status);
        questions = filterQuestionByGrade(questions, lstGrades);
        questions = filterQuestionBySubject(questions, lstCourses);

        printQuestions(questions);

        // Set Result
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("filteredQuestions", questions);
//        intent.putExtras(bundle);
//        setResult(RESULT_OK, intent);
//        finish();
    }

    private ArrayList<Question> sortQuestionByUpvote(ArrayList<Question> questions, String sort) {
        ArrayList<Question> newQuestions = new ArrayList<Question>();

        if (sort.equals("MOST_VIEW")) {
            // Get all
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
        // ELSE => Get all (Do nothing)
        return questions;
    }

    private ArrayList<Question> filterAnsweredQuestions(ArrayList<Question> questions, ArrayList<Answer> answers) {
        // No Duplicate
        LinkedHashSet<Question> setAnsweredQuestions = new LinkedHashSet<Question>();

        for (Answer answer : answers) {
            for (Question question : questions) {
                if (answer.getQuestionId().equals(question.getId())) {
                    setAnsweredQuestions.add(question);
                }
            }
        }
        return new ArrayList<Question>(setAnsweredQuestions);
    }

    // For Testing
    private void printQuestions(ArrayList<Question> questions) {
        for (Question question : questions) {
            Log.d("AAA", question.getTitle());
        }
    }

    private ArrayList<Question> filterQuestionByStatus(ArrayList<Question> questions, ArrayList<Answer> answers, String status) {
        if (status.equals("ANSWERED")) {
            ArrayList<Question> answeredQuestions = filterAnsweredQuestions(questions, answers);
            // TEST
            // printQuestions(answeredQuestions);
            return answeredQuestions;

        } else if (status.equals("UNANSWERED")) {
            ArrayList<Question> answeredQuestions = filterAnsweredQuestions(questions, answers);
            for (Question question : answeredQuestions) {
                questions.remove(question);
            }
            // TEST
            // printQuestions(questions);
            return questions;
        }
        // ELSE => ALL => Get all (Do Nothing)
        // TEST
        // printQuestions(questions);
        return questions;
    }

    private ArrayList<Question> filterQuestionByGrade(ArrayList<Question> questions, List<String> lstGrades) {
        // size == 0 => Get all (Do nothing)
        if (lstGrades.size() == 0) {
            return questions;
        }

        // Use LinkedHashSet To Avoid Duplicate (1 Question has multiple grade?)
        LinkedHashSet<Question> tempQuestions = new LinkedHashSet<Question>();
        for(String gradeId :lstGrades){
            for(Question question : questions){
                if(question.getGradeId().equals(gradeId)){
                    tempQuestions.add(question);
                }
            }
        }
        return new ArrayList<Question>(tempQuestions);
    }

    private ArrayList<Question> filterQuestionBySubject(ArrayList<Question> questions, List<String> lstCourses) {
        // size == 0 => Get all (Do nothing)
        if (lstCourses.size() == 0) {
            return questions;
        }

        // Use LinkedHashSet To Avoid Duplicate (1 Question has multiple subject?)
        LinkedHashSet<Question> tempQuestions = new LinkedHashSet<Question>();
        for(String courseId :lstCourses){
            for(Question question : questions){
                if(question.getSubjectId().equals(courseId)){
                    tempQuestions.add(question);
                }
            }
        }
        return new ArrayList<Question>(tempQuestions);
    }
}