package app.pharma.com.pharma.activity.Care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.Like_Adapter;
import app.pharma.com.pharma.Adapter.Pharma_Care_Adapter;
import app.pharma.com.pharma.Adapter.Pill_Order_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pharma_Care_Consturct;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Detail;

public class Care_Pharma extends AppCompatActivity {
    int Mainpage = 1;
    DatabaseHandle db;
    User user;
    TextView tvNull;
    RecyclerView lv;
    TextView tvTitle;
    RelativeLayout imgBack;
    SwipeRefreshLayout swip;
    Pharma_Care_Adapter adapter;
    ArrayList<Pharma_Care_Consturct> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care__pharma);
        Common.context = this;
        db = new DatabaseHandle();
        init();

    }

    private void init() {
        tvTitle = (TextView)findViewById(R.id.title);
        imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.list_pharma_care));
        swip = findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage =1;
                getPage(Mainpage);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvNull = findViewById(R.id.txt_null);
        arr = new ArrayList<>();
        setRecycle();



    }

    public void setRecycle(){
        lv = findViewById(R.id.lv_pharma);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lv.setLayoutManager(layoutManager);
        adapter = new Pharma_Care_Adapter(getApplicationContext(), arr);
        lv.setAdapter(adapter);

        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Mainpage++;
                getPage(Mainpage);
            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getApplicationContext(),Detail.class);
                        it.putExtra("key","pharma");
                        it.putExtra("id",arr.get(i).getId());
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }

    private void getPage(int i) {
        if(Utils.isLogin()){
            final boolean[] isEmpty = {false};
            if(!Utils.isNetworkEnable(this)){
                Utils.dialogNotif(getResources().getString(R.string.no_internet));
            }else{
                if(i==1){
                    arr.clear();
                }
                user = db.getAllUserInfor();
                Map<String,String> map = new HashMap<>();
                map.put("type","store");
                map.put("accessToken",user.getToken());
                map.put("page",i+"");
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LIST_PHARMA_CARE",response);
                        try {
                            swip.setRefreshing(false);
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString(JsonConstant.CODE);

                            switch (code){
                                case "0":
                                    JSONArray data = jo.getJSONArray(JsonConstant.LIST_FAVOR_STORE);
                                    if(data.length()>0){
                                        for (int i =0; i<data.length();i++){
                                            JSONObject idx = data.getJSONObject(i);
                                            JSONObject order = idx.getJSONObject(JsonConstant.STORE);
                                            JSONArray images = order.getJSONArray(JsonConstant.IMAGE);
                                            JSONObject location = order.getJSONObject(JsonConstant.MAP_LOCATION);
                                            Pharma_Care_Consturct list = new Pharma_Care_Consturct();
                                            list.setId(order.getString(JsonConstant.ID));
                                            list.setName(order.getString(JsonConstant.NAME));
                                            list.setAdr(order.getString(JsonConstant.USER_ADR));
                                            list.setImage(images.getString(0));
                                            if(order.has(JsonConstant.STAR)){
                                                list.setStar(order.getDouble(JsonConstant.STAR));
                                            }else{
                                                list.setStar((double)0);
                                            }

                                            list.setLat(location.getDouble(JsonConstant.LAT));
                                            list.setLng(location.getDouble(JsonConstant.LONG));
                                            arr.add(list);
                                        }
                                        isEmpty[0] = false;
                                    }else{
                                        isEmpty[0] = true;

                                    }
                                    break;
                                    default:
                                        break;
                            }

                        } catch (JSONException e) {
                            swip.setRefreshing(false);
                            isEmpty[0] = true;
                            e.printStackTrace();
                        }
                        if(isEmpty[0]&&Mainpage>1){
                            Mainpage = Mainpage-1;
                        }
                        if(arr.size()>0){
                            setIsEmpty(false);
                        }else{
                            setIsEmpty(true);
                        }
                        adapter.notifyDataSetChanged();

                    }
                };
                Utils.PostServer(this, ServerPath.LIST_FAVOR,map,response);
            }


        }


    }


    public void setIsEmpty(boolean empty){
        if(empty){
            lv.setVisibility(View.GONE);
            tvNull.setVisibility(View.VISIBLE);
        }else{
            lv.setVisibility(View.VISIBLE);
            tvNull.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        Common.context = this;
        Mainpage = 1;
        getPage(Mainpage);
        super.onResume();
    }
}
