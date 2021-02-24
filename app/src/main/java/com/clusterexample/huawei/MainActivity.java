package com.clusterexample.huawei;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.clusterexample.huawei.models.PuntoVO;
import com.clusterexample.huawei.utils.Servicios;
import com.clusterexample.huawei.utils.SharedPreferencesManager;
import com.clusterexample.huawei.utils.Singleton;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationAvailability;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * HUAWEI LOCATION
     **/
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;
    private Button GoToMaps;
    double latitude;
    double longitude;
    private List<PuntoVO> lista_puntos = new ArrayList<PuntoVO>();
    private Servicios servicios = new Servicios();
    private int total_pag = 0;
    private int pagina = 0;
    private static Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationHuawei();
        GoToMaps = findViewById(R.id.goToMaps);
        GoToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distancia = "5";
                lista_puntos.clear();
                //pagina = 0;
                //singleton.setTouch_map(false);
                servicios.ServicioPuntoCercanosHomeHuawei(MainActivity.this, latitude + "", longitude + "", distancia, "0", "0", 0);
            }
        });

    }

    /**
     * Huawei Location Method
     */
    public void locationHuawei() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationRequest = new LocationRequest();
        // Sets the interval for location update (unit: Millisecond)
        mLocationRequest.setInterval(5000);
        // Sets the priority
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (null == mLocationCallback) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        List<Location> locations = locationResult.getLocations();
                        if (!locations.isEmpty()) {
                            for (Location location : locations) {
                                Log.i("HuaweiLocation",
                                        "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.getLongitude()
                                                + "," + location.getLatitude() + "," + location.getAccuracy());
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                SharedPreferencesManager.setLatitud(getApplicationContext(), latitude + "");
                                SharedPreferencesManager.setLongitud(getApplicationContext(), longitude + "");
                            }
                        }
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    if (locationAvailability != null) {
                        boolean flag = locationAvailability.isLocationAvailable();
                        Log.i("HuaweiLocation", "onLocationAvailability isLocationAvailable:" + flag);
                    }
                }
            };
        }
    }

    /**
     * Requests a location update and calls back on the specified Looper thread.
     */
    private void requestLocationUpdatesWithCallback() {
        Log.i("HuaweiLocation", "requestLocationUpdatesWithCallback");
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            // Before requesting location update, invoke checkLocationSettings to check device settings.
            Task<LocationSettingsResponse> locationSettingsResponseTask = mSettingsClient.checkLocationSettings(locationSettingsRequest);
            locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i("HuaweiLocation", "check location settings success");
                    mFusedLocationProviderClient
                            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("HuaweiLocation", "requestLocationUpdatesWithCallback onSuccess");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("HuaweiLocation",
                                            "requestLocationUpdatesWithCallback onFailure:" + e.getMessage());
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("HuaweiLocation", "checkLocationSetting onFailure:" + e.getMessage());
                            int statusCode = ((ApiException) e).getStatusCode();
                            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                try {
                                    //When the startResolutionForResult is invoked, a dialog box is displayed, asking you to open the corresponding permission.
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("HuaweiLocation", "PendingIntent unable to execute request.");
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("HuaweiLocation", "requestLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }

    /**
     * Removed when the location update is no longer required.
     */
    private void removeLocationUpdatesWithCallback() {
        try {
            Task<Void> voidTask = mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            voidTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("HuaweiLocation", "removeLocationUpdatesWithCallback onSuccess");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("HuaweiLocation", "removeLocationUpdatesWithCallback onFailure:" + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e("HuaweiLocation", "removeLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }

    /**
     * Method that add markers to a global array to then be used in the class that calls the Huawei Map.
     */

    public void agregarPinesHuawei(List<PuntoVO> puntos, String total_pag) {
        this.total_pag = Integer.parseInt(total_pag);
        for (int i = 0; i < puntos.size(); i++) {
            lista_puntos.add(puntos.get(i));
        }
        singleton.setLista_puntos(puntos);
        singleton.setTotal_pag(total_pag);
        singleton.setZoomMap(13.5f);
        Intent detalle = new Intent(getApplicationContext(), ActivityMapaClusterHuawei.class);
        startActivity(detalle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeLocationUpdatesWithCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdatesWithCallback();
    }
}