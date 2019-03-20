package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseFragment;
import com.zxjk.duoduo.ui.base.ContentActivity;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author Administrator
 * @// TODO: 2019\3\20 0020 个人信息页面，仿微信的
 */
public class PersonalInformationFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.m_information_title_bar)
    TitleBar titleBar;

    public static Fragment newInstance(){
        PersonalInformationFragment fragment=new PersonalInformationFragment();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_personal_information,null);
        ButterKnife.bind(this,view);
        initUI();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void initUI() {

        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.m_personal_information_add_contact_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_personal_information_add_contact_btn:

                Intent intent=new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("tag",1);
                startActivity(intent);

                break;
            default:
                break;
        }

    }

    public void getFriendInfoById(String friendId){
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendInfoById(friendId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver())
                .compose(RxSchedulers.normalTrans())
                .subscribe(new Consumer<FriendInfoResponse>() {
                    @Override
                    public void accept(FriendInfoResponse friendInfoResponse) throws Exception {


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d("DEBUG",throwable.getMessage());
                    }
                });
    }
}
