package com.ogif.kotae.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.ogif.kotae.R;
import com.ogif.kotae.data.model.ManagedUser;
import com.ogif.kotae.data.model.Post;
import com.ogif.kotae.data.model.Question;
import com.ogif.kotae.utils.ManagedQuestionAdapter;
import com.ogif.kotae.utils.ManagedUserAdapter;

import java.util.Date;

public class AdminActivity extends AppCompatActivity {
    TabHost tabHost;
    ListView lvUser, lvQuestion;
    ManagedUserAdapter managedUserAdapter;
    ManagedQuestionAdapter managedQuestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addControls();
        fakeData();
    }

    private void addControls() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabQuestion = tabHost.newTabSpec("question");
        TabHost.TabSpec tabUser = tabHost.newTabSpec("user");

        tabQuestion.setContent(R.id.tab_question);
        tabUser.setContent(R.id.tab_user);

        tabQuestion.setIndicator("Question");
        tabUser.setIndicator("User");

        tabHost.addTab(tabQuestion);
        tabHost.addTab(tabUser);

        lvUser = (ListView) findViewById(R.id.lv_user_admin);
        managedUserAdapter = new ManagedUserAdapter(AdminActivity.this, R.layout.partial_user_item_admin);
        lvUser.setAdapter(managedUserAdapter);

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ManagedUser managedUser = managedUserAdapter.getItem(i);
                System.out.println(managedUser.getUserName());
            }
        });

        lvQuestion = (ListView) findViewById(R.id.lv_question_admin);
        managedQuestionAdapter = new ManagedQuestionAdapter(AdminActivity.this,R.layout.partial_question_item_admin);
        lvQuestion.setAdapter(managedQuestionAdapter);
    }

    private void fakeData() {
        managedUserAdapter.add(new ManagedUser("1", "Nguyen Van A", 10, 5, 3, 1, false));
        managedUserAdapter.add(new ManagedUser("2", "Nguyen Van B", 10, 5, 3, 1, true));
        managedUserAdapter.add(new ManagedUser("3", "Nguyen Van C", 10, 5, 3, 1, false));
        managedUserAdapter.add(new ManagedUser("4", "Nguyen Van D", 10, 5, 3, 1, false));
        managedUserAdapter.add(new ManagedUser("5", "Nguyen Van E", 10, 5, 3, 1, true));

        managedQuestionAdapter.add(new Question().setTitle("Question 1").setAuthorId("Alibaba").setContent("Cho em hỏi tí").setPostTime(new Date().getTime()).setUpvote(10).setDownvote(10).setReport(3).setBlocked(true));
        managedQuestionAdapter.add(new Question().setTitle("Question 1").setAuthorId("Alibaba").setContent("Cho em hỏi tí").setPostTime(new Date().getTime()).setUpvote(10).setDownvote(10).setReport(3).setBlocked(true));
        managedQuestionAdapter.add(new Question().setTitle("Question 1").setAuthorId("Alibaba").setContent("Cho em hỏi tí").setPostTime(new Date().getTime()).setUpvote(10).setDownvote(10).setReport(3).setBlocked(true));
        managedQuestionAdapter.add(new Question().setTitle("Question 1").setAuthorId("Alibaba").setContent("Cho em hỏi tí").setPostTime(new Date().getTime()).setUpvote(10).setDownvote(10).setReport(3).setBlocked(true));
        managedQuestionAdapter.add(new Question().setTitle("Question 1").setAuthorId("Alibaba").setContent("Cho em hỏi tí").setPostTime(new Date().getTime()).setUpvote(10).setDownvote(10).setReport(3).setBlocked(true));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }
}