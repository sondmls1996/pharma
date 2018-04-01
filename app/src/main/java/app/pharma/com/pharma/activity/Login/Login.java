package app.pharma.com.pharma.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView tv_reg;
    TextView tv_lostpass;
    EditText eduser;
    EditText edpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {
        Common.context = this;
        tv_reg = findViewById(R.id.tv_register);
        tv_lostpass = findViewById(R.id.tv_lost_pass);
        eduser = findViewById(R.id.ed_user);
        edpass = findViewById(R.id.ed_pass);
//        Utils.setCompondEdt(R.drawable.profile,eduser);
//        Utils.setCompondEdt(R.drawable.padlock,edpass);
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
                Intent it2 = new Intent(getApplicationContext(),Get_code.class);
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
