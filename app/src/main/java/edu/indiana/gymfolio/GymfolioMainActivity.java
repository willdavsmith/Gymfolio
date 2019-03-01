package edu.indiana.gymfolio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;

public class GymfolioMainActivity extends AppCompatActivity implements OnClickListener {


    public void onClick(View v) {
        switch(v.getId()) {
            // In the case where the user selects the 'Customize Workout' Button,
            // Create a new Intent and start the GymfolioCustomizeActivity activity.
            case R.id.btn_customize_workout:
                Intent i = new Intent(this, GymfolioCustomizeActivity.class);
                startActivity(i);
                break;
            // In the case where the user selects the 'Begin Workout' Button,
            // Create a new Intent and start the GymfolioWorkoutActivity activity.
            case R.id.btn_begin_workout:
                Intent j = new Intent(this, GymfolioWorkoutActivity.class);
                startActivity(j);
                break;
            // In the case where the user selects the 'exit' Button,
            // Exit the application
            case R.id.btn_exit:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_main);

        // Set the click listener for the begin button
        View BeginButton = findViewById(R.id.btn_begin_workout);
        BeginButton.setOnClickListener(this);

        // Set the click listener for the customize button
        View CustomizeButton = findViewById(R.id.btn_customize_workout);
        CustomizeButton.setOnClickListener(this);

        // Set the click listener for the exit button
        View ExitButton = findViewById(R.id.btn_exit);
        ExitButton.setOnClickListener(this);
    }
}
