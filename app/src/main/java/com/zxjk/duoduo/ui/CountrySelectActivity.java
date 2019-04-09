package com.zxjk.duoduo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.ui.adapter.CountryCodeAdapter;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.utils.CountryCodeConstantsUtils;
import com.zxjk.duoduo.utils.DensityUtils;
import com.zxjk.duoduo.weight.LetterIndexView;
import com.zxjk.duoduo.weight.LivIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import butterknife.BindView;

/**
 * Demo class
 * 这个是国家代码选择的类
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class CountrySelectActivity extends BaseActivity implements View.OnClickListener {

    ListView lv_list;
    ArrayList<CountryEntity> allCountryCodeList;
    ArrayList<CountryEntity> countryCodeList;
    CountryCodeAdapter mCountryCodeAdapter;
    //
    private LivIndex litterIdx;
    //
    SearchView searchView;
    TextView tv_search_cancel;
    private static final String EXTRA_DATA="data";

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CountrySelectActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_select);
        lv_list = findViewById(R.id.lv_list);
        setHeadTitle(getString(R.string.country_select_head));
        initList();
        initSearchView();
        buildLitterIdx();
    }

    private void initList() {
        allCountryCodeList = CountryCodeConstantsUtils.getCountryList(getApplicationContext());
        Collections.sort(allCountryCodeList, comparator);//排序
        countryCodeList = (ArrayList<CountryEntity>) allCountryCodeList.clone();
        //
        mCountryCodeAdapter = new CountryCodeAdapter(getApplicationContext(), countryCodeList);
        lv_list.setAdapter(mCountryCodeAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryEntity countryEntity = (CountryEntity) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA, countryEntity);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void buildLitterIdx() {
        LetterIndexView livIndex = findViewById(R.id.liv_index);
        livIndex.setNormalColor(getResources().getColor(R.color.contacts_letters_color));
        TextView litterHit = findViewById(R.id.tv_hit_letter);
        litterIdx = mCountryCodeAdapter.createLivIndex(lv_list, livIndex, litterHit);
        litterIdx.show();
    }

    Comparator<CountryEntity> comparator = new Comparator<CountryEntity>() {
        @Override
        public int compare(CountryEntity o1, CountryEntity o2) {
            return o1.pinyin.compareTo(o2.pinyin);
        }
    };

    private void initSearchView() {
        searchView = findViewById(R.id.mSearchView);
//        searchView.setIconified(false);//是否一开始就处于显示SearchView的状态
        searchView.setIconifiedByDefault(true);//是否可以隐藏
        searchView.setQueryHint(getString(R.string.contact_search));
//        searchView.set
//        final int closeImgId = getResources().getIdentifier("search_close_btn", "id", getPackageName());
        ImageView search_button = searchView.findViewById(R.id.search_button);

        search_button.setImageResource(R.drawable.ic_search);
        //
        ImageView search_mag_icon = searchView.findViewById(R.id.search_mag_icon);
        search_mag_icon.setImageResource(R.drawable.ic_search);
        search_mag_icon.setVisibility(View.GONE);
        //删除按钮
        ImageView closeImg = searchView.findViewById(R.id.search_close_btn);
        closeImg.setImageResource(R.drawable.icon_edit_delete);
        //输入框
        SearchView.SearchAutoComplete mEditSearchView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        mEditSearchView.setTextColor(Color.WHITE);
        mEditSearchView.setHintTextColor(Color.GRAY);
        //提示
        LinearLayout tipLayout = searchView.findViewById(R.id.search_edit_frame);
        //搜索按钮
        ImageView icTip = searchView.findViewById(R.id.search_mag_icon);
        icTip.setImageResource(R.drawable.ic_search);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showSearchView(false);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchView(true);
            }
        });
        //文本内容监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchKey(newText);
                return true;
            }
        });
        //
        tv_search_cancel = findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);
    }

    public void searchKey(String key) {
        countryCodeList.clear();
        if (TextUtils.isEmpty(key)) {
            countryCodeList.addAll(allCountryCodeList);
            mCountryCodeAdapter.notifyDataSetChanged();
            return;
        }
//        mClubAdapter.setKeyWord(key);
        for (CountryEntity countryEntity : allCountryCodeList) {
            if (countryEntity.countryName.contains(key) || countryEntity.countryCode.contains(key)) {
                countryCodeList.add(countryEntity);
            }
        }
        mCountryCodeAdapter.notifyDataSetChanged();
    }

    public void showSearchView(boolean show) {
        if (show) {
            findViewById(R.id.rl_head_normal).setVisibility(View.GONE);
            tv_search_cancel.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_head_normal).setVisibility(View.VISIBLE);
            tv_search_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                searchView.setQuery("", true);
                searchView.setIconified(true);
                showKeyboard(false);
                break;
        }
    }

    public void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    public void setHeadTitle(int resId) {
        ((TextView) findViewById(R.id.tv_head_title)).setText(resId);
    }

    public void setHeadTitle(String title) {
        ((TextView) findViewById(R.id.tv_head_title)).setText(title);
    }

    public void setHeadStatusBar() {
        View view = findViewById(R.id.head_StatusBar);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        view.setLayoutParams(layoutParams);
    }

    protected int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public Button getLeftBtn() {
        return ((Button) findViewById(R.id.btn_head_left));
    }

    public Button getLeftBtn(int width, int height) {
        Button leftView = getLeftBtn();
        ViewGroup.LayoutParams leftViewLayoutParams = leftView.getLayoutParams();
        leftViewLayoutParams.width = DensityUtils.dip2px(this, width);
        leftViewLayoutParams.height = DensityUtils.dip2px(this, height);
        leftView.setLayoutParams(leftViewLayoutParams);
        return leftView;
    }

    public Button getRightBtn() {
        return ((Button) findViewById(R.id.btn_head_right));
    }

    public Button getRightBtn(int width, int height) {
        Button rightView = getRightBtn();
        ViewGroup.LayoutParams rightViewLayoutParams = rightView.getLayoutParams();
        rightViewLayoutParams.width = DensityUtils.dip2px(this, width);
        rightViewLayoutParams.height = DensityUtils.dip2px(this, height);
        rightView.setLayoutParams(rightViewLayoutParams);
        return rightView;
    }

}
