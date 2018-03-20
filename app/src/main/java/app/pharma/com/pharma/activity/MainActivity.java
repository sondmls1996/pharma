package app.pharma.com.pharma.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.pharma.com.pharma.Fragment.Dr_Fragment;
import app.pharma.com.pharma.Fragment.Meo_Fragment;
import app.pharma.com.pharma.Fragment.Pharma_Fragment;
import app.pharma.com.pharma.Fragment.Pill_Fragment;
import app.pharma.com.pharma.Fragment.Sick_Fragment;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Class fragment;
    ImageView imgnav;
    ImageView img_pill;
    ImageView img_sick;
    ImageView img_dr;
    ImageView img_pharma;
    ImageView img_meo;
    TextView tv_pill;
    TextView tv_sick;
    TextView tv_dr;
    TextView tv_pharma;
    TextView tv_meo;
    DrawerLayout drawer;
    LinearLayout ln_pill;
    LinearLayout ln_sick;
    LinearLayout ln_dr;
    LinearLayout ln_pharma;
    LinearLayout ln_meo;
    GetScrollBroadcast scroll_broadcast;
    FrameLayout fragContrent;
    CardView cv;
    Resources r;
    AppBarLayout appbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        imgnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

      ln_pill.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(scroll_broadcast==null){
            IntentFilter fliter = new IntentFilter();
            fliter.addAction(Constant.SCROLL_LV);
            scroll_broadcast = new GetScrollBroadcast();
            registerReceiver(scroll_broadcast,fliter);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("STT","pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
        Log.d("STT","des");
    }

    private void unregister() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scroll_broadcast);
    }



    private void initView() {

        appbar = findViewById(R.id.app_bar);
        r = getResources();
        cv = (CardView)findViewById(R.id.cv_bot);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        imgnav = (ImageView)findViewById(R.id.img_nav);
        img_pill = (ImageView)findViewById(R.id.pill_image);
        img_sick = (ImageView)findViewById(R.id.sick_image);
        img_dr = (ImageView)findViewById(R.id.dr_image);
        img_pharma = (ImageView)findViewById(R.id.pharma_image);
        img_meo = (ImageView)findViewById(R.id.meo_image);

        tv_pill = (TextView)findViewById(R.id.pill_text);
        tv_sick = (TextView)findViewById(R.id.sick_text);
        tv_dr = (TextView)findViewById(R.id.dr_text);
        tv_pharma = (TextView)findViewById(R.id.pharma_text);
        tv_meo = (TextView)findViewById(R.id.meo_text);

        ln_pill = (LinearLayout)findViewById(R.id.ln_pill);
        ln_sick = (LinearLayout)findViewById(R.id.ln_sick);
        ln_dr = (LinearLayout)findViewById(R.id.ln_dr);
        ln_pharma = (LinearLayout)findViewById(R.id.ln_pharma);
        ln_meo = (LinearLayout)findViewById(R.id.ln_meo);

        ln_pill.setOnClickListener(this);
        ln_sick.setOnClickListener(this);
        ln_dr.setOnClickListener(this);
        ln_pharma.setOnClickListener(this);
        ln_meo.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
    public void ReplaceFrag(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(scroll_broadcast==null){
            IntentFilter fliter = new IntentFilter();
            fliter.addAction(Constant.SCROLL_LV);
            scroll_broadcast = new GetScrollBroadcast();
            registerReceiver(scroll_broadcast,fliter);

        }
    }

    private void changeColor() {
        img_meo.setImageDrawable(r.getDrawable(R.drawable.news_gray));
        img_pill.setImageDrawable(r.getDrawable(R.drawable.pill));
        img_sick.setImageDrawable(r.getDrawable(R.drawable.sick2));
        img_dr.setImageDrawable(r.getDrawable(R.drawable.dr_nghe));
        img_pharma.setImageDrawable(r.getDrawable(R.drawable.pharma2));

        tv_pharma.setTextColor(r.getColor(R.color.gray));
        tv_pill.setTextColor(r.getColor(R.color.gray));
        tv_sick.setTextColor(r.getColor(R.color.gray));
        tv_dr.setTextColor(r.getColor(R.color.gray));
        tv_meo.setTextColor(r.getColor(R.color.gray));


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ln_pill:
                changeColor();
                tv_pill.setTextColor(r.getColor(R.color.blue));
                img_pill.setImageDrawable(r.getDrawable(R.drawable.pill_blue));
                fragment = Pill_Fragment.class;
                ReplaceFrag(fragment);
                break;
            case R.id.ln_sick:
                changeColor();
                tv_sick.setTextColor(r.getColor(R.color.blue));
                img_sick.setImageDrawable(r.getDrawable(R.drawable.sick));
                fragment = Sick_Fragment.class;
                ReplaceFrag(fragment);
                break;
            case R.id.ln_dr:
                changeColor();
                tv_dr.setTextColor(r.getColor(R.color.blue));
                img_dr.setImageDrawable(r.getDrawable(R.drawable.dr_nghe_blue));
                fragment = Dr_Fragment.class;
                ReplaceFrag(fragment);
                break;
            case R.id.ln_pharma:
                changeColor();
                tv_pharma.setTextColor(r.getColor(R.color.blue));
                img_pharma.setImageDrawable(r.getDrawable(R.drawable.pharmablue));
                fragment = Pharma_Fragment.class;
                ReplaceFrag(fragment);
                break;
            case R.id.ln_meo:
                changeColor();
                tv_meo.setTextColor(r.getColor(R.color.blue));
                img_meo.setImageDrawable(r.getDrawable(R.drawable.news_blue));
                fragment = Meo_Fragment.class;
                ReplaceFrag(fragment);
                break;
        }
    }

    class GetScrollBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null){
                int action = intent.getIntExtra("action",1);
                if(action==Constant.ACTION_DOWN){
                    Log.d("hahaha","down");
//                    final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
//                    final Animation myAnim2 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein2);
                  //  appbar.setAnimation(myAnim2);
                 //   appbar.setVisibility(View.GONE);
                  //  cv.setAnimation(myAnim);
                    cv.setVisibility(View.GONE);
                }else{
                    Log.d("hahaha","up");
//                    final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
//                    final Animation myAnim2 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout2);
//                    appbar.setAnimation(myAnim2);
                  //  appbar.setVisibility(View.VISIBLE);
               //     cv.setAnimation(myAnim);
                    cv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
