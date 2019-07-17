package com.zxjk.duoduo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class ChangeLanguageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.checkbox_zh_cn)
    ImageView checkBoxZhCn;

    @BindView(R.id.checkbox_zh_tw)
    ImageView checkBoxZhTw;

    @BindView(R.id.checkbox_en_us)
    ImageView checkBoxEnUs;

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ChangeLanguageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.checkbox_zh_cn, R.id.checkbox_zh_tw, R.id.checkbox_en_us})
    @Override
    public void onClick(View v) {

        boolean isTrue = true;

        switch (v.getId()) {
            case R.id.checkbox_zh_cn:
                break;
            case R.id.checkbox_zh_tw:
                break;
            case R.id.checkbox_en_us:
                break;
            default:

                break;
        }

    }
}
