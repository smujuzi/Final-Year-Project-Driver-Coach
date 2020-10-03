package com.example.stuart.drivercoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Acceleration extends AppCompatActivity {

    String DriverID; //Defines variable for user identification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceleration); //Sets the mentioned layout as the display when this activity is running.

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity
    }

    public void menu (View view) //when the menu button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Menu.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }


}
