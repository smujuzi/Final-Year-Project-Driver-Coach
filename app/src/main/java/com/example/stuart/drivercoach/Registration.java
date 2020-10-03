package com.example.stuart.drivercoach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Formatter;


/**
 * Created by Stuart on 13/02/2019.
 */



public class Registration extends AppCompatActivity {

    // Variables defined
    EditText name,email,pass;
    Button register /*,login*/;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration); //Sets the mentioned layout as the display when this activity is running.

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //This makes your design cover the full screen.
                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        name= (EditText) findViewById(R.id.name); //attaches variable to the user's input into the application
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.pass);
        register= (Button) findViewById(R.id.register);//attaches the variable to login button


        connectionClass = new ConnectionClass(); //creates new connection with database

        progressDialog=new ProgressDialog(this); //variable to allow user to know status of application

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //when the register button is pressed the function dologin is run


                Doregister doregister = new Doregister();
                doregister.execute("");
            }
        });


    }

    public class Doregister extends AsyncTask<String,String,String>
    {

        /*Variables defined to collect the details input
        by users into the text boxes on screen */

        String namestr=name.getText().toString();
        String emailstr=email.getText().toString();
        String passstr=pass.getText().toString();

        String z=""; //string variable  stored with nothing
        boolean isSuccess=false; //boolean variable defined as false

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..."); // displays message onto screen
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(namestr.trim().equals("")|| emailstr.trim().equals("") ||passstr.trim().equals(""))
                z = "Please enter all fields...."; //if the user leaves the text boxes black they shall be prompted to input something
            else
            {
                try {
                    Connection con = connectionClass.CONN(); // connectionClass is called to connect to database
                    if (con == null) {
                        z = "Please check your internet connection"; //If the internet connection fails this message shows
                    } else {

                        //query Variable defined and assigned SQL code to insert data into the demoregister table in the database
                        String query="insert into demoregister values(NULL,'"+namestr+"','"+emailstr+"','"+passstr+"')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query); /* Command stored in the "query" variable is executed to communicate
                                                    with database */

                        z = "Registeration successful"; //Indicates to user that their new account has been created
                        isSuccess=true; //Sets the boolean of the isSuccess variable to true


                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false; //Sets the boolean of the isSuccess variable to false
                    z = "Exceptions"+ex; // stores the phrase into string
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show(); //Displays on the screen what ever is stored in the z variable


            if(isSuccess) {
                //Intent used to start new activity
                Intent intent=new Intent(Registration.this,MainActivity.class);

                intent.putExtra("name",namestr); //name is taken to the next activity.
                intent.putExtra("email",emailstr); //email is taken to the next activity.
                intent.putExtra("pass",passstr); //pass is taken to the next activity.

                startActivity(intent);

            }


            progressDialog.hide();
        }
    }



}
