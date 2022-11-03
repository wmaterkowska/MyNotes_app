package com.example.mynotes.adapters;

import android.content.ContentProvider;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.mynotes.MainActivity;
import com.example.mynotes.R;
import com.example.mynotes.model.Note;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        TextView tv;
        Note n = noteList.get(position);
        Set<String> folders = n.getFolders();
        Set<String> foldersForChips = new HashSet<>(folders);
        foldersForChips.remove("Notes");
        foldersForChips.remove("All Notes");

        NoteHolder holder = new NoteHolder();


        if (listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_layout, null);

            holder.foldersChipGroup = (ChipGroup) v.findViewById(R.id.chips_folders);
            holder.foldersChipGroup.setChipSpacingHorizontal(2);
            holder.foldersChipGroup.setClickable(false);
            holder.foldersChipGroup.setFocusable(false);
            for (String folder : foldersForChips) {
                Chip folderChip = new Chip(context);
                folderChip.setText(folder);
                folderChip.setTextSize(8);
                folderChip.setEnsureMinTouchTargetSize(false);
                folderChip.setHeight(40);
                folderChip.setChipMinHeight(10);
                folderChip.setBackgroundColor(Color.TRANSPARENT);
                folderChip.setChipBackgroundColor(null);

                ChipDrawable chipFolderDrawable = ChipDrawable.createFromAttributes(context, null,0, R.style.Widget_App_Chip);
                folderChip.setChipDrawable(chipFolderDrawable);

                holder.foldersChipGroup.addView(folderChip);
            }

            tv = (TextView) v.findViewById(R.id.content);

            holder.contentView = tv;
            v.setTag(holder);
        } else {
            holder = (NoteHolder) v.getTag();
        }

        if (getItem(position).getBackgroundColor() != null) {
            CardView card1 = v.findViewById(R.id.cardView);
            card1.setCardBackgroundColor(Color.parseColor(getItem(position).getBackgroundColor()));
        }

        holder.contentView.setText(n.getContent());

        return v;
    }


    public void resetData() {
        noteList = origNoteList;
    }

    // NOTE HOLDER ---------------------------------------------------------------------------------
    public static class NoteHolder {
        public TextView contentView;
        public ChipGroup foldersChipGroup;

    }

    @Override
    public Filter getFilter() {
        if (noteFilter == null) {
            noteFilter = new NoteFilter();
        }
        return noteFilter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
