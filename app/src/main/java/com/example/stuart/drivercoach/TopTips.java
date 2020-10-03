package com.example.stuart.drivercoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TopTips extends AppCompatActivity {

    String DriverID; //Defines variable for user identification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_tips); //Sets the mentioned layout as the display when this activity is running.

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity

    }

    public void acceleration (View view) //when the acceleration button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Acceleration.class);//Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void gear_change (View view) //when the gear change button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, GearChange.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void braking (View view) //when the braking button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Braking.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void menu (View view) //when the menu button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Menu.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }


}
