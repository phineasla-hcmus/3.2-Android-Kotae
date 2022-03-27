package com.ogif.kotae.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ogif.kotae.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static String formatDate(@NonNull Date postTime, @NonNull Context context) {
        Date cur = new Date();
        long diffInMillis = Math.abs(cur.getTime() - postTime.getTime());
        if (diffInMillis < 60000) {
            return context.getString(R.string.timestamp_just_now);
        }
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (diffInMinutes < 60) {
            return context.getString(R.string.timestamp_minutes, diffInMinutes);
        }
        long diffInHours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (diffInHours < 24) {
            return context.getString(R.string.timestamp_hours, diffInHours);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(postTime);
    }
}
