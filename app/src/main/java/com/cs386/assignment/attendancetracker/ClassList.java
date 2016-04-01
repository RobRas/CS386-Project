package com.cs386.assignment.attendancetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.LinkedList;

public class ClassList extends AppCompatActivity {
    public final static String LECTURE_ID_MESSAGE = "com.cs386.assignment.attendancetracker.CLASS_ID";
    public final static String LECTURE_NAME_MESSAGE = "com.cs386.assignment.attendancetracker.CLASS_NAME";

    private void createButtons(LinkedList<Lecture> lectureList) {
        for (Lecture lecture : lectureList) {
            createButton(lecture);
        }
    }

    private void createButton(final Lecture lecture) {
        Button button = new Button(this);
        button.setText(lecture.getName());

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, ClassActivity.class);
                intent.putExtra(LECTURE_ID_MESSAGE, lecture.getID());
                intent.putExtra(LECTURE_NAME_MESSAGE, lecture.getName());
                startActivity(intent);
            }
        });

        LinearLayout ll = (LinearLayout)findViewById(R.id.ButtonLinearLayout);
        ll.addView(button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Temporary! Pull the Teacher values from the database
        Teacher teacher = new Teacher("123", "Omar");

        LinkedList<Lecture> lectureList = new LinkedList<Lecture>();

        Lecture lecture0 = new Lecture("0", "CS 126");
        Lecture lecture1 = new Lecture("1", "CS 126");
        Lecture lecture2 = new Lecture("2", "CS 136");
        Lecture lecture3 = new Lecture("3", "CS 200");

        lectureList.add(lecture0);
        lectureList.add(lecture1);
        lectureList.add(lecture2);
        lectureList.add(lecture3);

        createButtons(lectureList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
