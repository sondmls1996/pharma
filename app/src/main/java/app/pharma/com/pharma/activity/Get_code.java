package app.pharma.com.pharma.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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
            case R.id.btn_confirmcode:
                showDialogRate();
                break;
        }
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
