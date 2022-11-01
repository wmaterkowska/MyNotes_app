package com.example.mynotes;

import static com.example.mynotes.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

public class NoteEditorActivity extends AppCompatActivity {

    static int notePosition;
    static Note note;

    CardView colorPalette;

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect viewRect = new Rect();
        colorPalette = findViewById(id.colorPaletteCard);

        colorPalette.getGlobalVisibleRect(viewRect);
        if (colorPalette.getVisibility() == View.VISIBLE && !viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            colorPalette.setVisibility(View.INVISIBLE);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_note_editor);

        MainActivity.listView.setAdapter(MainActivity.noteAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        EditText editText = findViewById(id.editText);
        Intent intent = getIntent();

        notePosition = intent.getIntExtra("noteId", -1);
        if (notePosition != -1) {
            note = MainActivity.notes.get(notePosition);
            editText.setText(note.getContent());
            notePosition = MainActivity.notes.indexOf(note);
        } else {
            note = new Note("");
            MainActivity.notes.add(0, note);
            notePosition = MainActivity.notes.indexOf(note);
            MainActivity.noteAdapter.notifyDataSetChanged();
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.notes);
        sharedPreferences.edit().putString("Notes", json).apply();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.get(notePosition).setContent(String.valueOf(charSequence));
                MainActivity.noteAdapter.notifyDataSetChanged();

                // Creating Object of SharedPreferences to store data in the phone
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.notes);
                sharedPreferences.edit().putString("Notes", json).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        // fab for changing color to PINK ----------------------------------------------------------
        FloatingActionButton fabGreen = findViewById(id.pink);
        fabGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    editText.setBackgroundColor(Color.parseColor("#E8B2B5"));
                    note.setBackgroundColor("#E8B2B5");

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#E8B2B5"));

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

        // fab for changing color to BLUE ----------------------------------------------------------
        FloatingActionButton fabBlue = findViewById(id.blue);
        fabBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    editText.setBackgroundColor(Color.parseColor("#2E4F6C"));
                    note.setBackgroundColor("#2E4F6C");

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#2E4F6C"));

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
                    sharedPreferences.edit().putString("Notes", json).apply();

                }
            }
        });

        // fab for changing color to GREEN ---------------------------------------------------------
        FloatingActionButton fabPink = findViewById(id.green);
        fabPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    editText.setBackgroundColor(Color.parseColor("#7E9C65"));
                    note.setBackgroundColor("#7E9C65");

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#7E9C65"));

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

        // fab for changing color to YELLOW --------------------------------------------------------
        FloatingActionButton fabYellow = findViewById(id.yellow);
        fabYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    editText.setBackgroundColor(Color.parseColor("#D1C357"));
                    note.setBackgroundColor("#D1C357");

                    CardView card = MainActivity.listView.getChildAt(notePosition).findViewById(id.cardView);
                    card.setCardBackgroundColor(Color.parseColor( "#D1C357"));

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

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

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
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

                    MainActivity.notes.set(notePosition, note);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(MainActivity.notes);
                    sharedPreferences.edit().putString("Notes", json).apply();
                }
            }
        });

        // changing the background color
        if(note.getBackgroundColor() != null) {
            editText.setBackgroundColor(Color.parseColor(note.getBackgroundColor()));
        }
    }


}