package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetGameClassResponse;

import razerdp.basepopup.BasePopupWindow;

public class ChooseFanYongPopWindow extends BasePopupWindow {

    private View tvTitle1;
    private View tvTitle2;
    private View tvTitle3;

    private View line1;
    private View line2;
    private View line3;

    private RecyclerView recycler1;
    private RecyclerView recycler2;
    private RecyclerView recycler3;

    //会员
    private int select1 = -1;
    //整线业绩
    private int select2 = -1;
    //提成比例
    private int select3 = -1;

    public interface OnClickListener {
        void onclick();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ChooseFanYongPopWindow(Context context, GetGameClassResponse data) {
        super(context);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                select1 = -1;
                select2 = -1;
                select3 = -1;
                recycler2.setVisibility(View.INVISIBLE);
                recycler3.setVisibility(View.INVISIBLE);
                tvTitle2.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                tvTitle3.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                recycler1.getAdapter().notifyDataSetChanged();
                recycler2.getAdapter().notifyDataSetChanged();
                recycler3.getAdapter().notifyDataSetChanged();
            }
        });

        findViewById(R.id.ivCommit).setOnClickListener(v -> {
            //确认
            if (select1 == -1 || select2 == -1 || select3 == -1) {
                ToastUtils.showShort(R.string.unselect);
                return;
            }
            dismiss(true);
            if (onClickListener != null) {
                onClickListener.onclick();
            }
        });

        tvTitle1 = findViewById(R.id.tvTitle1);
        tvTitle2 = findViewById(R.id.tvTitle2);
        tvTitle3 = findViewById(R.id.tvTitle3);

        line1 = ((ViewGroup) tvTitle1.getParent()).getChildAt(1);
        line2 = ((ViewGroup) tvTitle2.getParent()).getChildAt(1);
        line3 = ((ViewGroup) tvTitle3.getParent()).getChildAt(1);

        recycler1 = findViewById(R.id.recycler1);
        recycler2 = findViewById(R.id.recycler2);
        recycler3 = findViewById(R.id.recycler3);

        recycler1.setLayoutManager(new LinearLayoutManager(context));
        recycler2.setLayoutManager(new LinearLayoutManager(context));
        recycler3.setLayoutManager(new LinearLayoutManager(context));

        recycler1.setAdapter(new BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder>(R.layout.activity_list_item,
                data.getCommissionConfig()) {
            @Override
            protected void convert(BaseViewHolder helper, GetGameClassResponse.CommissionConfigBean item) {
                ((TextView) helper.getView(R.id.text1)).setText(item.getGrade());
                helper.getView(R.id.text1).setOnClickListener(v -> {
                    notifyItemChanged(select1);
                    select1 = helper.getAdapterPosition();
                    notifyItemChanged(select1);
                    if (line2.getVisibility() == View.INVISIBLE && line3.getVisibility() != View.VISIBLE) {
                        recycler2.setVisibility(View.VISIBLE);
                        line2.setVisibility(View.VISIBLE);
                        tvTitle2.setVisibility(View.VISIBLE);
                        line1.setVisibility(View.INVISIBLE);
                    }
                });

                TextPaint paint = ((TextView) helper.getView(R.id.text1)).getPaint();
                if (helper.getAdapterPosition() == select1) {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.black));
                    paint.setFakeBoldText(true);
                } else {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.textcolor2));
                    paint.setFakeBoldText(false);
                }
            }
        });

        recycler2.setAdapter(new BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder>(R.layout.activity_list_item,
                data.getCommissionConfig()) {
            @Override
            protected void convert(BaseViewHolder helper, GetGameClassResponse.CommissionConfigBean item) {
                String max = item.getMax();
                if (Integer.parseInt(max) >= 10000) {
                    max = Integer.parseInt(item.getMax()) / 10000 + "万";
                }
                ((TextView) helper.getView(R.id.text1)).setText(max);
                helper.getView(R.id.text1).setOnClickListener(v -> {
                    notifyItemChanged(select2);
                    select2 = helper.getAdapterPosition();
                    notifyItemChanged(select2);
                    if (line3.getVisibility() == View.INVISIBLE) {
                        recycler3.setVisibility(View.VISIBLE);
                        line3.setVisibility(View.VISIBLE);
                        tvTitle3.setVisibility(View.VISIBLE);
                        line2.setVisibility(View.INVISIBLE);
                    }
                });

                TextPaint paint = ((TextView) helper.getView(R.id.text1)).getPaint();
                if (helper.getAdapterPosition() == select2) {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.black));
                    paint.setFakeBoldText(true);
                } else {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.textcolor2));
                    paint.setFakeBoldText(false);
                }
            }
        });

        recycler3.setAdapter(new BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder>(R.layout.activity_list_item,
                data.getCommissionConfig()) {
            @Override
            protected void convert(BaseViewHolder helper, GetGameClassResponse.CommissionConfigBean item) {
                helper.setText(R.id.text1, "每万返佣" + Float.parseFloat(item.getCommission()) * 10000);

                helper.getView(R.id.text1).setOnClickListener(v -> {
                    notifyItemChanged(select3);
                    select3 = helper.getAdapterPosition();
                    notifyItemChanged(select3);
                });

                TextPaint paint = ((TextView) helper.getView(R.id.text1)).getPaint();
                if (helper.getAdapterPosition() == select3) {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.black));
                    paint.setFakeBoldText(true);
                } else {
                    ((TextView) helper.getView(R.id.text1)).setTextColor(ContextCompat.getColor(context, R.color.textcolor2));
                    paint.setFakeBoldText(false);
                }
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_choose_fanyong);
    }

}
