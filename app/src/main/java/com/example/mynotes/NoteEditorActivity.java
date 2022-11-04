package com.example.mynotes;

import static com.example.mynotes.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.mynotes.model.Note;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NoteEditorActivity extends AppCompatActivity {

    static int notePosition;
    static Note note;

    CardView colorPalette;
    CardView saveToFolder;

    MainActivity mainActivity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // options menu on the title bar ---------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        colorPalette = findViewById(id.colorPaletteCard);
        saveToFolder = findViewById(id.save_to_folder);
        
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case id.color:
                if (colorPalette.getVisibility() == View.INVISIBLE){
                    colorPalette.setVisibility(View.VISIBLE);
                } else {
                    colorPalette.setVisibility(View.INVISIBLE);
                }
                return true;
            case id.save_to_folder:
                if (saveToFolder.getVisibility() == View.INVISIBLE){
                    saveToFolder.setVisibility(View.VISIBLE);
                } else {
                    saveToFolder.setVisibility(View.INVISIBLE);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        colorPalette = findViewById(id.colorPaletteCard);
        saveToFolder = findViewById(id.save_to_folder);

        colorPalette.getGlobalVisibleRect(viewRect);
        if (colorPalette.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            colorPalette.setVisibility(View.INVISIBLE);
            return true;
        }

        saveToFolder.getGlobalVisibleRect(viewRect);
        if (saveToFolder.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            saveToFolder.setVisibility(View.INVISIBLE);
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }


    //==============================================================================================
    // ON CREATE
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_note_editor);

        MainActivity.listView.setAdapter(MainActivity.noteAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        EditText editText = findViewById(id.edit_text);
        Intent intent = getIntent();

        notePosition = intent.getIntExtra("noteId", -1);
        if (notePosition != -1) {
            note = MainActivity.allNotes.get(notePosition);
            editText.setText(note.getContent());
            notePosition = MainActivity.allNotes.indexOf(note);
        } else {
            note = new Note("");
            MainActivity.notesToShow.add(0, note);
            MainActivity.allNotes.add(0,note);
            notePosition = MainActivity.allNotes.indexOf(note);
            MainActivity.noteAdapter.notifyDataSetChanged();
        }


        // managing EditText and changes in note ---------------------------------------------------
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.allNotes);
        sharedPreferences.edit().putString("Notes", json).apply();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.allNotes.get(notePosition).setContent(String.valueOf(charSequence));
                MainActivity.noteAdapter.notifyDataSetChanged();

                // Creating Object of SharedPreferences to store data in the phone
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.allNotes);
                sharedPreferences.edit().putString("Notes", json).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // creating chips of the folders of the note -----------------------------------------------
        Set<String> foldersForChips = new HashSet<>(note.getFolders());
        foldersForChips.remove("Notes");
        foldersForChips.remove("All Notes");

        for (String folder : foldersForChips ) {
            ChipGroup foldersChips = findViewById(id.chip_group_folder_of_the_note);
            foldersChips.setChipSpacingHorizontal(8);
            foldersChips.setPadding(0,0,0,0);
            foldersChips.setClickable(false);
            foldersChips.setFocusable(false);
            foldersChips.setBackgroundColor(Color.TRANSPARENT);

            Chip newChip = new Chip(foldersChips.getContext());
            newChip.setText(folder);
            ChipDrawable chipFolderDrawable = ChipDrawable.createFromAttributes(this, null,0, R.style.Widget_App_Chip);
            newChip.setChipDrawable(chipFolderDrawable);

            foldersChips.addView(newChip);
        }


        // creating folders chips on CardView and handle saving note to folder ---------------------
        Set<String> foldersForSavingChips = new HashSet<>(MainActivity.folders);
        foldersForSavingChips.remove("Notes");
        foldersForSavingChips.remove("All Notes");
        foldersForSavingChips.remove("Recycle Bin");

        for (String folder : foldersForSavingChips) {
            ChipGroup foldersChips = findViewById(R.id.chip_group_folders_to_save);
            Chip newChip = new Chip(foldersChips.getContext());
            newChip.setText(folder);
            foldersChips.addView(newChip);

            newChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (note.getFolders().contains(folder)) {
                        note.getFolders().remove(folder);
                    } else {
                        note.addFolder(folder);
                    }

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.allNotes);
                    sharedPreferences.edit().putString("Notes", json).apply();

                    recreate();
                }
            });
        }


        // fabs for changing color -----------------------------------------------------------------
        Map<Integer, String> colors = new HashMap<>();
        colors.put(id.pink, "#E8B2B5");
        colors.put(id.blue, "#2E4F6C");
        colors.put(id.green, "#7E9C65");
        colors.put(id.yellow, "#D1C357");

        for (Integer key : colors.keySet()) {
            FloatingActionButton fabColor = findViewById(key);
            fabColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.listView != null) {
                        editText.setBackgroundColor(Color.parseColor(colors.get(key)));
                        note.setBackgroundColor(colors.get(key));

                        CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                        card.setCardBackgroundColor(Color.parseColor(colors.get(key)));

                        MainActivity.notesToShow.set(notePosition, note);

                        ConstraintLayout layout_edit = findViewById(id.layout_edit_note);
                        layout_edit.setBackgroundColor(Color.parseColor(colors.get(key)));

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(MainActivity.allNotes);
                        sharedPreferences.edit().putString("Notes", json).apply();
                    }
                }
            });
        }

        // fab for changing color to WHITE ---------------------------------------------------------
        FloatingActionButton fabWhite = findViewById(id.white);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    int mode = AppCompatDelegate.getDefaultNightMode();
                    if (mode == 1) {
                        note.setBackgroundColor(null);
                    } else if (mode == 2){
                        note.setBackgroundColor("#FFFFFF");
                    }
                    editText.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#FFFFFF"));

                    MainActivity.notesToShow.set(notePosition, note);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.allNotes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

        // fab for changing color to BLACK ---------------------------------------------------------
        FloatingActionButton fabRed = findViewById(id.black);
        fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    int mode = AppCompatDelegate.getDefaultNightMode();
                    if (mode == 2) {
                        note.setBackgroundColor(null);
                    } else if (mode == 1){
                        note.setBackgroundColor("#111111");
                    }
                    editText.setBackgroundColor(Color.parseColor("#111111"));

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#111111"));

                    MainActivity.notesToShow.set(notePosition, note);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.allNotes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

        // changing the background color
        if(note.getBackgroundColor() != null) {
            editText.setBackgroundColor(Color.parseColor(note.getBackgroundColor()));
            ConstraintLayout layout_edit = findViewById(id.layout_edit_note);
            layout_edit.setBackgroundColor(Color.parseColor(note.getBackgroundColor()));
        }
    }


}