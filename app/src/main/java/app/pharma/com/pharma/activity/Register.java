package app.pharma.com.pharma.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Common.context = this;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}
