package com.example.esra.myapplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helpers {

    public static String constructMapsRequest(LatLng from, LatLng to, String mode) {
        final String KEY = "AIzaSyCYFW8bMdi3pNPNflPdCEtuikCFSgemsqw";

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + from.latitude + "," + from.longitude
                + "&destination=" + to.latitude + "," + to.longitude
                + "&sensor=false&units=metric&mode=" + mode + "&key=" + KEY;

        Log.i("URL: ", url);

        return url;
    }

    public static String makeHTTPRequest(String url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.connect();
            InputStream stream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            String response = stringBuffer.toString();

            Log.d("Response", response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
