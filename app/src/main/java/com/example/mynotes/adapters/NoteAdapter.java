package com.example.mynotes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.mynotes.R;
import com.example.mynotes.model.Note;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NoteAdapter extends ArrayAdapter<Note> implements Filterable {

    private ArrayList<Note> noteList;
    private Context context;
    private NoteFilter noteFilter;
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
        TextView tvTitle;
        TextView tvContent;
        CardView cv;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            noteList.sort(Comparator.comparing(Note::getDateTime).reversed());
        }

        Note n = noteList.get(position);
        Set<String> folders = n.getFolders();
        Set<String> foldersForChips = new HashSet<>(folders);
        foldersForChips.remove("Notes");
        foldersForChips.remove("All Notes");

        NoteHolder holder = new NoteHolder();

        if (listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_layout, null);


            // view of the labels of the note ------------------------------------------------------
            holder.foldersChipGroup = (ChipGroup) v.findViewById(R.id.labels_of_note);
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

            tvTitle = (TextView) v.findViewById(R.id.title);
            tvContent = (TextView) v.findViewById(R.id.content);

            holder.titleView = tvTitle;
            holder.contentView = tvContent;

            v.setTag(holder);
        } else {
            holder = (NoteHolder) v.getTag();
            v.refreshDrawableState();
        }

        // view of the title and content of the note -----------------------------------------------
        if (n.getTitle() == "" || n.getTitle().isEmpty() || n.getTitle() == null ) {
            holder.contentView.setVisibility(View.VISIBLE);
            holder.contentView.setText(n.getContent());
            holder.contentView.setPadding(20,0,20, 0);
        } else if (n.getContent() == "" || n.getContent().isEmpty() || n.getContent() == null){
            holder.titleView.setVisibility(View.VISIBLE);
            holder.titleView.setText(n.getTitle());
            holder.titleView.setPadding(20,0,20, 0);
        } else {
            holder.titleView.setVisibility(View.VISIBLE);
            holder.titleView.setText(n.getTitle());
            holder.titleView.setPadding(20,0,20, 0);
            holder.contentView.setVisibility(View.VISIBLE);
            holder.contentView.setText(n.getContent());
            holder.contentView.setPadding(20,0,20, 0);
        }

        // background color ------------------------------------------------------------------------
        cv = v.findViewById(R.id.cardView);
        holder.cardView = cv;
        if (n.getBackgroundColor() != null) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(n.getBackgroundColor()));
        }

        return v;
    }

    public void resetData() {
        noteList = origNoteList;
    }

    // NOTE HOLDER ---------------------------------------------------------------------------------
    public static class NoteHolder {
        public CardView cardView;
        public TextView titleView;
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


    // NOTE FILTER ---------------------------------------------------------------------------------
    private class NoteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = origNoteList;
                results.count = origNoteList.size();
            }
            else {
                // filtering operation
                List<Note> nNoteList = new ArrayList<Note>();

                for (Note n : noteList) {
                    if ( n.getContent().toUpperCase().contains(constraint.toString().toUpperCase())){
                        nNoteList.add(n);
                    } else if (n.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        nNoteList.add(n);
                    } //else {
                      //  nNoteList.remove(n);
                    // }
                }
                notifyDataSetChanged();
                results.values = nNoteList;
                results.count = nNoteList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if (filterResults.count == 0) {
                //notifyDataSetChanged();
                notifyDataSetInvalidated();
            } else {
                resetData();
                noteList = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
            notifyDataSetChanged();
        }
    }



}
