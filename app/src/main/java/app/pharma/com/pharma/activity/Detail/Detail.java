package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import app.pharma.com.pharma.Fragment.Pharma_Detail_Fragment;
import app.pharma.com.pharma.R;

public class Detail extends AppCompatActivity {
    Class fragment;
    FrameLayout fragDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        fragment = Pharma_Detail_Fragment.class;
        fragDetail = findViewById(R.id.frag_detail);
        ReplaceFrag(fragment);
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
}
