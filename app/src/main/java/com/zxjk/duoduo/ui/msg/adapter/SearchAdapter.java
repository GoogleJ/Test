package com.zxjk.duoduo.ui.msg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zxjk.duoduo.R;
import com.zxjk.duoduo.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    /**
     * adapter传递过来的数据集合
     */
    private List<SearchBean> list = new ArrayList<>();
    /**
     * 需要改变颜色的text
     */
    private String text;
    /**
     * 属性动画
     */
//    private Animator animator;

    /**
     * 在MainActivity中设置text
     */
    public void setText(String text) {
        this.text = text;
    }

    public SearchAdapter(Context context, List<SearchBean> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        /**如果没有进行搜索操作或者搜索之后点击了删除按钮 我们会在MainActivity中把text置空并传递过来*/
        if (text != null) {
            //设置span
            SpannableString string = matcherSearchText(Color.rgb(255, 0, 0), list.get(position), text);
            holder.mTvText.setText(string);
        } else {
            holder.mTvText.setText(list.get(position)+"");
        }
        //属性动画
//        animator = AnimatorInflater.loadAnimator(context, R.animator.anim_set);
//        animator.setTarget(holder.mLlItem);
//        animator.start();
        //点击监听
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Recyclerview的点击监听接口
     */
    public interface onItemClickListener {
        void onClick(View view, int pos);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(SearchAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout mLlItem;
        //        private SimpleDraweeView mImgvSimple;
        private TextView mTvText;

        private TextView uuid;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLlItem = (ConstraintLayout) itemView.findViewById(R.id.m_item_search_layout);
//            mImgvSimple = (SimpleDraweeView) itemView.findViewById(R.id.imgv_simple);
            mTvText = (TextView) itemView.findViewById(R.id.m_item_search_text);
            uuid=(TextView)itemView.findViewById(R.id.m_item_search_dudu_id);

        }
    }

    /**
     * 正则匹配 返回值是一个SpannableString 即经过变色处理的数据
     */
    private SpannableString matcherSearchText(int color, SearchBean text, String keyword) {
        SpannableString spannableString = new SpannableString(text.getUserName());

        //条件 keyword
        Pattern pattern = Pattern.compile(keyword);
        //匹配
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            //ForegroundColorSpan 需要new 不然也只能是部分变色
            spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //返回变色处理的结果
        return spannableString;
    }

}
