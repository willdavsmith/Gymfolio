package edu.indiana.gymfolio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class GymfolioSelectActivity extends AppCompatActivity {

    String TAG = "SELECTEDITEMSFROMWEEK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymfolio_workout);
        String[] weekdays = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean[] value = extras.getBooleanArray("weekdaysSelectedArray");
            for (int k=0; k<weekdays.length; k++) {
                if (value[k]) Log.d(TAG, "Day Selected: " + weekdays[k]);
            }
        }
    }

}