package com.example.stuart.drivercoach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Menu extends AppCompatActivity {

    String DriverID; //Defines variable for user identification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); //Sets the mentioned layout as the display when this activity is running.

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity
        Toast.makeText(getBaseContext(),""+DriverID,Toast.LENGTH_LONG).show(); //Shows the DriverID of the user currently logged in

    }

    public void begin_drive (View view) //when the begin drive button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, BeginDrive.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void track_record (View view) //when the track record button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, TrackRecord.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);
    }

    public void top_tips (View view) //when the top tips button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, TopTips.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);
    }

    public void statistics (View view) //when the statistics button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Statistics.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);
    }

    public void sign_out(View view) //when the sign out button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, MainActivity.class); //Intent used to start new activity

        startActivity(startNewActivity);
    }


}
