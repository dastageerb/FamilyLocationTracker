package com.example.familyLocationTracker.views.home;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

//public class LocationUpdaterService extends LifecycleService
//{
//
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private LocationRequest locationRequest;
//
//
//    public LocationUpdaterService()
//    {
//
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onCreate()
//    {
//        super.onCreate();
//
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        locationRequest = LocationRequest.create();
//        locationRequest.setInterval(30000);
//        locationRequest.setFastestInterval(20000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//
//        LocationCallback locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult)
//            {
//
//                Intent locationIntent = new Intent();
//
//                locationIntent.putExtra("latitude",locationResult.getLastLocation().getLatitude());
//                locationIntent.putExtra("longitude",locationResult.getLastLocation().getLongitude());
//                locationIntent.setAction("FILTER");
//                sendBroadcast(locationIntent);
//
//                updateDataBase(locationResult.getLastLocation().getLatitude(),
//                        locationResult.getLastLocation().getLongitude());
//
//            }
//        };
//
//
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//
//
//        Log.d(Constants.TAG, "onCreate: Service");
//
//    } // onCreate closed
//
//    private void updateDataBase(double latitude, double longitude)
//    {
//
//        DatabaseReference documentReference = FirebaseDatabase.getInstance().getReference();
//
//        String time = DateFormat.getDateTimeInstance().format(new Date());
//        documentReference
//                .child(Constants.DRIVER_CURRENT_LOCATION)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
//                .setValue(new TrackingData(latitude,longitude,time));
//
//
//    } // updateDataBase
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent)
//    {
//        return null;
//    }


//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//
//        Log.d(Constants.TAG, "onStartCommand: ");
//        return START_STICKY;
//    }
//
//
//    @Override
//    public void onDestroy()
//    {
//        super.onDestroy();
//    }


//} // ForGroundService