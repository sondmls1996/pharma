package app.pharma.com.pharma.activity.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class ChangePass extends AppCompatActivity implements View.OnClickListener {
    ImageView avt,avt2;
    EditText ed_oldpass,ed_newpass,ed_renewpass;
    ImageView header_bg;
    Button btn_ok;
    DatabaseHandle data;
    User user;
    Utils utils = new Utils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        init();

    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        data = new DatabaseHandle();

        tvTitle.setText(getResources().getString(R.string.title_change_pass));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ed_oldpass = findViewById(R.id.ed_oldpass);
        ed_newpass = findViewById(R.id.ed_newpass);
        ed_renewpass = findViewById(R.id.ed_renewpass);
//        Utils.setCompondEdt(R.drawable.padlock,ed_oldpass);
//        Utils.setCompondEdt(R.drawable.padlock,ed_renewpass);
//        Utils.setCompondEdt(R.drawable.padlock,ed_newpass);

        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);
        btn_ok = findViewById(R.id.btn_accep);
        btn_ok.setOnClickListener(this);
        if(Utils.isLogin()){
            user = data.getAllUserInfor();
            Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new TransImage()).into(avt);
            Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
            Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new BlurImagePicasso()).into(header_bg);

        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_accep:
               utils.showLoading(this,20000,true);
                    String oldPass = ed_oldpass.getText().toString();
                    String newPass = ed_newpass.getText().toString();
                    String reNewPass  =ed_renewpass.getText().toString();

                    if(TextUtils.isEmpty(oldPass)||TextUtils.isEmpty(newPass)||TextUtils.isEmpty(reNewPass)){
                        utils.showLoading(getApplicationContext(),20000,false);
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.notnull),Toast.LENGTH_SHORT).show();
                    }else if(!newPass.equals(reNewPass)){
                        utils.showLoading(getApplicationContext(),20000,false);
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.pass_notequal),Toast.LENGTH_SHORT).show();
                    }else{
                        Map<String,String> map = new HashMap<>();
                        map.put("accessToken",user.getToken());
                        map.put("oldPass",oldPass);
                        map.put("newPass",newPass);
                        map.put("reNewPass",reNewPass);
                        Response.Listener<String> response = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("RESPONSE_CHANGE_PASS",response);
                                    JSONObject jo = new JSONObject(response);
                                    String code = jo.getString(JsonConstant.CODE);
                                    switch (code){
                                        case "0":
                                            utils.showLoading(getApplicationContext(),20000,false);
                                            Utils.dialogNotif(getResources().getString(R.string.update_pass_succes));
                                            break;
                                        case "1":
                                            utils.showLoading(getApplicationContext(),20000,false);
                                            Utils.dialogNotif(getResources().getString(R.string.update_pass_failse));
                                            break;
                                        case "-1":
                                            utils.showLoading(getApplicationContext(),20000,false);
                                            Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                                            break;
                                            default:
                                                utils.showLoading(getApplicationContext(),20000,false);
                                                Utils.dialogNotif(getResources().getString(R.string.error));
                                                break;
                                    }
                                } catch (JSONException e) {
                                    utils.showLoading(getApplicationContext(),20000,false);
                                    Utils.dialogNotif(getResources().getString(R.string.error));
                                    e.printStackTrace();
                                }
                            }
                        };
                        Utils.PostServer(ChangePass.this, ServerPath.CHANGE_PASS,map,response);
                    }
                break;
        }
    }

    @Override
    protected void onResume() {
        Common.context = this;
        if(Utils.isLogin()){
            user = data.getAllUserInfor();
        }else{
            Utils.dialogNotif(getResources().getString(R.string.you_not_login));
        }
        super.onResume();

    }

}
