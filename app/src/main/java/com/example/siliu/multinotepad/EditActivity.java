package com.example.siliu.multinotepad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private final static String TAG = "EditActivity";
    private EditText noteTitle;
    private EditText noteText;
    private int pos;
    private String origTitleStr;
    private String origTextStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteText = (EditText) findViewById(R.id.noteText);

        noteText.setMovementMethod(new ScrollingMovementMethod());
        noteText.setTextIsSelectable(true);

        Intent intent = getIntent();

        if (intent.hasExtra("NOTE_POS")) {
            pos = intent.getIntExtra("NOTE_POS", -1);
        } else {
            pos = -1;
        }

        if (intent.hasExtra("NOTE_TITLE")) {
            origTitleStr = intent.getStringExtra("NOTE_TITLE");
            noteTitle.setText(origTitleStr);
        } else {
            noteTitle.setText("");
        }

        if (intent.hasExtra("NOTE_TEXT")) {
            origTextStr = intent.getStringExtra("NOTE_TEXT");
            noteText.setText(origTextStr);
        } else {
            noteText.setText("");
        }
    }

    @Override
    public void onBackPressed() {

        backPressedAlertDialog().show();
//        super.onBackPressed();
    }

    public Dialog backPressedAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_save_note);
        builder.setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doneSaveNote();
            }
        });

        builder.setNegativeButton(R.string.dialog_save_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(v.getContext(), "Save is canceled.", Toast.LENGTH_SHORT).show();
                //Exit the edit activity
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });

        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                doneSaveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doneSaveNote() {

        Intent data = new Intent();

        if (noteTitle.getText().toString().equals("")) {
            setResult(RESULT_CANCELED, data);
            finish();
            Toast.makeText(this, "Note NOT saved. The note cannot be saved without a title. ", Toast.LENGTH_SHORT).show();
        } else if(noteTitle.getText().toString().equals(origTitleStr) && noteText.getText().toString().equals(origTextStr)){
            setResult(RESULT_CANCELED, data);
            finish();
        } else {
            data.putExtra("NOTE_POS", pos);
            data.putExtra("NOTE_TITLE", noteTitle.getText().toString());
            data.putExtra("NOTE_TEXT", noteText.getText().toString());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
