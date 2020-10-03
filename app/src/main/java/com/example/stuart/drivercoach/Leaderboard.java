package com.example.stuart.drivercoach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class Leaderboard extends AppCompatActivity {

    String DriverID; //Defines variable for user identification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity


    }



    public void menu(View view) //when the menu button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Menu.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }

}

