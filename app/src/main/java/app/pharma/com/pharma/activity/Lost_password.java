package app.pharma.com.pharma.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Lost_password extends AppCompatActivity implements View.OnClickListener {
    Button btn_getcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lost_password);
        Common.context = this;
        btn_getcode = findViewById(R.id.btn_getcode);
        btn_getcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getcode:
                Intent it = new Intent(Common.context,Get_code.class);
                startActivity(it);
                break;
        }
    }
}
