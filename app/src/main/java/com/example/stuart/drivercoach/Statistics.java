package com.example.stuart.drivercoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Statistics extends AppCompatActivity {

    String DriverID; //Defines variable for user identification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics); //Sets the mentioned layout as the display when this activity is running.

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity
    }

    public void leaderboard(View view) //when the leaderboard button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Leaderboard.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void fuel_saved(View view) //when the fuel saved button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, FuelSaved.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }
    public void menu(View view) //when the menu button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Menu.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }


}
