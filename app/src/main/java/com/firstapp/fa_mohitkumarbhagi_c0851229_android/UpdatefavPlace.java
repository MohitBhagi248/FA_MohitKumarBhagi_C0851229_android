package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdatefavPlace extends AppCompatActivity  implements
        View.OnClickListener, OnMapReadyCallback,
        LocationListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    EditText et_name,et_address;
    CheckBox cb;
    Button btn;

    String plname,address,createdOn,visited;
    double latitude,longitude, latitude_add,longitude_add;

    private GoogleMap mMap;
    Marker marker;
    Marker marker_add;
    MarkerOptions markerOptions_add;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationManager lm;

    boolean isGPSEnabled, isNetworkEnabled;

    Location location;

    String s_address, s_city, s_state, s_country, s_postalCode;
    String s_title;

    int d,m,y;
    String date;

    int i=0;

    DatabaseHelper databaseHelper;

    String in_id,in_placename,in_address,in_latitude,in_longitude,in_visited,in_createdon;

    double latitude3, longitude3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatefav_place);

        et_name=findViewById(R.id.et_plname);
        et_address=findViewById(R.id.et_address);
        cb=findViewById(R.id.checkBox);
        btn=findViewById(R.id.button);

        Intent intent = getIntent();

        in_id=intent.getStringExtra("id");
        in_placename=intent.getStringExtra("placename");
        in_address=intent.getStringExtra("address");
        in_latitude=intent.getStringExtra("latitude");
        in_longitude=intent.getStringExtra("longitude");
        in_visited=intent.getStringExtra("visited");
        in_createdon=intent.getStringExtra("createdon");

        latitude3=Double.parseDouble(in_latitude);
        longitude3=Double.parseDouble(in_longitude);

        latitude_add=latitude3;
        longitude_add=longitude3;

        et_name.setText(in_placename);
        et_address.setText(in_address);

        if(in_visited.equalsIgnoreCase("visited"))
        {
            cb.setChecked(true);
        }

        databaseHelper=new DatabaseHelper(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_map);
        mapFragment.getMapAsync(this);

        btn.setOnClickListener(this);

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

//        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

 //       lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 2, this);
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
    @Override
    public void onClick(View view)
    {
        if(view==btn)
        {
            plname=et_name.getText().toString().trim();
            address=et_address.getText().toString().trim();

            if(plname.equals(""))
            {
                et_name.setError("enter place name");
            }
            else if(address.equals(""))
            {
                et_address.setError("enter address");
            }
            else
            {
                if(cb.isChecked())
                {
                    visited="Visited";
                }
                else
                {
                    visited="Not visited yet";
                }

                AddressBean ab = new AddressBean();
                ab.setId(Integer.parseInt(in_id));
                ab.setPlacename(plname);
                ab.setAddress(address);
                ab.setLatitude(String.valueOf(latitude_add));
                ab.setLongitude(String.valueOf(longitude_add));
                ab.setVisited(visited);
                ab.setCreatedon(createdOn);

                Log.e("id...11", ""+ab.getId());

                int b = databaseHelper.updateFavPlace(ab);

                Log.e("b....",""+b);

                if(b>0)
                {
                    Toast.makeText(this, "address update successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        Log.e("latitude", ""+latitude3);
        Log.e("longitude", ""+longitude3);

        mMap=googleMap;

        mMap.clear();

        LatLng latLng = new LatLng(latitude3, longitude3);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title(s_address);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        Log.e("latitude change",""+location.getLatitude());
        Log.e("longitude change",""+location.getLongitude());

        if(i==0)
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.add_map);
            mapFragment.getMapAsync(this);
        }

        i=1;

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng)
    {
        mMap.clear();

        if(marker_add!=null)
        {
            marker_add.remove();
        }
        latitude_add=latLng.latitude;
        longitude_add=latLng.longitude;

        Log.e("latitude click",""+latitude_add);
        Log.e("longitude click",""+longitude_add);

        markerOptions_add = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).draggable(true).title("New Marker");

        markerOptions_add.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        marker_add=mMap.addMarker(markerOptions_add);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude_add, longitude_add, 1);

            if(addresses!=null && addresses.size()>0)
            {
                s_address = addresses.get(0).getAddressLine(0);
                s_city = addresses.get(0).getLocality();
                s_state = addresses.get(0).getAdminArea();
                s_country = addresses.get(0).getCountryName();
                s_postalCode = addresses.get(0).getPostalCode();

                s_title = s_address +","+s_city+","+s_state+","+s_country;
                marker_add.setTitle(s_address);
                marker_add.showInfoWindow();

                et_address.setText(s_address);
            }
            else
            {
                Calendar ca = Calendar.getInstance();

                d=ca.get(Calendar.DATE);
                m=ca.get(Calendar.MONTH)+1;
                y=ca.get(Calendar.YEAR);

                String d2,m2;

                if(d<10)
                {
                    d2="0"+d;
                }
                else
                {
                    d2=String.valueOf(d);
                }
                if(m<10)
                {
                    m2="0"+m;
                }
                else
                {
                    m2=String.valueOf(m);
                }

                date=d2+"/"+m2+"/"+y;

                createdOn=date;

                marker_add.setTitle(date);
                marker_add.showInfoWindow();

                et_address.setText("couldn't fetch address");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses==null)
        {
            Calendar ca = Calendar.getInstance();

            d=ca.get(Calendar.DATE);
            m=ca.get(Calendar.MONTH)+1;
            y=ca.get(Calendar.YEAR);

            String d2,m2;

            if(d<10)
            {
                d2="0"+d;
            }
            else
            {
                d2=String.valueOf(d);
            }
            if(m<10)
            {
                m2="0"+m;
            }
            else
            {
                m2=String.valueOf(m);
            }

            date=d2+"/"+m2+"/"+y;

            createdOn=date;

            marker_add.setTitle(date);
            marker_add.showInfoWindow();

            et_address.setText("couldn't fetch address");

        }

        mMap.setOnMarkerDragListener(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

        latitude_add=marker.getPosition().latitude;
        longitude_add=marker.getPosition().longitude;

        Log.e("latitude drag",""+latitude_add);
        Log.e("longitude drag",""+longitude_add);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude_add, longitude_add, 1);
            s_address = addresses.get(0).getAddressLine(0);
            s_city = addresses.get(0).getLocality();
            s_state = addresses.get(0).getAdminArea();
            s_country = addresses.get(0).getCountryName();
            s_postalCode = addresses.get(0).getPostalCode();

            s_title = s_address +",\n"+s_city+",\n"+s_state+","+s_country;
            marker_add.setTitle(s_address);
            marker_add.showInfoWindow();

            et_address.setText(s_address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses==null)
        {
            Calendar ca = Calendar.getInstance();

            d=ca.get(Calendar.DATE);
            m=ca.get(Calendar.MONTH)+1;
            y=ca.get(Calendar.YEAR);

            String d2,m2;

            if(d<10)
            {
                d2="0"+d;
            }
            else
            {
                d2=String.valueOf(d);
            }
            if(m<10)
            {
                m2="0"+m;
            }
            else
            {
                m2=String.valueOf(m);
            }

            date=d2+"/"+m2+"/"+y;

            createdOn=date;

            marker_add.setTitle(date);
            marker_add.showInfoWindow();

            et_address.setText("couldn't fetch address");

        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}