package app.pharma.com.pharma.Fragment.Pharma;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Detail;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Insite_List extends Fragment {
    RecyclerView lv;
    List_Pharma_Adapter adapter;
    int lastVisibleItem = 0;
    LinearLayout ln_null;
    TextView not_near, showall;
    SwipeRefreshLayout swip;
    boolean isNomar = true,isSearch = false;
    BroadcastReceiver broadcastCloseSearch;
    String key = "";
    private int lastY = 0;
    boolean isLoading = false;
    String type = "";
    BroadcastReceiver broadcastSearch;
    TextView tvnull;
    int Mainpage = 1;
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
        if(Utils.isKeyboardShow(v,getActivity())){
            Utils.hideKeyboard(getActivity());
        }
        Constant.inFragment = "pharma";
        registerBroadcast();
        init();
        return v;
    }
    public void setRecycle(View v){
        lv = v.findViewById(R.id.lv_pharma);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(layoutManager);
        adapter = new List_Pharma_Adapter(getActivity(), arr);
        lv.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(arr.size()>=15){
                    Mainpage++;

                    loadManager(Mainpage,isNomar,isSearch,key,type);
                }

            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getActivity(), Detail.class);
                        it.putExtra("key","pharma");
                        it.putExtra("id",arr.get(i).getId());
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }
    private void registerBroadcastCloseSearch() {
        broadcastCloseSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.CLOSE_SEARCH_ACTION)){
                    Utils.hideKeyboard(getActivity());
                    key = "";
//                    Mainpage = 1;
                    isSearch = false;
                    isNomar = true;
                  //  isFillter = false;
                    //  isNomar = true;
                    //  loadManager(Mainpage,isFillter,isNomar,isSearch,key);
                    // loadPageSearch(Mainpage,idPill,key);
                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.CLOSE_SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastCloseSearch,
                it);

    }


    private void init() {

        arr = new ArrayList<>();
        setRecycle(v);
        tvnull = v.findViewById(R.id.txt_null);
        ln_null = v.findViewById(R.id.ln_null);
        showall = v.findViewById(R.id.show_all);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_null.setVisibility(View.GONE);
                type = "show_all";
                key = "";

                isNomar = true;
                Mainpage = 1;
                isSearch = false;
                loadManager(Mainpage,isNomar,isSearch,key,type);
               // getData(1,type);
            }
        });
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage = 1;

                if(!key.equals("")){
                    isNomar = false;
                    isSearch = true;

                }else{
                    if(isSearch){
                        isNomar = false;
                        isSearch = true;
                        type = "show_all";
                    }else{

                        isNomar = true;
                        isSearch = false;
                        type = "";
                    }

                }
                loadManager(Mainpage,isNomar,isSearch,key,type);

            }
        });


        loadManager(Mainpage,isNomar,isSearch,key,type);
    }

    public void loadManager(int page, boolean isNomar, boolean isSearch,String key,String type){
    if(!isLoading){
        if (isNomar) {
            getData(page,type);
        }
        if(isSearch){
            loadPageSearch(page,key);
        }
    }

    }

    private void loadPageSearch(int page, String key){
        if(page==1){
            arr.clear();
            adapter.notifyDataSetChanged();
        }
        isLoading = true;
        Map<String, String> map = new HashMap<>();
        map.put("latGPS", Common.lat+"");
        map.put("longGPS",Common.lng+"");
        map.put("page",page+"");
        map.put("key",key);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SEARCH_RESPONSE",response);
                swip.setRefreshing(false);
                tvnull.setVisibility(View.GONE);
                ln_null.setVisibility(View.GONE);
                initJson(response,"show_all");
            }
        };
        Utils.PostServer(Common.context, ServerPath.LIST_PHARMA,map,response);
    }

    private void getData(int page, String type) {

        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
            isLoading = false;
        }else{
            if(page==1){
                arr.clear();
            }

            Log.d("LAT_LNG",Common.lat+"\n"+Common.lng);
            if(type.equals("show_all")){
                if(!isLoading){
                    isLoading = true;
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
                }

            }else{
                    if(!isLoading){
                        isLoading = true;
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
                    }

                }else{
                    Mainpage =1;
                    getData(Mainpage,"show_all");
                }
            }
        }


    }

    private void initJson(String response,String type) {
        Log.d("RESPONSE_INSITE_PHARMA",response);
        try {

            JSONObject jo = new JSONObject(response);

            if(jo.has(JsonConstant.CODE)){
                String code = jo.getString(JsonConstant.CODE);
                switch (code){
                    case "0":
                        getResponseData(jo,type);
                        isLoading = false;
                        break;
                    case "3":
                        getResponseData(jo,type);
                        isLoading = false;
                        break;
                    default:
                        getResponseData(jo,type);
                        isLoading = false;
                        break;
                }

            }



        }catch (Exception e){
            isLoading = false;
            e.printStackTrace();
        }
    }

    private void getResponseData(JSONObject jo,String type) {
        boolean isEmpty[] = {false};
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    if(type.equals("show_all")){
                        JSONArray array = jo.getJSONArray(JsonConstant.LIST_STORE);
                        if(array.length()>0){
                            for (int i =0; i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                JSONObject store = obj.getJSONObject(JsonConstant.STORE);
                                JSONArray images = store.getJSONArray(JsonConstant.IMAGE);
                                Pharma_Constructor pharma = new Pharma_Constructor();
                                pharma.setName(store.getString(JsonConstant.NAME));
                                pharma.setAdr(store.getString(JsonConstant.USER_ADR));
                                pharma.setComment(store.getString(JsonConstant.COMMENT));
                                pharma.setAvatar(images.getString(0));
                                pharma.setId(store.getString(JsonConstant.ID));
                                pharma.setLike(store.getString(JsonConstant.LIKE));
                                pharma.setRate(store.getDouble(JsonConstant.STAR));
                                JSONObject location = store.getJSONObject(JsonConstant.MAP_LOCATION);
                                pharma.setX(location.getDouble(JsonConstant.LAT));
                                pharma.setY(location.getDouble(JsonConstant.LONG));
                                arr.add(pharma);
                            }
                            isEmpty[0] = false;
                        }else {
                            isEmpty[0] = true;
                            return null;
                        }
                    }else{
                        JSONArray array = jo.getJSONArray(JsonConstant.AROUD_STORE);
                        if(array.length()>0){
                            for (int i =0; i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                JSONObject store = obj.getJSONObject(JsonConstant.STORE);
                                JSONArray images = store.getJSONArray(JsonConstant.IMAGE);
                                Pharma_Constructor pharma = new Pharma_Constructor();
                                pharma.setName(store.getString(JsonConstant.NAME));
                                pharma.setAdr(store.getString(JsonConstant.USER_ADR));
                                pharma.setComment(store.getString(JsonConstant.COMMENT));
                                pharma.setAvatar(images.getString(0));
                                pharma.setId(store.getString(JsonConstant.ID));
                                pharma.setLike(store.getString(JsonConstant.LIKE));
                                pharma.setRate(store.getDouble(JsonConstant.STAR));
                                JSONObject location = store.getJSONObject(JsonConstant.MAP_LOCATION);
                                pharma.setX(location.getDouble(JsonConstant.LAT));
                                pharma.setY(location.getDouble(JsonConstant.LONG));
                                arr.add(pharma);
                            }
                            isEmpty[0] = false;
                        }else {
                            isEmpty[0] = true;
                            return null;
                        }
                    }


                }catch (Exception e){
                    Utils.dialogNotif(getActivity().getResources().getString(R.string.server_err));
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Pharma_Fragment.arrPharma = arr;
                if(isEmpty[0]&&Mainpage>1){
                    Mainpage = Mainpage -1;
                }
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

    @Override
    public void onStop() {
        unRegister();
        super.onStop();
    }

    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastCloseSearch);
        broadcastSearch=null;
        broadcastCloseSearch=null;
    }
    private void registerBroadcast() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
                    Mainpage = 1;
                    isNomar = false;
                    isSearch = true;
                     key = intent.getStringExtra("key");
                     loadManager(Mainpage,isNomar,isSearch,key,type);

                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastSearch,
                it);
    }
}
