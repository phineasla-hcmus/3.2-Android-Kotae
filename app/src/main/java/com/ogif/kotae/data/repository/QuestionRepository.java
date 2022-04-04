package com.ogif.kotae.data.repository;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.StateWrapper;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.main.FilterQuestionActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;
    private final MutableLiveData<StateWrapper<Question>> mutableLiveData;

    private String orderBy = Question.Field.POST_TIME;
    private Query.Direction orderByDirection = Query.Direction.DESCENDING;

    public QuestionRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.questionsRef = db.collection("questions");
        this.mutableLiveData = new MutableLiveData<>();
    }

    private void onQueryListComplete(@NonNull Task<QuerySnapshot> query, @NonNull TaskListener.State<List<Question>> callback) {
        query.addOnSuccessListener(queryDocumentSnapshots -> {
            List<Question> questions = new ArrayList<>(queryDocumentSnapshots.size());
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                questions.add(document.toObject(Question.class));
            }
            callback.onSuccess(questions);
        }).addOnFailureListener(callback::onFailure);
    }

    @NonNull
    private Task<DocumentSnapshot> get(@NonNull String id) {
        return questionsRef.document(id).get();
    }

    public DocumentReference toDocumentRef(@NonNull Question question) {
        return questionsRef.document(question.getId());
    }

    public void get(@NonNull String id, @NonNull TaskListener.State<Question> callback) {
        get(id).addOnSuccessListener(documentSnapshot -> callback.onSuccess(documentSnapshot.toObject(Question.class)))
                .addOnFailureListener(callback::onFailure);
    }

    public void createQuestion(@NonNull String authorId, @NonNull String authorName, @NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {

        Question question = new Question.Builder().title(title)
                .author(authorId, authorName)
                .content(content)
                .subject(subjectId, subject)
                .grade(gradeId, grade)
                .build();
        questionsRef.add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public MutableLiveData<StateWrapper<Question>> getMutableLiveData() {
        return mutableLiveData;
    }

    public Query getHomeQuestions() {
        return questionsRef.whereEqualTo("blocked", false)
                .orderBy("postTime", Query.Direction.DESCENDING)
                .limit(Global.QUERY_LIMIT);
    }

    public void searchQuestionByKeyword(@NonNull String keyword, int limit, @NonNull TaskListener.State<List<Question>> callback) {
        Task<QuerySnapshot> query = questionsRef
                .orderBy("title")
                .startAt(keyword.toUpperCase())
                .endAt(keyword.toLowerCase() + "\uf8ff")
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getFilterQuestions(String sort, String status, List<String> lstGrades, List<String> lstCourses, @NonNull TaskListener.State<ArrayList<Question>> callback) {
        ArrayList<Question> filteredQuestions = new ArrayList<Question>();
        ArrayList<Answer> answers = new ArrayList<Answer>();

        final ArrayList<Question>[] result = new ArrayList[]{new ArrayList<Question>()};

        Query queryQuestion = questionsRef.whereEqualTo("blocked", false)
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
                        result[0] = filterQuestionsAndSetResult(sort, status, lstGrades, lstCourses, filteredQuestions, answers);
                        callback.onSuccess(result[0]);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AAA", e.toString());
                        callback.onFailure(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", e.toString());
                callback.onFailure(e);
            }
        });
    }

    private ArrayList<Question> filterQuestionsAndSetResult(String sort, String status, List<String> lstGrades, List<String> lstCourses, ArrayList<Question> questions, ArrayList<Answer> answers) {
        questions = sortQuestionByUpvote(questions, sort);
        questions = filterQuestionByStatus(questions, answers, status);
        questions = filterQuestionByGrade(questions, lstGrades);
        questions = filterQuestionBySubject(questions, lstCourses);
        return questions;
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
        for (String gradeId : lstGrades) {
            for (Question question : questions) {
                if (question.getGradeId().equals(gradeId)) {
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
        for (String courseId : lstCourses) {
            for (Question question : questions) {
                if (question.getSubjectId().equals(courseId)) {
                    tempQuestions.add(question);
                }
            }
        }
        return new ArrayList<Question>(tempQuestions);
    }
}
