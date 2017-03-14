package com.example.siliu.multinotepad;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by siliu on 2/13/17.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final static String TAG = "NoteAdapter";
    private List<Note> noteList;
    private MainActivity mainActivity;

    public NoteAdapter(List<Note> noteList , MainActivity mainActivity){
        this.noteList = noteList;
        this.mainActivity = mainActivity;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Creating new note...");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent,false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());

        //Display the first 80 characters of note text
        if(note.getNoteText().length() <= 80){
            holder.noteText.setText(note.getNoteText());
        }else{
            holder.noteText.setText(note.getNoteText().substring(0,80) + "...");
        }

        holder.lastSaveTime.setText(note.getLastSaveTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
