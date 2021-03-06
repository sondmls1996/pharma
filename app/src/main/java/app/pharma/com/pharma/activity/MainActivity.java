package app.pharma.com.pharma.activity;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Pill_Adapter;
import app.pharma.com.pharma.Fragment.Dr.Dr_Fragment;
import app.pharma.com.pharma.Fragment.Meo_Fragment;
import app.pharma.com.pharma.Fragment.Pharma.Pharma_Fragment;
import app.pharma.com.pharma.Fragment.Pill.Pill_Fragment;
import app.pharma.com.pharma.Fragment.Sick.Sick_Fragment;
import app.pharma.com.pharma.Model.BlurImagePicasso;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.TransImage;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Service.GetLocationService;
import app.pharma.com.pharma.activity.Care.Care_Order;
import app.pharma.com.pharma.activity.Care.Care_PILL_Activity;
import app.pharma.com.pharma.activity.Care.Care_Pharma;
import app.pharma.com.pharma.activity.Care.Care_Sick_Activity;
import app.pharma.com.pharma.activity.Login.Login;
import app.pharma.com.pharma.activity.User.Infor_User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Class fragment;
    ImageView imgnav;
    ImageView img_pill;
    ImageView img_sick;
    Spinner spiner;
     String appPackageName;
    ImageView img_dr;
    ImageView img_pharma;
    ImageView header_background;
    ImageView img_meo;
    User user;
    List_Pill_Adapter adapterPillSearch;
    ArrayList<Pill_Constructor> arrPillSearch;
    boolean isAnimated = false;
    TextView tv_pill;
    TextView tv_sick;
    TextView tv_dr;
    TextView tv_pharma;
    TextView title;
    TextView tv_meo;
    EditText edSearch;
    RelativeLayout rl_type_search;
    TextView tv_null;
    DrawerLayout drawer;
    LinearLayout ln_pill;
    LinearLayout ln_sick;
    LinearLayout ln_dr;
    RelativeLayout rl_search;
    LinearLayout ln_pharma;
    int minPrice = 0,maxPrice = 0;
    int page = 1;
    DatabaseHandle db;
    long delay = 300 ; // 1 seconds after user stops typing
    long last_text_edit = 0;
    LinearLayout ln_meo;
    ListView lv_search;
    FrameLayout fragContrent;
    CardView cv;
    Handler handler;
    Menu menu;
    Resources r;
    View headerview;
    AppBarLayout appbar;
    TextView nav_name;
    NavigationView nav;
    ImageView img_close;
    MenuItem logoutItem;
    ImageView avatar,avatar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        Common.context = this;
        appPackageName = getApplicationContext().getPackageName();
        db = new DatabaseHandle();
        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        initView();

    }
    private void initView() {
        title = (TextView)findViewById(R.id.title_main) ;
        handler = new Handler();
        edSearch = (EditText)findViewById(R.id.ed_search);
        rl_search = (RelativeLayout)findViewById(R.id.rl_search);
        appbar = findViewById(R.id.app_bar);
        img_close = (ImageView)findViewById(R.id.img_close);
        lv_search = findViewById(R.id.lv_search);
        rl_type_search = findViewById(R.id.rl_list_search);
        tv_null = findViewById(R.id.tv_null_search);
        r = getResources();
        nav = findViewById(R.id.nav_view);
        cv = (CardView)findViewById(R.id.cv_bot);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
         headerview = nav.getHeaderView(0);
        avatar = headerview.findViewById(R.id.img_avt);
        avatar2 = headerview.findViewById(R.id.img_avtbg);
        header_background = headerview.findViewById(R.id.header_bg);
        nav_name = headerview.findViewById(R.id.nav_name);
        nav_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isLogin()){
                    Intent it = new Intent(getApplicationContext(),Login.class);
                    startActivity(it);
                }
            }
        });


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
      //  Picasso.get().load(R.drawable.img_avt).into(Utils.setCiclerImage(avatar));

        nav.setNavigationItemSelectedListener(this);
        Menu menuNav = nav.getMenu();
        logoutItem = menuNav.getItem(menuNav.size()-1);
        imgnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable s) {

                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);


            }
        });

        ln_pill.performClick();

    }
    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                getSearch(edSearch.getText().toString());
            }
        }
    };
    private void getSearch(String s) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(Constant.SEARCH_ACTION);
                it.putExtra("key",s);
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(it);
            }
        }, 200);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);

        this.menu = menu;
        menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }


    private void searchSick(String json,int page) {
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
                rl_type_search.setVisibility(View.GONE);

                hideKeyboard();

//                Intent it = new Intent(Constant.SEARCH_ACTION);
//                it.putExtra("key","");
//                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(it);
                Intent it = new Intent(Constant.CLOSE_SEARCH_ACTION);

                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(it);
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
                ReplaceFrag(fragment);
                changeColor();
                rl_type_search.setVisibility(View.GONE);
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

                ReplaceFrag(fragment);
                changeColor();
                rl_type_search.setVisibility(View.GONE);
                tv_sick.setTextColor(r.getColor(R.color.blue));
                img_sick.setImageDrawable(r.getDrawable(R.drawable.sick));
                break;
            case R.id.ln_dr:

                title.setText(getResources().getString(R.string.search_dr));

                fragment = Dr_Fragment.class;
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                title.setVisibility(View.VISIBLE);
                rl_type_search.setVisibility(View.GONE);
                rl_search.setVisibility(View.GONE);

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
                rl_type_search.setVisibility(View.GONE);
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

                ReplaceFrag(fragment);
                changeColor();
                rl_type_search.setVisibility(View.GONE);
                tv_meo.setTextColor(r.getColor(R.color.blue));
                img_meo.setImageDrawable(r.getDrawable(R.drawable.news_blue));
                break;
            case R.id.img_avt:
                Utils.setAlphalAnimation(view);
                if(Utils.isLogin()){
                    Intent it = new Intent(Common.context, Infor_User.class);
                    startActivity(it);
                }else{
                    Intent it = new Intent(Common.context, Login.class);
                    startActivity(it);
                }

                break;

            case R.id.img_close:
                if(rl_search.getVisibility()==View.VISIBLE&&title.getVisibility()==View.GONE){
                    rl_search.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    rl_type_search.setVisibility(View.GONE);
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
                if(Utils.isLogin()){
                    Intent it2 = new Intent(getApplicationContext(),Care_PILL_Activity.class);
                    it2.putExtra("key","pill");
                    startActivity(it2);
                }else{
                    Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                }

                break;

            case R.id.order:
                if(Utils.isLogin()){
                    Intent it = new Intent(getApplicationContext(), Care_Order.class);
                    startActivity(it);
                }else{
                    Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                }

                break;
            case R.id.pharma_care:
                if(Utils.isLogin()){
                    Intent it2 = new Intent(getApplicationContext(), Care_Pharma.class);
                    startActivity(it2);
                }else{
                    Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                }

                break;

            case R.id.sub_sick:
                if(Utils.isLogin()){
                    Intent it3 = new Intent(getApplicationContext(),Care_Sick_Activity.class);
                    it3.putExtra("key","sick");
                    startActivity(it3);
                }else{
                    Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                }

                break;
            case R.id.log_out:
                if(Utils.isLogin()){
                    Map<String,String> map = new HashMap<>();
                    map.put("accessToken",user.getToken());
                    Response.Listener<String> response = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE_LOGOUT",response);
                        }
                    };
                    Utils.PostServer(this,ServerPath.LOG_OUT,map,response);

                    db.clearUserData();
                    Utils.setLogin(false);

                }
                Intent it4 = new Intent(getApplicationContext(),Login.class);
                startActivity(it4);
                break;
            case R.id.rate:
                // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
              //  showDialogRateApp();
                break;
            case R.id.share:
                Utils.shareLink("https://play.google.com/store/apps/details?id=" + appPackageName);
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

        stopService(new Intent(this, GetLocationService.class));
        Log.d("STT","des");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
        if(Utils.isLogin()){


            db = new DatabaseHandle();
             user = db.getAllUserInfor();

            nav_name.setText(user.getName());

            logoutItem.setVisible(true);
            Picasso.get().load(ServerPath.ROOT_URL+user.getAvt()).transform(new TransImage()).into(avatar);
            Picasso.get().load(R.drawable.white).transform(new TransImage()).into(avatar2);
            Picasso.get().load(ServerPath.ROOT_URL+user.getAvt()).transform(new BlurImagePicasso()).into(header_background);
            checkLogin();
        }else{
            nav_name.setText("Đăng nhập");
            logoutItem.setVisible(false);
            Picasso.get().load(R.drawable.laucher_white_1).transform(new TransImage()).into(avatar);
            Picasso.get().load(R.drawable.white).transform(new TransImage()).into(avatar2);
            Picasso.get().load(R.color.white).transform(new BlurImagePicasso()).into(header_background);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            RequestPermission();
        } else {
            if(!Utils.isMyServiceRunning(GetLocationService.class)){
                startService(new Intent(this, GetLocationService.class));
            }

        }
        hideKeyboard();
    }

    private void checkLogin() {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken",user.getToken());
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString(JsonConstant.CODE);
                    switch (code){
                        case "-1":
                            Utils.ShowNotifString(getResources().getString(R.string.session_out), new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                                @Override
                                public void onClose(Dialog dialog) {
                                    db.clearUserData();
                                    Utils.setLogin(false);
                                    Intent it = new Intent(getApplicationContext(),Login.class);
                                    startActivity(it);
                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(this,ServerPath.USER_INFO,map,response);
    }

    public void RequestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        Constant.PERMISSION_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            if(!Utils.isMyServiceRunning(GetLocationService.class)){
                startService(new Intent(this, GetLocationService.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(!Utils.isMyServiceRunning(GetLocationService.class)){
                        startService(new Intent(this, GetLocationService.class));
                    }
                } else {
                    Utils.dialogNotif("Ứng dụng sẽ không hiển thị nha thuốc xung quanh nếu không được cấp quyền vị trí");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
