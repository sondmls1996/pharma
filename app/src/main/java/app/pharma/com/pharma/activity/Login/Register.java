package app.pharma.com.pharma.activity.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Register extends AppCompatActivity {
    EditText eduser,edEmail,edpass,edRepass,edBirth,edPhone;
    CheckBox cb_acc;
    Button login_btn;
    int day,month,year1;
    Calendar calendar;
    String stringdate = "";
    SimpleDateFormat df;
    Utils util;
     Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Common.context = this;
         c = GregorianCalendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
      util = new Utils();
        df = new SimpleDateFormat("dd/MM/yyyy");
        stringdate = df.format(System.currentTimeMillis());
        init();



    }

    public void openDialogDate(int d, int m, int y){


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        year1 = year;
                        day = dayOfMonth;
                        month = monthOfYear;
                        c.set(Calendar.YEAR,year1);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,day);

                          edBirth.setText(df.format(c.getTime()));
                    }
                }, y, m, d);

        datePickerDialog.show();
    }

    private void init() {
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_register));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        login_btn = findViewById(R.id.btn_register);
        eduser = findViewById(R.id.ed_user);
        edpass = findViewById(R.id.ed_pass);
        edRepass = findViewById(R.id.ed_repass);
        edEmail = findViewById(R.id.ed_email);
        edPhone = findViewById(R.id.ed_phone);
        edBirth = findViewById(R.id.ed_birth);
        edBirth.clearFocus();
        edBirth.setFocusable(false);
        edBirth.setText(stringdate);

        edBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogDate(day,month,year1);
            }
        });
        cb_acc = findViewById(R.id.cb_acceep);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
//        Utils.setCompondEdt(R.drawable.profile,eduser);
//        Utils.setCompondEdt(R.drawable.padlock,edpass);
//        Utils.setCompondEdt(R.drawable.padlock,edRepass);
//        Utils.setCompondEdt(R.drawable.email,edEmail);
//        Utils.setCompondEdt(R.drawable.calendar,edBirth);

//        eduser.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.profile)),null,null,null);
//        edpass.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.padlock)),null,null,null);
//        edRepass.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.padlock)),null,null,null);
//        edEmail.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.email)),null,null,null);
//        edBirth.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.calendar)),null,null,null);

    }

    private void doRegister() {

        String user = eduser.getText().toString();
        String pass = edpass.getText().toString();
        String repass = edRepass.getText().toString();
        String email = edEmail.getText().toString();
        String phone = edPhone.getText().toString();
        String date = edBirth.getText().toString();
        util.showLoading(this,10000,true);
        if(user.length()>0&&pass.length()>0&&repass.length()>0&&email.length()>0&&date.length()>0&&phone.length()>0){
            if(!pass.equals(repass)){
                util.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.pass_notequal));

            }else if(!cb_acc.isChecked()){
                util.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.not_check));
            }else if(!Utils.isValidEmail(email)){
                util.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.validate_email));

            }
            else{
                Map<String ,String> map = new HashMap<>();
                map.put("userName",user);
                map.put("email",email);
                map.put("password",pass);
                map.put("passAgain",repass);
                map.put("phone",phone);

            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE_REGISTER",response);
                    try {
                        JSONObject jo = new JSONObject(response);
                        String code = jo.getString(JsonConstant.CODE);
                        if(code!=null&&code.length()>0){
                            if(code.equals("0")){
                                util.showLoading(Register.this,10000,false);
                                Utils.toast(getResources().getString(R.string.register_success));
                                Intent it = new Intent(getApplicationContext(),Login.class);
                                startActivity(it);
                                finish();
                            }else if(code.equals("1")){
                                util.showLoading(Register.this,10000,false);
                                Utils.toast(getResources().getString(R.string.has_acc));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        util.showLoading(Register.this,10000,false);
                        Utils.toast(getResources().getString(R.string.error));
                    }
                }
            };
            Utils.PostServer(this,ServerPath.REGISTER,map,response);

            }

        }else{
            util.showLoading(this,10000,false);
            Utils.dialogNotif(getResources().getString(R.string.notnull));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
