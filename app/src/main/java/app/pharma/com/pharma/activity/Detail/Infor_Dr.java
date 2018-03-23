package app.pharma.com.pharma.activity.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Infor_Dr extends AppCompatActivity {
    LinearLayout ln_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__dr);
        Common.context = this;
        ln_list = findViewById(R.id.ln_dr_inf);
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 10; i++){
            final View rowView = inflater2.inflate(R.layout.item_infor, null);
            ln_list.addView(rowView);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
