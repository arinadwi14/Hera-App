package com.example.herarevisi.Emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.herarevisi.Camera.CameraActivity;
import com.example.herarevisi.DB.MyDatabaseHelper;
import com.example.herarevisi.MainActivity;
import com.example.herarevisi.R;
import com.example.herarevisi.databinding.ActivityEmergencyContactsBinding;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {

    ActivityEmergencyContactsBinding binding;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EmergencyContactsActivity.this, MainActivity.class));
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        database
        Cursor cursor = null;
        List<EmergencyContact> emergencyContactList = new ArrayList<>();
        try {
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(EmergencyContactsActivity.this);
            cursor = myDatabaseHelper.readAllData();
        } catch (Exception e){

        }

        if (cursor == null){
            String text = "Tidak ada nomor darurat yang dihubungi";
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    EmergencyContact contact = new EmergencyContact(cursor.getString(1), cursor.getString(2));
                    emergencyContactList.add(contact);
                }
            }

            // emergencyContactList.add(new EmergencyContact("test" ,"0000000"));
            System.out.println("Daftar eme : " + emergencyContactList);

            List<String> fullNames = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            for (EmergencyContact contact : emergencyContactList){
                fullNames.add(contact.getFullName());
                phoneNumbers.add(contact.getPhoneNumber());
            }

            ArrayList<EmergencyContact> contactsList = new ArrayList<>();
            for (int i = 0; i < fullNames.size(); i++){
                EmergencyContact contact = new EmergencyContact(fullNames.get(i), phoneNumbers.get(i));
                contactsList.add(contact);
            }

            ListAdapter listAdapter = new ListAdapter(EmergencyContactsActivity.this, contactsList);

            binding.listViewContacts.setAdapter(listAdapter);
            binding.listViewContacts.setClickable(true);
            binding.listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String text = fullNames.get(i);
                    String phoneNumber = phoneNumbers.get(i);
                    makePhoneCall(phoneNumber);
                }
            });
        }
    }

    private void makePhoneCall(String number) {
        String dial = "tel:" + number;
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }
}