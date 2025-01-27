package com.example.herarevisi.Camera;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class PermissionManager {

    private static PermissionManager instance = null;
    private Context context;

    private PermissionManager(){

    }

    public static PermissionManager getInstance(Context context){
        if (instance == null){
            instance = new PermissionManager();
        }

        instance.init(context);
        return instance;
    }

    private void init(Context context) {
        this.context = context;
    }

    boolean checkPermissions(String[] permissions){
        int size = permissions.length;

        for (int i = 0; i < size; i++){
            if (ContextCompat.checkSelfPermission(context, permissions[i]) == PermissionChecker.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    void askPermissions(Activity activity, String[] permissions, int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    boolean handlePermissionResult(Activity activity, int requestCode, String[] permissions,
                                   int [] grantResults){
        boolean isAllPermissionsGranted = true;

        if (grantResults.length > 0){
            for (int i = 0; i < grantResults.length; i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity, "Perizinan Disetujui", Toast.LENGTH_SHORT).show();
                } else {
                    isAllPermissionsGranted = false;
                    Toast.makeText(activity, "Perizinan Ditolak", Toast.LENGTH_SHORT).show();
                    showPermissionRational(activity, requestCode, permissions, permissions[i]);
                    break;
                }
            }
        } else {
            isAllPermissionsGranted =false;
        }
        return isAllPermissionsGranted;
    }

    private void showPermissionRational(Activity activity, int requestCode, String[] permissions, String deniedPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)){
                showMessageOKCancel("Anda harus mengizinkan akses!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    askPermissions(activity, permissions, requestCode);
                                }
                            }
                        });
                return;
            }
        }
    }

    private void showMessageOKCancel(String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("Baik", onClickListener)
                .setNegativeButton("Batal", onClickListener)
                .create()
                .show();
    }
}
