package com.example.esra.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

public class ClassroomsMainActivity extends AppCompatActivity {
    ListView classroomslistview;
    private ArrayList<Classrooms> departments;
    private ArrayList<String> departmentCodes;
    private ArrayAdapter<String> adapter = null;
    private ArrayList<Classrooms2> classrooms2;
    private ArrayList<String> ClassroomsCodes;
    private ArrayList<String> orcun;
    EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms_main);
        classroomslistview = (ListView) findViewById(R.id.listView);
        // e1= (EditText) findViewById(R.id.editText);
        departments = new ArrayList<>();
        departmentCodes = new ArrayList<>();
        classrooms2 = new ArrayList<>();
        ClassroomsCodes = new ArrayList<>();
        new GetDepartments("http://178.62.227.214/departments.json", 0).execute();
        classroomslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new GetDepartments("http://178.62.227.214/departments/1.json", 1).execute();
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    private class GetDepartments extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(ClassroomsMainActivity.this);
        private String request;
        private int option;
        private JSONObject object;

        public GetDepartments(String request, int option) {
            this.request = request;
            this.option = option;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Getting Departments...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            switch (option) {
                case 0:
                    try {
                        JSONArray array = new JSONArray(Helpers.makeHTTPRequest(request));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject temp = array.getJSONObject(i);

                            departments.add(new Classrooms(temp.getString("code"), temp.getInt("id")));
                            departmentCodes.add(departments.get(i).getCode());
                        }
                        for (int i = 0; i < departments.size(); i++) {
                            Log.i("Departments : ", departments.get(i).toString());
                        }
                        return null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        object = new JSONObject(Helpers.makeHTTPRequest(request));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            switch (option) {
                case 0:
                    adapter = new ArrayAdapter<>(ClassroomsMainActivity.this, android.R.layout.simple_list_item_1, departmentCodes);
                    classroomslistview.setAdapter(adapter);
                    break;
                case 1:
                    Log.i("Json : ", object.toString());
                    try {
                        JSONArray array = object.getJSONArray("floors");
                        for (int i = 0; i < array.length(); i++) {
                            JSONArray temp = array.getJSONObject(i).getJSONArray("classrooms");

                            for (int j = 0; j < temp.length(); j++) {
                                classrooms2.add(new Classrooms2(temp.getJSONObject(j).getString("code"), temp.getJSONObject(j).getInt("floor_id")));
                                ClassroomsCodes.add(classrooms2.get(j).getClasscode() + "   " + classrooms2.get(j).getFloorsid());

                            }
                        }

                        orcun = new ArrayList<>();
                        for(int i =0;i<classrooms2.size();i++){
                            orcun.add(classrooms2.get(i).getClasscode()) ;
                            orcun.add(classrooms2.get(i).getFloorsid()+"");
                        }

                        Intent intent=new Intent(ClassroomsMainActivity.this,ShowingClassroomsActivity.class);
                        intent.putStringArrayListExtra("classrooms",orcun);
                        startActivity(intent);

                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }


    }
    }
}
