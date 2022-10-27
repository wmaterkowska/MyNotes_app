package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

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
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;

import java.util.List;

public class NoteEditorActivity extends AppCompatActivity {

    static int noteId;

    CardView colorPalette;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        colorPalette = findViewById(R.id.colorPaletteCard);
        
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.color:
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
        colorPalette = findViewById(R.id.colorPaletteCard);

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
        setContentView(R.layout.activity_note_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            MainActivity.notes.add(0,"");
            noteId = MainActivity.notes.indexOf("");
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

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


        FloatingActionButton fabGreen = findViewById(R.id.green);
        fabGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#4CAF50"));
                    editText.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
            }
        });

        FloatingActionButton fabBlue = findViewById(R.id.blue);
        fabBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#2196F3"));
                    editText.setBackgroundColor(Color.parseColor("#2196F3"));
                }
            }
        });

        FloatingActionButton fabPink = findViewById(R.id.pink);
        fabPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#4CAF50"));
                    editText.setBackgroundColor(Color.parseColor("#4CAF50"));
                }
            }
        });

        FloatingActionButton fabYellow = findViewById(R.id.yellow);
        fabYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#2196F3"));
                    editText.setBackgroundColor(Color.parseColor("#2196F3"));
                }
            }
        });


        FloatingActionButton fabWhite = findViewById(R.id.white);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#FFFFFF"));
                    editText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        FloatingActionButton fabRed = findViewById(R.id.red);
        fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.listView != null) {
                    MainActivity.listView.getChildAt(noteId).setBackgroundColor(Color.parseColor( "#2196F3"));
                    editText.setBackgroundColor(Color.parseColor("#2196F3"));
                }
            }
        });



    }


}