package com.zxjk.duoduo.ui.msgpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.PhoneInfo;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.PhoneContactAdapter;
import com.zxjk.duoduo.ui.msgpage.utils.GetPhoneNumberFromMobileUtils;
import com.zxjk.duoduo.ui.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 * @// TODO: 2019\3\30 0030 添加手机联系人页面
 */
public class PhoneContactActivity extends BaseActivity implements TextWatcher {
    TitleBar titleBar;
    RecyclerView mRecyclerView;
    List<PhoneInfo> list = new ArrayList<PhoneInfo>();
    EditText searchEdit;

    private GetPhoneNumberFromMobileUtils getPhoneNumberFromMobile;
    PhoneContactAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contact);

        titleBar = findViewById(R.id.title_bar);
        mRecyclerView = findViewById(R.id.phone_contact_recycler_view);
        titleBar.getLeftImageView().setOnClickListener(v -> finish());
        getPhoneNumberFromMobile = new GetPhoneNumberFromMobileUtils();
        searchEdit = findViewById(R.id.search_edit);
        searchEdit.addTextChangedListener(this);
        list = getPhoneNumberFromMobile.getPhoneNumberFromMobile(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new PhoneContactAdapter();
        mAdapter.setNewData(list);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> sendSMS("您的好友通过多多社区给您留言了啦，赶快注册去查看吧", position));

    }

    private void sendSMS(String smsBody, int position) {
        Uri smsToUri = Uri.parse("smsto:" + list.get(position).getNumber());
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        startActivity(intent);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String groupname = s.toString();
        if (groupname != null || groupname.length() > 0) {
            List<PhoneInfo> groupnamelist = search(groupname); //查找对应的群组数据
            mAdapter.setNewData(groupnamelist);
        } else {
            mAdapter.setNewData(list);
        }
        mAdapter.notifyDataSetChanged();


    }

    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    private List<PhoneInfo> search(String str) {
        List<PhoneInfo> filterList = new ArrayList<PhoneInfo>();// 过滤后的list
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (PhoneInfo contact : list) {
                if (contact.getName() != null) {
                    if (contact.getName().contains(simpleStr) || contact.getName().contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (PhoneInfo contact : list) {
                if (contact.getName() != null) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    boolean isNameContains = contact.getName().toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE));

//                    boolean isSortKeyContains = contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isSimpleSpellContains = contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));
//
//                    boolean isWholeSpellContains = contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE));

                    if (isNameContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }

}
