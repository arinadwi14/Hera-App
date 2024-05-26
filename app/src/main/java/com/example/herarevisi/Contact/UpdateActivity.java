package com.example.herarevisi.Contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.herarevisi.DB.MyDatabaseHelper;
import com.example.herarevisi.R;

public class UpdateActivity extends AppCompatActivity {

    EditText name_input, num_input, email_input;
    Button update_button, delete_button;
    String id, name, num, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name_input = findViewById(R.id.name_input2);
        num_input = findViewById(R.id.num_input2);
        email_input = findViewById(R.id.email_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle(name);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                name = name_input.getText().toString().trim();
                num = num_input.getText().toString().trim();
                email = email_input.getText().toString().trim();
                myDB.updateData(id, name, num, email);

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus " + name + " ?");
        builder.setMessage("Apakah kamu yakin ingin hapus " + name + " ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
        getIntent().hasExtra("num") && getIntent().hasExtra("email")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            num = getIntent().getStringExtra("num");
            email = getIntent().getStringExtra("email");

            //Setting Intent Data
            name_input.setText(name);
            num_input.setText(num);
            email_input.setText(email);
            Log.d("stev", name+" "+num+" "+email);
        } else {
            Toast.makeText(this, "Tidak Ada Data.", Toast.LENGTH_SHORT).show();
        }
    }
}