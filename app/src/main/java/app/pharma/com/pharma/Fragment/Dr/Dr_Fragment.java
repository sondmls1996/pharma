package app.pharma.com.pharma.Fragment.Dr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Adapter.List_Dr_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Dr_Constructor;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Dr;


public class Dr_Fragment extends Fragment {
    ListView lv;
    List_Dr_Adapter adapter;
    ArrayList<Dr_Constructor> arr;
    Context ct;
    int lastVisibleItem = 0;
    Spinner spiner;
    private int lastY = 0;
    public Dr_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dr_, container, false);
        lv = (ListView)v.findViewById(R.id.lv_dr);
        arr = new ArrayList<>();
        adapter = new List_Dr_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        ct = getContext();
        arr.add(new Dr_Constructor());
        arr.add(new Dr_Constructor());
        arr.add(new Dr_Constructor());
        arr.add(new Dr_Constructor());
        arr.add(new Dr_Constructor());
        adapter.notifyDataSetChanged();

        spiner = (Spinner) v.findViewById(R.id.spin_dr);
        List<String> categories = new ArrayList<String>();
        categories.add("Bác sỹ tâm thần");
        categories.add("Bác sỹ răng miệng");
        categories.add("Bác sỹ tâm lý");


        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);

        // attaching data adapter to spinner
        spiner.setAdapter(dataAdapter);

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
                Intent it = new Intent(getContext(), Infor_Dr.class);
                it.putExtra("key","pharma");
                getContext().startActivity(it);
            }
        });
        return v;
    }

}
