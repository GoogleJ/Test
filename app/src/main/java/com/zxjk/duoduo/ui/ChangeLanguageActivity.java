package com.zxjk.duoduo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 这个是语言算则的activity
 * @author Administrator
 */
@SuppressWarnings("ALL")
public class ChangeLanguageActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.checkbox_zh_cn)
    ImageView checkBoxZhCn;

    @BindView(R.id.checkbox_zh_tw)
    ImageView checkBoxZhTw;

    @BindView(R.id.checkbox_en_us)
    ImageView checkBoxEnUs;

    int type=0;


    public static void start(Activity activity) {
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
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @OnClick({R.id.checkbox_zh_cn,R.id.checkbox_zh_tw,R.id.checkbox_en_us})
    @Override
    public void onClick(View v) {

        boolean isTrue=true;

        switch (v.getId()){
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
