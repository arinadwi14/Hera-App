package com.example.herarevisi.Emergency;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.herarevisi.DB.MyDatabaseHelper;
import com.example.herarevisi.MainActivity;
import com.example.herarevisi.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmergencyListActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;

    private CardView cardViewEmergency;
    private static String number;
    private TextView textViewMessage;

    private static String locationStr;
    private static boolean check;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String emergencyPhoneNumber="";

    String[] appPermissions = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_list);

        check = true;

        // selecting cards
        cardViewEmergency = findViewById(R.id.cardViewEmergencyNumber);

        // database

        try {
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(EmergencyListActivity.this);
            emergencyPhoneNumber = myDatabaseHelper.getFirstRow().getPhoneNumber();
        }catch (Exception e)
        {

        }

        // location manager
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("location", location.toString());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresseList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresseList != null && addresseList.size() > 0) {
                        if (addresseList.get(0).getAdminArea() != null) {
                            locationStr = addresseList.get(0).getAddressLine(0);
                            // displays the location once but location keeps changing
                            if (check) {
                                String text = "Lokasi kamu : " + locationStr;
                                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                                toast.show();
                            }
                            check = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // listeners

        // 1.Fire
        cardViewEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = "112"; //Emergency Number at Semarang City
                String message = "Pengguna sedang dalam keadaan berbahaya, tolong bantu dia"; // this message is hard coded for now but it should get it information form db
                sendSms(message);
                makePhoneCall(number); // the number should be brought from database

            }
        });
    }

    private void makePhoneCall(String number) {

        try {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }catch (Exception e)
        {
            String text = "permission denied";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(EmergencyListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void sendSms(String sms) {
        //  ActivityCompat.requestPermissions(EmergencyListActivity.this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        try{
            if(locationStr != null)
            {
                sms += "\n" + "lokasi pengguna di: " + locationStr;
                SmsManager smsManager = SmsManager.getDefault();
                String firstEmergencyNumber = "HERA" + emergencyPhoneNumber;
                smsManager.sendTextMessage(firstEmergencyNumber, null, sms, null, null);
            }else{
                SmsManager smsManager = SmsManager.getDefault();
                String firstEmergencyNumber = "HERA" + emergencyPhoneNumber;
                smsManager.sendTextMessage(firstEmergencyNumber, null, sms, null, null);
            }
        }catch (Exception e)
        {
            String text = "permission denied";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(EmergencyListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

}
