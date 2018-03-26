package app.pharma.com.pharma.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView tv_reg;
    TextView tv_lostpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Common.context = this;
        tv_reg = findViewById(R.id.tv_register);
        tv_lostpass = findViewById(R.id.tv_lost_pass);
        tv_lostpass.setOnClickListener(this);
        tv_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                Intent it = new Intent(getApplicationContext(),Register.class);
                startActivity(it);
                break;
            case R.id.tv_lost_pass:
                Intent it2 = new Intent(getApplicationContext(),Lost_password.class);
                startActivity(it2);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
