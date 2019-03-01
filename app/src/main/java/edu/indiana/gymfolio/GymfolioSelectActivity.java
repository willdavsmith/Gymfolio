package edu.indiana.gymfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.nfc.FormatException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    // Logcat tag for debugging purposes
    String TAG = "SELECTEDITEMSFROMWEEK";

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

    // TODO: ADD DELETE BUTTON

    /**
     * Handles the on creation event for the SelectActivity.
     * @param savedInstanceState A Bundle Object with information from the last activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_select);

        View AddButton = findViewById(R.id.btn_add_workout);
        AddButton.setOnClickListener(this);

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
            // Adapted from
            // https://stackoverflow.com/questions/4540754/dynamically-add-elements-to-a-listview-android
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
                Log.d(TAG, "cannot find workout file");
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
                Log.d(TAG, "something went wrong...");
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
                    Log.d(TAG, "Incorrect format");
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
     * Appends a string to the instance variable 'workoutFile'
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
        File tempFile = new File(directory, "temp.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        input = new Scanner(workoutFile);
        // while a new line exists
        while(input.hasNextLine()) {
            // trim newline when comparing with lineToRemove
            String line = input.nextLine();
            try {
                if(parseWorkoutFromString(line).toString().equals(str)) continue;
            }
            catch (Exception e) {
                // Do nothing.
            }

            writer.write(line + '\n');
        }
        input.close();
        writer.close();
        Log.d(TAG, "" + tempFile.renameTo(workoutFile));
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
     * Creates the dialog. Structure adapted from Meeting 5.
     * @param id The id of the dialog to be created.
     * @return a Dialog object.
     */
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            // Adapted from
            // https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog

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
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            // Finally, Show the dialog
            alert.show();
        }
        else if (id == 1) {
            // Adapted from
            // https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog

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
            // Show a Toast Dialog showing that the item was saved successfully
            alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // input value from user
                    String value = input_2.getText().toString();
                    int parsedVal;
                    try {
                        parsedVal = Integer.parseInt(value) - 1;
                        Log.d(TAG, "" + parsedVal);
                        if (parsedVal < todaysWorkouts.size() && parsedVal >= 0) {
                            String element = todaysWorkouts.get(parsedVal);
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
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            // Finally, Show the dialog
            alert.show();
        }
        // If nothing is to be done, then return null
        return null;
    }
}