package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;

import com.ogif.kotae.Global;


public class QuestionUtils {
    public static final int OK = 0;
    public static final int INVALID_TITLE_LENGTH = 1;
    public static final int INVALID_CONTENT_LENGTH = 2;
    public static final int NOT_SELECTED_GRADE = 3;
    public static final int NOT_SELECTED_SUBJECT = 4;


    public static int isTitleValid(@NonNull CharSequence title) {
        return (title.length() < Global.TITLE_MIN || title.length() > Global.TITLE_MAX)
                ? INVALID_TITLE_LENGTH
                : OK;
    }

    public static int isContentValid(@NonNull CharSequence content) {
        if (content.length() < Global.CONTENT_MIN)
            return INVALID_CONTENT_LENGTH;
        return OK;
    }

    public static int isGradeValid(@NonNull CharSequence grade) {
        return (grade.length() > 0) ? NOT_SELECTED_GRADE : OK;
    }

    public static int isSubjectValid(@NonNull CharSequence subject) {
        return (subject.length() > 0) ? NOT_SELECTED_SUBJECT : OK;
    }
}
