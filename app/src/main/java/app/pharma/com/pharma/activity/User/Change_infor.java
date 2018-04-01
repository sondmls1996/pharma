package app.pharma.com.pharma.activity.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.R;

public class Change_infor extends AppCompatActivity {
    ImageView avt,avt2;
    ImageView header_bg;
    EditText edfullname,edEmail,edpass,edRepass,edBirth,edadr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);
        init();


    }

    private void init() {
        Common.context = this;
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        edpass = findViewById(R.id.ed_pass);
        edRepass = findViewById(R.id.ed_repass);
        edEmail = findViewById(R.id.ed_email);
        edBirth = findViewById(R.id.ed_birth);
        edadr = findViewById(R.id.ed_adr);
        edfullname=findViewById(R.id.ed_fullname);

//        Utils.setCompondEdt(R.drawable.profile,edfullname);
//        Utils.setCompondEdt(R.drawable.padlock,edpass);
//        Utils.setCompondEdt(R.drawable.padlock,edRepass);
//        Utils.setCompondEdt(R.drawable.email,edEmail);
//        Utils.setCompondEdt(R.drawable.calendar,edBirth);
//        Utils.setCompondEdt(R.drawable.blue_place,edadr);


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
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_bg);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }
}
