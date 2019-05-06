package com.zxjk.duoduo.ui.minepage;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {
    EditText feedbackEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_end = findViewById(R.id.tv_end);
        feedbackEdit = findViewById(R.id.feedback_edit);
        tv_end.setVisibility(View.VISIBLE);
        tv_end.setText(getString(R.string.commit));
        tv_title.setText(getString(R.string.feedback_title));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());
        tv_end.setOnClickListener(v -> {
            if (feedbackEdit.getText().toString().isEmpty()) {
                ToastUtils.showShort(getString(R.string.please_enter_feedback_comments));
            } else {
                ToastUtils.showShort(getString(R.string.transfer_commit_successful));
                finish();
            }
        });

    }


}
