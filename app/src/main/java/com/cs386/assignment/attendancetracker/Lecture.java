package com.cs386.assignment.attendancetracker;

/**
 * Created by rsr47 on 3/31/2016.
 */
public class Lecture {
    private String _id;
    private String _name;

    public Lecture(String id, String name) {
        _id = id;
        _name = name;
    }

    public String getID() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
