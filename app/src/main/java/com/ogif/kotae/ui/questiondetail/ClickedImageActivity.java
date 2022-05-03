package com.ogif.kotae.ui.questiondetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.databinding.ActivityClickedImageBinding;
import com.ogif.kotae.databinding.ActivityCreateQuestionBinding;
import com.ogif.kotae.ui.questiondetail.adapter.QuestionDetailAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ClickedImageActivity extends AppCompatActivity {
    private ActivityClickedImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClickedImageBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        Intent intent = getIntent();
        if (intent.hasExtra("image")){
            String image = intent.getStringExtra("image");
            Picasso.get().load(image).into(binding.ivImage);
        }


    }


}