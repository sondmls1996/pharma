package app.pharma.com.pharma.activity.User;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

public class Change_infor extends AppCompatActivity {
    ImageView avt,avt2;
    ImageView header_bg;
    EditText edfullname,edEmail,edPhone,edRepass,edBirth,edadr;
    DatabaseHandle db;
    Button update;
    Calendar c;
    Utils utils;
    String id;
    SimpleDateFormat format;
    int day,month,year1;
    User Mainuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);
        c = GregorianCalendar.getInstance();
        format = new SimpleDateFormat("dd-MM-yyyy");
        utils = new Utils();
        init();


    }

    private void init() {

        Common.context = this;
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        edPhone = findViewById(R.id.ed_phone);
        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        edEmail = findViewById(R.id.ed_email);
        edBirth = findViewById(R.id.ed_birth);
        edadr = findViewById(R.id.ed_adr);
        edfullname=findViewById(R.id.ed_fullname);

        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year1 = c.get(Calendar.YEAR);


        if(Utils.isLogin()){
            db = new DatabaseHandle();

            Mainuser = db.getAllUserInfor();
            id = Mainuser.getId();
            edfullname.setText(Mainuser.getName());
            edadr.setText(Mainuser.getAdr());
            edEmail.setText(Mainuser.getEmail());
            edBirth.clearFocus();
            edBirth.setFocusable(false);
            header_bg = findViewById(R.id.header_bg);
            if(Mainuser.getDate()>0){
                c.setTimeInMillis(Mainuser.getDate());
                edBirth.setText(format.format(c.getTime()));
            }else{
                //       c.setTimeInMillis(user.getDate());
                edBirth.setText(format.format(c.getTimeInMillis()));
            }

            edBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogDate(day,month,year1);
                }
            });
            edPhone.setText(Mainuser.getPhone());
        }

        TextView tvTitle = (TextView)findViewById(R.id.title);
        RelativeLayout imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_change_infor));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Picasso.get().load(ServerPath.ROOT_URL+Mainuser.getAvt()).transform(new TransImage()).into(avt);
        Picasso.get().load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.get().load(ServerPath.ROOT_URL+Mainuser.getAvt()).transform(new BlurImagePicasso()).into(header_bg);
    }

    private void update() {
        utils.showLoading(this,10000,true);
        String email = edEmail.getText().toString();
        String name = edfullname.getText().toString();
        String adr = edadr.getText().toString();
        String phone = edPhone.getText().toString();
        if(!phone.equals("")&&!adr.equals("")&&!name.equals("")){
            if(!Utils.isPhoneAccep(phone)){
                utils.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.validate_phone));
            }else if(!email.equals("")&&!Utils.isValidEmail(email)){
                utils.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.validate_email));
            }else{
                Map<String,String> map = new HashMap<>();
                map.put("email",email);
                map.put("fullName",name);
                map.put("addressDetail",adr);
                map.put("phone",phone);
                map.put("dob",c.getTimeInMillis()+"");
                map.put("accessToken",Mainuser.getToken());
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString(JsonConstant.CODE);
                            switch (code){
                                case "0":
                                    utils.showLoading(Change_infor.this,10000,false);

                                    User user = new User();
                                    user.setId(id);
                                    user.setEmail(email);
                                    user.setAdr(adr);
                                    user.setDate(c.getTimeInMillis());
                                    user.setPhone(phone);
                                    user.setName(name);

                                    user.setToken(Mainuser.getToken());
                                    user.setAvt(Mainuser.getAvt());
                                    user.setUserName(Mainuser.getUserName());
                                    db.updateOrInstall(user);
                            Utils.ShowNotifString(getResources().getString(R.string.change_infor_success),
                                    new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                                        @Override
                                        public void onClose(Dialog dialog) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                                       break;
                                case "-1":
                                    Utils.ShowNotifString(getResources().getString(R.string.session_out),
                                            new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                                                @Override
                                                public void onClose(Dialog dialog) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                    break;
                                default:
                                    utils.showLoading(Change_infor.this,10000,false);
                                    Utils.dialogNotif(getResources().getString(R.string.change_infor_failse));
                                    break;
                            }
                        } catch (JSONException e) {
                            utils.showLoading(Change_infor.this,10000,false);
                            Utils.dialogNotif(getResources().getString(R.string.change_infor_failse));
                            e.printStackTrace();
                        }
                        Log.d("RESPONSE_CHANGE_INFOR",response);
                    }
                };
                Utils.PostServer(this,ServerPath.CHANGE_INFO,map,response);
            }



        }else{
            if(name.equals("")){
                utils.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.name_not_null));
            }
            if(phone.equals("")){
                utils.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.phone_not_null));
            }
            if(adr.equals("")){
                utils.showLoading(this,10000,false);
                Utils.dialogNotif(getResources().getString(R.string.adr_not_null));
            }

        }


    }

    @Override
    protected void onResume() {
        Common.context = this;
        if(Utils.isLogin()){
            db = new DatabaseHandle();

            Mainuser = db.getAllUserInfor();
            id = Mainuser.getId();
            edfullname.setText(Mainuser.getName());
            edadr.setText(Mainuser.getAdr());
            edEmail.setText(Mainuser.getEmail());
            edBirth.clearFocus();
            edBirth.setFocusable(false);

            if(Mainuser.getDate()>0){
                c.setTimeInMillis(Mainuser.getDate());
                edBirth.setText(format.format(c.getTime()));
            }else{
                //       c.setTimeInMillis(user.getDate());
                edBirth.setText(format.format(c.getTimeInMillis()));
            }

            edBirth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogDate(day,month,year1);
                }
            });
            edPhone.setText(Mainuser.getPhone());
            Picasso.get().load(ServerPath.ROOT_URL+Mainuser.getAvt()).transform(new TransImage()).into(avt);
            Picasso.get().load(R.drawable.white).transform(new TransImage()).into(avt2);
            Picasso.get().load(ServerPath.ROOT_URL+Mainuser.getAvt()).transform(new BlurImagePicasso()).into(header_bg);
        }
        super.onResume();

    }

    public void openDialogDate(int d, int m, int y){


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        year1 = year;
                        day = dayOfMonth;
                        month = monthOfYear;
                        c.set(Calendar.YEAR,year1);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,day);
                        edBirth.setText(format.format(c.getTime()));
                    }
                }, y, m, d);

        datePickerDialog.show();
    }
}
