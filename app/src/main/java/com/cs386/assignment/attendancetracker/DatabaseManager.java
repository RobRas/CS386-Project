package com.cs386.assignment.attendancetracker;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
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

        studentList.add(new Student("0", "Robert", "Rasmussen", "123"));
        studentList.add(new Student("1", "Don", "Speer", "456"));
        studentList.add(new Student("2", "Steven", "Massey", "789"));
        studentList.add(new Student("3", "Leila", "Harrison", "914"));
        studentList.add(new Student("4", "Zachary", "Patten", "524"));

        // Sorts the list by last name. Keep me!
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Collator.getInstance().compare(s1.getLastName(), s2.getLastName());
            }
        });

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
}
