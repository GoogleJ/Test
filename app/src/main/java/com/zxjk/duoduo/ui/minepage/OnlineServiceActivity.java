package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\30 0030 在线客服功能的制作
 */
@SuppressLint("JavascriptInterface")
public class OnlineServiceActivity extends BaseActivity {
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_service);
        webView=findViewById(R.id.web_view);
        initSetting();
    }
    private void initSetting() {
        webView.loadUrl(Constant.currentUser.getOnlineService());
        WebSettings settings = webView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //等待证书响应
                handler.proceed();
            }
        });
    }
}
