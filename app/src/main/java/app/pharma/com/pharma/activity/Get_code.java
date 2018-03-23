package app.pharma.com.pharma.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Get_code extends AppCompatActivity implements View.OnClickListener {
    Button get_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        Common.context = this;
        get_code = findViewById(R.id.btn_confirmcode);
        get_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getcode:
                Intent it = new Intent(Common.context,Login.class);
                startActivity(it);
                break;
        }
    }
}
