package app.pharma.com.pharma.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Infor_User extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ln_changeinf;
    ImageView avt,avt2;
    ImageView header_bg;
    TextView tv_name;
    TextView tv_phone;
    SimpleDateFormat format;
    TextView tv_mail;
    TextView tv_adr;
    Calendar c;
    TextView tv_username;
    TextView tv_birth;
    LinearLayout ln_changepass;
    DatabaseHandle db = new DatabaseHandle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__user);
        c = GregorianCalendar.getInstance();
        format = new SimpleDateFormat("dd-MM-yyyy");
        init();


        //ln_changeinf = findViewById(R.id.ln_changeinf);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        if(Utils.isLogin()){
            User user = db.getAllUserInfor();
            tv_name.setText(checkNull(user.getName()));

            tv_mail.setText(checkNull(user.getEmail()));
            tv_phone.setText(checkNull(user.getPhone()));
            tv_adr.setText(checkNull(user.getAdr()));
            if(user.getDate()>0){
                c.setTimeInMillis(user.getDate());

                tv_birth.setText(checkNull(format.format(c.getTime())));
            }else{
                tv_birth.setText(checkNull(""));
            }


            Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new TransImage()).into(avt);
            Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
            Picasso.with(getApplicationContext()).load(ServerPath.ROOT_URL+user.getAvt()).transform(new BlurImagePicasso()).into(header_bg);

        }else{
            Utils.dialogNotif(getResources().getString(R.string.you_not_login));
        }
        super.onResume();

    }

    private String checkNull(String string){
        if(TextUtils.isEmpty(string)){
            string = "Chưa cập nhật";
        }
        return string;
    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        RelativeLayout imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_user_inf));
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_adr = findViewById(R.id.tv_adr);
        tv_birth = findViewById(R.id.tv_date);
        tv_username = findViewById(R.id.tv_username);
        tv_mail = findViewById(R.id.tv_mail);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);

        header_bg.setOnClickListener(this);
        ln_changeinf = findViewById(R.id.ln_changeinf);
        ln_changepass = findViewById(R.id.ln_changepass);
        ln_changeinf.setOnClickListener(this);
        ln_changepass.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ln_changeinf:
                Intent it = new Intent(Common.context,Change_infor.class);
                startActivity(it);
                break;
            case R.id.ln_changepass:
                Intent it2 = new Intent(Common.context,ChangePass.class);
                startActivity(it2);
                break;
            case R.id.header_bg:

                break;
        }
    }
}
