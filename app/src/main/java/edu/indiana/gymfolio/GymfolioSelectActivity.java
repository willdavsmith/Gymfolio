package edu.indiana.gymfolio;

/*
 * GymfolioSelectActivity.java
 *
 * Provides the majority of the functionality of the application.
 *
 * Utilizes Java File IO to Create, Read, Update, and Delete
 * workouts from their routine based on the day that they have selected
 * in the GymfolioCustomizeActivity.
 *
 * Provides a variety of methods for parsing, unparsing, and storing data
 * in files and various data structures within the class for local and persistent access.
 *
 * Provides the backend of the user interface which allows the user to
 * access the files indirectly, providing them feedback based on their input.
 *
 * Created by: Will Smith
 * Created on: 2/23/19
 * Last Modified by: Will Smith
 * Last Modified on: 3/1/19
 * Assignment/Project: A290 Android - Final Project
 * Part of: Gymfolio, associated to activity_gymfolio_select.xml
 *
 **/

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.FormatException;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GymfolioSelectActivity extends ListActivity implements OnClickListener {

    // ArrayList that will store the user's workouts for the day
    ArrayList<String> todaysWorkouts = new ArrayList<>();

    // An Adapter that will hold the data from above and connect it to the ListView
    ArrayAdapter<String> adapter;

    // Local arraylist that stores current workouts
    ArrayList<Workout> wkts = new ArrayList<>();

    // Instance variables for the home directory and workout file locations.
    File directory;
    File workoutFile;

    // A string representing the current day.
    String day;

    /**
     * Handles the on creation event for the SelectActivity.
     * @param savedInstanceState A Bundle Object with information from the last activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_select);

        // Add the 'add' button and set the click listener
        View AddButton = findViewById(R.id.btn_add_workout);
        AddButton.setOnClickListener(this);

        // Add the 'remove' button and set the click listener
        View RemoveButton = findViewById(R.id.btn_remove_workout);
        RemoveButton.setOnClickListener(this);

        // Get the extra data sent by the last activity
        Bundle bundle = getIntent().getExtras();

        // Make sure there are no null pointer exceptions...
        if (bundle != null) {
            // Get the boolean array sent by the last activity
            // (see 'weekdaysSelected' from GymfolioCustomizeActivity')
            String value = bundle.getString("weekdaySelected");

            // Instantiate the adapter with a ListView (this), the android default list item layout,
            // and the above ArrayList.
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todaysWorkouts);
            setListAdapter(adapter);

            // Instantiate the file path using getFilesDir and creating a new File
            // Under this home directory with 'weekday'.txt
            directory = getFilesDir();
            workoutFile = new File(directory, value + ".txt");

            // set the day to the correct value
            day = value;

            // Add the current day to the file
            try {
                appendStrToFile(value);
            }
            catch (Exception e) {
                Toast.makeText(getBaseContext(), "Failed to add to file.", Toast.LENGTH_SHORT).show();
            }

            // Parses and adds the current values in the file to the adapter,
            // Which then adds them to the ListView.
            try {
                wkts = getAllWorkouts(value);
                for (Workout w : wkts) {
                    adapter.add(w.toString());
                }
            }
            catch (Exception e) {
                Toast.makeText(getBaseContext(), "Incorrect format", Toast.LENGTH_SHORT).show();
            }
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
                    Toast.makeText(getBaseContext(), "Incorrect format", Toast.LENGTH_SHORT).show();
                }
            }
        }
        while (input.hasNextLine());
        input.close();
        return wks;
    }

    /**
     * Parses a String representation of a workout (as supplied by the user) and returns a Workout
     * Object from it.
     * @param value The string value to be parsed.
     * @return A workout object representation of the given string value.
     * @throws FormatException in the case of an unexpected format.
     */
    public static Workout parseWorkoutFromString(String value) throws FormatException {
        // parse the elements into a String array with the , split character
        // if the given string will not parse properly, throw an exception.
        String[] parsedElements = value.split(",");
        if (parsedElements.length == 4) {
            int sets = Integer.valueOf(parsedElements[0]);
            int reps = Integer.valueOf(parsedElements[1]);
            String workoutName = parsedElements[2];
            int weight = Integer.valueOf(parsedElements[3]);
            return new Workout(sets, reps, workoutName, weight);
        }
        else throw new FormatException();
    }

    /**
     * Appends a string and a new line to the instance variable 'workoutFile'
     * @param str The string to be appended.
     * @throws IOException in the case of an IO Error in adding to the file.
     */
    public void appendStrToFile(String str) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(workoutFile, true));
        out.write(str+'\n');
        out.close();
    }

    /**
     * Removes a string from the instance variable 'workoutFile'
     * @param str The string to be removed.
     * @throws IOException in the case of an IO Error in removing from the file.
     */
    public void removeStrFromFile(String str) throws IOException {
        Scanner input;
        // Creates a temporary file to hold the data from the current file
        File tempFile = new File(directory, "temp.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        input = new Scanner(workoutFile);
        // while a new line exists
        while(input.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            String line = input.nextLine();
            // if the line is equivalent to the line to be removed, then do not
            // add it to the temporary file
            try {
                if(parseWorkoutFromString(line).toString().equals(str)) continue;
            }
            catch (Exception e) {
                // Do nothing. This is working as intended.
            }

            // If it is not equivalent, then add it to the output file
            writer.write(line + '\n');
        }
        input.close();
        writer.close();
        // Set the file with the correct data to reference the file in question.
        tempFile.renameTo(workoutFile);
    }

    /**
     * Parses and adds a workout to the local ArrayList of current workouts.
     * @param value The unparsed workout string to be added.
     * @throws FormatException in the case of an unexpected format.
     */
    public void addWorkout(String value) throws FormatException {
        try {
            wkts.add(parseWorkoutFromString(value));
        }
        catch (FormatException e) {
            throw new FormatException();
        }
    }

    /**
     * Handles the buttons on the screen
     * @param v The button view that may be clicked.
     */
    @SuppressWarnings("deprecation")
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_workout) {
            // In the case where the user selects the 'Add' Button,
            // Invoke a dialog requesting the user to input a workout
            // and add it to the listView
            showDialog(0);
        }
        else if (v.getId() == R.id.btn_remove_workout) {
            showDialog(1);
        }
    }

    /**
     * Creates the dialog.
     * @param id The id of the dialog to be created.
     * @return a Dialog object.
     */
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // The keyboard manager
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // Toggle ON
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        // Determine the state of ID from showDialog(id)
        // 0 -> add button
        // 1 -> remove button
        if (id == 0) {
            // Create the input text box and instantiate the AlertDialog Builder
            final EditText input = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            // Set the title and the intro message
            alert.setTitle("      Workout Customizer     ");
            alert.setMessage("Format: Sets,Reps,Name,Weight");

            // Set the view for the AlertDialog Builder, in this case, set it to the EditText box
            alert.setView(input);

            // When the user writes their information into the EditText box,
            // Update the ListView holding their information and additionally
            // Show a Toast Dialog showing that the item was saved successfully
            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Toggle keyboard off
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    // flag for determining whether or not a workout should be added to the ListView
                    boolean flag = true;

                    // input value from user
                    String value = input.getText().toString();

                    // append the input to the file
                    // if this operation fails, inform the user
                    // and do not add it to the listView.
                    try {
                        appendStrToFile(value);
                    }
                    catch (IOException e) {
                        Toast.makeText(getBaseContext(), "Failed to append to file", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }

                    // try to add the workout to the array list
                    // if this operation fails, inform the user
                    // and do not add it to the listView.
                    try {
                        addWorkout(value);
                    }
                    catch (FormatException e) {
                        Toast.makeText(getBaseContext(), "Wrong Format", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }

                    // if flag is still true, then the string is safe to parse and add to the adapter.
                    // if this fails for some reason, inform the user.
                    if (flag) {
                        try {
                            adapter.add(parseWorkoutFromString(value).toString());
                        }
                        catch (FormatException e) {
                            Toast.makeText(getBaseContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                        }
                        // inform the user that the workout is saved
                        Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // If the user cancels the operation, nothing should be saved to the ListView,
            // and a Toast dialog pops up showing them that the operation was cancelled successfully
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Toggle keyboard off
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            // Finally, Show the dialog
            alert.show();
        }
        else if (id == 1) {
            // Create the input text box and instantiate the AlertDialog Builder
            final EditText input_2 = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            // Set the title and the intro message
            alert.setTitle("      Workout Remover     ");
            alert.setMessage("Input Index to Remove");

            // Set the view for the AlertDialog Builder, in this case, set it to the EditText box
            alert.setView(input_2);

            // When the user writes their information into the EditText box,
            // Update the ListView holding their information and additionally
            // Show a Toast Dialog showing that the item was deleted successfully
            alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Toggle keyboard off
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    // input value from user
                    String value = input_2.getText().toString();
                    int parsedVal;
                    try {
                        // Make sure the value is able to be parsed to an integer
                        // And set the index from 1-based to 0-based
                        parsedVal = Integer.parseInt(value) - 1;
                        // If the value is in the correct range
                        if (parsedVal < todaysWorkouts.size() && parsedVal >= 0) {
                            // Find the element in question from the index gived
                            String element = todaysWorkouts.get(parsedVal);

                            // Remove reference from all necessary lists and files.
                            // And inform the user of the result.
                            todaysWorkouts.remove(element);
                            try {
                                removeStrFromFile(element);
                            } catch (IOException e) {
                                Toast.makeText(getBaseContext(), "Failed to remove from file", Toast.LENGTH_LONG).show();
                            }
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Removed " + element, Toast.LENGTH_LONG).show();
                        }
                        else throw new NumberFormatException();
                    }
                    catch (NumberFormatException e) {
                        Toast.makeText(getBaseContext(), "Entered index is out of bounds", Toast.LENGTH_LONG).show();
                    }
                }
            });

            // If the user cancels the operation, nothing should be saved to the ListView,
            // and a Toast dialog pops up showing them that the operation was cancelled successfully
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Toggle keyboard off
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
            });

            // Finally, Show the dialog
            alert.show();
        }
        // If nothing is to be done, then return null
        return null;
    }
}