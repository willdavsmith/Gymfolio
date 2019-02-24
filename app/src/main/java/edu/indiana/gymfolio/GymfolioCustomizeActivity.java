package edu.indiana.gymfolio;

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
                boolean[] weekdaysSelected = new boolean[7];

                int x = 0;
                // For every switch on screen, update the weekdaysSelected
                // array with the expected value
                // (true for on, false for off)
                for (Switch s : switches) {
                    weekdaysSelected[x] = s.isChecked();
                    x++;
                }
                Intent i = new Intent(this, GymfolioSelectActivity.class);
                // As well as switching to the new Activity, send data in the form of a
                // boolean array containing info about what items are currently selected
                i.putExtra("weekdaysSelectedArray",weekdaysSelected);
                startActivity(i);
                break;
        }
    }
}