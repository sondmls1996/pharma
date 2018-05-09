package app.pharma.com.pharma.activity;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.pharma.com.pharma.Fragment.Dr.Dr_Fragment;
import app.pharma.com.pharma.Fragment.Meo_Fragment;
import app.pharma.com.pharma.Fragment.Pharma.Pharma_Fragment;
import app.pharma.com.pharma.Fragment.Pill.Pill_Fragment;
import app.pharma.com.pharma.Fragment.Sick.Sick_Fragment;
import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Like.Care_Activity;
import app.pharma.com.pharma.activity.Login.Login;
import app.pharma.com.pharma.activity.User.Infor_User;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Class fragment;
    ImageView imgnav;
    ImageView img_pill;
    ImageView img_sick;
    Spinner spiner;
    ArrayList<String> arr,arr_tp;
    ImageView img_dr;
    ImageView img_pharma;
    ImageView header_background;
    ImageView img_meo;
    boolean isAnimated = false;
    TextView tv_pill;
    TextView tv_sick;
    TextView tv_dr;
    TextView tv_pharma;
    TextView title;
    TextView tv_meo;
    EditText edSearch;
    DrawerLayout drawer;
    LinearLayout ln_pill;
    LinearLayout ln_sick;
    LinearLayout ln_dr;
    RelativeLayout rl_search;
    LinearLayout ln_pharma;
    DatabaseHandle db;
    LinearLayout ln_meo;
    GetScrollBroadcast scroll_broadcast;
    FrameLayout fragContrent;
    CardView cv;
    Menu menu;
    Resources r;
    View headerview;
    AppBarLayout appbar;
    NavigationView nav;
    ImageView img_close;
    ImageView avatar,avatar2;
    FloatingActionButton fillter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        Common.context = this;
        db = new DatabaseHandle();

        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        initView();


    }

    private void unregister() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scroll_broadcast);
        scroll_broadcast = null;
    }

    private void initView() {
        title = (TextView)findViewById(R.id.title_main) ;
        arr = new ArrayList<>();
        arr_tp = new ArrayList<>();
        edSearch = (EditText)findViewById(R.id.ed_search);
        rl_search = (RelativeLayout)findViewById(R.id.rl_search);
        appbar = findViewById(R.id.app_bar);
        img_close = (ImageView)findViewById(R.id.img_close);
        fillter = findViewById(R.id.fb_fill);
        fillter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        fillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFillter();
            }
        });
                r = getResources();
        nav = findViewById(R.id.nav_view);
        cv = (CardView)findViewById(R.id.cv_bot);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
         headerview = nav.getHeaderView(0);
        avatar = headerview.findViewById(R.id.img_avt);
        avatar2 = headerview.findViewById(R.id.img_avtbg);
        header_background = headerview.findViewById(R.id.header_bg);



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
        img_close.setOnClickListener(this);
        ln_pharma.setOnClickListener(this);
        ln_meo.setOnClickListener(this);
        avatar.setOnClickListener(this);
      //  Picasso.with(getApplicationContext()).load(R.drawable.img_avt).into(Utils.setCiclerImage(avatar));

        nav.setNavigationItemSelectedListener(this);

        imgnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        if(!Utils.checkNetwork(Common.context)){
            Utils.dialogNotif(getResources().getString(R.string.no_internet));
        }

        ln_pill.performClick();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);

        this.menu = menu;
        menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                    title.setVisibility(View.GONE);
                    rl_search.setVisibility(View.VISIBLE);
                    edSearch.setText(null);

                break;

            case R.id.close:
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                edSearch.setText(null);
                break;
        }
        return true;
    }
    public void ReplaceFrag(Class fragmentClass){
        new AsyncTask<Void,Void,Fragment>(){

            @Override
            protected Fragment doInBackground(Void... voids) {
                Fragment fragment = null;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commitAllowingStateLoss();
                return fragment;
            }

            @Override
            protected void onPostExecute(Fragment fragment) {

                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawers();
                }
                super.onPostExecute(fragment);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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


                title.setText(getResources().getString(R.string.search_pill));

                fragment = Pill_Fragment.class;
                if(menu!=null){
                    menu.getItem(0).setVisible(true);
                    menu.getItem(1).setVisible(false);
                }
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);

                fillter.show();
                ReplaceFrag(fragment);
                changeColor();
                tv_pill.setTextColor(r.getColor(R.color.blue));
                img_pill.setImageDrawable(r.getDrawable(R.drawable.pill_blue));
                break;
            case R.id.ln_sick:

                title.setText(getResources().getString(R.string.search_sick));

                fragment = Sick_Fragment.class;
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
               fillter.hide();
                ReplaceFrag(fragment);
                changeColor();
                tv_sick.setTextColor(r.getColor(R.color.blue));
                img_sick.setImageDrawable(r.getDrawable(R.drawable.sick));
                break;
            case R.id.ln_dr:

                title.setText(getResources().getString(R.string.search_dr));

                fragment = Dr_Fragment.class;
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                fillter.hide();
                ReplaceFrag(fragment);
                changeColor();
                tv_dr.setTextColor(r.getColor(R.color.blue));
                img_dr.setImageDrawable(r.getDrawable(R.drawable.dr_nghe_blue));
                break;
            case R.id.ln_pharma:

                title.setText(getResources().getString(R.string.search_pharma));

                fragment = Pharma_Fragment.class;
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                fillter.hide();
                ReplaceFrag(fragment);
                changeColor();
                tv_pharma.setTextColor(r.getColor(R.color.blue));
                img_pharma.setImageDrawable(r.getDrawable(R.drawable.pharmablue));
                break;
            case R.id.ln_meo:

                title.setText(getResources().getString(R.string.meo));

                fragment = Meo_Fragment.class;
                menu.getItem(1).setVisible(false);
                menu.getItem(0).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                fillter.hide();
                ReplaceFrag(fragment);
                changeColor();
                tv_meo.setTextColor(r.getColor(R.color.blue));
                img_meo.setImageDrawable(r.getDrawable(R.drawable.news_blue));
                break;
            case R.id.img_avt:
                Intent it = new Intent(Common.context, Infor_User.class);
                startActivity(it);
                break;

            case R.id.img_close:
                if(rl_search.getVisibility()==View.VISIBLE&&title.getVisibility()==View.GONE){
                    rl_search.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }
                break;
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.catalo_pills:
                ln_pill.performClick();
                break;
            case R.id.catalo_sick:
                ln_sick.performClick();
                break;
            case R.id.find_pharma:
                ln_pharma.performClick();
                break;
            case R.id.catalo_dr:
                ln_dr.performClick();
                break;
            case R.id.news:
                ln_meo.performClick();
                break;
            case R.id.sub_pill:
                Intent it2 = new Intent(getApplicationContext(),Care_Activity.class);
                it2.putExtra("key","pill");
                startActivity(it2);
                break;
            case R.id.sub_sick:
                Intent it3 = new Intent(getApplicationContext(),Care_Activity.class);
                it3.putExtra("key","sick");
                startActivity(it3);
                break;
            case R.id.log_out:
                if(Utils.isLogin()){
                    db.clearUserData();
                }
                Intent it4 = new Intent(getApplicationContext(),Login.class);
                startActivity(it4);
                break;
            case R.id.rate:
                showDialogRateApp();
                break;
            case R.id.share:
                Intent it5 = new Intent(getApplicationContext(),Share.class);
                startActivity(it5);
                break;

        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();


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

    @Override
    protected void onStop() {
        super.onStop();
        unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
        if(Utils.isLogin()){
            Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new TransImage()).into(avatar);
            Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avatar2);
            Picasso.with(getApplicationContext()).load(R.drawable.img_avt).transform(new BlurImagePicasso()).into(header_background);
        }else{
            Picasso.with(getApplicationContext()).load(R.drawable.laucher_white_1).transform(new TransImage()).into(avatar);
            Picasso.with(getApplicationContext()).load(R.drawable.white).transform(new TransImage()).into(avatar2);
            Picasso.with(getApplicationContext()).load(R.color.white).transform(new BlurImagePicasso()).into(header_background);
        }
    }

    private void showDialogFillter() {
        arr.clear();
        arr_tp.clear();
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fillter);
        dialog.setCanceledOnTouchOutside(true);
        Spinner sp_sick = dialog.findViewById(R.id.spin_sick);
        Spinner sp_tp = dialog.findViewById(R.id.spin_tp);
        TextView tv_price = dialog.findViewById(R.id.tv_price);
        AppCompatSeekBar seek = dialog.findViewById(R.id.seek_bar);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_price.setText("Giá: "+progress+" - 200.000đ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(!db.isCataloPillEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_PILL);
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                arr.add(list.get(i).getName());
            }

        }
        if(!db.isCataloPillEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_PILL_INTRO);
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                arr_tp.add(list.get(i).getName());
            }

        }



        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arr);
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);
        sp_sick.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter2 =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arr_tp);
        dataAdapter2.setDropDownViewResource(R.layout.custom_text_spiner);
        sp_tp.setAdapter(dataAdapter2);



        dialog.show();
    }

    private void showDialogRateApp() {
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate_app);
        dialog.setCanceledOnTouchOutside(true);
//        TextView title = dialog.findViewById(R.id.txt_title_rate);
//        if(Detail.key.equals("pill")){
//            title.setText(getResources().getString(R.string.title_rate_pill));
//        }else if(Detail.key.equals("pharma")){
//            title.setText(getResources().getString(R.string.title_rate_pharma));
//        }else if(Detail.key.equals("sick")){
//            title.setText(getResources().getString(R.string.title_rate_sick));
//        }
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }

    class GetScrollBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null){
                int action = intent.getIntExtra("action",1);
//                if(action==Constant.ACTION_DOWN){
//                    final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
//                    cv.setAnimation(myAnim);
//                   //     cv.setVisibility(View.GONE);
//                        fillter.hide();
//
//                }else{
//
//                        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
//                        cv.setAnimation(myAnim);
//                        //    cv.setVisibility(View.VISIBLE);
//                        if(fragment==Pill_Fragment.class){
//                            fillter.show();
//                        }else{
//                            fillter.hide();
//                        }
//                        isAnimated = true;
//                    }



            }
        }
    }
}
