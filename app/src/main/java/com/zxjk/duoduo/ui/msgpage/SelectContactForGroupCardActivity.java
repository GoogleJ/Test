package com.zxjk.duoduo.ui.msgpage;

import android.os.Bundle;
import android.widget.EditText;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.TitleBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 * @// TODO: 2019\4\9 0009 选择群组分享个人名片
 */
public class SelectContactForGroupCardActivity extends BaseActivity {
    RecyclerView card_recycler_view;
    TitleBar title_bar;
    EditText search_select_contact;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact_for_group_card);
        initUI();
    }

    private void initUI() {
        card_recycler_view=findViewById(R.id.card_recycler_view);
        title_bar=findViewById(R.id.title_bar);
        search_select_contact=findViewById(R.id.search_select_contact);
        title_bar.getLeftImageView().setOnClickListener(v -> finish());
    }
}
