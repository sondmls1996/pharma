package app.pharma.com.pharma.activity.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Infor_Meo extends AppCompatActivity {
    WebView mWebView;
    String link;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__meo);
        Common.context = this;
        utils = new Utils();
        TextView tvTitle = (TextView)findViewById(R.id.title);
        Intent it = getIntent();
        utils.showLoading(this,20000,true);
        if(it.getExtras()!=null){
            link = it.getStringExtra("link");
            RelativeLayout imgBack = findViewById(R.id.img_back);
            tvTitle.setText(getResources().getString(R.string.title_meo));
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            mWebView = (WebView)findViewById(R.id.webview);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {

                    utils.showLoading(Infor_Meo.this,20000,false);
                }
            });

            mWebView.loadUrl(link);


        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }



}
