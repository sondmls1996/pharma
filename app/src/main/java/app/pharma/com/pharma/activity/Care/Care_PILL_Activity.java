package app.pharma.com.pharma.activity.Care;

import android.content.Intent;
import android.os.Bundle;
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

import app.pharma.com.pharma.Adapter.Like_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Like_Constructor;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Detail;

public class Care_PILL_Activity extends AppCompatActivity {
    RecyclerView lv;
    Like_Adapter adapter;
    DatabaseHandle db;
    User user;
    int Mainpage = 1;
    String key = "";
    ArrayList<Like_Constructor> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_);
        Common.context = this;
        Intent it = getIntent();
        if(it.getExtras()!=null){
            key = it.getExtras().getString("key");
        }
       init();

    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }

        TextView tvTitle = (TextView)findViewById(R.id.title);
        RelativeLayout imgBack = findViewById(R.id.img_back);
        if(key.equals("pill")){
            tvTitle.setText(getResources().getString(R.string.title_care_pill));
        }else{
            tvTitle.setText(getResources().getString(R.string.title_care_sick));
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        arr = new ArrayList<>();
        setRecycle();

        getDataLike(Mainpage);
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                Intent it = new Intent(getApplicationContext(), Detail.class);
//                it.putExtra("key","pill");
//                it.putExtra("id", arr.get(i).getId());
//                startActivity(it);
//            }
//        });

//        arr.add(new Like_Constructor());
//        arr.add(new Like_Constructor());
//        arr.add(new Like_Constructor());
//        arr.add(new Like_Constructor());
//        adapter.notifyDataSetChanged();
    }

    public void setRecycle(){
        lv = findViewById(R.id.lv_like);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        lv.setLayoutManager(layoutManager);
        adapter = new Like_Adapter(getApplicationContext(), arr,key);
        lv.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            //   getDataLike();
            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getApplicationContext(), Detail.class);
                        it.putExtra("key","pill");
                        it.putExtra("id", arr.get(i).getId());
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }

    private void getDataLike(int page) {

        if(Utils.isLogin()){
            Map<String,String> map = new HashMap<>();
            map.put("accessToken",user.getToken());
            map.put("type","product");
            map.put("page",page+"");
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE_CARE_PILL",response);

                    try {
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                JSONArray list = jo.getJSONArray(JsonConstant.LIST_FAVOR_PRODUCT);
                                for(int i=0;i<list.length();i++){
                                    JSONObject idx = list.getJSONObject(i);
                                    JSONObject product = idx.getJSONObject(JsonConstant.PRODUCT);
                                    JSONObject price = product.getJSONObject(JsonConstant.PRICE);
                                    JSONArray images = product.getJSONArray(JsonConstant.IMAGE);
                                    Like_Constructor like = new Like_Constructor();
                                    like.setName(product.getString(JsonConstant.NAME));
                                    like.setComment(product.getString(JsonConstant.COMMENT));
                                    like.setLike(product.getString(JsonConstant.LIKE));
                                    like.setDescri(product.getString(JsonConstant.DESCRI));
                                    like.setId(product.getString(JsonConstant.ID));
                                    like.setImage(images.getString(0));
                                    like.setPrice(price.getInt(JsonConstant.MONEY));
                                    arr.add(like);
                                }
                                adapter.notifyDataSetChanged();
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Utils.PostServer(this, ServerPath.LIST_FAVOR,map,response);
        }

    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }
}
