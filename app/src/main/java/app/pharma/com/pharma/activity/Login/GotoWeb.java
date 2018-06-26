package app.pharma.com.pharma.activity.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Meo;

public class GotoWeb extends AppCompatActivity {
    WebView mWebView;
    String link;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_web);
        Common.context = this;
        utils = new Utils();
        TextView tvTitle = (TextView) findViewById(R.id.title);
        Intent it = getIntent();
        utils.showLoading(this, 20000, true);
        if (!TextUtils.isEmpty(Utils.getTerm())) {
            link = Utils.getTerm();
            RelativeLayout imgBack = findViewById(R.id.img_back);
            tvTitle.setText(getResources().getString(R.string.title_term));
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            mWebView = (WebView) findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {

                    utils.showLoading(GotoWeb.this, 20000, false);
                }
            });

            mWebView.loadUrl(link);
        }else{
            utils.showLoading(GotoWeb.this, 20000, false);
            Toast.makeText(getApplicationContext(),"Không kết nối được",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }
}

