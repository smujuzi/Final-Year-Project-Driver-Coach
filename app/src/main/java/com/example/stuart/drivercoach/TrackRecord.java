package com.example.stuart.drivercoach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TrackRecord extends AppCompatActivity {

    // Variables defined
    TextView Results;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    String DriverID; //Defines variable for user identification


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_record); //Sets the mentioned layout as the display when this activity is running.



        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity

        connectionClass = new ConnectionClass(); //creates new connection with database


        progressDialog=new ProgressDialog(this); //variable to allow user to know status of application

        Results = (TextView) this.findViewById(R.id.textView21); //attaches variable to textbox in layout





                Dologin dologin=new Dologin(); //function called to be executed
                dologin.execute();




    }



    class Dologin extends AsyncTask<String,String,String> {


        String z = ""; //string variable  stored with nothing
        boolean isSuccess = false; //boolean variable defined as false
        StringBuilder sb; //string builder used later for displaying text on screen
        String id; //variable that shall be retrieved from database
        String speed,score,time,distance; //variables that shall be retrieved from database




        @Override
        protected String doInBackground(String... params) {
            isSuccess = false;
            z = "Connection Failed"; //displays this message if boolean remains false


            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Please check your internet connection"; //If the internet connection fails this message shows
                } else {
                    /*query variable defined and assigned SQL code to create a query from the trips table
                    that extracts the most recent trip from the database made by the currently logged in user*/
                    String query = "SELECT * FROM trips WHERE DriverID='"+DriverID+"' ORDER BY TripID DESC LIMIT 1 ";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);/* Command stored in the "query" variable is executed
                                                            to communicate with database */

                    while (rs.next()) {
                        /*Results from the query are retrieved by their column numbers and stored
                        into appropriate variables */

                        id =rs.getString(2);
                        score = rs.getString(5);
                        speed = rs.getString(3);
                        time=rs.getString(8);
                        distance=rs.getString(9);

                        /*User's results are then displayed onto the screen by building variables
                        into a single string and attaching them to a text box in the layout for this page*/
                        sb = new StringBuilder().append("Driver ID:").append(id).append("\n");
                        sb.append("Score:").append(score).append("\n");
                        sb.append("Average Speed:").append(speed).append("\n");
                        sb.append("Total Time:").append(time).append("\n");
                        sb.append("Total Distance:").append(distance).append("\n");

                        Results.setText(sb.toString());








                    }
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions" + ex;
            }


            return z;
        }





    }

    public void menu(View view) { //when the menu button is pressed this function is called

        Intent startNewActivity = new Intent(this, Menu.class); //Intent used to start new activity

        startNewActivity.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

        startActivity(startNewActivity);

    }

}

