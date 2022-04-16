package com.ogif.kotae.utils.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ogif.kotae.Global;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.Record;

import java.util.List;


public class QuestionUtils extends RecordUtils {

    public static boolean isTitleValid(@NonNull CharSequence title) {
        return (title.length() >= Global.TITLE_MIN && title.length() <= Global.TITLE_MAX);
    }

    public static boolean isGradeValid(@NonNull CharSequence grade) {
        return grade.length() != 0;
    }

    public static boolean isSubjectValid(@NonNull CharSequence subject) {
        return subject.length() != 0;
    }
}
