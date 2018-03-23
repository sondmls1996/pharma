package app.pharma.com.pharma.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;


public class Pharma_Detail_Fragment extends Fragment {

    private  ViewPager mPager;
    Slide_Image_Adapter adapter;
    ArrayList<String> arr;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.map,R.drawable.map,};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    public Pharma_Detail_Fragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v  = inflater.inflate(R.layout.fragment_pharma__detail_, container, false);
        mPager = v.findViewById(R.id.slide_image);

        arr = new ArrayList<>();
        arr.add("http://caodangykhoa.vn/wp-content/uploads/3-6.jpg");
        arr.add("http://caodangykhoa.vn/wp-content/uploads/3-6.jpg");
        arr.add("http://caodangykhoa.vn/wp-content/uploads/3-6.jpg");

        adapter = new Slide_Image_Adapter(Common.context, arr);
        mPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }
}