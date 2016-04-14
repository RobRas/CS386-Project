package com.cs386.assignment.attendancetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class TrackingAttendanceActivity extends AppCompatActivity {
    private Lecture lecture;

    private void createAttendanceList(ArrayList<Student> studentList) {
        Random random = new Random();   // Remove me later!
        for (Student student : studentList) {
            // Bluetooth code goes here
            student.setInAttendance(random.nextBoolean());  // Remove me later!
            createNewAttendance(student);
        }

        DatabaseManager.getInstance().incrementStudentAttendance(studentList, lecture);
    }

    private void createNewAttendance(Student student) {
        LinearLayout ll = new LinearLayout(this);

        TextView studentName = new TextView(this);
        studentName.setText(student.getName());
        ll.addView(studentName);

        TextView attendance = new TextView(this);
        attendance.setText(student.getInAttendance() ? "Present" : "Absent");
        attendance.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        attendance.setGravity(Gravity.RIGHT);
        ll.addView(attendance);

        ((LinearLayout)findViewById(R.id.trackingAttendanceLayout)).addView(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tracked Attendance");

        Intent intent = getIntent();
        String lectureID = intent.getStringExtra(ClassActivity.LECTURE_ID_MESSAGE);
        String lectureName = intent.getStringExtra(ClassActivity.LECTURE_NAME_MESSAGE);
        lecture = new Lecture(lectureID, lectureName);

        ((TextView)findViewById(R.id.trackingAttendanceClassNameLabel)).setText(lecture.getName());

        ArrayList<Student> studentList = DatabaseManager.getInstance().getStudentsInLecture(lecture);
        DatabaseManager.getInstance().getMacAddresses(studentList);

        createAttendanceList(studentList);
    }

}
