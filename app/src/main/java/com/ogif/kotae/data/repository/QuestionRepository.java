package com.ogif.kotae.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private final FirebaseFirestore db;
    private final CollectionReference questionsRef;

    public QuestionRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.questionsRef = db.collection("questions");

    }
}
