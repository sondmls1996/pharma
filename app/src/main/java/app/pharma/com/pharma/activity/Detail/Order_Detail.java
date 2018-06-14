package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Order_Detail extends AppCompatActivity {
    String id = "";
    DatabaseHandle db;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        Intent it = getIntent();
        if(it.getExtras()!=null){
            id = it.getStringExtra("id");
        }
        db = new DatabaseHandle();
        if(Utils.isLogin()){
            user = db.getAllUserInfor();
        }
        getData();
    }

    private void getData() {
        Map<String,String> map = new HashMap();
        map.put("idOrder",id);
        map.put("accessToken",user.getToken());
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DETAIL_ORDER",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray(JsonConstant.DATA);
                    for (int i =0; i<ja.length();i++){
                        JSONObject idx = ja.getJSONObject(i);
                        JSONObject order = idx.getJSONObject(JsonConstant.ORDER);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(this, ServerPath.DETAIL_ORDER,map,response);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}
