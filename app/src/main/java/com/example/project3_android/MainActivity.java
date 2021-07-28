package com.example.project3_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.CustomLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.net.URI;
import java.net.URISyntaxException;

import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GPS.class));
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {


                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mbamissah/ckrmoxs92beym17o5efbyoso0"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        try {
                            URI geoJsonUrl = new URI("http://192.168.29.102:8080/geoserver/Project3_Geoserver/wms?service=WMS&version=1.1.0&request=GetMap&layers=Project3_Geoserver%3Acountriesdbpedia&bbox=-180.0%2C-90.0%2C180.0%2C83.62359619140625&width=768&height=370&srs=EPSG%3A404000&styles=&format=geojson");
                            GeoJsonSource geoJsonSource = new GeoJsonSource("countriesdbpediaa", geoJsonUrl);
                            style.addSource(geoJsonSource);
                        } catch (URISyntaxException exception) {
                            Log.d("TAG", String.valueOf(exception));
                        }
//// Add the web map source to the map
//                        style.addSource(new VectorSource(
//                                "countriesdbpediaa",
//                                new TileSet("tileset", "http://192.168.29.102:8080/geoserver/Project3_Geoserver/wms?service=WMS&version=1.1.0&request=GetMap&layers=Project3_Geoserver%3Acountriesdbpedia&bbox=-180.0%2C-90.0%2C180.0%2C83.62359619140625&width=768&height=370&srs=EPSG%3A404000&styles=&format=application%2Fvnd.mapbox-vector-tile")));

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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}