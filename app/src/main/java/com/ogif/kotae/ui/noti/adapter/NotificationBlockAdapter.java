package com.ogif.kotae.ui.noti.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.ogif.kotae.R;
import com.ogif.kotae.data.TaskListener;
import com.ogif.kotae.data.model.NotificationBlock;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.data.model.User;
import com.ogif.kotae.data.repository.NotificationRepository;
import com.ogif.kotae.data.repository.QuestionRepository;
import com.ogif.kotae.data.repository.UserRepository;

import java.util.ArrayList;

public class NotificationBlockAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<NotificationBlock> notificationBlockList;
    private NotificationRepository notificationRepository;
    private QuestionRepository questionRepository;

    public NotificationBlockAdapter(Context context, int layout, ArrayList<NotificationBlock> notificationBlockList) {
        this.context = context;
        this.layout = layout;
        this.notificationBlockList = notificationBlockList;
        this.notificationRepository = new NotificationRepository();
        this.questionRepository = new QuestionRepository();
    }

    @Override
    public int getCount() {
        return notificationBlockList.size();
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

        TextView txtContent = (TextView) view.findViewById(R.id.tv_noti_content);
        TextView txtTime = (TextView) view.findViewById(R.id.tv_noti_time);

        NotificationBlock notificationBlock = notificationBlockList.get(i);
        txtTime.setText(notificationRepository.convertTimestampToRemaining(notificationBlock.getTimestamp()));

        UserRepository userRepository = new UserRepository();
        userRepository.getById(notificationBlock.getLastUserDoAction(), new TaskListener.State<User>() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("AAA", e.toString());
            }

            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    questionRepository.getQuestionById(notificationBlock.getQuestionId(), new TaskListener.State<Question>() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("AAA", e.toString());
                        }

                        @Override
                        public void onSuccess(Question question) {
                            if (question != null) {
                                String notiContent = notiContent = result.getUsername() + " ";
                                if (notificationBlock.getNumber() > 1) {
                                    notiContent += "and " + String.valueOf(notificationBlock.getNumber()) + " others ";
                                }
                                notiContent += notificationBlock.getAction().toLowerCase();
                                if (notificationBlock.getAction().equals("COMMENT")) {
                                    notiContent += "ed your question \"";
                                } else {
                                    notiContent += "d your question \"";
                                }
                                notiContent += question.getTitle() + "\".";
                                txtContent.setText(notiContent);
                            }
                        }
                    });

                }
            }
        });
        return view;
    }
}
