package com.example.siliu.multinotepad;

/**
 * Created by siliu on 2/13/17.
 */

public class Note {
    private String noteTitle;
    private String noteText;
    private String lastSaveTime;

    public Note(){
        this.noteTitle = "Initial Note";
        this.noteText = "This is my first Note";
        this.lastSaveTime = "17/02/2017";
    }

    public Note(String noteTitle , String noteText, String lastSaveTime){
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.lastSaveTime = lastSaveTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setLastSaveTime(String lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public String getLastSaveTime() {
        return lastSaveTime;
    }
}
