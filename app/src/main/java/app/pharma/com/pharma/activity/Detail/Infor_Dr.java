package app.pharma.com.pharma.activity.Detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Infor_Dr_Obj;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Infor_Dr extends AppCompatActivity {
    LinearLayout ln_list;
    LinearLayout ln_call, ln_mess;
    ImageView avt, avt2;
    String id = "";
    TextView dr_name,
            dr_phone,
            dr_adr,
            dr_office,
            dr_ck;
    String strPhone;
    Infor_Dr_Obj drObj;
    ImageView header_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__dr);
        Common.context = this;
        Intent it = getIntent();
        if (it.getExtras() != null) {
            id = it.getStringExtra("id");
        }
        ln_call = findViewById(R.id.ln_call);
        ln_mess = findViewById(R.id.ln_mess);
        ln_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
                if (Build.VERSION.SDK_INT >= 23) {
                    RequestPermission();
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strPhone));
                    if (ActivityCompat.checkSelfPermission(Infor_Dr.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            }
        });

        ln_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
            }
        });

        TextView tvTitle = (TextView) findViewById(R.id.title);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.detail_infor));
        dr_name = findViewById(R.id.dr_name);
        dr_adr = findViewById(R.id.dr_adr);
        dr_ck = findViewById(R.id.dr_ck);
        dr_office = findViewById(R.id.dr_workoff);
        dr_phone = findViewById(R.id.dr_phone);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);

        ln_list = findViewById(R.id.ln_dr_inf);
        loadData();

    }

    public void RequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constant.PHONE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strPhone));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strPhone));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                } else {
                    Utils.dialogNotif("Ứng dụng sẽ không thể gọi điện nếu không cấp quyền");
                }
                return;
            }

        }
    }

    private void loadData() {
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DR_DETAIL",response);
                try {
                    drObj = new Infor_Dr_Obj();
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString(JsonConstant.CODE);
                    switch (code){
                        case "0":
                            new AsyncTask<Void,Void,Void>(){

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    JSONObject Pharma = null;
                                    try {
                                        JSONObject data = jo.getJSONObject(JsonConstant.DATA);
                                        Pharma = data.getJSONObject(JsonConstant.PHARMACIS);
                                        drObj.setAdr(Pharma.getString(JsonConstant.USER_ADR));
                                        drObj.setName(Pharma.getString(JsonConstant.NAME));
                                        drObj.setAge(Pharma.getString(JsonConstant.AGE));
                                        drObj.setAvatar(Pharma.getString(JsonConstant.AVATAR));
                                        drObj.setHospital(Pharma.getString(JsonConstant.HOSPITAL));
                                        drObj.setPhone(Pharma.getString(JsonConstant.PHONE));
                                        JSONArray ck = Pharma.getJSONArray(JsonConstant.SPECIALLIST);
                                        String StrCk = "";
                                        for (int i =0; i<ck.length();i++){
                                            StrCk = StrCk+" "+ck.getString(i)+",";
                                        }
                                        if(StrCk.endsWith(",")){
                                            StrCk = StrCk.substring(0,StrCk.length()-1);
                                        }

                                        drObj.setCatalo(StrCk);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    dr_name.setText(drObj.getName());
                                    dr_adr.setText(drObj.getAdr());
                                    dr_ck.setText(drObj.getCatalo());
                                    dr_office.setText(drObj.getHospital());
                                    dr_phone.setText(drObj.getPhone());
                                    dr_name.setText(drObj.getName());
                                    strPhone = drObj.getPhone();
                                    Utils.loadTransimagePicasso(ServerPath.ROOT_URL+drObj.getAvatar(),avt);
                                  Utils.loadTransimagePicasso(R.drawable.white,avt2);

                                  Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+drObj.getAvatar())
                                 .transform(new BlurImagePicasso()).into(header_bg);
                                    super.onPostExecute(aVoid);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            break;
                        case "1":
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(this, ServerPath.DETAIL_DR,map,response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
