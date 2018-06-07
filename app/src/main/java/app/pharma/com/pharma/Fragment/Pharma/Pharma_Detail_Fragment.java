package app.pharma.com.pharma.Fragment.Pharma;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Pharma_Obj;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;
import app.pharma.com.pharma.activity.Show_map;
import me.relex.circleindicator.CircleIndicator;


public class Pharma_Detail_Fragment extends Fragment implements OnMapReadyCallback {


    ArrayList<String> arr;
    ImageView hearth;
    View v;
    boolean like = false;
    String id = "";
    LinearLayout ln,ln_phone,ln_mess;
    TextView tv_title;
    TextView tv_like, comment;
    ImageView img_share;
    int likeStt = 0;
    String linkShare = "";
    String strphone;
    User user;
    DatabaseHandle db;
    TextView adr;
    TextView around;
    Double star;
    double lat,lng;
    TextView pharmater;
    TextView phone;
    GoogleMap gg;
    private  ViewPager mPager;
    Slide_Image_Adapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Pharma_Obj pharma;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    public Pharma_Detail_Fragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v  = inflater.inflate(R.layout.fragment_pharma__detail_, container, false);
        init();
//        adapter.notifyDataSetChanged();
        return v;
    }
    public void RequestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constant.PHONE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strphone));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strphone));
                    startActivity(intent);
                } else {
                    Utils.dialogNotif("Ứng dụng sẽ không thể gọi điện nếu không cấp quyền");
                }
                return;
            }

        }
    }

        private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }

             pharma = new Pharma_Obj();
            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
            pharmater = v.findViewById(R.id.tv_pharmater);
            adr = v.findViewById(R.id.tv_adr);

            ln_phone = v.findViewById(R.id.ln_phone);
            ln_mess = v.findViewById(R.id.ln_mess);
            ln_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.setAlphalAnimation(view);
                    if(!strphone.equals("")){
                        if(Build.VERSION.SDK_INT>=23){
                        RequestPermission();
                        }else{
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strphone));
                            startActivity(intent);
                        }

                    }
                }
            });
            ln_mess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.setAlphalAnimation(view);
                    if(!strphone.equals("")){

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                + strphone)));
                    }
                }
            });
            ln = v.findViewById(R.id.ln_star_pharma);
            ln.removeAllViews();
            around = v.findViewById(R.id.tv_around);
            phone = v.findViewById(R.id.tv_phone);
            tv_title = v.findViewById(R.id.tv_name_pharma);
            tv_like = v.findViewById(R.id.txt_like);
            comment = v.findViewById(R.id.txt_comment);

            img_share = v.findViewById(R.id.img_share);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.setAlphalAnimation(view);
                    Utils.shareLink(linkShare);
                }
            });

            id = Detail.id;

            hearth = (ImageView)v.findViewById(R.id.hearth_img);

            hearth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.setAlphalAnimation(v);
                    onClickHeart();
