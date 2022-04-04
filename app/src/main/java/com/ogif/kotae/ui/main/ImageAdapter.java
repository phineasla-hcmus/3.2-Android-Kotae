package com.ogif.kotae.ui.main;

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

public class ImageAdapter extends BaseAdapter {
    private Context ctx;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<String> arrayLink;

    public ImageAdapter(Context ctx, ArrayList<Uri> mArrayUri,ArrayList<String> arrayLink) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
        this.arrayLink = arrayLink;
    }



    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
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
        if (arrayLink == null)
            //ivGallery.setImageURI(mArrayUri.get(position));
            Picasso.get().load(mArrayUri.get(position)).into(ivGallery);
        if (mArrayUri == null)
            Picasso.get().load(arrayLink.get(position)).into(ivGallery);
        return itemView;
    }
}
