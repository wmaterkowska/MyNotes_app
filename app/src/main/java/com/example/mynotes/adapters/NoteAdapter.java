package com.example.mynotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mynotes.R;
import com.example.mynotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> implements Filterable {

    private ArrayList<Note> noteList;
    private Context context;
    private Filter noteFilter;
    private ArrayList<Note> origNoteList;

    public NoteAdapter(ArrayList<Note> noteList, Context ctx) {
        super(ctx, android.R.layout.simple_expandable_list_item_1, noteList);
        this.noteList = noteList;
        this.context = ctx;
        this.origNoteList = noteList;
    }

    public int getCount() {
        return noteList.size();
    }

    public Note getItem(int position) {
        return noteList.get(position);
    }

    public long getItemId(int position) {
        return noteList.get(position).hashCode();
    }

    public View getView(int position, View listView, ViewGroup parent) {
        View v = listView;

        NoteHolder holder = new NoteHolder();

        if (listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_layout, null);

            TextView tv = (TextView) v.findViewById(R.id.content);
            holder.contentView = tv;
            v.setTag(holder);
        } else {
            holder = (NoteHolder) v.getTag();
        }

        Note n = noteList.get(position);
        holder.contentView.setText(n.getContent());

        return v;
    }

    public void resetData() {
        noteList = origNoteList;
    }

    public static class NoteHolder {
        public TextView contentView;
    }



    @Override
    public Filter getFilter() {
        if (noteFilter == null) {
            noteFilter = new NoteFilter();
        }
        return noteFilter;
    }

    private class NoteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {

                results.values = origNoteList;
                results.count = origNoteList.size();
            }
            else {
                // We perform filtering operation
                List<Note> nNoteList = new ArrayList<Note>();

                for (Note n : noteList) {
                    if (n.getContent().toUpperCase().contains(constraint.toString().toUpperCase()))
                        nNoteList.add(n);
                }

                results.values = nNoteList;
                results.count = nNoteList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if (filterResults.count == 0)
                notifyDataSetInvalidated();
            else {
                noteList = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }



}
