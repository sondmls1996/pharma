package app.pharma.com.pharma.Fragment.Pharma;


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

import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Pharma_Constructor;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Insite_List extends Fragment {
    ListView lv;
    List_Pharma_Adapter adapter;
    int lastVisibleItem = 0;
    private int lastY = 0;
    ArrayList<Pharma_Constructor> arr;
    View v;
    public Insite_List() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_insite__list, container, false);
        init();
        return v;
    }

    private void init() {
        lv = (ListView)v.findViewById(R.id.lv_pharma);
        arr = new ArrayList<>();
        adapter = new List_Pharma_Adapter(getContext(),0,arr);

          lv.setAdapter(adapter);
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        arr.add(new Pharma_Constructor());
        adapter.notifyDataSetChanged();
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
                    Common.context.sendBroadcast(it);
                }else if(firstVisibleItem < lastVisibleItem){
                    Intent it = new Intent(Constant.SCROLL_LV);
                    it.putExtra("action",Constant.ACTION_UP);
                    Common.context.sendBroadcast(it);
                }else{
                    if(top < lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_DOWN);
                        Common.context.sendBroadcast(it);
                    }else if(top > lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_UP);
                        Common.context.sendBroadcast(it);
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
                it.putExtra("key","pharma");

                startActivity(it);
            }
        });
    }

}
