package app.pharma.com.pharma.Fragment.Pharma;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;
import me.relex.circleindicator.CircleIndicator;


public class Pharma_Detail_Fragment extends Fragment implements OnMapReadyCallback {

    private  ViewPager mPager;
    Slide_Image_Adapter adapter;
    ArrayList<String> arr;
    View v;
    GoogleMap gg;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.pharma_img,R.drawable.img_dr,R.drawable.img_sick};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
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
            for(int i=0;i<IMAGES.length;i++)
                ImagesArray.add(IMAGES[i]);
            adapter = new Slide_Image_Adapter(Common.context,ImagesArray);

            mPager = (ViewPager) v.findViewById(R.id.slide_image);
            CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
            mPager.setAdapter(adapter);
            indicator.setViewPager(mPager);
            adapter.registerDataSetObserver(indicator.getDataSetObserver());
//
//
//            CirclePageIndicator indicator = (CirclePageIndicator)
//                    findViewById(R.id.indicator);
//
//            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
     //       indicator.setRadius(5 * density);

            NUM_PAGES =IMAGES.length;

            // Auto start of viewpager
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

            // Pager listener over indicator

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        double lat = 21.028005;
        double lng = 105.834675;
        gg.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
        gg.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
    }
}