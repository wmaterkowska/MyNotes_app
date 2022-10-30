package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewAnimator;


import com.example.mynotes.adapters.NoteAdapter;
import com.example.mynotes.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Note> notes = new ArrayList<>();
    static NoteAdapter noteAdapter;
    static ListView listView;
    static CardView themeChange;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu, menu);

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
        // super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id){
            case R.id.settings:
                themeChange = findViewById(R.id.themeCard);
                if (themeChange.getVisibility() == View.INVISIBLE) {
                    themeChange.setVisibility(View.VISIBLE);
                } else {
                    themeChange.setVisibility(View.INVISIBLE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        themeChange = findViewById(R.id.themeCard);

        themeChange.getGlobalVisibleRect(viewRect);
        if (themeChange.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            themeChange.setVisibility(View.INVISIBLE);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Collections.reverse(notes);

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


        // shared preferences to store the notes ---------------------------------------------------
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Notes", null);
        Type typeArrayNotes = new TypeToken<ArrayList<Note>>(){}.getType();
        ArrayList<Note> notesJson = gson.fromJson(json, typeArrayNotes);

        if (notesJson == null) {
            Note exampleNote = new Note("Example note");
            notes.add(exampleNote);
        } else {
            notes = notesJson;
        }


        // list view of the notes ------------------------------------------------------------------
        listView = findViewById(R.id.listView);
        noteAdapter = new NoteAdapter(notes, this);
        listView.setAdapter(noteAdapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int actualPosition = notes.indexOf(adapterView.getItemAtPosition(i));

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", actualPosition);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(" ")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                noteAdapter.notifyDataSetChanged();
                                noteAdapter.resetData();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = gson.toJson(notes);
                                sharedPreferences.edit().putString("Notes", json).apply();
                            }
                        }).setNegativeButton("No", null).show();
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
                settingsPreferences.edit().putInt("Mode", AppCompatDelegate.MODE_NIGHT_YES).apply();
            }
        });


    }

}