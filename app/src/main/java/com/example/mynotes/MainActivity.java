package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.mynotes.adapters.NoteAdapter;
import com.example.mynotes.model.Note;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Note> allNotes = new ArrayList<>();
    public static ArrayList<Note> notesToShow = new ArrayList<>();

    static NoteAdapter noteAdapter;
    public  static ListView listView;

    private static CardView themeChange;
    private static CardView foldersCard;
    private static BottomAppBar bottomAppBar;

    String folder = "Notes";
    public  static ArrayList<String> folders = new ArrayList<>(Arrays.asList("Notes", "Recycle Bin", "All Notes"));


    public ListView getListView() {
        return listView;
    }


    // TOP MENU ====================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuTopInflater = getMenuInflater();
        menuTopInflater.inflate(R.menu.notes_menu, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                noteAdapter.resetData();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.settings:
                themeChange = findViewById(R.id.theme_card);
                if (themeChange.getVisibility() == View.INVISIBLE) {
                    themeChange.setVisibility(View.VISIBLE);
                } else {
                    themeChange.setVisibility(View.INVISIBLE);
                }
                return true;
            case R.id.folders:
                foldersCard = findViewById(R.id.folders_card);
                if (foldersCard.getVisibility() == View.INVISIBLE) {
                    foldersCard.setVisibility(View.VISIBLE);
                } else {
                    foldersCard.setVisibility(View.INVISIBLE);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // for making card with settings disappear after clicking elsewhere ----------------------------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        themeChange = findViewById(R.id.theme_card);
        foldersCard = findViewById(R.id.folders_card);

        themeChange.getGlobalVisibleRect(viewRect);
        if (themeChange.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            themeChange.setVisibility(View.INVISIBLE);
            return true;
        }

        foldersCard.getGlobalVisibleRect(viewRect);
        if (foldersCard.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            foldersCard.setVisibility(View.INVISIBLE);
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
        setContentView(R.layout.activity_main);
        Collections.reverse(allNotes);


        // getting users choice of light/dark mode from SharedPreferences --------------------------
        SharedPreferences settingsPreferences = getApplicationContext().getSharedPreferences("com.example.settings", Context.MODE_PRIVATE);
        int choice = settingsPreferences.getInt("Mode", 0);
        if (choice == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (choice == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        // getting notes from sharedPreferences ----------------------------------------------------
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Notes", null);
        Type typeArrayNotes = new TypeToken<ArrayList<Note>>(){}.getType();
        ArrayList<Note> notesJson = gson.fromJson(json, typeArrayNotes);

        if (notesJson == null) {
            Note exampleNote = new Note("Example note");
            allNotes.add(exampleNote);
        } else {
            allNotes = notesJson;
        }

        // getting folders from sharedPreferences --------------------------------------------------
        SharedPreferences foldersPreferences = getApplicationContext().getSharedPreferences("com.example.folders", Context.MODE_PRIVATE);
        String jsonFoldersFromPref = foldersPreferences.getString("Folders", null);
        Set<String> gsonFolders = gson.fromJson(jsonFoldersFromPref, Set.class);

        if (gsonFolders != null) {
            for (String folder : gsonFolders) {
                if (!folders.contains(folder)) {
                    folders.add(folder);
                }
            }
        }

        // list view of the notes ------------------------------------------------------------------
        listView = findViewById(R.id.listView);

        for (Note note : allNotes) {
            if (note.getFolders().contains(folder) && !note.getFolders().contains("Recycle Bin")) {
                notesToShow.add(note);
            }
        }
        noteAdapter = new NoteAdapter(notesToShow, this);
        listView.setAdapter(noteAdapter);
        listView.setTextFilterEnabled(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int actualPosition = allNotes.indexOf(adapterView.getItemAtPosition(i));

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", actualPosition);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setItemChecked(i, true);
                bottomAppBar = (BottomAppBar) findViewById(R.id.bottomBar);
                bottomAppBar.setVisibility(View.VISIBLE);
                return true;
            }
        });


        // bottom menu -----------------------------------------------------------------------------
        BottomNavigationView bottomMenu = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Note noteToEdit = (Note) listView.getItemAtPosition(listView.getCheckedItemPosition());

                switch (item.getItemId()) {
                    case R.id.trash: // delete the note --------------------------------------------
                        if (noteToEdit.getFolders().contains("Recycle Bin")) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(" ")
                                    .setIcon(R.drawable.ic_baseline_delete_24)
                                    .setTitle("Do you want to delete this note forever?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            allNotes.remove(noteToEdit);
                                            notesToShow.remove(noteToEdit);

                                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                            Gson gson = new Gson();
                                            String json = gson.toJson(allNotes);
                                            sharedPreferences.edit().putString("Notes", json).apply();

                                            listView.setAdapter(noteAdapter);
                                            bottomAppBar.setVisibility(View.INVISIBLE);
                                        }
                                    }).setNegativeButton("No", null).show();
                        } else {
                            noteToEdit.addFolder("Recycle Bin");
                            notesToShow.remove(noteToEdit);

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String json = gson.toJson(allNotes);
                            sharedPreferences.edit().putString("Notes", json).apply();

                            listView.setAdapter(noteAdapter);
                            bottomAppBar.setVisibility(View.INVISIBLE);
                        }
                        return true;

                    case R.id.copy: // copy the note -----------------------------------------------
                        return true;
                    case R.id.labels: // set label of the note -------------------------------------
                        return true;
                }

                return true;
            }
        });


        // floating action button: add note --------------------------------------------------------
        FloatingActionButton fabAdd = findViewById(R.id.add_fab);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
        });


        // floating action button: set the light mode ----------------------------------------------
        FloatingActionButton fabLight = findViewById(R.id.light);
        fabLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                themeChange.setVisibility(View.VISIBLE);

                SharedPreferences settingsPreferences = getApplicationContext().getSharedPreferences("com.example.settings", Context.MODE_PRIVATE);
                settingsPreferences.edit().putInt("Mode", AppCompatDelegate.MODE_NIGHT_NO).apply();

                listView.refreshDrawableState();
                listView.setAdapter(noteAdapter);
            }
        });

        // floating action button: set the dark mode -----------------------------------------------
        FloatingActionButton fabDark = findViewById(R.id.dark);
        fabDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                themeChange.setVisibility(View.VISIBLE);

                SharedPreferences settingsPreferences = getApplicationContext().getSharedPreferences("com.example.settings", Context.MODE_PRIVATE);
                settingsPreferences.edit().putInt("Mode", AppCompatDelegate.MODE_NIGHT_YES).apply();;

                listView.refreshDrawableState();
                listView.setAdapter(noteAdapter);
            }
        });


        // chips for choosing folder to show -------------------------------------------------------
        for (String folder : folders) {
            ChipGroup foldersChips = findViewById(R.id.chip_group);
            Chip newChip = new Chip(foldersChips.getContext());
            newChip.setText(folder);
            foldersChips.addView(newChip);

            if (folder == "Recycle Bin" || folder == "All Notes") {
                newChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notesToShow.clear();
                        for (Note note : allNotes) {
                            if (note.getFolders().contains(folder)) {
                                notesToShow.add(note);
                            }
                        }
                        listView.setAdapter(noteAdapter);
                    }
                });
            } else {
                newChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notesToShow.clear();
                        for (Note note : allNotes) {
                            if (note.getFolders().contains(folder) && !note.getFolders().contains("Recycle Bin")) {
                                notesToShow.add(note);
                            }
                        }
                        listView.setAdapter(noteAdapter);
                    }
                });
            }

        }


    }

}