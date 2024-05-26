package com.example.herarevisi.SplashScreen;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herarevisi.MainActivity;
import com.example.herarevisi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private static String locationStr;
    private static boolean check;
    private LocationManager locationManager;
    private LocationListener locationListener;

    String[] appPermissions = {
            Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION
    };

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isLocationPermissionGranted = false;
    private boolean isCallPermissionGranted = false;
    private boolean isSend_sms_permissionGranted = false;

    private static final int PERMISSIONS_REQUEST_CODE = 123;


    private ViewPager viewPager;
    private CardView next;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private SaveState saveState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        check = true;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewPager = findViewById(R.id.viewPager);
        next = findViewById(R.id.nextCard);
        dotsLayout = findViewById(R.id.dotsLayout);
        saveState = new SaveState(this, "ob");

        if (saveState.getState() == 1){
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }

        ObAdapter adapter = new ObAdapter(this);
        viewPager.setAdapter(adapter);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotsFunction(position);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position < 3){
                            viewPager.setCurrentItem(position + 1, true);
                        } else {
                            saveState.setState(1);
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotsFunction(0);

        // permissions
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null)
                    isLocationPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                if (result.get(Manifest.permission.CALL_PHONE) != null)
                    isCallPermissionGranted = result.get(Manifest.permission.CALL_PHONE);
                if (result.get(Manifest.permission.SEND_SMS) != null)
                    isSend_sms_permissionGranted = result.get(Manifest.permission.SEND_SMS);
            }

        });

        requestPermissions();

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
                                String text = "Lokasi Anda Saat Ini : " + locationStr;
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

    private void requestPermissions() {
        // 1.location permission
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        // 2.call permission
        isCallPermissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;

        // 3.sms permission
        isSend_sms_permissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequests = new ArrayList<>();
        if (!isLocationPermissionGranted)
            permissionRequests.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (!isCallPermissionGranted)
            permissionRequests.add(Manifest.permission.CALL_PHONE);
        if (!isSend_sms_permissionGranted)
            permissionRequests.add(Manifest.permission.SEND_SMS);
        if (!permissionRequests.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequests.toArray(new String[0]));
        }
    }

    private void dotsFunction(int position) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("-"));
            dots[i].setTextColor(getColor(R.color.grey));
            dots[i].setTextSize(40);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setTextColor(R.color.purple);
            dots[position].setTextSize(40);
        }
    }
}