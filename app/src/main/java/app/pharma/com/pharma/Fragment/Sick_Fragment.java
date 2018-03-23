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

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Sick_Adapter;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sick_Fragment extends Fragment {
    ListView lv;
    List_Sick_Adapter adapter;
    ArrayList<Sick_Construct> arr;
    Context ct;
    int lastVisibleItem = 0;
    private int lastY = 0;
    View v;
    public Sick_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_sick_, container, false);
        initView();

        return v;
    }

    private void initView() {

        ct = getContext();
        lv = (ListView)v.findViewById(R.id.lv_sick);
        arr = new ArrayList<>();
        adapter = new List_Sick_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getActivity(), Detail.class);
                startActivity(it);
            }
        });
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
    }

}
