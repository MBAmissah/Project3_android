package com.example.project3_android;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Use the LocationComponent to easily add a device location "puck" to a Mapbox map.
 */
public class GPS extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_gps);
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GPS.this,MainActivity.class));
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        GPS.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mbamissah/ckrmoxs92beym17o5efbyoso0"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        try {
                            URI geoJsonUrl = new URI("http://192.168.29.102:8080/geoserver/Project3_Geoserver/wms?service=WMS&version=1.1.0&request=GetMap&layers=Project3_Geoserver%3Acountriesdbpedia&bbox=-180.0%2C-90.0%2C180.0%2C83.62359619140625&width=768&height=370&srs=EPSG%3A404000&styles=&format=geojson");
                            GeoJsonSource geoJsonSource = new GeoJsonSource("countriesdbpediaa", geoJsonUrl);
                            style.addSource(geoJsonSource);
                        } catch (URISyntaxException exception) {
                            Log.d("TAG", String.valueOf(exception));
                        }

// Create a RasterLayer with the source created above and then add the layer to the map
                        LineLayer terrainData = new LineLayer("web-map-source", "countriesdbpediaa");
                        terrainData.setSourceLayer("countriesdbpedia");
                        terrainData.setProperties(
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineCap(Property.LINE_CAP_ROUND),
                                lineColor(Color.parseColor("#ffffff")),
                                lineWidth(1.9f)
                        );

                        style.addLayer(terrainData);
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions in order to show its functionality.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "You didn't grant location permissions.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}