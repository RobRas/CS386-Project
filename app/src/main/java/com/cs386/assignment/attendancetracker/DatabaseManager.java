package com.cs386.assignment.attendancetracker;

import android.util.Log;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

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

    public ArrayList<Student> getStudentsInLecture(Lecture lecture) {
        ArrayList<Student> studentList = new ArrayList<>();

        // Get student IDs
        Thread t = new AccessDatabaseThread("select student_id from student_enrollment where class_id = '" + lecture.getID() + "'");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) { }

        String[] studentIDs = parseHTML(result);

        if (studentIDs.length > 0) {
            StringBuilder conditions = new StringBuilder();
            conditions.append("student_id = '" + studentIDs[1] + "'");
            for (int i = 1; i < studentIDs.length; i++) {
                conditions.append(" OR ");
                conditions.append("student_id = '" + studentIDs[i] + "'");
            }

            t = new AccessDatabaseThread("select * from student where " + conditions.toString());
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
            }

            String[] s = parseHTML(result);
            for (int i = 1; i < s.length; i += 3) { // s[0] is ""
                studentList.add(new Student(s[i], s[i + 1], s[i + 2]));
            }
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

    public void getStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        // Call setAttendance on each student for the given lecture
        Thread t = new AccessDatabaseThread("select student_id, days_attended from student_enrollment where class_id = '" + lecture.getID() + "'");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) { }
        String[] studentInfo = parseHTML(result);

        for (Student student : studentList) {
            for (int i = 1; i < studentInfo.length; i += 2) {
                if (student.getID().equals(studentInfo[i])) {
                    student.setAttendance(Integer.parseInt(studentInfo[i+1]));
                }
            }
        }
    }

    public void incrementStudentAttendance(ArrayList<Student> studentList, Lecture lecture) {
        for (Student student : studentList) {
            if (student.getInAttendance()) {
                Thread t = new AccessDatabaseThread("update student_enrollment set days_attended = days_attended+1 where student_id = '" + student.getID() + "' and class_id = '" + lecture.getID() + "'");
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) { }
            }
        }
    }

    public void getMacAddresses(ArrayList<Student> studentList) {
        for (Student student : studentList) {
            Thread t = new AccessDatabaseThread("select * from mac where student_id = '" + student.getID() + "'");
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) { }

            String[] s = parseHTML(result);
            for (int i = 1; i < s.length; i += 2) {
                student.addMacAddress(s[i].toUpperCase());
            }
        }
    }

    public ArrayList<Lecture> getLectures(Teacher teacher) {
        ArrayList<Lecture> lectureList = new ArrayList<>();

        Thread t = new AccessDatabaseThread("select * from class");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) { }

        String[] s = parseHTML(result);
        for (int i = 1; i < s.length; i += 2) { // s[0] is ""
            lectureList.add(new Lecture(s[i], s[i+1]));
        }

        // Sorts list by lecture name
        Collections.sort(lectureList, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture l1, Lecture l2) {
                return Collator.getInstance().compare(l1.getName(), l2.getName());
            }
        });

        return lectureList;
    }

    private String[] parseHTML(String html) {
        html = html.replaceAll("\t", "");
        html = html.replaceAll("<table>", "");
        html = html.replaceAll("</table>", "");
        html = html.replaceAll("<tr>", "");
        html = html.replaceAll("</tr>", "");

        String[] s = html.split("<td>");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replace("</td>", "");
        }

        return s;
    }


    private class AccessDatabaseThread extends Thread {
        String query;

        public AccessDatabaseThread(String query) {
            this.query = query.replace(" ", "%20");
            this.query = this.query.replace("'", "%27");
            this.query = this.query.replace(";", "%3B");
            this.query = this.query.replace(",", "%2C");
            this.query = this.query.replace("=", "%3D");
            this.query = this.query.replace(":", "%3A");
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
