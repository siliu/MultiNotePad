package com.example.siliu.multinotepad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by siliu on 2/13/17.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder{
    public TextView noteTitle;
    public TextView noteText;
    public TextView lastSaveTime;

    public NoteViewHolder(View view){
        super(view);
        noteTitle = (TextView) view.findViewById(R.id.noteTitle);
        noteText = (TextView) view.findViewById(R.id.noteText);
        lastSaveTime = (TextView) view.findViewById(R.id.lastSaveTime);
    }

}
