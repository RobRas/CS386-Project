package com.cs386.assignment.attendancetracker;

/**
 * Created by rsr47 on 3/31/2016.
 */
public class Student extends Person {
    private String _macAddress;
    private int _attendance;
    private boolean _inAttendance;

    public Student(String id, String firstName, String lastName, String macAddress) {
        super(id, firstName, lastName);
        _macAddress = macAddress;
    }

    public String getMacAddress() {
        return _macAddress;
    }

    public int getAttendance() {
        return _attendance;
    }

    public void setAttendance(int value) {
        _attendance = value;
    }

    public boolean getInAttendance() {
        return _inAttendance;
    }

    public void setInAttendance(boolean value) { _inAttendance = value; }
}
