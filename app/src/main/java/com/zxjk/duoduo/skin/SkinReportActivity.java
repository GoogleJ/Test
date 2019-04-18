package com.zxjk.duoduo.skin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;

import androidx.appcompat.app.AppCompatActivity;

public class SkinReportActivity extends AppCompatActivity {

    private EditText feedback_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_report);

        feedback_edit = findViewById(R.id.feedback_edit);
    }

    public void feedbackDetermine(View view) {
        if (TextUtils.isEmpty(feedback_edit.getText().toString().trim())) {
            ToastUtils.showShort(R.string.input_skin);
            return;
        }

        ToastUtils.showShort(R.string.report_success);

        finish();
    }

    public void returnBtn(View view) {
        finish();
    }
}
