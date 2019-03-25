package com.zxjk.duoduo.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @// TODO: 2019\3\25 0025 实名认证的dialog，用来选择证件类型 
 */
public class DocumentSelectionDialog extends Dialog {
    private View view;
    Context context;

    @BindView(R.id.id_card_icon)
    ImageView idCard;
    @BindView(R.id.passport_icon)
    ImageView passport;
    @BindView(R.id.other_identity_card_icon)
    ImageView otherIdentityCard;

    public DocumentSelectionDialog(@NonNull Context context) {
        super(context, R.style.dialogstyle);
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.dialog_document_selection,null);
        ButterKnife.bind(this,view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        initUI();
    }

    private void initUI() {
//        idCard.setImageDrawable(R.drawable.selector_document_selection);
    }
}
