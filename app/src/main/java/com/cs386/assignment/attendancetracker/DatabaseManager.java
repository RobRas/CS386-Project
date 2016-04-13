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
    private String result;

    private static final DatabaseManager _instance = new DatabaseManager();

    private DatabaseManager() {
        // Static class, should never be instantiated

        try {
            // This is RIDICULOUSLY insecure code. Fix me up if there's time.
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
            }};
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) { }
    }

    public static DatabaseManager getInstance() { return _instance; }

    public static String parseHTML(String html) {
        html = html.replaceAll("\t", "");
        html = html.replaceAll("<table>", "");
        html = html.replaceAll("</table>", "");
        html = html.replaceAll("<tr>", "");
        html = html.replaceAll("</tr>", "");

        return html;
    }

    public ArrayList<Student> getStudentsInLecture(Lecture lecture) {
        ArrayList<Student> studentList = new ArrayList<>();

        Thread t = new AccessDatabaseThread("select * from student");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) { }

        String[] s = parseHTML(result).split("<td>");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replace("</td>", "");
        }
        for (int i = 1; i < s.length; i += 3) { // s[0] is ""
            studentList.add(new Student(s[i], s[i+1], s[i+2]));
        }

        // Sorts list by last name.
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Collator.getInstance().compare(s1.getLastName(), s2.getLastName());
            }
        });

        return studentList;
    }

    public static ArrayList<Student> getStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        // Call setAttendance on each student for the given lecture
        Random random = new Random();
        for (Student student : studentList) {
            student.setAttendance(random.nextInt(5));
        }

        return studentList;
    }

    public static void incrementStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        for (Student student : studentList) {
            if (student.getInAttendance()) {
                // Increment student attendance in the database for the given lecture here
            }
        }
    }

    public static ArrayList<Lecture> getLectures(Teacher teacher) {
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

    private class AccessDatabaseThread extends Thread {
        String query;

        public AccessDatabaseThread(String query) {
            this.query = query.replace(" ", "%20");
            this.query = this.query.replace("'", "%27");
            this.query = this.query.replace(";", "%3B");
            this.query = this.query.replace(",", "%2C");
        }

        @Override
        public void run() {
            StringBuffer sb = new StringBuffer();
            try {
                URL databaseURL = new URL("https://mygeeto.cefns.nau.edu/attendance/query.php?q=" + query);

                BufferedReader in = new BufferedReader(new InputStreamReader(databaseURL.openStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }

                in.close();
            } catch(Exception e) {
                Log.e("ERROR", e.toString());
            }

            result = sb.toString();
        }
    }
}
