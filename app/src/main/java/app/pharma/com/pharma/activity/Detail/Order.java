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
import app.pharma.com.pharma.R;
import me.relex.circleindicator.CircleIndicator;

public class Order extends AppCompatActivity {
    EditText ed_adr,ed_fullname,ed_email,ed_phone;
    ImageView img_back;
    TextView title;
    private ViewPager mPager;
    Slide_Image_Adapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();
    }

    private void init() {



        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);

        mPager = (ViewPager) findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        NUM_PAGES =2;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

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

//        Utils.setCompondEdt(R.drawable.blue_place,ed_adr);
//        Utils.setCompondEdt(R.drawable.profile,ed_fullname);
//        Utils.setCompondEdt(R.drawable.email,ed_email);
//        Utils.setCompondEdt(R.drawable.phone_blue,ed_phone);
    }
}
