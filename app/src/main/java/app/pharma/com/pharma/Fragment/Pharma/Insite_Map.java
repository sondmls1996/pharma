package app.pharma.com.pharma.Fragment.Pharma;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;


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
        hideKeyboard();
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
           gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Vị trí của tôi"));
           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12f));
       }else{
           lat = 17.0828177;
           lng = 106.38272;
         //  gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),8f));
       }
        ArrayList<Pharma_Constructor> arrPm = Pharma_Fragment.arrPharma;
        if(arrPm!=null&&arrPm.size()>0){
            for (int i=0; i<arrPm.size();i++){
               new AsyncTask<Integer,MarkerOptions,Void>(){

                   @Override
                   protected Void doInBackground(Integer... i) {
                       MarkerOptions markerOptions = new MarkerOptions();
                       markerOptions.position(new LatLng(arrPm.get(i[0]).getX(),arrPm.get(i[0]).getY()));
                       markerOptions.title(arrPm.get(i[0]).getName());
                       markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                       markerOptions.snippet(arrPm.get(i[0]).getAdr());
                       publishProgress(markerOptions);
                       return null;
                   }

                   @Override
                   protected void onProgressUpdate(MarkerOptions... values) {
                       gg.addMarker(values[0]);
                       super.onProgressUpdate(values);
                   }

                   @Override
                   protected void onPostExecute(Void aVoid) {
                       super.onPostExecute(aVoid);
                   }
               }.execute(i);




                int finalI = i;


            }
            gg.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if(!marker.getTitle().equals("Vị trí của tôi")){
                        for (int i =0; i<arrPm.size();i++){
                            if(marker.getPosition().latitude==arrPm.get(i).getX()){
                                Intent it = new Intent(getActivity(), Detail.class);
                                it.putExtra("key","pharma");
                                it.putExtra("id",arrPm.get(i).getId());
                                startActivity(it);
                            }
                        }
                    }

                }
            });
        }




    }
    public void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
