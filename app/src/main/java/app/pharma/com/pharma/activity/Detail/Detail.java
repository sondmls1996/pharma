package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.pharma.com.pharma.Fragment.Pharma_Detail_Fragment;
import app.pharma.com.pharma.Fragment.Pharma_Rate;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    Class fragment;
    FrameLayout fragDetail;
    TextView tv_left, tv_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Common.context = this;
        fragDetail = findViewById(R.id.frag_detail);
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        tv_left.performClick();
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
                fragment = Pharma_Detail_Fragment.class;

                ReplaceFrag(fragment);
                break;
            case R.id.tv_right:
                changeColor();
                tv_right.setBackgroundColor(getResources().getColor(R.color.white));
                tv_right.setTextColor(getResources().getColor(R.color.blue));
                fragment = Pharma_Rate.class;

                ReplaceFrag(fragment);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }

    public void changeColor(){
        tv_left.setTextColor(getResources().getColor(R.color.black));
        tv_right.setTextColor(getResources().getColor(R.color.black));
        tv_left.setBackgroundColor(getResources().getColor(R.color.light_gray));
        tv_right.setBackgroundColor(getResources().getColor(R.color.light_gray));
    }
}
