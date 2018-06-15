package app.pharma.com.pharma.activity.Care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import app.pharma.com.pharma.activity.Detail.Detail;

public class Care_Pharma extends AppCompatActivity {
    int page = 1;
    DatabaseHandle db;
    User user;
    ListView lv;
    TextView tvTitle;
    RelativeLayout imgBack;
    Pharma_Care_Adapter adapter;
    ArrayList<Pharma_Care_Consturct> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care__pharma);
        Common.context = this;
        db = new DatabaseHandle();
        init();
        getPage(1);
    }

    private void init() {
        tvTitle = (TextView)findViewById(R.id.title);
        imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.list_pharma_care));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lv = findViewById(R.id.lv_pharma);
        arr = new ArrayList<>();
        adapter = new Pharma_Care_Adapter(getApplicationContext(),R.layout.item_order_pill,arr);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.setAlphalAnimation(view);
                Intent it = new Intent(getApplicationContext(),Detail.class);
                it.putExtra("key","pharma");
                it.putExtra("id",arr.get(i).getId());
                startActivity(it);
            }
        });


    }
    private void getPage(int i) {
        if(Utils.isLogin()){
            if(!Utils.isNetworkEnable(this)){
                Utils.dialogNotif(getResources().getString(R.string.no_internet));
            }else{
                user = db.getAllUserInfor();
                Map<String,String> map = new HashMap<>();
                map.put("type","store");
                map.put("accessToken",user.getToken());
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LIST_PHARMA_CARE",response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString(JsonConstant.CODE);
                            JSONArray data = jo.getJSONArray(JsonConstant.LIST_FAVOR_STORE);
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
                                list.setStar(order.getDouble(JsonConstant.STAR));
                                list.setLat(location.getDouble(JsonConstant.LAT));
                                list.setLng(location.getDouble(JsonConstant.LONG));
                                arr.add(list);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("RESPONSE_LIST_ORDER",response);
                    }
                };
                Utils.PostServer(this, ServerPath.LIST_FAVOR,map,response);
            }


        }


    }
    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();
    }
}
