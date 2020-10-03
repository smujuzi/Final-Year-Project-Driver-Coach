package com.example.stuart.drivercoach;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

//Required for connection to database


public class Accelerometer extends AppCompatActivity implements SensorEventListener { //allows you to use the Sensor Event Listener interface in the Android SDK.

    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    Button finish;

    private TextView textView;
    //Sensor manager

    private SensorManager sensorManager; //private variables declared for sensor code.
    private Sensor accelerometerSensor;

    private float RCS = 0;  //Relative Cubic Speed
    private float PKE = 0;  //Positive Kinetic Energy
    private float lastx, lasty, lastz;
    private float averagex, averagey, averagez;
    private long lastUpdate = 0;
    private float speed = 0;
    private float angle;
    private float TotalTime = 0;
    private float count = 0;
    private float distance = 0;
    private float acceleration = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer);

        textView = (TextView) this.findViewById(R.id.textview11);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // Initializes private variables created above.
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        lastx = lasty = lastz = 0;

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        connectionClass = new ConnectionClass();

        progressDialog = new ProgressDialog(this);
        finish = (Button) findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dofinish dofinish = new Dofinish();
                dofinish.execute(Float.toString(averagex), //takes the average values of the accelerometer to the database.
                        Float.toString(averagey),
                        Float.toString(averagez),
                        Float.toString(RCS),
                        Float.toString(PKE));
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) { //This is standard method required to be added if a sensor is called. Used to detect movement.
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        // To get the values of each axis, we ask the sensor event for its values as shown below. The event's values attribute is an array of floats
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        acceleration = (Math.abs((x + y + z)));
        angle = (float) (Math.atan2(y, Math.sqrt(x * x + z * z)) / (Math.PI / 180));

        /* we need to make sure we only sample a subset of the data
            we get from the device's accelerometer. We store the system's
            current time (in milliseconds) store it in curTime and check
            whether more than 100 milliseconds have passed since the last
            time onSensorChanged was invoked.
            * */
        long curTime = System.currentTimeMillis();


        if ((curTime - lastUpdate) > 100) {


            TotalTime = TotalTime + (curTime - lastUpdate);
            lastUpdate = curTime;

            //speed = ((x + y + z ) /10); //in m/s.
            //speed = Math.abs((x + y + z ) /10); //in m/s.
            acceleration = (Math.abs((x + y + z)));
            speed = speed + acceleration/10;
            distance = distance + speed / 10;
            RCS = (RCS + ((speed * speed * speed) / 10)) / distance; //Need to figure out how to calculate the distance
            PKE = (PKE + (speed * speed)) / distance;


            //speed = Math.abs(x + y + z - lastx - lasty - lastz) / (TotalTime / 1000); //in m/s. You need to fix the speed!!!!

            lastx = lastx + x;
            lasty = lasty + y;
            lastz = lastz + z;


            count = count + 1;
            averagex = lastx / count;
            averagey = lasty / count;
            averagez = lastz / count;


            //Code to send values of accelerometer to the table "trips"

        }


        //Displays values onto screen

        StringBuilder sb = new StringBuilder().append("x:").append(x).append("\n");
        sb.append("y:").append(y).append("\n");
        sb.append("z:").append(z).append("\n");
        sb.append("degree:").append(angle).append("\n");
        sb.append("speed:").append(speed);
        textView.setText(sb.toString());


       /* if (x != lastx || y != lasty || z != lastz) {
            if (y > 3) {
                System.out.println("Acceleration is high! Slow Down");
            }
            if (x < 0) {
                System.out.println("Brakes are being pressed too hard! Slow Down");
            }
            if (x > 3) {
                System.out.println("Change Gear");
            }

            System.out.print(x);
            System.out.print(" ");
            System.out.print(y);
            System.out.print(" ");
            System.out.println(z);
        } */

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { //This is standard method required to be added if a sensor is called

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Accelerometer Page") // TODO: Define a title for the content shown.
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

    public class Dofinish extends AsyncTask<String, String, String> {


        String z = "";


        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Upload Failed: Please check your internet connection";
                } else {

                    //String query = "insert into trips values('" + xstr + "','" + ystr + "','" + zstr + "','" + anglestr + "','" + speedstr + "')";
                    String query = "insert into trips values(NULL,'" + params[0] + "','" + params[1] + "','" + params[2] + "','" + params[3] + "','" + params[4] + "')";

                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    z = "Trip Finished: Upload Complete";
                    isSuccess = true;


                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions" + ex;
            }

            return z;
        }


        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();


            if (isSuccess) {

                Intent intent = new Intent(Accelerometer.this, TrackRecord.class);

                startActivity(intent);

            }


            progressDialog.hide();
        }
    }
}




