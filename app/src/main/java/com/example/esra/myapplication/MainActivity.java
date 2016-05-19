package com.example.esra.myapplication;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ArrayList<Department> departments;
    private ArrayList<String> departmentCodes;

    private GoogleMap map;

    private LatLng currentLocation;
    private Department targetDepartment;
    private final String MODE = "walking";

    private Spinner departmentSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esrahazal);

        departments = new ArrayList<>();
        departmentCodes = new ArrayList<>();

        new FetchDepartmentsTask().execute();

        createMap();

        departmentSpinner = (Spinner) findViewById(R.id.deptSpinner);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object temp = parent.getItemAtPosition(position);
                String departmentCode = temp.toString();
                for (int i = 0; i < departments.size(); i++) {
                    if (departmentCode.equals(departments.get(i).getCode())) {
                        targetDepartment = departments.get(i);
                        updateMap();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void createMap() {
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = fm.getMap();
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                currentLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            }
        });

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void updateMap() {
        map.clear();

        LatLng targetLocation = targetDepartment.getLocation();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(targetLocation);
        markerOptions.title(targetDepartment.getName());
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(targetLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 15));

        if (currentLocation != null) {
            String request = Helpers.constructMapsRequest(currentLocation, targetLocation, MODE);
            new CalculateDirectionTask().execute(request);
        }
    }

    private class FetchDepartmentsTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

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
            try {
                String response = Helpers.makeHTTPRequest("http://178.62.227.214/departments.json");

                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = array.getJSONObject(i);

                    departments.add(new Department(temp.getString("name"), temp.getString("code"), temp.getDouble("latitude"), temp.getDouble("longitude")));
                    departmentCodes.add(departments.get(i).getCode());
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, departmentCodes);
            departmentSpinner.setAdapter(adapter);
        }


    }

    private class CalculateDirectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String response = "";
            try {
                response = Helpers.makeHTTPRequest(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new DrawRouteTask().execute(result);
        }
    }

    private class DrawRouteTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override //
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(6);
                polyLineOptions.color(Color.BLUE);
            }

            map.addPolyline(polyLineOptions);
        }
    }
}
