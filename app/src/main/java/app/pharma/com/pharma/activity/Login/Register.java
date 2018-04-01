package app.pharma.com.pharma.activity.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Register extends AppCompatActivity {
    EditText eduser,edEmail,edpass,edRepass,edBirth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Common.context = this;
        init();



    }

    private void init() {
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_register));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        eduser = findViewById(R.id.ed_user);
        edpass = findViewById(R.id.ed_pass);
        edRepass = findViewById(R.id.ed_repass);
        edEmail = findViewById(R.id.ed_email);
        edBirth = findViewById(R.id.ed_birth);

//        Utils.setCompondEdt(R.drawable.profile,eduser);
//        Utils.setCompondEdt(R.drawable.padlock,edpass);
//        Utils.setCompondEdt(R.drawable.padlock,edRepass);
//        Utils.setCompondEdt(R.drawable.email,edEmail);
//        Utils.setCompondEdt(R.drawable.calendar,edBirth);

//        eduser.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.profile)),null,null,null);
//        edpass.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.padlock)),null,null,null);
//        edRepass.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.padlock)),null,null,null);
//        edEmail.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.email)),null,null,null);
//        edBirth.setCompoundDrawables(Utils.setDrawableEdt(getResources().getDrawable(R.drawable.calendar)),null,null,null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
