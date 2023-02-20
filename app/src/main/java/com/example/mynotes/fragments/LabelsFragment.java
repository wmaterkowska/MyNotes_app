package com.example.mynotes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mynotes.MainActivity;
import com.example.mynotes.R;
import com.example.mynotes.model.Note;
import com.example.mynotes.services.LabelsContainer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class LabelsFragment extends Fragment {

    FragmentManager fragmentManager;

    public LabelsFragment() {
        super(R.layout.activity_labels);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_labels, container, false);

        LabelsContainer labelsContainer = new LabelsContainer(getContext().getApplicationContext());
        for (String label : labelsContainer.getLabels()) {
            ChipGroup labelsChips = view.findViewById(R.id.chip_group);
            Chip newChip = new Chip(labelsChips.getContext());
            newChip.setText(label);
            labelsChips.addView(newChip);

            if (label == "Recycle Bin" || label == "All Notes") {
                newChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.notesToShow.clear();
                        for (Note note : MainActivity.allNotes) {
                            if (note.getLabels().contains(label)) {
                                MainActivity.notesToShow.add(note);
                            }
                        }
                        MainActivity.listView.setAdapter(MainActivity.noteAdapter);
                    }
                });
            } else {
                newChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.notesToShow.clear();
                        for (Note note : MainActivity.allNotes) {
                            if (note.getLabels().contains(label) && !note.getLabels().contains("Recycle Bin")) {
                                MainActivity.notesToShow.add(note);
                            }
                        }
                        MainActivity.listView.setAdapter(MainActivity.noteAdapter);
                    }
                });
            }
        }


        return view;
    }

}
