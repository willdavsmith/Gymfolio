package edu.indiana.gymfolio;

/*
 * GymfolioCustomizeActivity.java
 *
 * Provides the functionality for choosing which day to customize in the application
 * and sending data to GymfolioSelectActivity for processing based on the selection.
 *
 * Created by: Will Smith
 * Created on: 2/23/19
 * Last Modified by: Will Smith
 * Last Modified on: 3/1/19
 * Assignment/Project: A290 Android - Final Project
 * Part of: Gymfolio, associated to activity_gymfolio_customize.xml
 *
 **/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

public class GymfolioCustomizeActivity extends AppCompatActivity implements OnClickListener {

    // Declaring the switches that will be on screen
    Switch mondaySwitch;
    Switch tuesdaySwitch;
    Switch wednesdaySwitch;
    Switch thursdaySwitch;
    Switch fridaySwitch;
    Switch saturdaySwitch;
    Switch sundaySwitch;

    // Aggregating the switches into one array for ease of access
    Switch[] switches;

    // A string array representing the string values of the days of the week
    String[] weekdays = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_customize);

        View SaveButton = findViewById(R.id.btn_save);
        SaveButton.setOnClickListener(this);

        // Instantiating the Switch buttons represented on screen
        mondaySwitch = (Switch) findViewById(R.id.swc_monday);
        tuesdaySwitch = (Switch) findViewById(R.id.swc_tuesday);
        wednesdaySwitch = (Switch) findViewById(R.id.swc_wednesday);
        thursdaySwitch = (Switch) findViewById(R.id.swc_thursday);
        fridaySwitch = (Switch) findViewById(R.id.swc_friday);
        saturdaySwitch = (Switch) findViewById(R.id.swc_saturday);
        sundaySwitch = (Switch) findViewById(R.id.swc_sunday);

        // Instantiating the switch array
        switches = new Switch[]
                {mondaySwitch,tuesdaySwitch,wednesdaySwitch,thursdaySwitch,fridaySwitch,saturdaySwitch,sundaySwitch};
    }

    public void onClick(View v) {
        switch(v.getId()) {
            // When the user chooses the 'save' button,
            // check the state of the buttons and save the information.
            case R.id.btn_save:
                String weekdaySelected = "none";

                int x = 0;
                // For every switch on screen, update weekdaySelected
                // as the selected day
                for (Switch s : switches) {
                    if (s.isChecked()) weekdaySelected = weekdays[x];
                    x++;
                }

                // If the user selected a valid day
                if (!weekdaySelected.equals("none")) {
                    Intent i = new Intent(this, GymfolioSelectActivity.class);
                    // As well as switching to the new Activity, send data in the form of a
                    // String containing info about what item is currently selected
                    i.putExtra("weekdaySelected", weekdaySelected);
                    startActivity(i);
                    break;
                }
                else {
                    // Otherwise, go back to the main screen
                    Intent i = new Intent(this, GymfolioMainActivity.class);
                    startActivity(i);
                    break;
                }
        }
    }
}