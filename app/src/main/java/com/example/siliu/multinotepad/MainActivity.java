package com.example.siliu.multinotepad;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener,View.OnLongClickListener{

    private final static String TAG = "MainActivity";
    private final static int EDIT_REQ = 1;        // Request code for edit activity

    private List<Note> noteList = new ArrayList<Note>();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        noteAdapter = new NoteAdapter(noteList,this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Load data in async task
        new AsyncNoteLoader(this).execute("NoteList.json");
    }

    //Get the currentTime as the last save time for note in the Edit Activity
    private String getCurrentTime() {

        String currentTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        return currentTime;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Save note list to JSON file in the onPause() phase
        saveNoteJson();
    }

    private void saveNoteJson() {
        Log.d(TAG, "Saving notes to JSON file ...");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("NoteList.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();
            for(Note note: noteList){
                writer.beginObject();
                writer.name("NoteTitle").value(note.getNoteTitle());
                writer.name("NoteText").value(note.getNoteText());
                writer.name("LastSaveTime").value(note.getLastSaveTime());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateData(ArrayList<Note> noteList ){
        this.noteList.addAll(noteList);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Note note = noteList.get(position);

        //Send intent to EditActivity when click on one note
        Intent intent_edit = new Intent(MainActivity.this, EditActivity.class);
        intent_edit.putExtra("NOTE_POS",position);
        intent_edit.putExtra("NOTE_TITLE", note.getNoteTitle());
        intent_edit.putExtra("NOTE_TEXT", note.getNoteText());
        startActivityForResult(intent_edit,EDIT_REQ);
    }

    @Override
    public boolean onLongClick(View v) {

        //Create confirmation dialog for deleting note
        deleteAlertDialog(v).show();
        return false;
    }

    public Dialog deleteAlertDialog(final View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_note);
        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                if(! noteList.isEmpty()){
                    int position = recyclerView.getChildAdapterPosition(v);
                    noteList.remove(position);
                    noteAdapter.notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Note is deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_delete_cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Toast.makeText(v.getContext(), "Delete is canceled.", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNote:

                //Open the Edit Activity with empty title and empty
                Intent intent_add = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent_add,EDIT_REQ);

                return true;
            case R.id.infoItem:

                //Open the About Activity, which indicates the app's information
                Intent intent_info = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_info);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_REQ) {
            if(resultCode == RESULT_OK){

                int position = data.getIntExtra("NOTE_POS",-1);
                String noteTitle = data.getStringExtra("NOTE_TITLE");
                String noteText = data.getStringExtra("NOTE_TEXT");
                String lastSaveTime = getCurrentTime();

                //Remove the old note
                if(position != -1)
                    noteList.remove(position);

                //Add the updated/new note to the top of the note list and notify the change to the adapter
                noteList.add(0,new Note(noteTitle,noteText,lastSaveTime));
                noteAdapter.notifyDataSetChanged();
                Log.d(TAG, "onActivityResult: New Note Saved!");

            }else{
                Log.d(TAG, "onActivityResult: Note Save Failed! Result code: " + resultCode);
            }
        } else{
            Log.d(TAG, "onActivityResult: Not the EDIT_REQ. Request Code: " + requestCode);
        }

    }
}
