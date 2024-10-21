package com.example.androidlabs;

public class ToDoItem {
    private final String text;
    private final boolean urgent;

    public ToDoItem(String text, boolean urgent) {
        this.text = text;
        this.urgent = urgent;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return urgent;
    }
}