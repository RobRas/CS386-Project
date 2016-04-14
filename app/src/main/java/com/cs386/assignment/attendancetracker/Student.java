package com.cs386.assignment.attendancetracker;

import java.util.ArrayList;

public class Student extends Person {
    private ArrayList<String> _macAddresses;
    private int _attendance;
    private boolean _inAttendance;

    public Student(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
        _macAddresses = new ArrayList<>();
    }

    public ArrayList<String> getMacAddresses() {
        return _macAddresses;
    }

    public void addMacAddress(String macAddress) {
        _macAddresses.add(macAddress);
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
