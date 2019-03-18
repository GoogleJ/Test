package com.zxjk.duoduo.weight;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
/**
 * 国家代码的索引
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class LivIndex {
    public int scrollOffset = 0;
    private final ListView lvContacts;
    private final RecyclerView recyclerView;

    private final LetterIndexView livIndex;

    private final TextView lblLetterHit;
    // 字母:所在的行的index
    private final Map<String, Integer> mapABC;

    public LivIndex(RecyclerView recyclerView, ListView contactsListView, LetterIndexView letterIndexView, TextView letterHit, Map<String, Integer> abcMap) {
        this.recyclerView = recyclerView;
        this.lvContacts = contactsListView;
        this.livIndex = letterIndexView;
        this.lblLetterHit = letterHit;
        this.mapABC = abcMap;
        this.livIndex.setOnTouchingLetterChangedListener(new LetterChangedListener());
    }

    /**
     * 更新索引表
     * @param letters
     */
    public void updateLetters(String[] letters){
        livIndex.setLetters(letters);
    }
    /**
     * 显示
     */
    public void show() {
        this.livIndex.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    public void hide() {
        this.livIndex.setVisibility(View.GONE);
    }

    private class LetterChangedListener implements LetterIndexView.OnTouchingLetterChangedListener {

        @Override
        public void onHit(String letter) {
            lblLetterHit.setVisibility(View.VISIBLE);
            lblLetterHit.setText(letter);
            int index = -1;
            if ("↑".equals(letter)) {
                index = 0;
            } else if (mapABC.containsKey(letter)) {
                index = mapABC.get(letter);
            }
            if (index < 0) {
                return;
            }
            if (lvContacts != null) {
                index += lvContacts.getHeaderViewsCount();
                if (index < lvContacts.getCount()) {
                    lvContacts.setSelectionFromTop(index, 0);
                }
            }
            if (recyclerView != null) {
                if (index + scrollOffset < recyclerView.getAdapter().getItemCount() && index + scrollOffset < recyclerView.getLayoutManager().getItemCount()) {
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, index + scrollOffset);
                }
            }
        }

        @Override
        public void onCancel() {
            lblLetterHit.setVisibility(View.INVISIBLE);
        }
    }


}
