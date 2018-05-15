package app.pharma.com.pharma.activity.Login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Get_code extends AppCompatActivity implements View.OnClickListener {
    Button get_code;
    Utils utils;
    EditText ed_email;
    String mail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        utils = new Utils();
        init();

    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        ed_email = findViewById(R.id.ed_email);
     //   Utils.setCompondEdt(R.drawable.padlock,ed_email);
        tvTitle.setText(getResources().getString(R.string.lost_pass));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        get_code = findViewById(R.id.btn_confirmcode);
        get_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirmcode:
              utils.showLoading(this,10000,true);
                mail = ed_email.getText().toString();
                if(!Utils.isValidEmail(mail)){
                utils.showLoading(this,10000,false);
                    Toast.makeText(getApplicationContext(),
                            "Email không đúng định dạng",Toast.LENGTH_SHORT).show();
            }
               if(!TextUtils.isEmpty(mail)&& Utils.isValidEmail(mail)){
                   Map<String,String> map = new HashMap<>();
                   map.put("email",mail);
                   Response.Listener<String> response = new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           try {
                               JSONObject jo = new JSONObject(response);
                               String code = jo.getString(JsonConstant.CODE);
                               switch (code){
                                   case "0":
                                       utils.showLoading(Get_code.this,10000,false);
                                       showDialogRate();
                                       break;
                                   case "1":
                                       utils.showLoading(Get_code.this,10000,false);
                                       Toast.makeText(getApplicationContext(),
                                               "Email này chưa được đăng ký",Toast.LENGTH_SHORT).show();
                                       break;
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                               utils.showLoading(Get_code.this,10000,false);
                               Toast.makeText(getApplicationContext(),getApplicationContext().
                                       getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();

                           }
                       }
                   };
                   Utils.PostServer(this, ServerPath.LOST_PASS,map,response);

               }
                break;
        }
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }
    private void showDialogRate() {
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();

        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_type_code);
        dialog.setCanceledOnTouchOutside(false);
        EditText type_code = dialog.findViewById(R.id.ed_typecode);
        Button btn_acc = dialog.findViewById(R.id.btn_update_pass);
        btn_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showLoading(Get_code.this,20000,true);
                String code = type_code.getText().toString();
                if(TextUtils.isEmpty(code)){
                    Map<String, String> map = new HashMap<>();
                    map.put("email",mail);
                    map.put("codeForgetPass",code);
                    Response.Listener<String> response = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE_GETCODE",response);
                            JSONObject jo = null;
                            try {
                                jo = new JSONObject(response);
                                String code = jo.getString(JsonConstant.CODE);
                                switch (code){
                                    case "0":
                                        utils.showLoading(Get_code.this,20000,false);
                                        Intent it = new Intent(getApplicationContext(),Login.class);
                                        startActivity(it);
                                        finish();
                                        Toast.makeText(getApplicationContext(),
                                                "Mật khẩu mới đã được gửi vào email",Toast.LENGTH_SHORT).show();
                                        break;
                                    case "1":
                                        utils.showLoading(Get_code.this,20000,false);
                                        Toast.makeText(getApplicationContext(),
                                                "Mã xác nhận không đúng",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    Utils.PostServer(Get_code.this,ServerPath.TYPE_CODE,map,response);
                }
            }
        });
        dialog.show();
    }
}
