package app.pharma.com.pharma.activity.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class ChangePass extends AppCompatActivity implements View.OnClickListener {
    ImageView avt,avt2;
    EditText ed_oldpass,ed_newpass,ed_renewpass;
    ImageView header_bg;
    Button btn_ok;
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
        Utils.setCompondEdt(R.drawable.padlock,ed_oldpass);
        Utils.setCompondEdt(R.drawable.padlock,ed_renewpass);
        Utils.setCompondEdt(R.drawable.padlock,ed_newpass);

        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);
        btn_ok = findViewById(R.id.btn_accep);
        btn_ok.setOnClickListener(this);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_bg);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_accep:
          //      showDialogRate();
                break;
        }
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }

}
