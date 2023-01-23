package com.example.mynotes.model;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Note {

    private int id;
    private String title;
    private String content;
    private String backgroundColor;
    private String dateTime;
    private Set<String> folders = new HashSet<>();

    public Note(String content) {
        this.title = "";
        this.content = content;
        id = new Random().nextInt();
        folders.add("Notes");
        folders.add("All Notes");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.now().toString();
        }
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Set<String> getFolders() {
        return folders;
    }

    public void setFolders(Set<String> folders) {
        this.folders = folders;
    }

    public void addFolder(String folder) {
        this.folders.add(folder);
    }


}
