package edu.indiana.gymfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.nfc.FormatException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static edu.indiana.gymfolio.GymfolioSelectActivity.parseWorkoutFromString;

public class GymfolioWorkoutActivity extends ListActivity {

    // TODO: crashing
    String TAG = "SELECTEDITEMSFROMWEEK";

    // ArrayList that will store the user's workouts for the day
    ArrayList<String> todaysWorkouts = new ArrayList<>();

    // An Adapter that will hold the data from above and connect it to the ListView
    ArrayAdapter<String> adapter;

    String today;
    File directory;
    File workoutFile;

    // Local arraylist that stores current workouts
    ArrayList<Workout> wkts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_workout);

        Log.d(TAG, today + "   ");

        Date now = new Date();
        SimpleDateFormat s = new SimpleDateFormat("EEEE");
        today = s.format(now);
        Log.d(TAG, today);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todaysWorkouts);
        setListAdapter(adapter);

        // TODO crashes if this doesnt exist
        directory = getFilesDir();
        workoutFile = new File(directory, today + ".txt");
        Log.d(TAG, workoutFile.toString());

        try {
            wkts = getAllWorkouts(today);
            for (Workout w : wkts) {
                adapter.add(w.toString());
            }
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Could not find workout for " + today, Toast.LENGTH_LONG).show();
        }
    }


    public ArrayList<Workout> getAllWorkouts(String day) throws FileNotFoundException {
        ArrayList<Workout> wks = new ArrayList<>();
        Scanner input;
        input = new Scanner(workoutFile);
        // while a new line exists
        do {
            // Scan the file line by line
            String line = input.nextLine();
            if (!line.equals(day)) {
                try {
                    // parse the line as a workout and add it to the return array list
                    Workout result = parseWorkoutFromString(line);
                    wks.add(result);
                } catch (FormatException e) {
                    Log.d(TAG, "Incorrect format");
                }
            }
        }
        while (input.hasNextLine());
        input.close();
        return wks;
    }
}
