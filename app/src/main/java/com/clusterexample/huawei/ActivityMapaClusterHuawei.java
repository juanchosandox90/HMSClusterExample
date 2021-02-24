package com.clusterexample.huawei;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.clusterexample.huawei.models.PuntoVO;
import com.clusterexample.huawei.utils.MyHuaweiItem;
import com.clusterexample.huawei.utils.Servicios;
import com.clusterexample.huawei.utils.SharedPreferencesManager;
import com.clusterexample.huawei.utils.Singleton;
import com.huawei.clustering.Cluster;
import com.huawei.clustering.ClusterManager;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.HWLocation;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.site.api.model.Site;

import java.util.ArrayList;
import java.util.List;

public class ActivityMapaClusterHuawei extends FragmentActivity implements OnMapReadyCallback {

    private HuaweiMap mMap; // Might be null if Google Play services APK is not available.
    private MapView mMapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest mLocationRequest;

    LocationCallback mLocationCallback;
    private static final String TAG = "MapViewDemoActivity";
    private static final int REQUEST_CODE = 1000;

    private ArrayList<Site> sitList = new ArrayList();

    private static final String[] RUNTIME_PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET
    };

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    LatLng sourceLatLng;
    double latitude;
    double longitude;
    LatLng ubicacion;
    boolean abierto = false;
    private static Singleton singleton = Singleton.getInstance();
    private Servicios servicios;
    private List<PuntoVO> lista_puntos = new ArrayList<PuntoVO>();
    private Marker marker1;
    private List<Marker> marker = new ArrayList<Marker>();
    private List<Marker> marker_click = new ArrayList<Marker>();
    private Boolean abiertos = false;   // determina si la busqueda es puntos abiertos o cerrados

    private float previousZoomLevel = 14.0f;
    private boolean isZooming = false;
    private Boolean cargar = false;
    private int total_pag = 0;
    private int pagina = 0;

    float actualZoom;
    private ClusterManager<MyHuaweiItem> mClusterManager;
    private boolean isSourceAdded = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cluster_huawei);
        abierto = false;
        abiertos = false;
        lista_puntos.clear();
        latitude = Double.parseDouble(SharedPreferencesManager.getLatitud(getApplicationContext()));
        longitude = Double.parseDouble(SharedPreferencesManager.getLongitud(getApplicationContext()));
        ubicacion = new LatLng(latitude, longitude);

        if (!hasPermissions(this, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE);
        }

        mMapView = findViewById(R.id.mapHuawei);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
        // Set the location update interval (in milliseconds).
        mLocationRequest.setInterval(10000);
        // Set the weight.
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "---onLocationResult--");
                if (locationResult != null) {
                    // Process the location callback result.
                    if (locationResult != null) {
                        List<HWLocation> hwLocations = locationResult.getHWLocationList();
                        List<Location> locations = locationResult.getLocations();

                        if (hwLocations != null && hwLocations.size() > 0) {
                            Log.d(TAG, "---onLocationResult--hwLocations---" + hwLocations.size());
                            HWLocation hwLocation = hwLocations.get(0);
                            Log.d(TAG, "---onLocationResult--hwLocation---" + hwLocation.getLatitude() + "<----->" + hwLocation.getLongitude());
                            //Log.d(TAG, "---onLocationResult--hwLocation---");
                            LatLng latLng = new LatLng(hwLocation.getLatitude(), hwLocation.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                            mMap.animateCamera(cameraUpdate);
                            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                            sourceLatLng = latLng;
                            //addSource(sourceLatLng);


                        } else {
                            Log.d(TAG, "---onLocationResult--hwLocations not found---");
                        }

                        if (locations != null && locations.size() > 0) {
                            Log.d(TAG, "---onLocationResult--locations---" + locations.size());
                        } else {
                            Log.d(TAG, "---onLocationResult--locations not found---");
                        }
                    }
                }
            }
        };

        checkDeviceLocationSettings();
        getLoc();

    }

    private void addSource(LatLng latLng) {
        if (!isSourceAdded) {
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);
            isSourceAdded = true;
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkDeviceLocationSettings() {
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        //mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        // Initiate location requests when the location settings meet the requirements.
                        fusedLocationProviderClient
                                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Processing when the API call is successful.
                                        Log.d(TAG, "---onSuccess LocationSettingsResponse-");
                                        fusedLocationProviderClient.getLastLocation();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "---onFailure LocationSettingsResponse-");
                        // Device location settings do not meet the requirements.
                        int statusCode = ((ApiException) e).getStatusCode();
                        if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                // Call startResolutionForResult to display a pop-up asking the user to enable related permission.
                                rae.startResolutionForResult(ActivityMapaClusterHuawei.this, 0);
                            } catch (IntentSender.SendIntentException sie) {
                                //...
                            }
                        }
                    }
                });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }
    }

    private void getLoc() {
        fusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Processing when the API call is successful.
                        Log.d(TAG, "onSuccess location found");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Processing when the API call fails.
                        Log.d(TAG, "onFailure location found");
                    }
                });
    }


    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        huaweiMap.setMyLocationEnabled(true);

        huaweiMap.setOnMapLoadedCallback(new HuaweiMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d("MapLoaded: ", "[Map] Loaded.");
            }
        });
        mMap = huaweiMap;

        /*
        CLUSTER: si
         */
        // Declare a variable for the cluster manager.
        // Position the map.
        actualZoom = singleton.getZoomMap();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);
        //Cluster Items 1 by using addItems
        List<MyHuaweiItem> clusterItems = new ArrayList<>();
        for (int i = 0; i < lista_puntos.size(); i++) {
            double latitude = Double.parseDouble(lista_puntos.get(i).getLATITUDE());
            double longitude = Double.parseDouble(lista_puntos.get(i).getLONGITUDE());
            LatLng location = new LatLng(latitude, longitude);
            clusterItems.add(new MyHuaweiItem(location, lista_puntos.get(i).getNOMBRE(), R.drawable.mapa_pin));
        }

        if (singleton.getTotal_pag() == null) {
            singleton.setCiudad("Seleccione/-");
            singleton.setSoluciones("Seleccione/-");
            String data_sol[] = singleton.getSoluciones().split("/");
            String data_ciud[] = singleton.getCiudad().split("/");
            // spServicios.setText("" + data_sol[0]);
            //  spCiudad.setText("" + data_ciud[0]);
            singleton.setFiltroPorCiudad("no");
            singleton.setTipoProductoNew("0");
            singleton.setAbiertoNew("0");
            singleton.setIdCiudadNew("0");

            String distancia = "5";
            lista_puntos.clear();
            pagina = 0;
            singleton.setTouch_map(false);
            servicios.ServicioPuntoCercanosHuawei(this, latitude + "", longitude + "", distancia, "0", "0", 0);
            // servicios.ServicioPuntoCercanosHome(this,  "2.9368003", "-75.2819831",distancia, "0", "0", 0);

        } else {
            Log.e("Total_Pag:", "Not null!");
            mClusterManager.addItems(clusterItems);
            clusterManagers(mClusterManager);
            agregarPines(singleton.getLista_puntos(), singleton.getTotal_pag());
        }
    }

    /*
     * Clases y metodos para hacer cluster sobre el mapa
     */
    private void clusterManagers(ClusterManager<MyHuaweiItem> clusterManager) {
        clusterManager.setCallbacks(new ClusterManager.Callbacks<MyHuaweiItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyHuaweiItem> cluster) {
                Log.d(TAG, "onClusterClick");
                return false;
            }

            @Override
            public boolean onClusterItemClick(MyHuaweiItem clusterItem) {
                Log.d(TAG, "onClusterItemClick");
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        singleton.setTipoProductoNew("0");
        singleton.setIdCiudadNew("0");
        singleton.setFiltroPorCiudad("no");
        singleton.setNombreBusqueda("");
        singleton.setTipoDocumentoBusqueda("");
        singleton.setAbiertoNew("0");

        finish();
    }

    public void agregarPines(List<PuntoVO> puntos, String total_pag) {
        lista_puntos.clear();
        mClusterManager.clearItems();
        this.total_pag = Integer.parseInt(total_pag);
        lista_puntos.addAll(puntos);

        mMap.clear();
        marker.clear();
        marker_click.clear();
        marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

        for (int i = 0; i < lista_puntos.size(); i++) {
            double latitude = Double.parseDouble(lista_puntos.get(i).getLATITUDE());
            double longitude = Double.parseDouble(lista_puntos.get(i).getLONGITUDE());
            LatLng location = new LatLng(latitude, longitude);
            MyHuaweiItem offsetItem = new MyHuaweiItem(location, lista_puntos.get(i).getNOMBRE(), R.drawable.mapa_pin);
            mClusterManager.addItem(offsetItem);
        }

        if (lista_puntos.size() > 0) {
            double latitude = Double.parseDouble(lista_puntos.get(0).getLATITUDE());
            double longitude = Double.parseDouble(lista_puntos.get(0).getLONGITUDE());
            if (singleton.getPasoZoom() == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f));
                singleton.setPasoZoom(1);
            } else if (singleton.getPasoZoom() == 1) {
                singleton.setPasoZoom(0);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.3f));
            }

        }

    }
}