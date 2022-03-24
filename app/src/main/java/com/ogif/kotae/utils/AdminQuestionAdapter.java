package com.ogif.kotae.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ogif.kotae.data.model.Question;

import java.util.ArrayList;


public class AdminQuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Question> questionArrayList;


    @Override
    public int getCount() {
        return questionArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);

        
        return null;
    }
}
