package app.pharma.com.pharma.activity.Like;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.Like_Adapter;
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

        lv = (ListView)findViewById(R.id.lv_meo);
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
}
