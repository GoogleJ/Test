package com.zxjk.duoduo.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.weight.PayPsdInputView;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SetUpPaymentPwdFragment extends BaseFragment {

    PayPsdInputView payPsdInputView;
    TitleBar titleBar;

    public static SetUpPaymentPwdFragment newInstance(){
        SetUpPaymentPwdFragment fragment=new SetUpPaymentPwdFragment();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payPsdInputView.setComparePassword( new PayPsdInputView.onPasswordListener() {

            @Override
            public void onDifference(String oldPsd, String newPsd) {
                // TODO: 2018/1/22  和上次输入的密码不一致  做相应的业务逻辑处理
//                Toast.makeText(getContext(), "两次密码输入不同" + oldPsd + "!=" + newPsd, Toast.LENGTH_SHORT).show();
//                payPsdInputView.cleanPsd();
            }


            @Override
            public void onEqual(String psd) {
                // TODO: 2017/5/7 两次输入密码相同，那就去进行支付楼
                Toast.makeText(getContext(), "密码相同" + psd, Toast.LENGTH_SHORT).show();
//                payPsdInputView.setComparePassword("");
//                payPsdInputView.cleanPsd();
            }

            @Override
            public void inputFinished(String inputPsd) {
                // TODO: 2018/1/3 输完逻辑
                Toast.makeText(getContext(), "输入完毕：" + inputPsd, Toast.LENGTH_SHORT).show();
                payPsdInputView.setComparePassword(inputPsd);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_set_payment_pwd,null);
        initUI(view);

        return view;

    }

    private void initUI(View view) {
        payPsdInputView=view.findViewById(R.id.m_set_payment_pwd_edit);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }



}
