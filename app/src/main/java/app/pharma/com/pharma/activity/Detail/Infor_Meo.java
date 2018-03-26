package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Infor_Meo extends AppCompatActivity {
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__meo);
        Common.context = this;
        TextView tvTitle = (TextView)findViewById(R.id.title);
        ImageView imgBack = (ImageView)findViewById(R.id.img_back);
        tvTitle.setText(getResources().getString(R.string.title_meo));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.loadUrl("http://m.willowtreeapps.com/");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Common.context = this;
    }



}
