package com.example.herarevisi.DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.herarevisi.Contact.ContactsActivity;
import com.example.herarevisi.Emergency.EmergencyContact;
import com.example.herarevisi.MainActivity;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "EmergencyApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contact";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUM = "phone_number";
    private static final String COLUMN_EMAIL = "email";

    public MyDatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
                + " TEXT, " + COLUMN_NUM + " TEXT, " + COLUMN_EMAIL + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addContact (String name, String number, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_NUM, number);
        cv.put(COLUMN_EMAIL, email);
        System.out.println("test");
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            System.out.println("test1");
            Toast.makeText(context, "Gagal menambahkan nomor telepon", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("test2");
            Toast.makeText(context, "Berhasil menambahkan nomor telepon", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String name, String number, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_NUM, number);
        cv.put(COLUMN_EMAIL, email);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Gagal memperbarui kontak", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Berhasil memperbarui kontak", Toast.LENGTH_SHORT).show();
        }
    }

    public EmergencyContact getFirstRow(){
        String query = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null){
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
        }

        if (cursor != null){
            return new EmergencyContact(cursor.getString(1), cursor.getString(2));
        }
       return null;
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Gagal untuk menghapus kontak", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Berhasil menghapus kontak", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
