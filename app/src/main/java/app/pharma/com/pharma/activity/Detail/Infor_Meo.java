package app.pharma.com.pharma.activity.Detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

public class Infor_Meo extends AppCompatActivity {
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__meo);
        Common.context = this;
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
