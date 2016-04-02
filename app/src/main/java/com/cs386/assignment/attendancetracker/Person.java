package com.cs386.assignment.attendancetracker;

public class Person {
    private String _id;
    private String _firstName;
    private String _lastName;

    public Person(String id, String firstName, String lastName) {
        _id = id;
        _firstName = firstName;
        _lastName = lastName;
    }

    public String getID() {
        return _id;
    }

    public String getFirstName() { return _firstName; }

    public String getLastName() { return _lastName; }

    public String getName() { return _firstName + " " + _lastName; }
}
