package app.pharma.com.pharma.activity.Like;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.Like_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Like_Constructor;
import app.pharma.com.pharma.R;

public class Care_Activity extends AppCompatActivity {
    ListView lv;
    Like_Adapter adapter;
    ArrayList<Like_Constructor> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_);

        init();

    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_care));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lv = (ListView)findViewById(R.id.lv_like);
        arr = new ArrayList<>();
        adapter = new Like_Adapter(getApplicationContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Like_Constructor());
        arr.add(new Like_Constructor());
        arr.add(new Like_Constructor());
        arr.add(new Like_Constructor());
        arr.add(new Like_Constructor());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }
}
