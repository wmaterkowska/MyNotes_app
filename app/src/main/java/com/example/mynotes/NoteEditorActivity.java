package com.example.mynotes;

import static com.example.mynotes.MainActivity.noteAdapter;
import static com.example.mynotes.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NoteEditorActivity extends AppCompatActivity {

    static int notePosition;
    static Note note;

    CardView colorPalette;
    CardView saveToFolder;

    Set<String> labelsOfNote;
    Set<String> labelsForChipsToSave;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // TOP MENU ====================================================================================
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        colorPalette = findViewById(id.colorPaletteCard);
        saveToFolder = findViewById(id.save_to_label);
        
        switch (item.getItemId()) {
            case android.R.id.home:
                noteAdapter.resetData();
                noteAdapter.notifyDataSetChanged();
                this.finish();
                return true;
            case id.color:
                if (colorPalette.getVisibility() == View.INVISIBLE){
                    colorPalette.setVisibility(View.VISIBLE);
                } else {
                    colorPalette.setVisibility(View.INVISIBLE);
                }
                return true;
            case id.save_to_label:
                if (saveToFolder.getVisibility() == View.INVISIBLE){
                    saveToFolder.setVisibility(View.VISIBLE);
                } else {
                    saveToFolder.setVisibility(View.INVISIBLE);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    // for making card with settings disappear after clicking elsewhere ----------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        colorPalette = findViewById(id.colorPaletteCard);
        saveToFolder = findViewById(id.save_to_label);

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

        // MainActivity.listView.setAdapter(MainActivity.noteAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        // Edit text for Title and Content ---------------------------------------------------------
        EditText editTextTitle = findViewById(id.edit_text_title);
        EditText editTextContent = findViewById(id.edit_text);
        Intent intent = getIntent();


        notePosition = intent.getIntExtra("noteId", -1);

        if (notePosition != -1) {
            note = MainActivity.allNotes.get(notePosition);
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        } else {
            note = new Note("");
            MainActivity.notesToShow.add(0, note);
            MainActivity.allNotes.add(0,note);
        }

        noteAdapter.notifyDataSetChanged();
        Integer noteToShowPosition = MainActivity.notesToShow.indexOf(note);

        // changing the background color
        if(note.getBackgroundColor() != null) {
            editTextContent.setBackgroundColor(Color.parseColor(note.getBackgroundColor()));
            ConstraintLayout layout_edit = findViewById(id.layout_edit_note);
            layout_edit.setBackgroundColor(Color.parseColor(note.getBackgroundColor()));
            noteAdapter.notifyDataSetChanged();
        }


        // managing EditText for Note and changes in note ------------------------------------------
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.allNotes);
        sharedPreferences.edit().putString("Notes", json).apply();

        editTextTitle.addTextChangedListener(new TextWatcher() { // Title --------------------------
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesToShow.get(noteToShowPosition).setTitle(String.valueOf(charSequence));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    note.setDateTime(LocalDateTime.now().toString());
                }

                // Creating Object of SharedPreferences to store data in the phone
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.allNotes);
                sharedPreferences.edit().putString("Notes", json).apply();

                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextContent.addTextChangedListener(new TextWatcher() { // Content ----------------------
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesToShow.get(noteToShowPosition).setContent(String.valueOf(charSequence));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    note.setDateTime(LocalDateTime.now().toString());
                }

                // Creating Object of SharedPreferences to store data in the phone
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.allNotes);
                sharedPreferences.edit().putString("Notes", json).apply();

                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // creating chips of the labels of the note -----------------------------------------------
        labelsOfNote = new HashSet<>(note.getFolders());
        labelsOfNote.remove("Notes");
        labelsOfNote.remove("All Notes");

        for (String label : labelsOfNote) {
            ChipGroup foldersChips = findViewById(id.chip_group_folder_of_the_note);
            foldersChips.setChipSpacingHorizontal(2);
            foldersChips.setPadding(0,0,0,0);
            foldersChips.setClickable(false);
            foldersChips.setFocusable(false);
            foldersChips.setBackgroundColor(Color.TRANSPARENT);

            Chip labelOfNote = new Chip(foldersChips.getContext());
            labelOfNote.setText(label);
            labelOfNote.setClickable(false);
            labelOfNote.setFocusable(false);
            ChipDrawable chipFolderDrawable = ChipDrawable.createFromAttributes(this, null,0, style.Widget_App_Chip);
            labelOfNote.setChipDrawable(chipFolderDrawable);

            foldersChips.addView(labelOfNote);
        }

        // OPTIONS CARDS ===========================================================================

        // creating label chips on CardView and handle saving note with label ----------------------
        labelsForChipsToSave = new HashSet<>(MainActivity.folders);
        labelsForChipsToSave.remove("Notes");
        labelsForChipsToSave.remove("All Notes");

        ChipGroup labelsChips = findViewById(id.chip_group_labels_to_save);

        for (String folder : labelsForChipsToSave) {
            Chip newChip = new Chip(labelsChips.getContext());
            newChip.setText(folder);
            labelsChips.addView(newChip);

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
                    noteAdapter.notifyDataSetChanged();
                }
            });

            newChip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(NoteEditorActivity.this)
                            .setTitle(" ")
                            .setIcon(drawable.ic_baseline_delete_24)
                            .setTitle("Do you want to delete this label?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    labelsChips.removeView(newChip);
                                    MainActivity.folders.remove(folder);

                                    for (Note note : MainActivity.allNotes) {
                                        if (note.getFolders().contains(folder)) {
                                            note.getFolders().remove(folder);
                                        }
                                    }

                                    noteAdapter.notifyDataSetChanged();

                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(MainActivity.folders);
                                    sharedPreferences.edit().putString("Folders", json).apply();
                                }
                            }).setNegativeButton("No", null).show();
                    return true;
                }
            });

        }

        // button to add the folder ----------------------------------------------------------------
        FloatingActionButton addLabelButton;
        addLabelButton = findViewById(id.add_label_button);
        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(id.add_label_input);
                String newLabel = editText.getText().toString();

                if( newLabel != "") {
                    if (!MainActivity.folders.contains(newLabel)) {
                        MainActivity.folders.add(newLabel);
                    }
                    SharedPreferences foldersPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.folders);
                    foldersPreferences.edit().putString("Folders", json).apply();

                    Chip newChip = new Chip(labelsChips.getContext());
                    newChip.setText(newLabel);
                    labelsChips.addView(newChip);

                    newChip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            note.addFolder(newLabel);

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String json = gson.toJson(MainActivity.allNotes);
                            sharedPreferences.edit().putString("Notes", json).apply();

                            recreate();
                        }
                    });

                    newChip.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            new AlertDialog.Builder(NoteEditorActivity.this)
                                    .setTitle(" ")
                                    .setIcon(drawable.ic_baseline_delete_24)
                                    .setTitle("Do you want to delete this label?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            labelsChips.removeView(newChip);
                                            MainActivity.folders.remove(newLabel);

                                            for (Note note : MainActivity.allNotes) {
                                                if (note.getFolders().contains(newLabel)) {
                                                    note.getFolders().remove(newLabel);
                                                }
                                            }

                                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
                                            Gson gson = new Gson();
                                            String json = gson.toJson(MainActivity.folders);
                                            sharedPreferences.edit().putString("Folders", json).apply();
                                        }
                                    }).setNegativeButton("No", null).show();
                            return true;
                        }
                    });
                }
                editText.getText().clear();
            }

        });


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
                        editTextContent.setBackgroundColor(Color.parseColor(colors.get(key)));

                        MainActivity.notesToShow.get(noteToShowPosition).setBackgroundColor(colors.get(key));

                        // note.setBackgroundColor(colors.get(key));

                        CardView card = MainActivity.listView.getChildAt(noteToShowPosition).findViewById(id.cardView);
                        card.setCardBackgroundColor(Color.parseColor(colors.get(key)));

                        // MainActivity.allNotes.set(notePosition, note);

                        ConstraintLayout layout_edit = findViewById(id.layout_edit_note);
                        layout_edit.setBackgroundColor(Color.parseColor(colors.get(key)));

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(MainActivity.allNotes);
                        sharedPreferences.edit().putString("Notes", json).apply();

                        noteAdapter.notifyDataSetChanged();
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
                    editTextContent.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    CardView card = MainActivity.listView.getChildAt(noteToShowPosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#FFFFFF"));

                    // MainActivity.notesToShow.set(notePosition, note);


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
                    editTextContent.setBackgroundColor(Color.parseColor("#111111"));

                    CardView card = MainActivity.listView.getChildAt(noteToShowPosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#111111"));

                    // MainActivity.notesToShow.set(notePosition, note);


                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.allNotes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

    }


}