package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateGameGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Bean> data = new ArrayList<>();
    private Context context;
    private boolean showFanYong = false;

    public ArrayList<Bean> getData() {
        return data;
    }

    {
        Bean bean = new Bean();
        bean.setLevel("会员级");
        bean.setTotalAchievement("10万");
        bean.setPercent("每万返佣50");
        data.add(bean);
        data.add(bean);
        data.add(bean);
        data.add(bean);
        data.add(bean);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        if (viewType == 1) {
            return new ViewHolder1(LayoutInflater.from(context).inflate(R.layout.item_create_gamegroup1, parent, false));
        } else if (viewType == 2) {
            return new ViewHolder2(LayoutInflater.from(context).inflate(R.layout.item_create_gamegroup2, parent, false));
        } else {
            return new ViewHolder3(LayoutInflater.from(context).inflate(R.layout.item_create_gamegroup3, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 3) {
            ((ViewHolder3) holder).bindData(data.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else if (position == getItemCount() - 1) {
            return 2;
        } else {
            return 3;
        }
    }

    //上部分布局
    class ViewHolder1 extends RecyclerView.ViewHolder {
        ViewHolder1(@NonNull View itemView) {
            super(itemView);
            MZBannerView banner = itemView.findViewById(R.id.banner);
            // 设置数据
            banner.setPages(Arrays.asList(R.drawable.ic_create_gamegroup_banner1
                    , R.drawable.ic_create_gamegroup_banner2
                    , R.drawable.ic_create_gamegroup_banner3), BannerViewHolder::new);
            banner.setCanLoop(false);
            banner.setIndicatorVisible(false);

            ImageView ivGameModes = itemView.findViewById(R.id.ivGameModes);
            ImageView ivChouShui = itemView.findViewById(R.id.ivChouShui);

            CheckBox cbGame1 = itemView.findViewById(R.id.cbGame1);
            CheckBox cbGame2 = itemView.findViewById(R.id.cbGame2);
            CheckBox cbGame3 = itemView.findViewById(R.id.cbGame3);

            SeekBar seekbar = itemView.findViewById(R.id.seekbar);
            TextView tvChoushui = itemView.findViewById(R.id.tvChoushui);

            LinearLayout llFanyong1 = itemView.findViewById(R.id.llFanyong1);
            LinearLayout llFanyong2 = itemView.findViewById(R.id.llFanyong2);

            SwitchCompat switchFanyong = itemView.findViewById(R.id.switchFanyong);

            switchFanyong.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    showFanYong = true;
                    llFanyong2.setVisibility(View.VISIBLE);
                    llAddFanYong.setVisibility(View.VISIBLE);
                } else {
                    showFanYong = false;
                    llFanyong2.setVisibility(View.GONE);
                    llAddFanYong.setVisibility(View.GONE);
                }
                if (data.size() > 0) {
                    notifyItemRangeChanged(1, data.size());
                }
            });

            banner.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    int realposition = position % 3;

                    cbGame1.setChecked(false);
                    cbGame2.setChecked(false);
                    cbGame3.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekbar.setProgress(0, true);
                    } else {
                        seekbar.setProgress(0);
                    }

                    if (realposition == 0) {
                        showFanYong = false;
                        ivGameModes.setImageResource(R.drawable.ic_create_gamegroup_youxi);
                        ivChouShui.setImageResource(R.drawable.ic_create_gamegroup_choushui);
                        llFanyong1.setVisibility(View.GONE);
                        llFanyong2.setVisibility(View.GONE);
                        llAddFanYong.setVisibility(View.GONE);
                        cbGame1.setBackgroundResource(R.drawable.selector_create_gamegroup1);
                        cbGame2.setBackgroundResource(R.drawable.selector_create_gamegroup1);
                        cbGame3.setBackgroundResource(R.drawable.selector_create_gamegroup1);
                        llCommit.setBackgroundResource(R.drawable.shape_bac_create_gamegroup_commit1);
                    } else if (realposition == 1) {
                        showFanYong = false;
                        ivGameModes.setImageResource(R.drawable.ic_create_gamegroup_youxi1);
                        ivChouShui.setImageResource(R.drawable.ic_create_gamegroup_choushui1);
                        llFanyong1.setVisibility(View.GONE);
                        llFanyong2.setVisibility(View.GONE);
                        llAddFanYong.setVisibility(View.GONE);
                        cbGame1.setBackgroundResource(R.drawable.selector_create_gamegroup2);
                        cbGame2.setBackgroundResource(R.drawable.selector_create_gamegroup2);
                        cbGame3.setBackgroundResource(R.drawable.selector_create_gamegroup2);
                        llCommit.setBackgroundResource(R.drawable.shape_bac_create_gamegroup_commit2);
                    } else {
                        ivGameModes.setImageResource(R.drawable.ic_create_gamegroup_youxi2);
                        ivChouShui.setImageResource(R.drawable.ic_create_gamegroup_choushui2);
                        llFanyong1.setVisibility(View.VISIBLE);
                        cbGame1.setBackgroundResource(R.drawable.selector_create_gamegroup3);
                        cbGame2.setBackgroundResource(R.drawable.selector_create_gamegroup3);
                        cbGame3.setBackgroundResource(R.drawable.selector_create_gamegroup3);
                        llCommit.setBackgroundResource(R.drawable.shape_bac_create_gamegroup_commit3);
                        if (switchFanyong.isChecked()) {
                            llFanyong2.setVisibility(View.VISIBLE);
                            llAddFanYong.setVisibility(View.VISIBLE);
                            showFanYong = true;
                        } else {
                            llFanyong2.setVisibility(View.GONE);
                            llAddFanYong.setVisibility(View.GONE);
                            showFanYong = false;
                        }
                    }
                    if (data.size() > 0) {
                        notifyItemRangeChanged(1, data.size());
                    }
                }
            });

            seekbar.setMax(40);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvChoushui.setText((progress) / 10f + 1 + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_create_gamegroup_banner, null);
            mImageView = new ImageView(context);
            mImageView = inflate.findViewById(R.id.iv);
            return inflate;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            mImageView.setImageResource(data);
        }
    }

    //确认建群按钮和添加返佣按钮
    private LinearLayout llAddFanYong;
    private LinearLayout llCommit;

    class ViewHolder2 extends RecyclerView.ViewHolder {

        ViewHolder2(@NonNull View itemView) {
            super(itemView);
            llAddFanYong = itemView.findViewById(R.id.llAddFanYong);
            llCommit = itemView.findViewById(R.id.llCommit);
        }
    }

    //返佣设置form数据
    class ViewHolder3 extends RecyclerView.ViewHolder {
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tvDelete;
        private TextView tvModify;
        private LinearLayout ll;
        private int height;

        ViewHolder3(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvModify = itemView.findViewById(R.id.tvModify);
            ll = itemView.findViewById(R.id.ll);

            tvDelete.setOnClickListener(v -> {
                data.remove(getAdapterPosition() - 1);
                notifyItemRemoved(getAdapterPosition());
            });
            tvModify.setOnClickListener(v -> {

            });
        }

        private void bindData(Bean bean) {
            tv1.setText(bean.getLevel());
            tv2.setText(bean.getTotalAchievement());
            tv3.setText(bean.getPercent());
            if (height == 0) {
                height = CommonUtils.dip2px(context, 40);
            }
            ll.getLayoutParams().height = (showFanYong ? height : 0);
        }
    }

    class Bean {
        private String level;
        private String totalAchievement;
        private String percent;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getTotalAchievement() {
            return totalAchievement;
        }

        public void setTotalAchievement(String totalAchievement) {
            this.totalAchievement = totalAchievement;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }
    }

}
