package com.example.esra.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ListIterator;

public class ShowingClassroomsActivity extends AppCompatActivity {
    private static ArrayList<Classrooms> departments;
    private static ArrayList<Object> departmentCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_classrooms);
        ArrayList<String> classroomlar = new ArrayList();
        Bundle bunble = getIntent().getExtras();
        //classroomlar.add(bundle.getStringArrayList("classrooms"));
        classroomlar = bunble.getStringArrayList("classrooms");

       for (int i = 0; i < classroomlar.size(); i+=2) {
           classroomlar.get(i);
        }


    }
    // return null;

    //  return null;


}

