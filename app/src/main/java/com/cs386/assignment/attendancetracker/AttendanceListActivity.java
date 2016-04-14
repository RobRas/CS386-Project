package com.cs386.assignment.attendancetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceListActivity extends AppCompatActivity {

    private ProgressDialog progress;

    private void showList(ArrayList<Student> studentList) {
        for (Student student : studentList) {
            createNewAttendance(student);
        }
    }

    private void createNewAttendance(Student student) {
        LinearLayout ll = new LinearLayout(this);

        TextView studentName = new TextView(this);
        studentName.setText(student.getName());
        ll.addView(studentName);

        TextView attendance = new TextView(this);
        attendance.setText(Integer.toString(student.getAttendance()));
        attendance.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        attendance.setGravity(Gravity.RIGHT);
        ll.addView(attendance);

        ((LinearLayout)findViewById(R.id.attendanceListLayout)).addView(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Attendance");

        Intent intent = getIntent();
        String lectureID = intent.getStringExtra(ClassActivity.LECTURE_ID_MESSAGE);
        String lectureName = intent.getStringExtra(ClassActivity.LECTURE_NAME_MESSAGE);
        Lecture lecture = new Lecture(lectureID, lectureName);

        ((TextView)findViewById(R.id.attendanceListClassNameLabel)).setText(lecture.getName());

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Accessing Database...");
        progress.setCancelable(true);
        progress.show();

        ArrayList<Student> studentList = DatabaseManager.getInstance().getStudentsInLecture(lecture);
        DatabaseManager.getInstance().getStudentAttendance(studentList, lecture);
        showList(studentList);

        progress.dismiss();
    }

}
