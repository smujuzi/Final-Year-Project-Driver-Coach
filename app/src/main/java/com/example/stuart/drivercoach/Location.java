package com.example.stuart.drivercoach;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Stuart on 22/03/2019.
 */


public class Location extends Activity implements LocationListener {

    // Variables defined
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    Button finish;
    String DriverID; //Defines variable for user identification

    //Driver Evaluation variables
    private float Score;
    private float SumRCS= 0;
    private float SumPKE = 0;
    private float SpeedChange = 0;
    private float SqSpeed = 0;
    private float SqLastSpeed = 0;
    private float FinalRCS = 0;
    private float FinalPKE = 0;
    private long curTime;
    private long lastUpdate = 0;
    private float lastSpeed = 0;
    private float counter = 0;

    private float distance;
    private float acceleration;

    private float TotalSpeed;
    private float TotalDistance;
    private float TotalAcceleration;
    private float TotalTime =0;

    private float AvgSpeed;
    private float MaxSpeed = 0;
    private float speed;
    int gpsEnabled = 0;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        DriverID = getIntent().getStringExtra("DriverID"); //Allows you to link variables from previous activity

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        connectionClass = new ConnectionClass(); //creates new connection with database

        progressDialog = new ProgressDialog(this); //variable to allow user to know status of application
        finish = (Button) findViewById(R.id.finish);//attaches the variable to finish button

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //when the finish button is pressed the function finish is run

                // finish functinon used to send driver results to database
                Location.Dofinish dofinish = new Location.Dofinish();
                dofinish.execute(DriverID,
                        Float.toString(AvgSpeed),
                        Float.toString(MaxSpeed),
                        Float.toString(Score),
                        Float.toString(FinalRCS),
                        Float.toString(FinalPKE),
                        Float.toString(TotalTime),
                        Float.toString(TotalDistance));

            }
        });

        gpsEnabled = 1;



        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); // lm used to capture GPS

       //A check is done to see if permissions has been granted in Android Studio to access the GPS of phone
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                    , 10);
            Log.i("GPS", "Permissions error for GPS"); //error in accessing GPS
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Log.i("GPS", "Permissions ok for GPS"); //Confirms that access to GPS has been accepted
        this.onLocationChanged(null);

    }

