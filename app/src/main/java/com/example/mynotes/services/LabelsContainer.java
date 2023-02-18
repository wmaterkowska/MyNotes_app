package com.example.mynotes.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mynotes.MainActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class LabelsContainer {

    private ArrayList<String> labels;
    private Context context;

    public LabelsContainer(Context context) {
        this.context = context;
        this.labels = new ArrayList<>(Arrays.asList("Notes", "Recycle Bin", "All Notes"));
    }

    public ArrayList<String> getLabels() {

        Gson gson = new Gson();

        SharedPreferences labelsPreferences = context.getSharedPreferences("com.example.labels", Context.MODE_PRIVATE);
        String jsonLabelsFromPref = labelsPreferences.getString("Labels", null);
        Set<String> gsonLabels = gson.fromJson(jsonLabelsFromPref, Set.class);

        if (gsonLabels != null) {
            for (String label : gsonLabels) {
                if (!this.labels.contains(label)) {
                    this.labels.add(label);
                }
            }
        }
        return this.labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }
}
