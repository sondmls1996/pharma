package app.pharma.com.pharma.Fragment.Pharma;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Response;
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
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;


public class Insite_Map extends Fragment implements OnMapReadyCallback {
    View v;
    GoogleMap gg;
    double lat, lng;
    boolean isStart = true;
    BroadcastReceiver broadcastSearch;
    ArrayList<Pharma_Constructor> arrPm;
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

        gg.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(Utils.isGpsEnable(getActivity())){
                    if(gg.getMyLocation()!=null){
                        Common.lat = gg.getMyLocation().getLatitude();
                        Common.lng = gg.getMyLocation().getLongitude();
                    }
                }else{
                    Utils.dialogNotif(getResources().getString(R.string.gps_off));
                }

                return false;
            }
        });
//       if(Common.lat!=0&&Common.lng!=0){
//            lat = Common.lat;
//            lng = Common.lng;
//           gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Vị trí của tôi"));
//           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),8f));
//       }else{
//           lat = 17.0828177;
//           lng = 106.38272;
//         //  gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
//           gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),8f));
//       }
        arrPm = Pharma_Fragment.arrPharma;
        setMarker(arrPm);

    }

    private void setMarker(ArrayList<Pharma_Constructor> arrPm) {
        gg.clear();

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
        if(isStart){
            if(Common.lat!=0&&Common.lng!=0){
                lat = Common.lat;
                lng = Common.lng;
                gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Vị trí của tôi"));
                gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),10f));
            }else{
                lat = 17.0828177;
                lng = 106.38272;
                //   gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),10f));
            }
        }else{
            if(Common.lat!=0&&Common.lng!=0){
                lat = Common.lat;
                lng = Common.lng;
                gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Vị trí của tôi"));
                //   gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),8f));
            }else{
                lat = 17.0828177;
                lng = 106.38272;
                gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                // gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),8f));
            }
        }
    }

    public void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public void onResume() {
        if(broadcastSearch==null){
            registerBroadcast();
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        unRegister();
        super.onStop();
    }

    private void loadPageSearch(int page, String key){
        if(page==1){
            arrPm.clear();
            //adapter.notifyDataSetChanged();
        }
        Map<String, String> map = new HashMap<>();
        map.put("latGPS", Common.lat+"");
        map.put("longGPS",Common.lng+"");
        map.put("page",page+"");
        map.put("key",key);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                initJson(response,"show_all");
            }
        };
        Utils.PostServer(Common.context, ServerPath.LIST_PHARMA,map,response);
    }

    private void initJson(String response,String type) {
        Log.d("RESPONSE_INSITE_PHARMA",response);
        try {

            JSONObject jo = new JSONObject(response);

            if(jo.has(JsonConstant.CODE)){
                String code = jo.getString(JsonConstant.CODE);
                switch (code){
                    case "0":
                        getResponseData(jo,type);
                        break;
                    case "3":
                        getResponseData(jo,type);
                        break;
                    default:
                        getResponseData(jo,type);
                        break;
                }

            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getResponseData(JSONObject jo,String type) {
        boolean isEmpty[] = {false};
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    arrPm.clear();
                    JSONArray array = jo.getJSONArray(JsonConstant.LIST_STORE);
                    if(array.length()>0){
                        for (int i =0; i<array.length();i++){
                            JSONObject obj = array.getJSONObject(i);
                            JSONObject store = obj.getJSONObject(JsonConstant.STORE);
                            JSONArray images = store.getJSONArray(JsonConstant.IMAGE);
                            Pharma_Constructor pharma = new Pharma_Constructor();
                            pharma.setName(store.getString(JsonConstant.NAME));
                            pharma.setAdr(store.getString(JsonConstant.USER_ADR));
                            pharma.setComment(store.getString(JsonConstant.COMMENT));
                            pharma.setAvatar(images.getString(0));
                            pharma.setId(store.getString(JsonConstant.ID));
                            pharma.setLike(store.getString(JsonConstant.LIKE));
                            pharma.setRate(store.getDouble(JsonConstant.STAR));
                            JSONObject location = store.getJSONObject(JsonConstant.MAP_LOCATION);
                            pharma.setX(location.getDouble(JsonConstant.LAT));
                            pharma.setY(location.getDouble(JsonConstant.LONG));
                            arrPm.add(pharma);
                        }
                        isEmpty[0] = false;
                    }else {
                        isEmpty[0] = true;
                        return null;
                    }

                }catch (Exception e){
                    Utils.dialogNotif(getActivity().getResources().getString(R.string.server_err));
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setMarker(arrPm);
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        broadcastSearch=null;
    }
    private void registerBroadcast() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
//                    Mainpage = 1;
//                    isNomar = false;
//                    isSearch = true;
                    isStart = false;
                    String key = intent.getStringExtra("key");
                    loadPageSearch(1,key);

                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastSearch,
                it);
    }
}
