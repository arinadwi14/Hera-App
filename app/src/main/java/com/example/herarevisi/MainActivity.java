package com.example.herarevisi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.herarevisi.Camera.CameraActivity;
import com.example.herarevisi.Contact.ContactsActivity;
import com.example.herarevisi.Contact.ListContactsActivity;
import com.example.herarevisi.DB.MyDatabaseHelper;
import com.example.herarevisi.Emergency.EmergencyContactsActivity;
import com.example.herarevisi.Emergency.EmergencyListActivity;
import com.example.herarevisi.Guide.GuideActivity;
import com.example.herarevisi.Setting.SettingActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener{

    Switch switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ToggleButton toggleButton;
    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    private String emergencyPhoneNumber = "1234567";
    private static final int PERMISSION_REQUEST_CODE = 1;

    Button btnPhone;
    CardView cvContact, cvEmergency, cvGallery, cvWa, cvSetting, cvGuide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPhone = findViewById(R.id.btnPhone);
        cvContact = findViewById(R.id.cvContact);
        cvEmergency = findViewById(R.id.cvEmergency);
        cvGallery = findViewById(R.id.cvGallery);
        cvWa = findViewById(R.id.cvWa);
        cvSetting = findViewById(R.id.cvSetting);
        cvGuide = findViewById(R.id.cvGuide);

        switchMode = findViewById(R.id.switchMode);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode){
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }

                editor.apply();
            }
        });

        cvGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });


        cvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        cvWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    startLocationUpdates();
                }
            }
        });

        cvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmergencyListActivity.class);
                startActivity(intent);
            }
        });

        cvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListContactsActivity.class);
                startActivity(intent);
            }
        });

        toggleButton = findViewById(R.id.panicButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()){
                    startSiren();
                } else {
                    stopSiren();
                }
            }
        });

//        database
        try {
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(MainActivity.this);
            emergencyPhoneNumber = myDatabaseHelper.getFirstRow().getPhoneNumber();
        } catch (Exception e){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void startSiren() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.police_siren);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Toast.makeText(MainActivity.this, "Tombol Panik Menyala!", Toast.LENGTH_SHORT).show();
    }

    private void stopSiren() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            Toast.makeText(MainActivity.this, "Tombol Panik Berhenti!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onReQuestPermissions(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startSiren();
            } else {
                Toast.makeText(MainActivity.this, "Perizinan Ditolak!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Mendapatkan lokasi sast ini
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

//        Mengirim lokasi ke Whatsapp
        String message = "Tolong aku!! Aku sedang dalam bahaya!" +
                "\nLokasiku ada di: https://maps.google.com/?q=" + latitude + "," + longitude;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
//        startActivity(sendIntent);

        if (sendIntent.resolveActivity(getPackageManager()) != null){
            startActivity(sendIntent);
        } else {
            Toast.makeText(MainActivity.this, "Whatsapp tidak terinstall", Toast.LENGTH_SHORT).show();
        }

//      Memberhentikan pembaruan lokasi setelah berhasil mengirim ke WhatsApp
        locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Izin akses lokasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

}