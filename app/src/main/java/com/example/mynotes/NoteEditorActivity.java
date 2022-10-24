package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.mynotes.model.Note;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    static int noteId;
    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();

        //ArrayList<String> noteList = mainActivity.retrieveOrderedNotes(mainActivity.notes);

        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            editText.setText(mainActivity.notes.get(noteId));
        } else {
            MainActivity.addNote("", mainActivity.notes);
            // MainActivity.notes.add(0,"");
            //noteId = MainActivity.notes.indexOf("");
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
                mainActivity.retrieveOrderedNotes(mainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}