package app.pharma.com.pharma.Fragment.Dr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Dr_Adapter;
import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Dr;
import io.realm.RealmList;


public class Dr_Fragment extends Fragment {
    ListView lv;
    List_Dr_Adapter adapter;
    ArrayList<Dr_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context ct;
    int lastVisibleItem = 0;
    Spinner spiner;
    int page = 1;
    BroadcastReceiver broadcastSearch;
    TextView tv_null;
    SwipeRefreshLayout swip;
    String idDr;
    DatabaseHandle db;
    private int lastY = 0;
    public Dr_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dr_, container, false);
        Constant.inFragment = "dr";
        lv = (ListView)v.findViewById(R.id.lv_dr);
        arr = new ArrayList<>();
        adapter = new List_Dr_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        db = new DatabaseHandle();
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(1);
            }
        });
        ct = getContext();
        arrCata = new ArrayList<>();
        tv_null = v.findViewById(R.id.txt_null);
        adapter.notifyDataSetChanged();

        spiner = (Spinner) v.findViewById(R.id.spin_dr);
        List<String> categories = new ArrayList<String>();
        if(!db.isCataloDrEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_DR);
            RealmList<CataloModel> list = cata.getListCatalo();

            for (int i =0; i <list.size();i++){
                CataloModel model = new CataloModel();
                model.setName(list.get(i).getName());
                model.setId(list.get(i).getId());
                arrCata.add(model);
                categories.add(list.get(i).getName());
            }

        }

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);

        // attaching data adapter to spinner
        spiner.setAdapter(dataAdapter);
        spiner.setSelection(0);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = categories.get(i);
                for (int j =0;j<arrCata.size();j++){
                    if(arrCata.get(j).getName().equals(text)){

                        idDr = arrCata.get(j).getId();
                         loadPage(1);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.setAlphalAnimation(view);
                Intent it = new Intent(getContext(), Infor_Dr.class);
                it.putExtra("id",arr.get(i).getId());
                getContext().startActivity(it);
            }
        });
        return v;
    }

    public void isEmpty(boolean empty){
        if(empty){
            tv_null.setVisibility(View.VISIBLE);
        }else{
            tv_null.setVisibility(View.GONE);
        }
    }



    private void loadPage(int page) {

        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            isEmpty(false);
            if(page==1){
                arr.clear();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idDr);
            getData(map);

        }

    }

    private void getData(Map<String, String> map) {
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swip.setRefreshing(false);
                Log.d("RESPONSE_DR",response);
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.has(JsonConstant.CODE)){
                        String code = jobj.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                JSONArray ja = jobj.getJSONArray(JsonConstant.LIST_DR);

                                for (int i = 0; i<ja.length();i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    JSONObject pharma = jo.getJSONObject(JsonConstant.PHARMACIS);
                                    Dr_Constructor dr = new Dr_Constructor();
                                    dr.setName(pharma.getString(JsonConstant.NAME));
                                    dr.setAvatar(pharma.getString(JsonConstant.AVATAR));
//                                    dr.setRate(pharma.getDouble(JsonConstant.STAR));
                                    dr.setId(pharma.getString(JsonConstant.ID));
                                    dr.setHospital(pharma.getString(JsonConstant.HOSPITAL));
                                    dr.setWork_year(pharma.getString(JsonConstant.WORK_YEAR));
                                    dr.setAge(pharma.getString(JsonConstant.AGE));
                                    arr.add(dr);

                                }
                                if(arr.size()>0){
                                    isEmpty(false);
                                }else{
                                    isEmpty(true);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case "1":
                                Utils.dialogNotif(getActivity().getResources().getString(R.string.error));
                                break;
                        }
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
                if(arr.size()>0){
                    isEmpty(false);
                }else{
                    isEmpty(true);
                }
            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_DR,map,response);
    }


    @Override
    public void onResume() {
        if(broadcastSearch==null){
            registerBroadcast();
        }
        super.onResume();

    }
    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        broadcastSearch=null;
    }
    private void registerBroadcast() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
                    String key = intent.getStringExtra("key");
                    loadPageSearch(1,key);
                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastSearch,
                it);
    }

    private void loadPageSearch(int i, String key) {
        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            isEmpty(false);
            if(page==1){
                arr.clear();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idDr);
            map.put("key",key);
            getData(map);

        }
    }

    @Override
    public void onStop() {
        unRegister();
        super.onStop();
    }


}
