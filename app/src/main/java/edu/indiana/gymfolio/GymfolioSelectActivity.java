package edu.indiana.gymfolio;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.View.OnClickListener;

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

    public void onClick(View v) {
        switch(v.getId()) {
            // In the case where the user selects the 'Add' Button,
            // Invoke a dialog requesting the user to input a workout
            // and add it to the listView
            case R.id.btn_add_workout:
                //TODO: ADD DIALOG FOR USER INPUT
                //TODO: ADD USER INFO TO FILE
                //TODO: ADD TOAST (SUCCESS!)
                adapter.add("sample");
        }
    }

}