package app.pharma.com.pharma.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Pharma_Constructor;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Fragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    ListView lv;
    List_Pharma_Adapter adapter;
    ArrayList<Pharma_Constructor> arr;
    Context ct;
    MapView mMapView;
    int lastVisibleItem = 0;
    TextView tv_list;
    TextView tv_map;
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
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        initView();
        tv_list.performClick();

        return v;

    }

    private void initView() {

        lv = (ListView)v.findViewById(R.id.lv_pharma);
        arr = new ArrayList<>();
        adapter = new List_Pharma_Adapter(getContext(),0,arr);
        ct = getContext();
        lv.setAdapter(adapter);
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        adapter.notifyDataSetChanged();

        tv_list = v.findViewById(R.id.pharma_list);
        tv_map = v.findViewById(R.id.pharma_map);
        tv_map.setOnClickListener(this);
        tv_list.setOnClickListener(this);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                int top = 0;
                if(lv.getChildAt(0) != null){
                    top = lv.getChildAt(0).getTop();
                }

                if(firstVisibleItem > lastVisibleItem){
                    Intent it = new Intent(Constant.SCROLL_LV);
                    it.putExtra("action",Constant.ACTION_DOWN);
                    ct.sendBroadcast(it);
                }else if(firstVisibleItem < lastVisibleItem){
                    Intent it = new Intent(Constant.SCROLL_LV);
                    it.putExtra("action",Constant.ACTION_UP);
                    ct.sendBroadcast(it);
                }else{
                    if(top < lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_DOWN);
                        ct.sendBroadcast(it);
                    }else if(top > lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_UP);
                        ct.sendBroadcast(it);
                    }
                }
                lastVisibleItem = firstVisibleItem;
                lastY = top;

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getActivity(), Detail.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pharma_list:
                changeColor(tv_list);

                break;
            case R.id.pharma_map:
                changeColor(tv_map);
                break;
        }
    }

    private void changeColor(TextView tv) {
        tv_map.setTextColor(Constant.resources.getColor(R.color.black));
        tv_map.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        tv_list.setTextColor(Constant.resources.getColor(R.color.black));
        tv_list.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        if (tv==tv_map){
            tv_map.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_map.setBackgroundColor(Constant.resources.getColor(R.color.white));
            lv.setVisibility(View.GONE);
            Intent it = new Intent(Constant.SCROLL_LV);
            it.putExtra("action",Constant.ACTION_DOWN);
            ct.sendBroadcast(it);
        }else{
            tv_list.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_list.setBackgroundColor(Constant.resources.getColor(R.color.white));
            lv.setVisibility(View.VISIBLE);
        }
    }


}
