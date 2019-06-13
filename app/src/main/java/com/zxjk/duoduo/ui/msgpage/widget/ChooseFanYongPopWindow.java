package com.zxjk.duoduo.ui.msgpage.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.response.GetGameClassResponse;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ChooseFanYongPopWindow extends BasePopupWindow {

    private int checkPosition = -1;

    public interface OnClickListener {
        void onclick(GetGameClassResponse.CommissionConfigBean bean);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ChooseFanYongPopWindow(Context context, List<GetGameClassResponse.CommissionConfigBean> data) {
        super(context);

        RecyclerView recycler = findViewById(R.id.recycler);
        TextView tv_commit = findViewById(R.id.tv_commit);

        tv_commit.setOnClickListener(v -> {
            dismiss(true);
            if (onClickListener != null) {
                if (checkPosition == -1) {
                    return;
                }
                onClickListener.onclick(data.get(checkPosition));
            }
        });

        BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder> adapter = new BaseQuickAdapter<GetGameClassResponse.CommissionConfigBean, BaseViewHolder>
                (R.layout.activity_list_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, GetGameClassResponse.CommissionConfigBean item) {
                String max = item.getMax();
                if (Integer.parseInt(max) >= 10000) {
                    max = Integer.parseInt(max) / 10000 + "万";
                }

                String commission = "每万返佣" + Float.parseFloat(item.getCommission()) * 10000;

                helper.setText(R.id.tv1, item.getGrade())
                        .setText(R.id.tv2, max)
                        .setText(R.id.tv3, commission);

                helper.getView(R.id.iv).setVisibility(checkPosition == -1 ? View.INVISIBLE : (checkPosition == helper.getAdapterPosition() ? View.VISIBLE : View.INVISIBLE));
            }
        };

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(context));

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (checkPosition != -1) {
                adapter.notifyItemChanged(checkPosition);
            }
            checkPosition = position;
            adapter.notifyItemChanged(checkPosition);
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_choose_fanyong);
    }

}
