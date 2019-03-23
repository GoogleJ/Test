package com.zxjk.duoduo.ui.walletpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import razerdp.basepopup.BasePopupWindow;

public class ChooseCountWindow extends BasePopupWindow {
    ListView listview;
    List<String> data;

    private OnChooseCount onChooseCount;

    public void setOnChooseCount(OnChooseCount onChooseCount) {
        this.onChooseCount = onChooseCount;
    }

    public interface OnChooseCount {
        void chooseCount(String count);
    }

    public ChooseCountWindow(Context context) {
        super(context);
    }

    public void setData(List<String> data) {
        this.data = data;
        listview.setAdapter(new MyAdaper());
        listview.setOnItemClickListener((parent, view, position, id) -> {
            if (onChooseCount != null) {
                onChooseCount.chooseCount(data.get(position));
            }
        });
    }

    @Override
    public View onCreateContentView() {
        listview = (ListView) createPopupById(R.layout.window_choosecount);
        return listview;
    }

    class MyAdaper extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                int i = CommonUtils.dip2px(getContext(), 10);
                TextView textView = new TextView(getContext());
                textView.setTextSize(15);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textcolor1));
                if (position == 0) {
                    textView.setPadding(i * 2, 0, i * 2, i);
                }
                textView.setPadding(i * 2, 0, i * 2, 0);
                textView.setText(data.get(position));
                convertView = textView;
            }
            return convertView;
        }
    }
}
