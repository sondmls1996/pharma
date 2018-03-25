package app.pharma.com.pharma.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;


public class Pharma_Detail_Fragment extends Fragment {

    private  ViewPager mPager;
    Slide_Image_Adapter adapter;
    ArrayList<String> arr;
    View v;
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
            for(int i=0;i<IMAGES.length;i++)
                ImagesArray.add(IMAGES[i]);

            mPager = (ViewPager) v.findViewById(R.id.slide_image);


            mPager.setAdapter(new Slide_Image_Adapter(Common.context,ImagesArray));
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
//            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int position) {
//                    currentPage = position;
//
//                }
//
//                @Override
//                public void onPageScrolled(int pos, float arg1, int arg2) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int pos) {
//
//                }
//            });
    }
}