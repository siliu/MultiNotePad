package com.example.siliu.multinotepad;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by siliu on 2/20/17.
 */

public class AsyncNoteLoader extends AsyncTask<String, Integer, String> {

    private static final String TAG = "AsyncNoteLoader";
    private MainActivity mainActivity;

    public AsyncNoteLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading note list...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String jsonStr) {

        // parse jsonStr to List<Node>
        if(jsonStr.equals("")){
            mainActivity.updateData(new ArrayList<Note>());
            return;
        }
        ArrayList<Note> noteList = parseNoteJsonFile(jsonStr);

        mainActivity.updateData(noteList);
    }

    @Override
    protected String doInBackground(String... params) {

        String filename = params[0];
        Log.d(TAG, "doInBackground: " + filename);

        StringBuilder sb = new StringBuilder();
        // read all lines into stringBuilder

        try {
            InputStream is = mainActivity.getApplicationContext().openFileInput(filename);
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while((line=reader.readLine()) != null){
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());
            return sb.toString();

        } catch (FileNotFoundException e) {
            Log.e(TAG, "doInBackground: No note JSON file exist !" );
            return "";
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Fail to read the JSON file !");
            return null;
        }
    }

    private ArrayList<Note> parseNoteJsonFile(String jsonStr) {

        ArrayList<Note> noteList = new ArrayList<>();
        try {

            JSONArray mainJsonArray  = new JSONArray(jsonStr);

            for(int i=0 ; i<mainJsonArray.length() ; i++){
                JSONObject noteJasonObject= (JSONObject) mainJsonArray.get(i);
                String noteTitle = noteJasonObject.getString("NoteTitle");
                String noteText = noteJasonObject.getString("NoteText");
                String lastSaveTime = noteJasonObject.getString("LastSaveTime");

                noteList.add(new Note(noteTitle, noteText, lastSaveTime));
            }

            return noteList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parseNoteJsonFile: JSON file not formatted!");
            return null;
        }
    }
}
