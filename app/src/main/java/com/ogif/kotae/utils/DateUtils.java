package com.ogif.kotae.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.ogif.kotae.R;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
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

    @NonNull
    private static Calendar startOfDay(@NonNull Calendar calendar) {
        // clear would not reset the hour of day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }

    @NonNull
    public static Date startOfDay() {
        return startOfDay(Calendar.getInstance()).getTime();
    }

    @NonNull
    public static Date firstDayOfWeek() {
        Calendar calendar = startOfDay(Calendar.getInstance());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    @NonNull
    public static Date firstDayOfMonth() {
        Calendar calendar = startOfDay(Calendar.getInstance());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date asDate(@NonNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(@NonNull LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(@NonNull Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(@NonNull Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
