package app.pharma.com.pharma;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.GetCL;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.Service.GetLocationService;
import app.pharma.com.pharma.activity.MainActivity;
import io.realm.RealmList;

public class Wellcome extends AppCompatActivity {
    DatabaseHandle databaseHandle;
    RealmList<CataloModel> list;
    StringBuffer type;
    LocationManager locationManager;
    Location loc;
    boolean canGetLocation = false;
    double lat;
    double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        Common.context = this;
        databaseHandle = new DatabaseHandle();
        startService(new Intent(this, GetLocationService.class));

    }

    public void checkData() {
        if (!databaseHandle.isCataloEmpty()) {
            if (databaseHandle.isCataloSickEmpty()) {
                getCataloPill(ServerPath.CATALO_SICK);
            } else if (databaseHandle.isCataloPillEmpty()) {
                getCataloPill(ServerPath.CATALO_PILL);
            } else if (databaseHandle.isCataloPillIntroEmpty()) {
                getCataloPill(ServerPath.CATALO_PILL_INTRO);
            } else if (databaseHandle.isCataloDrEmpty()) {
                getCataloPill(ServerPath.CATALO_DR);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                }, 3000);
            }
        } else {
            if (Utils.isNetworkEnable(this)) {
                getCataloPill(ServerPath.CATALO_SICK);
            } else {
                Utils.dialogNotif("Hãy bật kết nối mạng để tiếp tục");
            }
        }


    }


    @Override
    protected void onResume() {

        checkData();
        super.onResume();
    }


    private void getCataloPill(String link) {
        final String[] array = {null};

        Response.Listener<String> listener = response ->{
            try{
                databaseHandle = new DatabaseHandle();
                list = new RealmList<>();
                Log.d("RESPONSE_CATALO",response);
                JSONObject job = new JSONObject(response);
                JSONArray ja = null;

                     ja = job.getJSONArray(JsonConstant.LIST_CAT_DISE);


                for(int i = 0; i < ja.length();i++){
                    type = new StringBuffer();
                    JSONObject index = ja.getJSONObject(i);
                    JSONObject catalo = index.getJSONObject(JsonConstant.CATEGORY);
                     type.append(catalo.getString(JsonConstant.TYPE));
                    CataloModel model = new CataloModel();
                    model.setId(catalo.getString(JsonConstant.ID));
                    model.setName(catalo.getString(JsonConstant.NAME));

                    list.add(model);

                }
                Catalo cataloMain = new Catalo();
                cataloMain.setType(type.toString());
                cataloMain.setListCatalo(list);
                databaseHandle.updateOrInstall(cataloMain);

                checkData();

            }catch (Exception e){
                e.printStackTrace();
            }
        };

        GetCL get = new GetCL(link,listener);
        RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        que.add(get);
    }
}
