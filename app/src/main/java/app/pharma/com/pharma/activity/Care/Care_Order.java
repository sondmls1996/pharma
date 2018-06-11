package app.pharma.com.pharma.activity.Care;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Care_Order extends AppCompatActivity {
        int page = 1;
        DatabaseHandle db;
        User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care__order);
        db = new DatabaseHandle();
        getPage(1);

    }

    private void getPage(int i) {
        if(Utils.isLogin()){
            user = db.getAllUserInfor();
            Map<String,String> map = new HashMap<>();
            map.put("page",i+"");
            map.put("accessToken",user.getToken());
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE_LIST_ORDER",response);
                }
            };
            Utils.PostServer(this, ServerPath.LIST_ORDER,map,response);
        }


    }
}
