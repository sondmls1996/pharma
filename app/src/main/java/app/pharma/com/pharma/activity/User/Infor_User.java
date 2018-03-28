package app.pharma.com.pharma.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.R;

public class Infor_User extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ln_changeinf;
    ImageView avt,avt2;
    ImageView header_bg;
    LinearLayout ln_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__user);
        init();


        //ln_changeinf = findViewById(R.id.ln_changeinf);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_user_inf));



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_bg);
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
