package app.pharma.com.pharma.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.pharma.com.pharma.R;

public class Show_map extends AppCompatActivity implements OnMapReadyCallback{
    double lat,lng;
    GoogleMap gg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent it = getIntent();
        if(it.getExtras()!=null){
            lat = it.getDoubleExtra("lat",0);
            lng = it.getDoubleExtra("long",0);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        if(lat!=0&&lng!=0){
            gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
            gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15f));
        }
    }
}
