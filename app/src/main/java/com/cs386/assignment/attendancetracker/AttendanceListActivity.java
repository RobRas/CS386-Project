package com.cs386.assignment.attendancetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

public class AttendanceListActivity extends AppCompatActivity {

    private void createAttendanceList(LinkedList<Student> studentList) {
        for (Student student : studentList) {
            createNewAttendance(student);
        }
    }

    private LinkedList<Student> getStudents(Lecture lecture) {  // Lecture param for database lookup. Unused currently
        LinkedList<Student> studentList = new LinkedList<Student>();

        Student s1 = new Student("1", "Robert Rasmussen", "123");
        s1.setAttendance(5);
        studentList.add(s1);
        Student s2 = new Student("1", "Don Speer", "456");
        s2.setAttendance(4);
        studentList.add(s2);
        Student s3 = new Student("1", "Steven Massey", "789");
        s3.setAttendance(2);
        studentList.add(s3);

        return studentList;
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

        ((LinearLayout)findViewById(R.id.attendanceLayout)).addView(ll);
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

        ((TextView)findViewById(R.id.classNameText)).setText(lecture.getName());

        LinkedList<Student> studentList = getStudents(lecture);
        createAttendanceList(studentList);
    }

}
