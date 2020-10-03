/* ***********************************ORIGINAL WORKING CODE BELOW****************************************************************

TO-DO: Work out how to store variables from registration class and transfer them into this class

*/
package com.example.stuart.drivercoach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    // Variables defined
    EditText email, pass;
    Button login;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Sets the mentioned layout as the display when this activity is running.

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //This makes your design cover the full screen.
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email= (EditText) findViewById(R.id.email); //attaches variable to the user's input into the application
        pass= (EditText) findViewById(R.id.pass);


        login= (Button) findViewById(R.id.login); //attaches the variable to login button

        connectionClass = new ConnectionClass(); //creates new connection with database


        progressDialog=new ProgressDialog(this); //variable to allow user to know status of application


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //when the login button is pressed the function dologin is run
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });


    }

    public void sign_up(View view) //when the sign up button is pressed this function is called
    {

        Intent startNewActivity = new Intent(this, Registration.class); //Intent used to start new activity
        startActivity(startNewActivity);

    }

    private class Dologin extends AsyncTask<String,String,String> {




        String emailstr = email.getText().toString();//User input is stored into a string variable
        String passstr = pass.getText().toString(); //User input is stored into a string variable

        String z=""; //string variable  stored with nothing
        boolean isSuccess=false; //boolean variable defined as false

        String DriverID,nm,em,password; //variables that shall be retrieved from database


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..."); // displays message onto screen
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            isSuccess = false;
            z = "Login failed"; /* if the user input details don't match database
                                 then this message is displayed */

            if(emailstr.trim().equals("") ||passstr.trim().equals(""))
                z = "Please enter all fields...."; //if the user leaves the text boxes black they shall be prompted to input something
            else
            {
                try {
                    Connection con = connectionClass.CONN(); // connectionClass is called to connect to database
                    if (con == null) {
                        z = "Please check your internet connection"; //If the internet connection fails this message shows
                    } else {

                        /*query variable defined and assigned SQL code to create a query from the demoregister table
                          that extracts all emails and passwords from the database*/
                        String query="SELECT * FROM demoregister WHERE email='"+emailstr+"' AND PASSWORD = '"+passstr+"'";

                        Statement stmt = con.createStatement();
                        ResultSet rs=stmt.executeQuery(query);/* Command stored in the "query" variable is executed
                                                               to communicate with database */

                        while (rs.next())
                        {
                            /*Results from the query are retrieved by their column numbers and stored
                            into appropriate variables */

                            DriverID = rs.getString(1);
                            nm= rs.getString(2);
                            em=rs.getString(3);
                            password=rs.getString(4);

                            /*Conditional statement that checks whether the details input by the user
                              to login match any of the stored details in the database */
                            if(em.equals(emailstr)&&password.equals(passstr))
                            {
                                isSuccess=true; //Sets the boolean of the isSuccess variable to true
                                z = "Login successful"; // Indicates to user that correct details were used

                                return z;
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false; // boolean variable is set to false
                    z = "Exceptions"+ex; // stores the phrase into string
                }
            }


            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show(); //Displays on the screen what ever is stored in the z variable


            if(isSuccess) {
                Intent intent=new Intent(MainActivity.this,Menu.class); //This allows you to carry over values from one activity to another.

                intent.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

                startActivity(intent);
            }


            progressDialog.hide();

        }
    }
}