//Further permissions are checked
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Log.i("GPS", "Permissions ok for GPS");
            this.onLocationChanged(null);

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.

        }
    }

    @Override
    //Checks to see when the GPS of the phone has changed
    public void onLocationChanged(android.location.Location location) {
        //Assigns variable to the text box in the layout of the page
        TextView txt = (TextView) this.findViewById(R.id.textView);

        //Conditional statement checks whether a GPS signal has been received.
        if (location == null) {
            txt.setText("-.- m/s"); /*Default display on the screen
                                    when no GPS signal has been received */
        } else {


            speed = location.getSpeed(); /*Speed value from GPS is stored
                                         into a variable "Speed" */


            /* we need to make sure we only sample a subset of the data
                we get from the device's accelerometer. We store the system's
                current time (in milliseconds) store it in curTime and check
                whether more than 100 milliseconds have passed since the last
                time onSensorChanged was invoked.
                * */
            curTime = System.currentTimeMillis();


            if ((curTime - lastUpdate) > 100 && speed > 0 ) //conditional statements used to ensure GPS taken
                                                            //at 100ms intervals and when vehicle is moving

            {
                //Vehicle parameters determined and calculations conducted below
                counter = counter + 1;
                TotalTime = TotalTime + 1f;
                lastUpdate = curTime;
                distance = speed*1f;
                acceleration = (speed - lastSpeed)/0.1f;
                SqSpeed = speed * speed;
                SpeedChange = SqSpeed - SqLastSpeed ;

                    SumRCS = SumRCS + ((speed * speed * speed) * 1f);
                    if( SpeedChange > 0.f ) {
                        SumPKE = SumPKE + SpeedChange;
                    }




                TotalSpeed = TotalSpeed + speed;
                TotalDistance = TotalDistance + distance;
                TotalAcceleration = TotalAcceleration + acceleration;

                FinalRCS = SumRCS / TotalDistance;
                FinalPKE = SumPKE / TotalDistance;

                SqLastSpeed = speed * speed;
                lastSpeed = speed;

                if (speed > MaxSpeed) {
                    MaxSpeed = speed;
                }
                AvgSpeed = TotalSpeed / counter;

                Tips(acceleration,AvgSpeed); //Sends values to Tips for display of Real-time feedback
                DriverEvaluation(FinalRCS,FinalPKE); //Sends values to Driver Evaluation for analysis of user data

            }



            }

            //Displays real-time vehicle parameters onto screen during trip.
            StringBuilder sb = new StringBuilder().append("Speed:").append(speed).append("\n");
            sb.append("Acceleration:").append(acceleration).append("\n");
            sb.append("Distance:").append(TotalDistance).append("\n");
            sb.append("Time:").append(TotalTime).append("\n");
            txt.setText(sb.toString());

        }




        @Override
        public void onStatusChanged (String s,int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled (String s){

        }

        @Override
        public void onProviderDisabled (String s){

        }


        /**
         * ATTENTION: This was auto-generated to implement the App Indexing API.
         * See https://g.co/AppIndexing/AndroidStudio for more information.
         */

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Location Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private void Tips (float A, float S)

    {  /*Conditional statements used to implement the characteristic parameters  from
        FTP75 drive cycle. Sets different display messages depending on the value of
        acceleration determined in real time.*/

        if (A > 0.57f)
        {
            Toast.makeText(getBaseContext(), "Accelerate Smoothly" , Toast.LENGTH_LONG).show();
        }

        if (A < -0.57f)
        {
            Toast.makeText(getBaseContext(), "Brake Gently" , Toast.LENGTH_LONG).show();
        }

        else
        {
            Toast.makeText(getBaseContext(), "Good Driving" , Toast.LENGTH_LONG).show();

        }

    }

    private void DriverEvaluation (float R, float P)

    { /*Conditional statements used to implement the various
        score determination conditions applied from benchmark data*/

        if (R > 200 && R < 380)
        {
            if (P > 0.29f && P < 0.4f)

            {
                    Score = 10;
            }

        }

        if (R > 180 && R < 450)
        {
            if (P > 0.25f && P < 0.43f)

            {
                Score = 8;
            }

        }

        if (R > 170 && R < 530)
        {
            if (P > 0.23f && P < 0.45f)

            {
                Score = 6;
            }

        }

        if (R > 150 && R < 660)
        {
            if (P > 0.21f && P < 0.5f)

            {
                Score = 4;
            }

        }

        if (R > 0 && R < 1200)
        {
            if (P > 0.1f && P < 0.7f)

            {
                Score = 2;
            }

        }

        else
        {
            Score = 11111111; //Means there was an error
        }



    }

    public class Dofinish extends AsyncTask<String, String, String> {


        String z = ""; //string variable  stored with nothing


        boolean isSuccess = false; //boolean variable defined as false

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..."); // displays message onto screen
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                Connection con = connectionClass.CONN();// connectionClass is called to connect to database
                if (con == null) {
                    z = "Upload Failed: Please check your internet connection"; //If the internet connection fails this message shows
                } else {

                    //query Variable defined and assigned SQL code to insert data into the demoregister table in the database
                    String query = "insert into trips values(NULL,'" + params[0] + "','" + params[1] + "','" + params[2] + "','" + params[3] + "','" + params[4] + "','" + params[5] + "','" + params[6] + "','" + params[7] + "')";

                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query); /* Command stored in the "query" variable is executed to communicate
                                                    with database */

                    z = "Trip Finished: Upload Complete"; //Indicates to user that their information has been uploaded to database
                    isSuccess = true; //Sets the boolean of the isSuccess variable to true


                }
            } catch (Exception ex) {
                isSuccess = false; //Sets the boolean of the isSuccess variable to false
                z = "Exceptions" + ex; // stores the phrase into string
            }

            return z;
        }


        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show(); //Displays on the screen what ever is stored in the z variable


            if (isSuccess) {

                Intent intent = new Intent(Location.this, TrackRecord.class); //Intent used to start new activity

                intent.putExtra("DriverID",DriverID); //DriverID is taken to the next activity.

                startActivity(intent);
            }


            progressDialog.hide();

            }

        }

    }



