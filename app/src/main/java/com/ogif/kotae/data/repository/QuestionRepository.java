package com.ogif.kotae.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ogif.kotae.Global;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.Answer;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.ui.main.FilterQuestionActivity;
import com.ogif.kotae.utils.DateUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class QuestionRepository extends RecordRepository<Question> {
    private static final String TAG = "QuestionRepository";

    public QuestionRepository() {
        super(Global.COLLECTION_QUESTION);
    }

    public QuestionRepository(String authorId) {
        super(Global.COLLECTION_QUESTION, authorId);
    }

    public QuestionRepository(VoteRepository voteRepository) {
        super(Global.COLLECTION_QUESTION, voteRepository);
    }

    @Nullable
    @Override
    protected Question toObject(@NonNull DocumentSnapshot snapshot) {
        return snapshot.toObject(Question.class);
    }

    @Override
    protected List<Question> toObjects(@NonNull QuerySnapshot snapshots) {
        return snapshots.toObjects(Question.class);
    }

    public void createQuestion(@NonNull String authorId, @NonNull String authorName, @NonNull String title, @NonNull String content, @NonNull String subjectId, @NonNull String gradeId, @NonNull String subject, @NonNull String grade) {
        Question question = new Question.Builder().title(title)
                .author(authorId, authorName)
                .content(content)
                .subject(subjectId, subject)
                .grade(gradeId, grade)
                .build();
        collectionRef.add(question)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference
                        .getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
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


    public Query getHomeQuestions() {
        return collectionRef.whereEqualTo("blocked", false)
                .orderBy("postTime", Query.Direction.DESCENDING)
                .limit(Global.QUERY_LIMIT);
    }

    public void searchQuestionByKeyword(@NonNull String keyword, int limit, @NonNull TaskListener.State<List<Question>> callback) {
        Task<QuerySnapshot> query = collectionRef
                .orderBy("title")
                .startAt(keyword.toUpperCase())
                .endAt(keyword.toLowerCase() + "\uf8ff")
                .limit(limit)
                .get();
        onQueryListComplete(query, callback);
    }

    public void getFilterQuestions(String sort, String status, List<String> lstGrades, List<String> lstCourses, ArrayList<Question> questionsInput, @NonNull TaskListener.State<ArrayList<Question>> callback) {
        ArrayList<Question> filteredQuestions;
        if (questionsInput != null) {
            filteredQuestions = questionsInput;
        } else {
            filteredQuestions = new ArrayList<>();
        }
        ArrayList<Answer> answers = new ArrayList<>();

        final ArrayList<Question>[] result = new ArrayList[]{new ArrayList<Question>()};

        Query queryQuestion = collectionRef.whereEqualTo("blocked", false)
                .orderBy("upvote", Query.Direction.DESCENDING);
        queryQuestion.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshotsQuestions) {
                if (filteredQuestions.size() == 0) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshotsQuestions) {
                        Question question = documentSnapshot.toObject(Question.class);
                        filteredQuestions.add(question);
                    }
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

    private ArrayList<Question> sortQuestionByUpvote(ArrayList<Question> questions, String sort) {
        ArrayList<Question> newQuestions = new ArrayList<Question>();
        switch (sort) {
            case FilterQuestionActivity.SORT_MOST_VIEW:
                // Get all
                // Default : Order by vote desc
                return questions;
            case FilterQuestionActivity.SORT_TOP_WEEK:
                Timestamp firstDayOfWeek = new Timestamp(DateUtils.firstDayOfWeek());
                for (Question question : questions) {
                    if (question.getPostTime().compareTo(firstDayOfWeek.toDate()) != -1) {
                        // Log.d("AAA", String.valueOf(question.getPostTime().toString()));
                        newQuestions.add(question);
                    }
                }
                return newQuestions;
            case FilterQuestionActivity.SORT_TOP_MONTH:
                Timestamp firstDayOfMonth = new Timestamp(DateUtils.firstDayOfMonth());

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
        if (status.equals(FilterQuestionActivity.STATUS_ANSWERED)) {
            ArrayList<Question> answeredQuestions = filterAnsweredQuestions(questions, answers);
            return answeredQuestions;

        } else if (status.equals(FilterQuestionActivity.STATUS_UNANSWERED)) {
            ArrayList<Question> answeredQuestions = filterAnsweredQuestions(questions, answers);
            for (Question question : answeredQuestions) {
                questions.remove(question);
            }
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
        LinkedHashSet<Question> tempQuestions = new LinkedHashSet<>();
        for (String gradeId : lstGrades) {
            for (Question question : questions) {
                if (question.getGradeId().equals(gradeId)) {
                    tempQuestions.add(question);
                }
            }
        }
        return new ArrayList<>(tempQuestions);
    }

    private ArrayList<Question> filterQuestionBySubject(ArrayList<Question> questions, List<String> lstCourses) {
        // size == 0 => Get all (Do nothing)
        if (lstCourses.size() == 0) {
            return questions;
        }

        // Use LinkedHashSet To Avoid Duplicate (1 Question has multiple subject?)
        LinkedHashSet<Question> tempQuestions = new LinkedHashSet<>();
        for (String courseId : lstCourses) {
            for (Question question : questions) {
                if (question.getSubjectId().equals(courseId)) {
                    tempQuestions.add(question);
                }
            }
        }
        return new ArrayList<>(tempQuestions);
    }

    public void getQuestionsOrderByReport(@NonNull TaskListener.State<ArrayList<Question>> callback) {
        ArrayList<Question> questionArrayList = new ArrayList<>();

        Query queryQuestion = collectionRef.orderBy("report", Query.Direction.DESCENDING);
        queryQuestion.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Question question = documentSnapshot.toObject(Question.class);
                    questionArrayList.add(question);
                }
                callback.onSuccess(questionArrayList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void blockQuestion(String questionID, boolean blocked, @NonNull TaskListener.State<Void> callback) {
        DocumentReference ref = collectionRef.document(questionID);
        ref.update(
                "blocked", blocked
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onSuccess(unused);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getQuestionById(String questionID, @NonNull TaskListener.State<Question> callback) {
        DocumentReference ref = collectionRef.document(questionID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Question question = documentSnapshot.toObject(Question.class);
                callback.onSuccess(question);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public Query getFilterQuestionQuery(String sort, String status, ArrayList<String> lstGrades, ArrayList<String> lstCourses) {
        Query query = collectionRef.whereEqualTo("blocked", false);
        switch (sort) {
            case FilterQuestionActivity.SORT_MOST_VIEW: {
                query = query.orderBy("upvote", Query.Direction.DESCENDING);
                break;
            }
            case FilterQuestionActivity.SORT_TOP_WEEK: {
                Timestamp firstDayOfWeek = new Timestamp(DateUtils.firstDayOfWeek());
                query = query.whereGreaterThanOrEqualTo("postTime", firstDayOfWeek);
//                query = query.orderBy("upvote", Query.Direction.DESCENDING);
                break;
            }
            case FilterQuestionActivity.SORT_TOP_MONTH: {
                Timestamp firstDayOfMonth = new Timestamp(DateUtils.firstDayOfMonth());
//                query = query.orderBy("upvote", Query.Direction.DESCENDING);
                query = query.whereGreaterThanOrEqualTo("postTime", firstDayOfMonth);
                break;
            }
            default: {
                query = query.orderBy("postTime", Query.Direction.DESCENDING);
                break;
            }
        }

        if (lstCourses.size() != 0) {
            query = query.whereIn("subjectId", lstCourses);
        }

        // Can only select 1 option
        if (lstGrades.size() != 0) {
            query = query.whereEqualTo("gradeId", lstGrades.get(0));
        }
        return query;
    }
}
