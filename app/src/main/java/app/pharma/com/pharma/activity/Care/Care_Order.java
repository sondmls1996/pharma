package app.pharma.com.pharma.activity.Care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.Pill_Order_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Order_Detail;

public class Care_Order extends AppCompatActivity {
        int Mainpage = 1;
        DatabaseHandle db;
        User user;
        boolean isLoading = false;
        RecyclerView lv;
        TextView tvTitle;
        RelativeLayout imgBack;
        SwipeRefreshLayout sw;
        TextView tvNull;
        Pill_Order_Adapter adapter;
        ArrayList<Order_List_Constructor> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care__order);
        Common.context = this;
        db = new DatabaseHandle();
        init();


    }

    private void init() {
        tvTitle = (TextView)findViewById(R.id.title);
        imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.list_order));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvNull = findViewById(R.id.txt_null);
        arr = new ArrayList<>();
        sw = findViewById(R.id.swip);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage =1;
                getPage(Mainpage);
            }
        });
        setRecycle();


    }
    public void setRecycle(){
        lv = findViewById(R.id.lv_order);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lv.setLayoutManager(layoutManager);
        adapter = new Pill_Order_Adapter(getApplicationContext(), arr);
        lv.setAdapter(adapter);

        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(!isLoading){
                    Mainpage++;
                    getPage(Mainpage);
                }

            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getApplicationContext(), Order_Detail.class);
                        it.putExtra("id",arr.get(i).getId());
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }
    private void getPage(int i) {
        if(!isLoading){
            if(Utils.isLogin()){
                if(!Utils.isNetworkEnable(this)){
                    Utils.dialogNotif(getResources().getString(R.string.no_internet));
                    isLoading = false;
                }else{
                    if(i==1){
                        arr.clear();
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = true;
                    boolean isEmpty[] = {false};
                    user = db.getAllUserInfor();
                    Map<String,String> map = new HashMap<>();
                    map.put("page",i+"");
                    map.put("accessToken",user.getToken());
                    Response.Listener<String> response = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                sw.setRefreshing(false);
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString(JsonConstant.CODE);
                                JSONArray data = jo.getJSONArray(JsonConstant.DATA);
                                if(data.length()>0){
                                    for (int i =0; i<data.length();i++){
                                        JSONObject idx = data.getJSONObject(i);
                                        JSONObject order = idx.getJSONObject(JsonConstant.ORDER);
                                        JSONObject product = order.getJSONObject(JsonConstant.PRODUCT_ORDER);
                                        Order_List_Constructor list = new Order_List_Constructor();
                                        list.setId(order.getString(JsonConstant.ID));
                                        list.setName(product.getString(JsonConstant.NAME));
                                        list.setPrice(order.getInt(JsonConstant.TOTAL_MONEY));
                                        list.setImage(product.getString(JsonConstant.IMAGE));
                                        list.setDate(order.getLong(JsonConstant.TIME));
                                        list.setStatus(order.getString(JsonConstant.STATUS));
                                        arr.add(list);
                                    }
                                    isEmpty[0]= false;
                                }else{
                                    isEmpty[0]= true;
                                }

                                if(isEmpty[0]&&Mainpage>1){
                                    Mainpage = Mainpage-1;
                                }
                                if(arr.size()>0){
                                    setIsEmpty(false);
                                }else{
                                    setIsEmpty(true);
                                }
                                isLoading = false;
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                sw.setRefreshing(false);
                                adapter.notifyDataSetChanged();
                                isLoading = false;
                                e.printStackTrace();
                            }

                            Log.d("RESPONSE_LIST_ORDER",response);
                        }
                    };
                    Utils.PostServer(this, ServerPath.LIST_ORDER,map,response);
                }


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
        getPage(1);
        super.onResume();
    }
}
