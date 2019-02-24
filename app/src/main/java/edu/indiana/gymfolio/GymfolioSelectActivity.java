package edu.indiana.gymfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class GymfolioSelectActivity extends ListActivity implements OnClickListener {

    // Logcat tag for debugging purposes
    String TAG = "SELECTEDITEMSFROMWEEK";

    // ArrayList that will store the user's workouts for the day
    ArrayList<String> todaysWorkouts = new ArrayList<>();

    // An Adapter that will hold the data from above and connect it to the ListView
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_select);

        View AddButton = findViewById(R.id.btn_add_workout);
        AddButton.setOnClickListener(this);

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

            //TODO: ADD FILE I/O TO GET CURRENT WORKOUTS
            adapter.add(value);
        }
    }

    @SuppressWarnings("deprecation")
    public void onClick(View v) {
        switch(v.getId()) {
            // In the case where the user selects the 'Add' Button,
            // Invoke a dialog requesting the user to input a workout
            // and add it to the listView
            case R.id.btn_add_workout:
                showDialog(0);
        }
    }

    // Creates the dialog. Structure adapted from Meeting 5.
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            // Constructs the Dialog Box using a switch case with one option
            case 0:
                // Adapted from
                // https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog

                // Create the input text box and instantiate the AlertDialog Builder
                final EditText input = new EditText(this);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                // Set the title and the intro message
                alert.setTitle("Workout Customizer");
                alert.setMessage("Ex. 3x15 Bench 145");

                // Set the view for the AlertDialog Builder, in this case, set it to the EditText box
                alert.setView(input);

                // When the user writes their information into the EditText box,
                // Update the ListView holding their information and additionally
                // Show a Toast Dialog showing that the item was saved successfully
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        adapter.add(value);
                        //TODO: ADD USER INFO TO FILE
                        Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
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