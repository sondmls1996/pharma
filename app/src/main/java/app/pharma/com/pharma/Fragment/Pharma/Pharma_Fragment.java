package app.pharma.com.pharma.Fragment.Pharma;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Fragment extends Fragment implements View.OnClickListener {

    ListView lv;
    List_Pharma_Adapter adapter;
    ArrayList<Pharma_Constructor> arr;
    Context ct;
    MapView mMapView;
    Class fragment;
    int page = 1;
    public static ArrayList<Pharma_Constructor> arrPharma;
    TextView tv_list;
    TextView tv_map;
    int lastVisibleItem = 0;
    private int lastY = 0;
    View v;
    public Pharma_Fragment() {
        // Required empty public constructor
    }
    GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_pharma, container, false);
        arrPharma = new ArrayList<>();
        initView();
        tv_list.performClick();

        return v;

    }

    private void initView() {
        ct = getContext();


        tv_list = v.findViewById(R.id.pharma_list);
        tv_map = v.findViewById(R.id.pharma_map);
        tv_map.setOnClickListener(this);
        tv_list.setOnClickListener(this);

//
    }




    public void ReplaceFrag(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_insite, fragment).commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pharma_list:
                changeColor(tv_list);
                fragment = Insite_List.class;
                ReplaceFrag(fragment);
                break;
            case R.id.pharma_map:
                changeColor(tv_map);
                fragment = Insite_Map.class;
                ReplaceFrag(fragment);
                break;
        }
    }

    private void changeColor(TextView tv) {
        tv_map.setTextColor(getActivity().getResources().getColor(R.color.gray));
        tv_map.setBackgroundColor(getActivity().getResources().getColor(R.color.light_gray));
        tv_list.setTextColor(getActivity().getResources().getColor(R.color.gray));
        tv_list.setBackgroundColor(getActivity().getResources().getColor(R.color.light_gray));
        if (tv==tv_map){
            tv_map.setTextColor(getActivity().getResources().getColor(R.color.blue));
            tv_map.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
         //   lv.setVisibility(View.GONE);

        }else{
            tv_list.setTextColor(getActivity().getResources().getColor(R.color.blue));
            tv_list.setBackgroundColor(getActivity().getResources().getColor(R.color.white));

         //   lv.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {

        super.onResume();

    }

}
