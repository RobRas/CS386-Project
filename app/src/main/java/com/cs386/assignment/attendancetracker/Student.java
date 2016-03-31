package com.cs386.assignment.attendancetracker;

/**
 * Created by rsr47 on 3/31/2016.
 */
public class Student extends Person {
    private String _macAddress;

    public Student(String id, String name, String macAddress) {
        super(id, name);
        _macAddress = macAddress;
    }

    public String getMacAddress() {
        return _macAddress;
    }
}
