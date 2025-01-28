package com.example.flynow.Model;

public class Location {
    private int Id;
    private String Name;

    public Location() {
    }

    @Override//ממיר את שמות המקומות לסטרינג
    public String toString() {
        return Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
