package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.ToastUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020  发送验证
 */
public class VerificationFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.m_verification_title_bar)
    TitleBar titleBar;

    public static Fragment newInstance(){
        VerificationFragment fragment=new VerificationFragment();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_verification,null);
        ButterKnife.bind(this,view);
        initUI();
        return view;
    }
    protected void initUI() {
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }
    @OnClick({R.id.m_verification_icon,R.id.m_verification_send_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.m_verification_send_btn:
//                showToast("这里是发送按钮");
                QrCodeActivity.start(getActivity());

                break;
            case R.id.m_verification_icon:
                ToastUtils.showShort("此功能暂未实现'");

                break;
        }
    }


}
