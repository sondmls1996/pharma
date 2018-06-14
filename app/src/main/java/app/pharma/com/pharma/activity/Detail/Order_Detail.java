package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Order_Detail extends AppCompatActivity {
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        Intent it = getIntent();
        if(it.getExtras()!=null){
            id = it.getStringExtra("id");
        }
        getData();
    }

    private void getData() {
        Map<String,String> map = new HashMap();
        map.put("id",id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DETAIL_ORDER",response);
            }
        };
        Utils.PostServer(this, ServerPath.DETAIL_ORDER,map,response);
    }
}
