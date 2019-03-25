package com.zxjk.duoduo.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zxjk.duoduo.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * 支付宝或者微信的类型
 */
public class PaymentTypeDialog extends Dialog implements View.OnClickListener {
    private View view;
    Context context;

    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.edit_information)
    EditText editInformation;


    String wechat="1";
    String alipay="2";

    String editContent;

    public PaymentTypeDialog(@NonNull Context context) {

        super(context, R.style.dialogstyle);
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.dialog_payment_type,null);
        ButterKnife.bind(this,view);
    }
    public void show(String title,String type){
        show();
        dialogTitle.setText(title);
        if (wechat.equals(type)){
            //微信的
            editInformation.setHint(R.string.nick);
        }else if (alipay.equals(type)){
            //支付宝的
            editInformation.setHint(R.string.alipay_number);
            editInformation.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else{
            editInformation.setHint(R.string.bank_id_card_edit);
            editInformation.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);



    }


    @OnClick({R.id.cancel_btn,R.id.determine_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.determine_btn:
                if (onClickListener!=null){

                    onClickListener.determine(editInformation.getText().toString());
                }
                break;
                default:
                    dismiss();
                    break;
        }

    }

    public OnClickListener onClickListener;
    public interface OnClickListener{

        void determine(String editContent);
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
}
