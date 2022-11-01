package com.example.mynotes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Note {

    private int id;
    private String content;
    private String backgroundColor;
    private LocalDateTime dateTime;
    private ArrayList<String> folders;

    public Note(String content) {
        this.content = content;
        id = new Random().nextInt();
        folders = new ArrayList<>();
        folders.add("AllNotes");
        folders.add("Notes");
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

    public ArrayList<String> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<String> folders) {
        this.folders = folders;
    }

    public void addFolder(String folder) {
        this.folders.add(folder);
    }

    public void deleteFolder(String folder) {
        this.folders.remove(folder);
    }


}
