package com.cs386.assignment.attendancetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ClassActivity extends AppCompatActivity {
    public final static String LECTURE_ID_MESSAGE = "com.cs386.assignment.attendancetracker.CLASS_ID";
    public final static String LECTURE_NAME_MESSAGE = "com.cs386.assignment.attendancetracker.CLASS_NAME";

    private Lecture lecture;

    public void onClickTrackAttendance(View view) {
        Intent intent = new Intent(this, TrackingAttendanceActivity.class);
        intent.putExtra(LECTURE_ID_MESSAGE, lecture.getID());
        intent.putExtra(LECTURE_NAME_MESSAGE, lecture.getName());
        startActivity(intent);
    }

    public void onClickShowAttendance(View view) {
        Intent intent = new Intent(this, AttendanceListActivity.class);
        intent.putExtra(LECTURE_ID_MESSAGE, lecture.getID());
        intent.putExtra(LECTURE_NAME_MESSAGE, lecture.getName());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class");

        Intent intent = getIntent();

        String lectureID = intent.getStringExtra(ClassList.LECTURE_ID_MESSAGE);
        String lectureName = intent.getStringExtra(ClassList.LECTURE_NAME_MESSAGE);
        lecture = new Lecture(lectureID, lectureName);

        TextView label = (TextView)findViewById(R.id.ClassName);
        label.setText(lecture.getName());
    }

}
