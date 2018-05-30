package app.pharma.com.pharma.activity.User;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
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
    SimpleDateFormat format;
    int day,month,year1;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);
        c = GregorianCalendar.getInstance();
        format = new SimpleDateFormat("dd-MM-yyyy");
        init();


    }

    private void init() {
        db = new DatabaseHandle();
        user = db.getAllUserInfor();
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

        edfullname.setText(user.getName());
        edadr.setText(user.getAdr());
        edEmail.setText(user.getEmail());
        edBirth.clearFocus();
        edBirth.setFocusable(false);

        if(user.getDate()>0){
            c.setTimeInMillis(user.getDate());
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
        edPhone.setText(user.getPhone());

        header_bg = findViewById(R.id.header_bg);
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_change_infor));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new BlurImagePicasso()).into(header_bg);
    }

    private void update() {

        Map<String,String> map = new HashMap<>();
        map.put("email",edEmail.getText().toString());
        map.put("fullName",edfullname.getText().toString());
        map.put("addressDetail",edadr.getText().toString());
        map.put("phone",edPhone.getText().toString());
        map.put("dob",c.getTimeInMillis()+"");
        map.put("accessToken",user.getToken());
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            Log.d("RESPONSE_CHANGE_INFOR",response);
            }
        };
        Utils.PostServer(this,ServerPath.CHANGE_INFO,map,response);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }

    public void openDialogDate(int d, int m, int y){


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
