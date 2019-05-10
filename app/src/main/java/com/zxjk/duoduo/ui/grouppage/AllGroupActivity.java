package com.zxjk.duoduo.ui.grouppage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.grouppage.adapter.AllGroupAdapter;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author L
 * create at 2019/5/7
 * description: 一起游戏
 */
@SuppressLint("SetTextI18n")
public class AllGroupActivity extends BaseActivity {

    private EditText etSearch;
    private RecyclerView recycler;
    private AllGroupAdapter adapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.game_together));
        findViewById(R.id.rl_back).setOnClickListener(v -> finish());

        etSearch = findViewById(R.id.etSearch);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllGroupAdapter();
        recycler.setAdapter(adapter);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .getAllPlayGroup(Constant.userId)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(response -> {
                    List<GetAllPlayGroupResponse.GroupListBean> groupList = new ArrayList<>();
                    groupList.addAll(response.getGroupList());
                    for (GetAllPlayGroupResponse.GroupListBean bean : response.getGroupList()) {
                        for (String groupid : response.getJoin()) {
                            if (groupid.equals(bean.getId())) {
                                groupList.remove(bean);
                                bean.setHasJoined(true);
                                groupList.add(0, bean);
                            }
                        }
                    }
                    adapter.setData(groupList);
                    adapter.setOnItemClickLitener(position -> {
                        GetAllPlayGroupResponse.GroupListBean groupListBean = groupList.get(position);
                        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog1).setConvertListener(new ViewConvertListener() {

                            @Override
                            protected void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                                TextView tv_title = viewHolder.getView(R.id.tv_title);
                                TextView tv_content = viewHolder.getView(R.id.tv_content);
                                TextView tv_notarize = viewHolder.getView(R.id.tv_notarize);
                                tv_title.setText(getString(R.string.hint_duoduo_id));
                                tv_content.setText(getString(R.string.hint_duoduo_id2) + " " + groupListBean.getDuoduoId());
                                tv_notarize.setText(getString(R.string.fuzhi_duoduo_id));
                                viewHolder.getView(R.id.tv_notarize).setOnClickListener(v -> {
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    cm.setText(groupListBean.getDuoduoId());
                                    ToastUtils.showShort(getString(R.string.duplicated_to_clipboard));
                                    baseNiceDialog.dismiss();

                                });
                            }
                        }).setDimAmount(0.5f)
                                .setOutCancel(true)
                                .show(getSupportFragmentManager());
                    });
                }, this::handleApiError);


    }


}
