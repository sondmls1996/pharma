package app.pharma.com.pharma.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.pharma.com.pharma.R;

public class Show_map extends AppCompatActivity implements OnMapReadyCallback {
    double lat, lng;
    GoogleMap gg;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        title = findViewById(R.id.title);
        title.setText("Bản đồ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent it = getIntent();
        if (it.getExtras() != null) {
            lat = it.getDoubleExtra("lat", 0);
            lng = it.getDoubleExtra("long", 0);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gg.setMyLocationEnabled(true);

        if(lat!=0&&lng!=0){
            gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
            gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15f));
        }
    }
}
