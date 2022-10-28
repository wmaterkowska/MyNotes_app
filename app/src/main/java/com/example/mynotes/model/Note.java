package com.example.mynotes.model;

import java.time.LocalDateTime;
import java.util.Random;

public class Note {

    private int id = 0;
    private String content;
    private String backgroundColor;
    private LocalDateTime dateTime;

    public Note(String content) {
        this.content = content;
        this.id += 1;
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {this.id = id;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
