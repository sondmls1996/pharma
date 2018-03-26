package app.pharma.com.pharma.Fragment.Pill;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment_Detail extends Fragment {
    LinearLayout ln;
    private  ViewPager mPager;
    Slide_Image_Adapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.pharma_img,R.drawable.img_dr,R.drawable.img_sick};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    public Pill_Fragment_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pill__fragment__detail, container, false);
        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);
        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);

        mPager = (ViewPager) v.findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        LayoutInflater inflater2 = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 5; i++){
            final View rowView = inflater2.inflate(R.layout.item_pill_lq, null);
            ln.addView(rowView);

        }
        return v;
    }

}