//                    checkHearth();
                }
            });
            getData();

    }

    private void onClickHeart() {
        if(Utils.isLogin()){
            int likestt = pharma.getLikeStt();

            Map<String, String> map = new HashMap<>();
            map.put("type","store");
            map.put("id",pharma.getId());
            map.put("accessToken",user.getToken());
            if(likestt==0){
                map.put("likeStatus","1");
            }else{
                map.put("likeStatus","0");
            }

            Response.Listener<String> response  = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("LIKE_STT_PILL",response);
                    try {
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString(JsonConstant.CODE);
                        if(code.equals("0")){
                            if(likestt==0){
                                pharma.setLikeStt(1);

                            }else{
                                pharma.setLikeStt(0);
                            }

                            checkHearth(pharma.getLikeStt());
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Utils.PostServer(getActivity(),ServerPath.LIKE_PILL,map,response);
        }else{
            Utils.dialogNotif(getActivity().getResources().getString(R.string.you_not_login));
        }

    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("id",id);

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RESPONSE_PHARMA",response);
                    JSONObject jo = new JSONObject(response);
                    if(jo.has(JsonConstant.CODE)){
                        String code = jo.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                new AsyncTask<Void,Void,Void>(){

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        ImagesArray.clear();
                                        JSONObject data = null;
                                        try {
                                            data = jo.getJSONObject(JsonConstant.DATA);
                                            JSONObject store = data.getJSONObject(JsonConstant.STORE);
                                            JSONObject maploc = store.getJSONObject(JsonConstant.MAP_LOCATION);

                                            pharma.setName(store.getString(JsonConstant.NAME));
                                            pharma.setAdr(store.getString(JsonConstant.USER_ADR));
                                            pharma.setId(store.getString(JsonConstant.ID));
                                            pharma.setLike(store.getString(JsonConstant.LIKE));
                                            pharma.setStar(store.getDouble(JsonConstant.STAR));
                                            pharma.setComment(store.getString(JsonConstant.COMMENT));
                                            JSONArray images = store.getJSONArray(JsonConstant.IMAGE);

                                            for (int i =0; i<images.length();i++){
                                                if(!images.getString(i).equals("")){
                                                    ImagesArray.add(images.getString(i));
                                                }

                                            }
                                            pharma.setImage(ImagesArray);
                                            pharma.setPhone(store.getString(JsonConstant.PHONE));
                                            pharma.setLat(maploc.getDouble(JsonConstant.LAT));
                                            pharma.setLng(maploc.getDouble(JsonConstant.LONG));
                                            pharma.setLinkShare(store.getString(JsonConstant.LINK_SHARE));
                                            pharma.setLikeStt(store.getInt(JsonConstant.LIKE_STT));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        Detail.headerObj = pharma;
                                        tv_title.setText(pharma.getName());
                                        star = pharma.getStar();

                                        lat = pharma.getLat();
                                        lng = pharma.getLng();
                                        adr.setText(pharma.getAdr());
                                        phone.setText(pharma.getPhone());
                                        strphone = pharma.getPhone();
                                        tv_like.setText(pharma.getLike());
                                        comment.setText(pharma.getComment());
                                        likeStt = pharma.getLikeStt();
                                        linkShare = pharma.getLinkShare();
                                        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);
                                        mPager = (ViewPager) v.findViewById(R.id.slide_image);
                                        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
                                        mPager.setAdapter(adapter);
                                        indicator.setViewPager(mPager);
                                        adapter.registerDataSetObserver(indicator.getDataSetObserver());
                                        adapter.notifyDataSetChanged();
                                        int s = Integer.valueOf(star.intValue());
                                        LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        if(s !=0){
                                            for(int i = 0; i<s;i++){
                                                View star = vi.inflate(R.layout.star, null);

                                                ln.addView(star, 0, new ViewGroup.LayoutParams(40, 40));
                                            }
                                        }else{
                                            View nullView = vi.inflate(R.layout.null_textview, null);
                                            ln.addView(nullView, 0);
                                        }
// insert into main view
                                        checkHearth(pharma.likeStt);
                                        if(gg!=null){
                                            setMap();
                                        }


                                        super.onPostExecute(aVoid);
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




                                break;
                            case "1":
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(getActivity(), ServerPath.DETAIL_PHARMA,map,response);
    }

    private void setMap() {
        if(lat!=0&&lng!=0){
            gg.clear();
            gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
            gg.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),18f));

            if(Common.lat!=0&&Common.lng!=0){
                Location location = new Location("");
                location.setLatitude(Common.lat);
                location.setLongitude(Common.lng);
                Location finishLocation = new Location("");
                finishLocation.setLatitude(lat);
                finishLocation.setLongitude( lng);

                float distance = location.distanceTo(finishLocation);
                if(distance>=1000){
                    distance = distance/1000;
                    if(distance>0){
                        int d = (int) Math.ceil(distance);
                        String straround = d+"";
                        around.setText("Cách "+straround+" km");
                    }else{
                    }
                }else{
                    if(distance>0){
                        int d = (int) Math.ceil(distance);
                        String straround = d+"";

                        around.setText("Cách "+straround+" m");

                    }
                }

            }

        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;

        setMap();

        gg.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(lat!=0&&lng!=0){
                    Intent it = new Intent(getActivity(), Show_map.class);
                    it.putExtra("lat",lat);
                    it.putExtra("long",lng);
                    getActivity().startActivity(it);
                }
            }
        });
    }

    public void checkHearth(int likestt){
        if(likestt==0){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));

        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));

        }
    }
}