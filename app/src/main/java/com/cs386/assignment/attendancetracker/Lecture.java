package com.cs386.assignment.attendancetracker;

/**
 * Created by rsr47 on 3/31/2016.
 */
public class Lecture {
    private int _id;
    private String _name;

    public Lecture(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
