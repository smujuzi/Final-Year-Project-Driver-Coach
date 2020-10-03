package com.example.stuart.drivercoach;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Stuart on 13/02/2019.
 */



public class ConnectionClass {

    String classs = "com.mysql.jdbc.Driver";

    String url = "jdbc:mysql://10.154.1.101/drivercoachapp";
    String un = "ride";
    String password = "789";




    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);

            conn = DriverManager.getConnection(url, un, password);


            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}

    /*String ip;

    WifiManager manager = (WifiManager)  getApplicationContext().getSystemService(WIFI_SERVICE);
ip = Formatter.IPAddress(manager.getConnectionInfo().getIpAddress());
        System.out.println("IP address is "+ip);*/

