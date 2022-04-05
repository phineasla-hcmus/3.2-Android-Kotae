package com.ogif.kotae;

import android.graphics.Bitmap;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Global {
    public static final int CONTENT_MIN = 30;
    public static final int PASSWORD_MIN = 6;
    public static final int PASSWORD_MAX = 16;
    public static final int TITLE_MIN = 10;
    public static final int TITLE_MAX = 40;
    public static final int USERNAME_MIN = 3;
    public static final int USERNAME_MAX = 16;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({COLLECTION_ANSWER,
                COLLECTION_BOOKMARK,
                COLLECTION_COMMENT,
                COLLECTION_GRADE,
                COLLECTION_QUESTION,
                COLLECTION_USER,
                COLLECTION_SUBJECT,
                COLLECTION_VOTE})
    public @interface Collection {
    }

    public static final String COLLECTION_ANSWER = "answers";
    public static final String COLLECTION_BOOKMARK = "bookmarks";
    public static final String COLLECTION_COMMENT = "comments";
    public static final String COLLECTION_GRADE = "grade";
    public static final String COLLECTION_QUESTION = "questions";
    public static final String COLLECTION_USER = "users";
    public static final String COLLECTION_SUBJECT = "subjects";
    public static final String COLLECTION_VOTE = "votes";

    public static final int QUERY_LIMIT = 20;

    public static final String SETTING_KEY_NIGHT_MODE = "night_mode";
    @Deprecated
    public static final String SETTING_KEY_LANGUAGES = "languages";

    public static final String DEFAULT_USER_AVATAR = "avatars/profile3.png";
}
