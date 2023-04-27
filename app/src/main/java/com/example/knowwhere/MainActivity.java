package com.example.knowwhere;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    LocationManager locationManager;
    LocationListener locationListener;
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    TextView tvHeading;

    Button sailbut;
    Button trekbut;

    ConstraintLayout menulayo;
    ConstraintLayout mainlayo;

    public void sailorbutton(View view)
    {
        sailbut = findViewById(R.id.button);
        menulayo = findViewById(R.id.menulayo);
        mainlayo = findViewById(R.id.mainlayo);
        menulayo.setVisibility(View.INVISIBLE);
        mainlayo.setVisibility(View.VISIBLE);
        mainlayo.setBackground(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.bg));
        ImageView compassimg = findViewById(R.id.imageViewCompass);
        compassimg.setImageDrawable(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.comppirate));
        ImageView nameimg = findViewById(R.id.imageView);
        nameimg.setImageDrawable(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.appnamepirate));


    }

    public void trekerbutton(View view)
    {
        sailbut = findViewById(R.id.button);
        menulayo = findViewById(R.id.menulayo);
        mainlayo = findViewById(R.id.mainlayo);
        menulayo.setVisibility(View.INVISIBLE);
        mainlayo.setVisibility(View.VISIBLE);
        mainlayo.setBackground(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.bgtrek));
        ImageView compassimg = findViewById(R.id.imageViewCompass);
        compassimg.setImageDrawable(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.img_compass));
        ImageView nameimg = findViewById(R.id.imageView);
        nameimg.setImageDrawable(ContextCompat.getDrawable(mainlayo.getContext(),R.drawable.appnametrek));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FOR LAYOUTS



        menulayo = findViewById(R.id.menulayo);
        mainlayo = findViewById(R.id.mainlayo);
        mainlayo.setVisibility(View.INVISIBLE);

        //FOR COMPASS
        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        //FOR LOCATION INFOS

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location)
            {
               // Log.i("location : ",location.toString());
                updatelocinfo(location);

            }
        };

        // ADDING PERMISSION CHECKER

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lstkwn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lstkwn!=null)
            {
                updatelocinfo(lstkwn);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
              starListening();

            }
        }
    }

    public void starListening()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }

    // FUNCTION TO UPDATE LOCATION INFO
    public void updatelocinfo(Location location)
    {
       // Log.i("location:",location.toString());
        TextView lattextview = findViewById(R.id.latitudetext);
        TextView longtextview = findViewById(R.id.longitudetext);
        TextView accutext = findViewById(R.id.accuracytext);
        TextView addtext = findViewById(R.id.addresstext);
        TextView altext = findViewById(R.id.altitudetexttextView4);

        lattextview.setText("Latitude : "+Double.toString(location.getLatitude()));
        longtextview.setText("Longitude : "+Double.toString(location.getLongitude()));
        accutext.setText("Accuracy : "+Double.toString(location.getAccuracy())+"%");
        altext.setText("Altitude : "+Double.toString(location.getAltitude())+" meters");

        String address = " Congratulations You are Lost !!!! ";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null&&addressList.size()>0)
            {
                Log.i("place info",addressList.get(0).toString());
                address = "Address :\n";
                if(addressList.get(0).getThoroughfare()!=null)
                {
                    address+=addressList.get(0).getThoroughfare()+"\n";
                }
                if(addressList.get(0).getLocality()!=null)
                {
                    address+=addressList.get(0).getLocality()+", ";
                }
                if(addressList.get(0).getPostalCode()!=null)
                {
                    address+=addressList.get(0).getPostalCode()+"\n";
                }
                if(addressList.get(0).getAdminArea()!=null)
                {
                    address+=addressList.get(0).getAdminArea()+", ";
                }
                if(addressList.get(0).getCountryName()!=null)
                {
                    address+=addressList.get(0).getCountryName();
                }
                else
                {
                    address+="";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addtext.setText(address);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading Towards : " + convertDegreeToCardinalDirection(degree) +"\n"+ Float.toString(degree) + " °");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public String convertDegreeToCardinalDirection(Float directionInDegrees){
        String cardinalDirection = null;
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "NORTH";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "NORTH OF NORTH EAST";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "NORTH EAST";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "EAST OF NORTH EAST";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "EAST";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "EAST OF SOUTH EAST";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "SOUTH EAST";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "SOUTH OF SOUTH EAST";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "SOUTH";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "SOUTH OF SOUTH WEST";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "SOUTH WEST";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "WEST OF SOUTH WEST";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "WEST";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "WEST OF NORTH WEST";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "NORTH WEST";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "NORTH OF NORTH WEST";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }
}