package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.firstapp.fa_mohitkumarbhagi_c0851229_android.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String id,placename,address,latitude,longitude,visited,createdon;

    LocationManager lm;

    int i=0;

    double latitude2,latitude3;
    double longitude2,longitude3;

    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;

    TextView tv_dist,tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tv_dist=binding.tvdist;
        tv_time=binding.tvtime;

        Intent intent = getIntent();

        id=intent.getStringExtra("id");
        placename=intent.getStringExtra("placename");
        address=intent.getStringExtra("address");
        latitude=intent.getStringExtra("latitude");
        longitude=intent.getStringExtra("longitude");
        visited=intent.getStringExtra("visited");
        createdon=intent.getStringExtra("createdon");

        latitude3=Double.parseDouble(latitude);
        longitude3=Double.parseDouble(longitude);

        mDestination=new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        mMarkerPoints = new ArrayList<>();

        fetchLocation();

    }

    @SuppressLint("MissingPermission")
    private void fetchLocation()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 2, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat=Double.parseDouble(latitude);
        double lng=Double.parseDouble(longitude);

        LatLng latlng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(latlng).title(address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,15));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude2=location.getLatitude();
        longitude2=location.getLongitude();

        mOrigin=new LatLng(latitude2, longitude2);

        if(mMarkerPoints.size()>1){
            mMarkerPoints.clear();
            mMap.clear();
        }

        mMarkerPoints.add(mOrigin);
        mMarkerPoints.add(mDestination);

        drawRoute();

        Log.e("latitude change",""+location.getLatitude());
        Log.e("longitude change",""+location.getLongitude());

        if(i==0)
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        i=1;

    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

    }

    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=AIzaSyCx8EPbn-_-jTpHJGyqArrqMPfYK2ushVU";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

                LatLng lng2=new LatLng(latitude3, longitude3);

                getDestinationInfo(lng2);


            }else
                Log.e("error","no route.............");
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }

    private void getDestinationInfo(LatLng latLngDestination) {
        String serverKey = "AIzaSyCx8EPbn-_-jTpHJGyqArrqMPfYK2ushVU";
        final LatLng origin = new LatLng(latitude2, longitude2);
        final LatLng destination = latLngDestination;
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();

                            showingDistanceTime(distance, duration);
                            // Showing distance and time to the user in the UI \\
//                            String message = "Total Distance is " + distance + " and Estimated Time is " + duration;
//                            StaticMethods.customSnackBar(consumerHomeActivity.parentLayout, message,
//                                    getResources().getColor(R.color.colorPrimary),
//                                    getResources().getColor(R.color.colorWhite), 3000);

                            //--------------Drawing Path-----------------\\
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this,
                                    directionPositionList, 5, getResources().getColor(R.color.color2));
                        //    mMap.addPolyline(polylineOptions);
                            //--------------------------------------------\\

                            //-----------Zooming the map according to marker bounds-------------\\
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(destination);
                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            mMap.animateCamera(cu);
                            //------------------------------------------------------------------\\

                        } else if (status.equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(MapsActivity.this, "No routes exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                        Log.e("error",t.getMessage());
                    }
                });
        //-------------------------------------------------------------------------------\\

    }

    public void showingDistanceTime(String distance, String time)
    {
        tv_dist.setText("Total Distance - "+distance+" KM");

        tv_time.setText("Total Time - "+time);
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}