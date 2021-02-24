package com.clusterexample.huawei;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Splash extends Activity {

    private final int DURACION_SPLASH = 4000;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // DataBaseHelper db = new DataBaseHelper(Splash.this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    permissioncheck();
                } else {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }, DURACION_SPLASH);
    }

//-************************ permission check ***********************************************************************

    private void permissioncheck() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("FINE_LOCATION");
        if (addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("COARSE_LOCATION");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                StringBuilder message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));
                showMessageOKCancel(message.toString(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[0]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                                }
                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[0]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //insertDummyContact();
    }


    private boolean addPermission(List<String> permissionsList, String permission) {

        boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    cond = false;
            }

            cond = true;


        } else {
            // Pre-Marshmallow
            cond = true;
        }

        return false;

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Splash.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 23) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            //Toast.makeText(SplashScreen.this, " Permissions are jddddd", Toast.LENGTH_SHORT).show();
            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted
                // insertDummyContact();

                //Toast.makeText(SplashScreen.this, " Permissions are l", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                // Permission Denied
                Toast.makeText(Splash.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        finish();
                    }
                }, 3000);
            }

        }
    }

}