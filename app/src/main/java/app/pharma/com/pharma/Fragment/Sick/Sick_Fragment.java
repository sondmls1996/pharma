package app.pharma.com.pharma.Fragment.Sick;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import app.pharma.com.pharma.Adapter.List_Sick_Adapter;
import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sick_Fragment extends Fragment {
    ListView lv;
    List_Sick_Adapter adapter;
    ArrayList<Sick_Construct> arr;
    ArrayList<CataloModel> arrcata;
    SwipeRefreshLayout swip;
    TextView tvNull;
    Context ct;
    DatabaseHandle db;
    String idSick;
    BroadcastReceiver broadcastSearch;
    int lastVisibleItem = 0;
    private int lastY = 0;
    View v;
    Spinner spiner;
    public Sick_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_sick_, container, false);
         Constant.inFragment = "sick";
         registerBroadcast();
        arrcata = new ArrayList<>();
        initView();

        return v;
    }

    private void initView() {
        db = new DatabaseHandle();
        ct = getContext();
        lv = (ListView)v.findViewById(R.id.lv_sick);
        arr = new ArrayList<>();
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(1);
            }
        });
        adapter = new List_Sick_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        tvNull = v.findViewById(R.id.txt_null);
        spiner = (Spinner) v.findViewById(R.id.spin_sick);
        List<String> categories = new ArrayList<String>();
        if(!db.isCataloSickEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_SICK);
            
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                CataloModel model = new CataloModel();
                model.setName(list.get(i).getName());
                model.setId(list.get(i).getId());
                arrcata.add(model);
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
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = categories.get(i);
                
                
                for (int j =0;j<arrcata.size();j++){
                    if(arrcata.get(j).getName().equals(text)){

                        idSick = arrcata.get(j).getId();
                        loadPage(1);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.setAlphalAnimation(view);
                Intent it = new Intent(getActivity(), Detail.class);
                it.putExtra("key","sick");
                it.putExtra("id", arr.get(i).getId());
                startActivity(it);
            }
        });

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

    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        broadcastSearch=null;
    }

    private void loadPage(int page) {

        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            if(page==1){
                arr.clear();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idSick);
            getData(map);
        }

    }

    private void loadPageSearch(int page,String key) {

        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            if(page==1){
                arr.clear();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idSick);
            map.put("key",key);
            getData(map);
        }

    }

    private void getData(Map<String, String> map) {
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swip.setRefreshing(false);
                Log.d("RESPONSE_SICK",response);
//

//                JSONArray ja = null;
                try {
                    JSONObject jobj = new JSONObject(response);
                    if(jobj.has(JsonConstant.CODE)){
                        String code = jobj.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                new AsyncTask<Void,Void,Void>(){

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        JSONArray listDis = null;

                                        try {

                                            listDis = jobj.getJSONArray(JsonConstant.LIST_DISE);
                                            for (int i =0;i<listDis.length();i++){

                                                JSONObject jo = listDis.getJSONObject(i);
                                                JSONObject product = jo.getJSONObject(JsonConstant.DISEASE);
                                                Sick_Construct sick = new Sick_Construct();
                                                sick.setName(product.getString(JsonConstant.NAME));
                                                sick.setDescri(product.getString(JsonConstant.DESCRI));
                                                sick.setId(product.getString(JsonConstant.ID));
                                                JSONArray images = product.getJSONArray(JsonConstant.IMAGE);
                                                sick.setImage(images.getString(0));
                                                JSONObject catalo = product.getJSONObject(JsonConstant.CATEGORY_LOW);
                                                sick.setCatalo(catalo.getString(JsonConstant.CATEGORY_LOW));
                                                //   sick.setCmt(product.getInt(JsonConstant.COMMENT));
                                                sick.setLike(product.getInt(JsonConstant.LIKE));
                                                sick.setDate(product.getLong(JsonConstant.TIME));
                                                arr.add(sick);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        if(arr.size()>0){
                                            tvNull.setVisibility(View.GONE);
                                        }else{
                                            tvNull.setVisibility(View.VISIBLE);
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



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_SICK,map,response);

    }

    @Override
    public void onResume() {
        if(broadcastSearch==null){
            registerBroadcast();
        }

//        Intent it = new Intent(Constant.SCROLL_LV);
//        it.putExtra("action",Constant.ACTION_UP);
//        ct.sendBroadcast(it);
        super.onResume();

    }

    @Override
    public void onStop() {
        unRegister();

        super.onStop();
    }
}
