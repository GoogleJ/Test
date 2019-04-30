package com.zxjk.duoduo.ui.minepage;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * @author Administrator
 */
@SuppressLint("JavascriptInterface")
public class OnlineServiceActivity extends BaseActivity {
    WebView webView;
    String url;
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_service);
        webView = findViewById(R.id.web_view);
        tvTitle = findViewById(R.id.tvTitle);
        url = getIntent().getStringExtra("url");
        if (url != null) {
            tvTitle.setText(R.string.user_agreement);
        }
        initSetting();
    }

    private void initSetting() {
        webView.loadUrl(url == null ? Constant.currentUser.getOnlineService() : url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
