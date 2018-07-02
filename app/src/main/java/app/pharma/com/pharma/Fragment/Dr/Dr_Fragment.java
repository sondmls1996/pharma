package app.pharma.com.pharma.Fragment.Dr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Infor_Dr;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;


public class Dr_Fragment extends Fragment {
    RecyclerView lv;
    List_Dr_Adapter adapter;
    ArrayList<Dr_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context ct;
    int lastVisibleItem = 0;
    Spinner spiner;
    int Mainpage = 1;
    BroadcastReceiver broadcastSearch, broadcastCloseSearch;
    TextView tv_null;
    SwipeRefreshLayout swip;
    String idDr = "";
    boolean isSearch,isNomar;
    DatabaseHandle db;
    private int lastY = 0;
    String key = "";
    public Dr_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dr_, container, false);
        Constant.inFragment = "dr";

        arr = new ArrayList<>();


        setRecycle(v);
        db = new DatabaseHandle();
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage = 1;
                isSearch = false;
                isNomar = true;
                key = "";
                loadManager(Mainpage,isNomar,isSearch,key);

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
                        if(!idDr.equals(arrCata.get(j).getId())){
                            idDr = arrCata.get(j).getId();
                            Mainpage = 1;
                            key="";
                            isSearch = false;
                            isNomar = true;
                            loadManager(Mainpage,isNomar,isSearch,key);
                        }

                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return v;
    }

    public void setRecycle(View v){
        lv = v.findViewById(R.id.lv_dr);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(layoutManager);
        adapter = new List_Dr_Adapter(getActivity(), arr);
        lv.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Mainpage++;

                loadManager(Mainpage,isNomar,isSearch,key);
            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getContext(), Infor_Dr.class);
                        it.putExtra("id",arr.get(i).getId());
                        getContext().startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }

    public void loadManager(int page, boolean isNomar, boolean isSearch,String key){
        if(isNomar){
            loadPage(page);
        }
        if(isSearch){
            loadPageSearch(page,key);
        }
    }

    public void isEmpty(boolean empty){
        if(empty){
            lv.setVisibility(View.GONE);
            tv_null.setVisibility(View.VISIBLE);
        }else{
            lv.setVisibility(View.VISIBLE);
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
        final boolean[] isEmptyList = {false};
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
                                new AsyncTask<Void,Void,Void>(){

                                    @Override
                                    protected Void doInBackground(Void... voids) {

                                        JSONArray ja = null;
                                        try {
                                            ja = jobj.getJSONArray(JsonConstant.LIST_DR);
                                            if(ja.length()>0){
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
                                                isEmptyList[0] = false;
                                            }else{
                                                isEmptyList[0] = true;
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        if(isEmptyList[0]&&Mainpage>1){
                                            Mainpage = Mainpage -1;
                                        }

                                        if(arr.size()>0){
                                            isEmpty(false);
                                        }else{
                                            isEmpty(true);
                                        }
                                        adapter.notifyDataSetChanged();
                                        super.onPostExecute(aVoid);
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                break;
                            case "1":
                                Utils.dialogNotif(getActivity().getResources().getString(R.string.error));
                                break;
                        }
                    }


                } catch (JSONException e) {
                    Utils.dialogNotif(getActivity().getResources().getString(R.string.server_err));
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
        if(broadcastCloseSearch==null){
            registerBroadcastCloseSearch();
        }
        super.onResume();

    }

    private void registerBroadcastCloseSearch() {
        broadcastCloseSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.CLOSE_SEARCH_ACTION)){
                    Mainpage = 1;
                    isSearch = false;
                    key = "";
                    isNomar = true;
                    loadManager(Mainpage,isNomar,isSearch,key);
                //    loadPage(Mainpage);
                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.CLOSE_SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastCloseSearch,
                it);
    }

    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastCloseSearch);
        broadcastCloseSearch=null;
        broadcastSearch=null;
    }
    private void registerBroadcast() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
                     key = intent.getStringExtra("key");
                    Mainpage = 1;
                    isSearch = true;
                    isNomar = false;
                    loadManager(Mainpage,isNomar,isSearch,key);
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
            if(i==1){
                arr.clear();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",i+"");
        //    map.put("type",idDr);
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
