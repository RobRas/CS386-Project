package com.cs386.assignment.attendancetracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/*
 * This class should be the only way you interact with the database.
 * Add new static methods as needed and rewrite the old methods to
 * access the database.
 */
public final class DatabaseManager {
    private static final DatabaseManager _instance = new DatabaseManager();

    private String result;

    private DatabaseManager() {
        // Static class, should never be instantiated
    }

    public static DatabaseManager getInstance() {
        return _instance;
    }

    public ArrayList<Student> getStudentsInLecture(Context context, Lecture lecture) {
        ArrayList<Student> studentList = new ArrayList<>();

        Thread t = new Thread(new GetDatabaseRunnable(context, "select * from student"));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("URL", result);

        // Replace me with code that accesses the database
        studentList.add(new Student("0", "Robert", "Rasmussen", "00:11:22:33:AA:BB"));
        studentList.add(new Student("1", "Don", "Speer", "44:55:66:77:CC:DD"));
        studentList.add(new Student("2", "Steven", "Massey", "88:99:11:22:EE:FF"));
        studentList.add(new Student("3", "Leila", "Harrison", "22:33:44:77:AA:BB"));
        studentList.add(new Student("4", "Zachary", "Patten", "55:66:77:33:CC:DD"));

        // Sorts the list by last name. Keep me!
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Collator.getInstance().compare(s1.getLastName(), s2.getLastName());
            }
        });

        return studentList;
    }

    public ArrayList<Student> getStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        // Call setAttendance on each student for the given lecture
        Random random = new Random();
        for (Student student : studentList) {
            student.setAttendance(random.nextInt(5));
        }

        return studentList;
    }

    public void incrementStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        for (Student student : studentList) {
            if (student.getInAttendance()) {
                // Increment student attendance in the database for the given lecture here
            }
        }
    }

    public ArrayList<Lecture> getLectures(Teacher teacher) {
        ArrayList<Lecture> lectureList = new ArrayList<>();

        // Replace me with code that accesses the database
        lectureList.add(new Lecture("0", "CS 126"));
        lectureList.add(new Lecture("1", "CS 126"));
        lectureList.add(new Lecture("2", "CS 136"));
        lectureList.add(new Lecture("3", "CS 200"));

        // Sorts the list by class name. Keep me!
        Collections.sort(lectureList, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture l1, Lecture l2) {
                return Collator.getInstance().compare(l1.getName(), l2.getName());
            }
        });

        return lectureList;
    }

    private class GetDatabaseRunnable implements Runnable {
        Context context;
        String query;
        ProgressDialog progress;

        public GetDatabaseRunnable(Context context, String query) {
            this.query = query.replace(" ", "%20");
            this.context = context;
            progress = new ProgressDialog(context);
            progress.setTitle("Please Wait...");
            progress.setMessage("Accessing Database...");
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        public void run() {
            result = "";
            try {
                URL databaseURL = new URL("https://mygeeto.cefns.nau.edu/attendance/query.php?q=" + query);

                // This is RIDICULOUSLY insecure code. Fix me up if there's time.
                SSLContext sc = SSLContext.getInstance("SSL");
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }
                } };
                sc.init(null, trustAllCerts, new SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                BufferedReader in = new BufferedReader(new InputStreamReader(databaseURL.openStream()));
                String inputLine;
                StringBuffer sb = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }

                in.close();
                result = sb.toString();
            } catch(Exception e) {
                Log.e("ERROR", e.toString());
            }
            progress.dismiss();
        }
    }
}
