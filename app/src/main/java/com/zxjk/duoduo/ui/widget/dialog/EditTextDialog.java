package com.zxjk.duoduo.ui.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zxjk.duoduo.R;

/**
 * Created by Jinzhenghua on 2017/3/27.
 */

public class EditTextDialog {
    private AlertDialog dialog;
    private EditText et_edittext_text;
    private Button btn_edittext_yes;
    private Button btn_edittext_no;

    public void show(Context context, String title, String yes, String no) {
        View view = View.inflate(context, R.layout.dialog_edittext, null);

        TextView tv_edittext_title = view.findViewById(R.id.tv_edittext_title);
        et_edittext_text = view.findViewById(R.id.et_edittext_text);
        btn_edittext_yes = view.findViewById(R.id.btn_edittext_yes);
        btn_edittext_no = view.findViewById(R.id.btn_edittext_no);

        tv_edittext_title.setText(title);
        btn_edittext_no.setText(no);
        btn_edittext_yes.setText(yes);

        btn_edittext_no.setOnClickListener(v -> dialog.dismiss());

        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();
    }

    public String getText() {
        return et_edittext_text.getText().toString();
    }

    public EditText getEdit() {
        return et_edittext_text;
    }

    public Button getYes() {
        return btn_edittext_yes;
    }

    public Button getNo() {
        return btn_edittext_no;
    }

    public void setInputType(int type) {
        et_edittext_text.setInputType(type);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
