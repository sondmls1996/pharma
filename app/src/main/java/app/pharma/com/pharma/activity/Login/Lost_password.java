package app.pharma.com.pharma.activity.Login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Lost_password extends AppCompatActivity implements View.OnClickListener {
    Button btn_getcode;
    EditText edmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lost_password);
        init();

    }

    private void init() {
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        edmail = findViewById(R.id.ed_email);
        Utils.setCompondEdt(R.drawable.email,edmail);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_lost_pass));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();

    }
    private void showDialogRate() {
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_type_code);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
