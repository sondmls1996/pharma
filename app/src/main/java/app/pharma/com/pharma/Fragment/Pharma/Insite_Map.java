package app.pharma.com.pharma.Fragment.Pharma;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;


public class Insite_Map extends Fragment implements OnMapReadyCallback {
    View v;
    GoogleMap gg;
    double lat, lng;

    public Insite_Map() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_insite__map, container, false);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
       if(Common.lat!=0&&Common.lng!=0){
            lat = Common.lat;
            lng = Common.lng;
           gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15f));
       }else{
           lat = 17.0828177;
           lng = 106.38272;
         //  gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),7f));
       }


    }
}
