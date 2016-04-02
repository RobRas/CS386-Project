package com.cs386.assignment.attendancetracker;

import java.util.LinkedList;
import java.util.Random;


/*
 * This class should be the only way you interact with the database.
 * Add new static methods as needed and rewrite the old methods to
 * access the database.
 */
public final class DatabaseManager {
    private DatabaseManager() {
        // Static class, should never be instantiated
    }

    public static LinkedList<Student> getStudentsInLecture(Lecture lecture) {
        LinkedList<Student> studentList = new LinkedList<>();

        Student s0 = new Student("0", "Robert Rasmussen", "123");
        Student s1 = new Student("1", "Don Speer", "456");
        Student s2 = new Student("2", "Steven Massey", "789");
        Student s3 = new Student("3", "Leila Harrison", "914");
        Student s4 = new Student("4", "Zachary Patten", "524");

        studentList.add(s0);
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);
        studentList.add(s4);

        return studentList;
    }

    public static LinkedList<Student> getStudentAttendance(LinkedList<Student> studentList, Lecture lecture) {
        Random random = new Random();
        for (Student student : studentList) {
            student.setAttendance(random.nextInt(5));
        }

        return studentList;
    }

    public static void incrementStudentAttendance(LinkedList<Student> studentList, Lecture lecture) {
        for (Student student : studentList) {
            if (student.getInAttendance()) {
                // Increment student attendance for the given lecture here
            }
        }
    }

    public static LinkedList<Lecture> getLectures(Teacher teacher) {
        LinkedList<Lecture> lectureList = new LinkedList<>();

        Lecture lecture0 = new Lecture("0", "CS 126");
        Lecture lecture1 = new Lecture("1", "CS 126");
        Lecture lecture2 = new Lecture("2", "CS 136");
        Lecture lecture3 = new Lecture("3", "CS 200");

        lectureList.add(lecture0);
        lectureList.add(lecture1);
        lectureList.add(lecture2);
        lectureList.add(lecture3);

        return lectureList;
    }
}
