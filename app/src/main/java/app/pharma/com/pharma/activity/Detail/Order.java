package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Pill_obj;
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
    EditText quality;
    TextView price;
    Pill_obj pillObj;
    TextView name_pill;
    LinearLayout ln_order;
    Slide_Image_Adapter adapter;
    DatabaseHandle db;
    int countPrice = 0;
    User user;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Common.context = this;
        Intent it = getIntent();
        if(it.getExtras()!=null){
            pillObj = (Pill_obj)it.getSerializableExtra("obj");
        }
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
        ln_order = findViewById(R.id.ln_order);
        name_pill = findViewById(R.id.name_pill);
        name_pill.setText(pillObj.getName());
        ln_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });

        title = findViewById(R.id.title);
        img_back = findViewById(R.id.img_back);
        title.setText(getResources().getString(R.string.order));
        quality = findViewById(R.id.ed_quality);

        price = findViewById(R.id.tv_price);
        quality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(s.equals("1")){
                        countPrice = Integer.parseInt(s.toString())*pillObj.getPrice();
                        price.setText(Constant.format.format((countPrice))+" VND");
                    }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(!s.toString().equals("")){
                        countPrice = Integer.parseInt(s.toString())*pillObj.getPrice();
                        price.setText(Constant.format.format((countPrice)));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        quality.setText("1");
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
    }

    private void order() {


    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();
    }
}
