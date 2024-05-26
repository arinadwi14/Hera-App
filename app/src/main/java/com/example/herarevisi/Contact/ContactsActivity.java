package com.example.herarevisi.Contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.herarevisi.DB.MyDatabaseHelper;
import com.example.herarevisi.MainActivity;
import com.example.herarevisi.R;

public class ContactsActivity extends AppCompatActivity {

    EditText editName, editnum, editemail;
    Button create_btn, add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        editName = findViewById(R.id.editName);
        editnum = findViewById(R.id.editnum);
        editemail = findViewById(R.id.editemail);
        create_btn = findViewById(R.id.createbtn);
        add_button = findViewById(R.id.createbtn);


        //this listener is to create contact on database when clicking on create button
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ContactsActivity.this);
                myDB.addContact(
                        editName.getText().toString().trim(),
                        editnum.getText().toString().trim(),
                        editemail.getText().toString().trim());
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ContactsActivity.this);
                myDB.addContact(editName.getText().toString().trim(),
                        editnum.getText().toString().trim(),
                        editemail.getText().toString().trim());
            }
        });
    }
}