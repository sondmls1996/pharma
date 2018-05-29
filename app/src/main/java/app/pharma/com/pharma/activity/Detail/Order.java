package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import me.relex.circleindicator.CircleIndicator;

public class Order extends AppCompatActivity {
    EditText ed_adr,ed_fullname,ed_email,ed_phone;
    ImageView img_back;
    TextView title;
    private ViewPager mPager;
    Slide_Image_Adapter adapter;
    DatabaseHandle db;
    User user;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Common.context = this;
        init();
    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }
        ImagesArray = Detail.imagesArray;
        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);
        mPager = (ViewPager) findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());


        title = findViewById(R.id.title);
        img_back = findViewById(R.id.img_back);
        title.setText(getResources().getString(R.string.order));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ed_adr = findViewById(R.id.ed_adr);
        ed_fullname = findViewById(R.id.ed_fullname);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        if(Utils.isLogin()){
            ed_adr.setText(user.getAdr());
            ed_fullname.setText(user.getName());
            ed_email.setText(user.getEmail());
            ed_phone.setText(user.getPhone());
        }
//        Utils.setCompondEdt(R.drawable.blue_place,ed_adr);
//        Utils.setCompondEdt(R.drawable.profile,ed_fullname);
//        Utils.setCompondEdt(R.drawable.email,ed_email);
//        Utils.setCompondEdt(R.drawable.phone_blue,ed_phone);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();
    }
}
