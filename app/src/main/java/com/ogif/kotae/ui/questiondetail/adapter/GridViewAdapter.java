package com.ogif.kotae.ui.questiondetail.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ogif.kotae.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    private List<String> mArrayUrl;


    public GridViewAdapter(Context ctx, List<String> mArrayUrl) {

        this.ctx = ctx;
        this.mArrayUrl = mArrayUrl;
    }



    @Override
    public int getCount() {
        if ( mArrayUrl!= null|| !mArrayUrl.isEmpty())
            return mArrayUrl.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mArrayUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item_question_image, parent, false);

        ivGallery = (ImageView) itemView.findViewById(R.id.iv_image);
            Picasso.get().load(mArrayUrl.get(position)).into(ivGallery);

        return itemView;
    }
}
