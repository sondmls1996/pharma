package app.pharma.com.pharma.Fragment.Pharma;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Insite_List extends Fragment {
    ListView lv;
    List_Pharma_Adapter adapter;
    int lastVisibleItem = 0;
    LinearLayout ln_null;
    TextView not_near, showall;
    SwipeRefreshLayout swip;
    private int lastY = 0;
    TextView tvnull;
    int page = 1;
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
        tvnull = v.findViewById(R.id.txt_null);
        ln_null = v.findViewById(R.id.ln_null);
        showall = v.findViewById(R.id.show_all);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_null.setVisibility(View.GONE);
                getData(1,"show_all");
            }
        });
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1,"");
            }
        });
        adapter = new List_Pharma_Adapter(getContext(),0,arr);

          lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.setAlphalAnimation(view);
                Intent it = new Intent(getActivity(), Detail.class);
                it.putExtra("key","pharma");
                it.putExtra("id",arr.get(i).getId());
                startActivity(it);
            }
        });
        getData(page,"");
    }

    private void getData(int page, String type) {
        if(page==1){
            arr.clear();
        }

        if(type.equals("show_all")){
            Map<String, String> map = new HashMap<>();
            map.put("latGPS", Common.lat+"");
            map.put("longGPS",Common.lng+"");
            map.put("page",page+"");
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    swip.setRefreshing(false);
                    tvnull.setVisibility(View.GONE);
                    ln_null.setVisibility(View.GONE);
                    initJson(response,type);
                }
            };
            Utils.PostServer(getActivity(), ServerPath.LIST_PHARMA,map,response);
        }else{
            if(Common.lat!=0&&Common.lng!=0){
                Map<String, String> map = new HashMap<>();
                map.put("latGPS", Common.lat+"");
                map.put("longGPS",Common.lng+"");
                map.put("page",page+"");
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swip.setRefreshing(false);
                        tvnull.setVisibility(View.GONE);
                        ln_null.setVisibility(View.GONE);
                        initJson(response,type);
                    }
                };
                Utils.PostServer(getActivity(), ServerPath.LIST_PHARMA,map,response);
            }else{
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swip.setRefreshing(false);
                        tvnull.setVisibility(View.GONE);
                        ln_null.setVisibility(View.GONE);
                        initJson(response,"show_all");
                    }
                };
                Utils.GetServer(getActivity(),ServerPath.LIST_PHARMA,response);
            }
        }





    }

    private void initJson(String response,String type) {
        Log.d("RESPONSE__PHARMA",response);
        try {

            JSONObject jo = new JSONObject(response);

            if(jo.has(JsonConstant.CODE)){
                String code = jo.getString(JsonConstant.CODE);
                switch (code){
                    case "0":
                        new AsyncTask<Void,Void,Void>(){

                            @Override
                            protected Void doInBackground(Void... voids) {
                                try{
                                    JSONArray array = jo.getJSONArray(JsonConstant.LIST_STORE);
                                    if(array.length()>0){
                                        for (int i =0; i<array.length();i++){
                                            JSONObject obj = array.getJSONObject(i);
                                            JSONObject store = obj.getJSONObject(JsonConstant.STORE);
                                            JSONArray images = store.getJSONArray(JsonConstant.IMAGE);
                                            Pharma_Constructor pharma = new Pharma_Constructor();
                                            pharma.setName(store.getString(JsonConstant.NAME));
                                            pharma.setAdr(store.getString(JsonConstant.USER_ADR));
                                        //    pharma.setComment(store.getString(JsonConstant.COMMENT));
                                            pharma.setAvatar(images.getString(0));
                                            pharma.setId(store.getString(JsonConstant.ID));
                                       //     pharma.setLike(store.getString(JsonConstant.LIKE));
                                        //    pharma.setRate(store.getDouble(JsonConstant.STAR));
                                            JSONObject location = store.getJSONObject(JsonConstant.MAP_LOCATION);
                                            pharma.setX(location.getDouble(JsonConstant.LAT));
                                            pharma.setY(location.getDouble(JsonConstant.LONG));
                                            arr.add(pharma);
                                        }
                                    }else {
                                        return null;
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if(type.equals("show_all")){
                                    if(arr.size()>0){
                                        tvnull.setVisibility(View.GONE);
                                    }else{
                                        tvnull.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    if(arr.size()>0){
                                        ln_null.setVisibility(View.GONE);
                                    }else{
                                        ln_null.setVisibility(View.VISIBLE);
                                    }
                                }

                                adapter.notifyDataSetChanged();
                                super.onPostExecute(aVoid);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                        break;
                    case "1":
                        break;
                }

            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
