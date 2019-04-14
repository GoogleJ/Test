package com.zxjk.duoduo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.CountryEntity;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.widget.LetterIndexView;
import com.zxjk.duoduo.ui.widget.SelectContryAdapter;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Demo class
 * 这个是国家代码选择的类
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class CountrySelectActivity extends BaseActivity {

    RecyclerView lv_list;
    ArrayList<CountryEntity> allCountryCodeList;
    private static final String EXTRA_DATA = "data";

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CountrySelectActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_select);
        lv_list = findViewById(R.id.lv_list);
        initList();
        buildLitterIdx();
    }

    private void initList() {
        SelectContryAdapter selectContryAdapter = new SelectContryAdapter();
        selectContryAdapter.setOnClickListener(entity -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, entity);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        lv_list.setLayoutManager(new LinearLayoutManager(this));
        lv_list.setAdapter(selectContryAdapter);
        allCountryCodeList = (ArrayList<CountryEntity>) getCountry();
        Collections.sort(allCountryCodeList, comparator);//排序
        selectContryAdapter.setData(allCountryCodeList);
    }

    private void buildLitterIdx() {
        LetterIndexView livIndex = findViewById(R.id.liv_index);
        livIndex.setNormalColor(getResources().getColor(R.color.contacts_letters_color));
        TextView litterHit = findViewById(R.id.tv_hit_letter);
        livIndex.setOnTouchingLetterChangedListener(new LetterIndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onHit(String letter) {
                lv_list.scrollToPosition(getScrollPosition(letter));
                litterHit.setVisibility(View.VISIBLE);
                litterHit.setText(letter);
            }

            @Override
            public void onCancel() {
                litterHit.setVisibility(View.GONE);
            }
        });
    }

    private int getScrollPosition(String letter) {
        for (int i = 0; i < allCountryCodeList.size(); i++) {
            if (allCountryCodeList.get(i).pinyin.equals(letter)) {
                return i;
            }
        }
        return -1;
    }

    Comparator<CountryEntity> comparator = (o1, o2) -> o1.pinyin.compareTo(o2.pinyin);

    private List<CountryEntity> getCountry() {
        String[] country = getResources().getStringArray(R.array.country);
        String[] countryCode = getResources().getStringArray(R.array.country_code);
        List<CountryEntity> entities = new ArrayList<>(country.length);
        for (int i = 0; i < country.length; i++) {
            CountryEntity countryEntity = new CountryEntity();
            countryEntity.countryName = country[i];
            countryEntity.countryCode = countryCode[i];
            countryEntity.pinyin = converterToFirstSpell(countryEntity.countryName);
            entities.add(countryEntity);
        }
        return entities;
    }

    public static String converterToFirstSpell(String chines) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char aNameChar : nameChar) {
            if (aNameChar > 128) {
                try {
                    pinyinName.append(PinyinHelper.toHanyuPinyinStringArray(
                            aNameChar, defaultFormat)[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(aNameChar);
            }
        }
        return pinyinName.toString().substring(0, 1);
    }

    public void back(View view) {
        finish();
    }

}
