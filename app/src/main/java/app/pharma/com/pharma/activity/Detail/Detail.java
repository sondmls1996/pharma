package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Fragment.Pharma.Pharma_Detail_Fragment;
import app.pharma.com.pharma.Fragment.Pharma.Pharma_Detail_Rate;
import app.pharma.com.pharma.Fragment.Pill.Pill_Fragment_Detail;
import app.pharma.com.pharma.Fragment.Sick.Sick_Detail_Fragment;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.R;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    Class fragment;
    FrameLayout fragDetail;
    public static String headerJson = "";
    public static String key = "";
    public static String id = "";
    public static Object headerObj;
    public static ArrayList<String> imagesArray;
    Pill_Constructor pillConstructor;
    Sick_Construct sickConstructor;
    Pharma_Constructor pharmaConstructor;
    TextView tv_left, tv_right;
    TextView tvTitle;
    RelativeLayout imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent it = getIntent();
        if(it.getExtras()!=null){
            key = it.getExtras().getString("key");
            id = it.getExtras().getString("id");

        }
        headerJson = "";
        imagesArray = new ArrayList<>();

        Common.context = this;
        tvTitle = (TextView)findViewById(R.id.title);
        imgBack = findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.detail_infor));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fragDetail = findViewById(R.id.frag_detail);
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        changeTextLeft();
        tv_left.performClick();
    }

    private void changeTextLeft() {
        if(key.equals("pharma")){
            tv_left.setText(getResources().getString(R.string.inf_pharma));
        }else if(key.equals("pill")){
            tv_left.setText(getResources().getString(R.string.inf_pill));
        }else{
            tv_left.setText(getResources().getString(R.string.inf_sick));
        }
    }

    public void ReplaceFrag(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frag_detail, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:

                    changeColor();
                    tv_left.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_left.setTextColor(getResources().getColor(R.color.blue));
                    if(key.equals("pill")){
                        fragment = Pill_Fragment_Detail.class;
                    }else if(key.equals("pharma")){
                        fragment = Pharma_Detail_Fragment.class;
                    }else if (key.equals("sick")){
                        fragment = Sick_Detail_Fragment.class;
                    }


                    ReplaceFrag(fragment);


                break;
            case R.id.tv_right:
                if(headerObj!=null) {
                    changeColor();
                    tv_right.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_right.setTextColor(getResources().getColor(R.color.blue));
                    fragment = Pharma_Detail_Rate.class;

                    ReplaceFrag(fragment);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }

    public void changeColor(){
        tv_left.setTextColor(getResources().getColor(R.color.gray));
        tv_right.setTextColor(getResources().getColor(R.color.gray));
        tv_left.setBackgroundColor(getResources().getColor(R.color.light_gray));
        tv_right.setBackgroundColor(getResources().getColor(R.color.light_gray));
    }
}
