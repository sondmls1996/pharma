package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.ChangePass;

public class Infor_User extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ln_changeinf;
    ImageView avt;
    ImageView header_bg;
    LinearLayout ln_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__user);
        Common.context = this;

        avt = findViewById(R.id.img_avt);

      //  Utils.setCiclerImage(rl_avt);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avt);
        header_bg = findViewById(R.id.header_bg);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_bg);
        header_bg.setOnClickListener(this);
        ln_changeinf = findViewById(R.id.ln_changeinf);
        ln_changepass = findViewById(R.id.ln_changepass);
        ln_changeinf.setOnClickListener(this);
        ln_changepass.setOnClickListener(this);
        //ln_changeinf = findViewById(R.id.ln_changeinf);
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
