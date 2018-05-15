package app.pharma.com.pharma.Fragment.Pharma;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
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


        private void init() {

            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
            pharmater = v.findViewById(R.id.tv_pharmater);
            adr = v.findViewById(R.id.tv_adr);
            ln = v.findViewById(R.id.ln_star_pharma);
            ln_phone = v.findViewById(R.id.ln_phone);
            ln_mess = v.findViewById(R.id.ln_mess);
            ln_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!strphone.equals("")){
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strphone));
                        startActivity(intent);
                    }
                }
            });
            ln_mess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!strphone.equals("")){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                                + strphone)));
                    }
                }
            });
            ln.removeAllViews();
            around = v.findViewById(R.id.tv_around);
            phone = v.findViewById(R.id.tv_phone);
            tv_title = v.findViewById(R.id.tv_name_pharma);
            tv_like = v.findViewById(R.id.txt_like);
            comment = v.findViewById(R.id.txt_comment);
            adapter = new Slide_Image_Adapter(Common.context,ImagesArray);
            img_share = v.findViewById(R.id.img_share);
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.shareLink(linkShare);
                }
            });
            mPager = (ViewPager) v.findViewById(R.id.slide_image);
            CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
            mPager.setAdapter(adapter);
            indicator.setViewPager(mPager);
            adapter.registerDataSetObserver(indicator.getDataSetObserver());
            id = Detail.id;

            hearth = (ImageView)v.findViewById(R.id.hearth_img);

            hearth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  checkHearth();
                }
            });
            getData();
            NUM_PAGES = 3;
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if(jo.has(JsonConstant.CODE)){
                        String code = jo.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                JSONObject store = jo.getJSONObject(JsonConstant.STORE);
                                tv_title.setText(store.getString(JsonConstant.NAME));
                                star = store.getDouble(JsonConstant.STAR);
                                JSONObject maploc = store.getJSONObject(JsonConstant.MAP_LOCATION);
                                lat = maploc.getDouble(JsonConstant.LAT);
                                lng = maploc.getDouble(JsonConstant.LONG);
                                adr.setText(store.getString(JsonConstant.USER_ADR));
                                phone.setText(store.getString(JsonConstant.PHONE));
                                strphone = store.getString(JsonConstant.PHONE);
                                tv_like.setText(store.getString(JsonConstant.LIKE));
                                comment.setText(store.getString(JsonConstant.COMMENT));
                                likeStt = store.getInt(JsonConstant.LIKE_STT);
                                linkShare = store.getString(JsonConstant.LINK_SHARE);

                                int s = Integer.valueOf(star.intValue());
                                LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

// insert into main view
                                for(int i = 0; i<s;i++){
                                    View star = vi.inflate(R.layout.star, null);

                                    ln.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
                                }
                                if(gg!=null){
                                    setMap();
                                }

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

    public void checkHearth(){
        if(like){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));
            like = false;
        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));
            like = true;
        }
    }
}