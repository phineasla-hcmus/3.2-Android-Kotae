package com.ogif.kotae.utils.model;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
        public static void formatDate(Date postTime, TextView timeView){
        Date cur = new Date();
        long diffInMillies = Math.abs(cur.getTime() - postTime.getTime());
        if (diffInMillies< 60000)
        {
            timeView.setText("Just now");
            return;
        }
        long diffInMins = TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
        if (diffInMins < 60)
        {
            if (diffInMins == 1) {
                timeView.setText("Asked " + diffInMins + " minute ago");
                return;
            }
            timeView.setText("Asked "+ diffInMins + " minutes ago");
            return;
        }
        long diffInHours = TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
        if (diffInHours <24){
            if (diffInHours == 1) {
                timeView.setText("Asked " + diffInHours + " hour ago");
                return;
            }
            timeView.setText("Asked "+ diffInHours + " hours ago");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        timeView.setText(dateFormat.format(postTime));
        return;
    }
}
