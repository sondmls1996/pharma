package app.pharma.com.pharma.Fragment;


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

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Meo_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Meo_Constructor;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Meo;

/**
 * A simple {@link Fragment} subclass.
 */
public class Meo_Fragment extends Fragment implements View.OnClickListener {
    ListView lv;
    List_Meo_Adapter adapter;
    ArrayList<Meo_Constructor> arr;
    int lastVisibleItem = 0;
    private int lastY = 0;
    TextView tv_focus;
    TextView tv_hearth;
    TextView tv_meo;
    View v;
    public Meo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v = inflater.inflate(R.layout.fragment_meo, container, false);
        initView();

        return v;
    }

    private void initView() {
        tv_focus = v.findViewById(R.id.tv_focus);
        tv_hearth = v.findViewById(R.id.tv_hearth);
        tv_meo = v.findViewById(R.id.tv_meo);

        tv_focus.setOnClickListener(this);
        tv_hearth.setOnClickListener(this);
        tv_meo.setOnClickListener(this);

        lv = (ListView)v.findViewById(R.id.lv_like);
        arr = new ArrayList<>();
        adapter = new List_Meo_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
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
                Intent it = new Intent(Common.context, Infor_Meo.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_focus:
                changeColor(tv_focus);
                break;
            case R.id.tv_hearth:
                changeColor(tv_hearth);
                break;
            case R.id.tv_meo:
                changeColor(tv_meo);
                break;
        }
    }

    private void changeColor(TextView tv) {
        tv_focus.setTextColor(Constant.resources.getColor(R.color.gray));
        tv_hearth.setTextColor(Constant.resources.getColor(R.color.gray));
        tv_meo.setTextColor(Constant.resources.getColor(R.color.gray));

        tv_focus.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        tv_hearth.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        tv_meo.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));

        if (tv==tv_focus){
            tv_focus.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_focus.setBackgroundColor(Constant.resources.getColor(R.color.white));
         //   lv.setVisibility(View.GONE);
        }else if(tv==tv_hearth){
            tv_hearth.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_hearth.setBackgroundColor(Constant.resources.getColor(R.color.white));
        //    lv.setVisibility(View.VISIBLE);
        }else{
            tv_meo.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_meo.setBackgroundColor(Constant.resources.getColor(R.color.white));
        }
    }
}
