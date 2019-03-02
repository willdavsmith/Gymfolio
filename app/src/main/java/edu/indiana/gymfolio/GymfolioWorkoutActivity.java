package edu.indiana.gymfolio;

/*
 * GymfolioWorkoutActivity.java
 *
 * Provides the functionality for viewing the stored workout in the application.
 * Utilizes Java File IO for accessing stored information and a
 * ListView for displaying the stored information.
 *
 * Created by: Will Smith
 * Created on: 2/23/19
 * Last Modified by: Will Smith
 * Last Modified on: 3/1/19
 * Assignment/Project: A290 Android - Final Project
 * Part of: Gymfolio, associated to activity_gymfolio_workout.xml
 *
 **/

import android.app.ListActivity;
import android.nfc.FormatException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import static edu.indiana.gymfolio.GymfolioSelectActivity.parseWorkoutFromString;

public class GymfolioWorkoutActivity extends ListActivity {

    // ArrayList that will store the user's workouts for the day
    ArrayList<String> todaysWorkouts = new ArrayList<>();

    // An Adapter that will hold the data from above and connect it to the ListView
    ArrayAdapter<String> adapter;

    String today;
    File directory;
    File workoutFile;

    // Local arraylist that stores current workouts
    ArrayList<Workout> wkts = new ArrayList<>();

    /**
     * Handles the on creation event for the WorkoutActivity.
     * @param savedInstanceState Not utilized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_workout);

        // Set the instance variable 'today' to be the current day of the week
        Date now = new Date();
        SimpleDateFormat s = new SimpleDateFormat("EEEE");
        today = s.format(now);

        // Set the array adapter, as in GymfolioSelectActivity
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todaysWorkouts);
        setListAdapter(adapter);

        // Instantiate the working directory and the current file for parsing
        directory = getFilesDir();
        workoutFile = new File(directory, today + ".txt");

        // Try to get all current workouts on the file, and if an error occurs,
        // Inform the user.
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

    /**
     * Parses and retrieves all workouts from the 'workoutFile' file and stores them
     * in an ArrayList.
     * @param day The day of the workout to parse.
     * @return An arraylist of workouts in the file
     * @throws FileNotFoundException in the case of a file opening or reading error.
     */
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
                    Toast.makeText(getBaseContext(), "Failed to Parse", Toast.LENGTH_LONG).show();
                }
            }
        }
        while (input.hasNextLine());
        input.close();
        return wks;
    }
}
