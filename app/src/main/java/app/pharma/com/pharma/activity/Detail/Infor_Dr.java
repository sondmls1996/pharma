package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.R;

public class Infor_Dr extends AppCompatActivity {
    LinearLayout ln_list;
    ImageView avt,avt2;
    ImageView header_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__dr);
        Common.context = this;

        avt = findViewById(R.id.img_avt);
        avt2 = findViewById(R.id.img_avtbg);
        header_bg = findViewById(R.id.header_bg);
        Picasso.with(getApplicationContext()).load("http://dreamstop.com/wp-content/uploads/2013/06/doctor-dream-meaning.jpg").transform(new TransImage()).into(avt);
        Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avt2);
        Picasso.with(getApplicationContext()).load("http://dreamstop.com/wp-content/uploads/2013/06/doctor-dream-meaning.jpg").transform(new BlurImagePicasso()).into(header_bg);
        ln_list = findViewById(R.id.ln_dr_inf);
//        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        for (int i = 0; i < 10; i++){
//            final View rowView = inflater2.inflate(R.layout.item_infor, null);
//            ln_list.addView(rowView);
//
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
