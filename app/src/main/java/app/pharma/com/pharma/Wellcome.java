package app.pharma.com.pharma;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.GetCL;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.activity.MainActivity;
import io.realm.RealmList;

public class Wellcome extends AppCompatActivity {
    DatabaseHandle databaseHandle;
    RealmList<CataloModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        Common.context = this;
        databaseHandle = new DatabaseHandle();

//        if(Utils.checkNetwork(this)){
//            databaseHandle.clearCataloData();
//            getCatalo();
//        }else{
//            if(!databaseHandle.isCataloEmpty()){
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(it);
//                        finish();
//                    }
//                },4000);
//            }else{
//                Utils.dialogNotif("Hãy bật kết nối mạng để tiếp tục");
//            }
//        }

    }

    public void checkData(){
        if(Utils.checkNetwork(this)){
            databaseHandle.clearCataloData();
            getCatalo();
        }else{
            if(!databaseHandle.isCataloEmpty()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                },4000);
            }else{
                Utils.dialogNotif("Hãy bật kết nối mạng để tiếp tục");
            }
        }
    }

    @Override
    protected void onResume() {
        checkData();
        super.onResume();
    }

    private void getCatalo() {
        Response.Listener<String> listener = response ->{
            try{
                JSONArray ja = new JSONArray(response);
                for(int i = 0; i < ja.length();i++){
                    JSONObject index = ja.getJSONObject(i);
                    JSONObject catalo = index.getJSONObject(JsonConstant.CATEGORY);
                    String type = catalo.getString(JsonConstant.TYPE);
                    CataloModel model = new CataloModel();
                    model.setId(catalo.getString(JsonConstant.ID));
                    model.setId(catalo.getString(JsonConstant.NAME));
                    list = new RealmList<>();
                    list.add(model);
                    Catalo cataloMain = new Catalo();
                    cataloMain.setType(type);
                    cataloMain.setListCatalo(list);
                    databaseHandle.updateOrInstall(cataloMain);
                }
                Catalo cata = databaseHandle.getListCataloById("product");
                RealmList<CataloModel> list2 = cata.getListCatalo();
                checkData();
                Log.d("PRODUCT_LIST",list2.get(0).getName().toString());
             //   Toast.makeText(getApplicationContext(),,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        };

        GetCL get = new GetCL(ServerPath.CATALO_PILL,listener);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }
}
