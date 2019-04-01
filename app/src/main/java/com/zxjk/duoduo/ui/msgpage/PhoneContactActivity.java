package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.PhoneInfo;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.PhoneContactAdapter;
import com.zxjk.duoduo.ui.msgpage.utils.GetPhoneNumberFromMobileUtils;
import com.zxjk.duoduo.weight.TitleBar;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.zxjk.duoduo.utils.PermissionUtils.verifyStoragePermissions;

/**
 * @author Administrator
 * @// TODO: 2019\3\30 0030 添加手机联系人页面
 */
public class PhoneContactActivity extends BaseActivity {
    TitleBar titleBar;
    RecyclerView mRecyclerView;
    List<PhoneInfo> list=new ArrayList<PhoneInfo>();

    private GetPhoneNumberFromMobileUtils getPhoneNumberFromMobile;
    PhoneContactAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact);
        verifyStoragePermissions(this);
        titleBar=findViewById(R.id.title_bar);
        mRecyclerView=findViewById(R.id.phone_contact_recycler_view);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        getPhoneNumberFromMobile = new GetPhoneNumberFromMobileUtils();

        list = getPhoneNumberFromMobile.getPhoneNumberFromMobile(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new PhoneContactAdapter();
        mAdapter.setNewData(list);
        mRecyclerView.setAdapter(mAdapter);

    }
}
