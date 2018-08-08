package app.pharma.com.pharma.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.MainActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView tv_reg;
    TextView tv_lostpass;
    EditText eduser;
    CheckBox check;
    TextView not_login;
    TextView save_login;
    EditText edpass;
    Utils util;
    DatabaseHandle databaseHandle;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        util = new Utils();
        init();

    }

    private void init() {
        Common.context = this;
        databaseHandle = new DatabaseHandle();
        check = findViewById(R.id.cb);
        tv_reg = findViewById(R.id.tv_register);
        tv_lostpass = findViewById(R.id.tv_lost_pass);
        eduser = findViewById(R.id.ed_user);
        edpass = findViewById(R.id.ed_pass);
        btnlogin = findViewById(R.id.btnlogin);
        save_login = findViewById(R.id.txt_cb_save_login);
        not_login = findViewById(R.id.not_login);
        not_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(it);
                finish();
            }
        });
        save_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.performClick();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            doLogin();

            }
        });
//        Utils.setCompondEdt(R.drawable.profile,eduser);
//        Utils.setCompondEdt(R.drawable.padlock,edpass);
        tv_lostpass.setOnClickListener(this);
        tv_reg.setOnClickListener(this);
        if(!Utils.getUserName().equals("")){
            eduser.setText(Utils.getUserName());
            if(!Utils.getPass().equals("")){
                edpass.setText(Utils.getPass());
            }

            check.setChecked(true);
        }
    }

    private void doLogin() {

        String user = eduser.getText().toString();
        String pass = edpass.getText().toString();
        util.showLoading(this,20000,true);
        if(user.length()>0&&pass.length()>0){
            if(check.isChecked()){
                Utils.setUserName(user);
                Utils.setPass(pass);
            }else{
                Utils.setUserName("");
                Utils.setPass("");
            }
            Map<String,String> map= new HashMap<>();
            map.put("userName",user);
            map.put("password",pass);
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("RESPONSE_LOGIN",response);
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString(JsonConstant.CODE);
                        switch (code){
                            case "1":
                                util.showLoading(Login.this,0,false);
                                Utils.dialogNotif(getResources().getString(R.string.login_fail));
                                break;
                            case "0":
                                JSONObject data = jo.getJSONObject(JsonConstant.DATA);
                                JSONObject acc = data.getJSONObject(JsonConstant.ACCOUNTS );
                                User user = new User();
                                user.setEmail(acc.getString(JsonConstant.EMAIL));
                                user.setAdr(acc.getString(JsonConstant.USER_ADR));
                                user.setId(acc.getString(JsonConstant.ID));
                                if(acc.has(JsonConstant.AVATAR)){
                                    user.setAvt(acc.getString(JsonConstant.AVATAR));
                                }
                                if(acc.has(JsonConstant.DOB)){
                                    if(acc.getString(JsonConstant.DOB).equals("")){
                                        user.setDate(0);
                                    }else{
                                        user.setDate(acc.getLong(JsonConstant.DOB));
                                    }
                                }else{
                                    user.setDate(0);
                                }


                                user.setUserName(acc.getString(JsonConstant.USERNAME));
                                user.setToken(acc.getString(JsonConstant.ACCESS));
                                user.setName(acc.getString(JsonConstant.FULLNAME));
                                if(acc.has(JsonConstant.PHONE)){
                                    user.setPhone(acc.getString(JsonConstant.PHONE));
                                }

                                databaseHandle.updateOrInstall(user);
                                Utils.setLogin(true);
                                util.showLoading(Login.this,0,false);
                                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(it);
                                finish();

                                break;
                            case "-1":
                                util.showLoading(Login.this,0,false);
                                Utils.dialogNotif(getResources().getString(R.string.user_pass_wrong));
                                break;
                        }
                    } catch (JSONException e) {
                        util.showLoading(Login.this,0,false);
                        Utils.dialogNotif(getResources().getString(R.string.error ));
                        e.printStackTrace();
                    }
                }
            };
            Utils.PostServer(this,ServerPath.LOGIN,map,response);

        }else{
            util.showLoading(this,0,false);
            if(user.equals("")){
                eduser.setError(getResources().getString(R.string.notnull));
            }

            if(pass.equals("")){
                edpass.setError(getResources().getString(R.string.notnull));
            }
         //   Utils.dialogNotif("Không được để trống");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                Intent it = new Intent(getApplicationContext(),Register.class);
                startActivity(it);
                break;
            case R.id.tv_lost_pass:
                Intent it2 = new Intent(getApplicationContext(),Get_code.class);
                startActivity(it2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
