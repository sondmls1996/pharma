package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.R;

public class Change_infor extends AppCompatActivity {
    ImageView avt,avt2;
    ImageView header_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_infor);
        Common.context = this;
        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_bg);
    }
}
