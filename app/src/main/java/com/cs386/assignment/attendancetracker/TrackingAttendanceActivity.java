package com.cs386.assignment.attendancetracker;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class TrackingAttendanceActivity extends AppCompatActivity {
    private final int REQUEST_ENABLE_BT = 1;

    private ProgressDialog progress;
    private Lecture lecture;
    private ArrayList<Student> studentList;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean wasEnabled;

    private void createAttendanceList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wasEnabled = mBluetoothAdapter.isEnabled();
        if (!wasEnabled) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            checkAttendance();
        }
    }

    private void checkAttendance() {
        progress.setMessage("Taking attendance...");
        for (Student student : studentList) {
            Thread t = new BluetoothThread(student);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) { }

            createNewAttendance(student);
        }

        if (!wasEnabled) {
            mBluetoothAdapter.disable();
        }

        DatabaseManager.getInstance().incrementStudentAttendance(studentList, lecture);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                checkAttendance();
            }
        }
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

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Accessing Database...");
        progress.setCancelable(true);
        progress.show();

        studentList = DatabaseManager.getInstance().getStudentsInLecture(lecture);
        DatabaseManager.getInstance().getMacAddresses(studentList);

        createAttendanceList();

        progress.dismiss();
    }

    private class BluetoothThread extends Thread {
        Student student;

        public BluetoothThread(Student student) {
            this.student = student;
        }

        @Override
        public void run() {
            for (String macAddress : student.getMacAddresses()) {
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
                if (device.getName() != null) {
                    student.setInAttendance(true);
                    break;
                } else {
                    // No device found
                }
            }
        }
    }
}
