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

        // A string array representing the string values of the days of the week
        String[] weekdays = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

        // Get the extra data sent by the last activity
        Bundle bundle = getIntent().getExtras();

        // Make sure there are no null pointer exceptions...
        if (bundle != null) {
            // Get the boolean array sent by the last activity
            // (see 'weekdaysSelected' from GymfolioCustomizeActivity')
            boolean[] value = bundle.getBooleanArray("weekdaysSelectedArray");

            // Instantiate the adapter with a ListView (this), the android default list item layout,
            // and the above ArrayList.
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todaysWorkouts);
            setListAdapter(adapter);

            for (int i=0; i<value.length; i++) {
                if (value[i]) {
                    adapter.add(weekdays[i]);
                    Log.d(TAG, weekdays[i]);
                }
            }
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            // In the case where the user selects the 'Add' Button,
            // Add an item to the ListView
            case R.id.btn_add_workout:
                adapter.add("sample");
        }
    }

}