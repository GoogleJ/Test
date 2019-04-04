package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.SelectForCardAdapter;
import com.zxjk.duoduo.weight.TitleBar;

import androidx.annotation.Nullable;

/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 选择联系人发送名片
 */
public class SelectContactForCardActivity extends BaseActivity {
    TitleBar titleBar;
    EditText searchEdit;

    SelectForCardAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_card);
        titleBar=findViewById(R.id.title_bar);
        searchEdit=findViewById(R.id.search_select_contact);

    }
}
