package com.zxjk.duoduo.ui.minepage;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\3\23 0023 意见反馈界面
 */
public class FeedbackActivity extends BaseActivity {
    EditText feedbackEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackEdit=findViewById(R.id.feedback_edit);
    }

    /**
     * 返回
     * @param view
     */
    public void returnBtn(View view){
        finish();
    }

    /**
     * 点击的确定按钮
     * @param view
     */
    public void feedbackDetermine(View view){
        if (feedbackEdit.getText().toString().isEmpty()){
            ToastUtils.showShort("请输入反馈的意见");
        }else{
            ToastUtils.showShort("提交成功");
        }
    }
}
