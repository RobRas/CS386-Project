package com.cs386.assignment.attendancetracker;

public class Student extends Person {
    private String _macAddress;
    private int _attendance;
    private boolean _inAttendance;

    public Student(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public String getMacAddress() {
        return _macAddress;
    }

    public void setMacAddress(String macAddress) {
        _macAddress = macAddress;
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
