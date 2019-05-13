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

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.response.GetGameClassResponse;
import com.zxjk.duoduo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateGameGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String gameType = "1";
    private String playId = "";
    private String pumpingRate = "0.01";
    private String proportionOfFees;
    private String typeName;
    private String commission;

    private GetGameClassResponse response;
    private ArrayList<GetGameClassResponse.CommissionConfigBean> data = new ArrayList<>();
    private Context context;
    private boolean showFanYong = false;

    private OnCreateGameGroupClick onCreateGameGroupClick;

    public void setOnCreateGameGroupClick(OnCreateGameGroupClick onCreateGameGroupClick) {
        this.onCreateGameGroupClick = onCreateGameGroupClick;
    }

    public interface OnCreateGameGroupClick {
        void click(String gameType, String playId, String pumpingRate, String proportionOfFees, String typeName, String commission);
    }

    public CreateGameGroupAdapter(GetGameClassResponse response) {
        this.response = response;
        data.addAll(response.getCommissionConfig());
        proportionOfFees = response.getGroupClass().get(0).getGuaranteeFee();
        typeName = response.getGroupClass().get(0).getTypeName();
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


    CheckBox cbGame1;
    CheckBox cbGame2;
    CheckBox cbGame3;

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

            cbGame1 = itemView.findViewById(R.id.cbGame1);
            cbGame2 = itemView.findViewById(R.id.cbGame2);
            cbGame3 = itemView.findViewById(R.id.cbGame3);

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
                    playId = "";

                    cbGame1.setChecked(false);
                    cbGame2.setChecked(false);
                    cbGame3.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekbar.setProgress(0, true);
                    } else {
                        seekbar.setProgress(0);
                    }

                    if (realposition == 0) {
                        gameType = "1";
                        proportionOfFees = response.getGroupClass().get(0).getGuaranteeFee();
                        typeName = response.getGroupClass().get(0).getTypeName();

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
                        gameType = "2";
                        proportionOfFees = response.getGroupClass().get(1).getGuaranteeFee();
                        typeName = response.getGroupClass().get(1).getTypeName();

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
                        gameType = "3";
                        proportionOfFees = response.getGroupClass().get(2).getGuaranteeFee();
                        typeName = response.getGroupClass().get(2).getTypeName();

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

//            seekbar.setMax(40);
            seekbar.setMax(4);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    tvChoushui.setText((progress) / 10f + 1 + "%");
                    tvChoushui.setText(progress + 1 + "%");
                    pumpingRate = "0.0" + (progress + 1);
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
        private View iv;
        private TextView tvBannerTitle1;
        private TextView tvBannerTitle2;
        private TextView tvBannerTitle3;
        private TextView tvBannerTitle4;
        private TextView tvBannerTitle5;
        private TextView tvBannerMoney;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_create_gamegroup_banner, null);
            iv = inflate.findViewById(R.id.iv);

            tvBannerTitle1 = inflate.findViewById(R.id.tvBannerTitle1);
            tvBannerTitle2 = inflate.findViewById(R.id.tvBannerTitle2);
            tvBannerTitle3 = inflate.findViewById(R.id.tvBannerTitle3);
            tvBannerTitle4 = inflate.findViewById(R.id.tvBannerTitle4);
            tvBannerTitle5 = inflate.findViewById(R.id.tvBannerTitle5);
            tvBannerMoney = inflate.findViewById(R.id.tvBannerMoney);

            return inflate;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            int realPosition = position % 3;
            iv.setBackgroundResource(data);
            GetGameClassResponse.GroupClassBean groupClassBean = response.getGroupClass().get(realPosition);

            tvBannerTitle1.setText(groupClassBean.getTypeName());
            tvBannerTitle2.setText(groupClassBean.getEnglishName());
            tvBannerTitle3.setText("●  人数范围：" + groupClassBean.getNumberLimit() + "人");
            tvBannerTitle4.setText("●  下注范围：" + groupClassBean.getMinimumBetAmount() + "-" + groupClassBean.getMaximumBetAmount());
            tvBannerTitle5.setText("●  下分手续费：" + (int) (Float.parseFloat(groupClassBean.getProportionOfFees()) * 100) + "%");
            tvBannerMoney.setText(groupClassBean.getGuaranteeFee() + "HK");
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

            llAddFanYong.setOnClickListener(v -> {

            });
            llCommit.setOnClickListener(v -> {
                //创建游戏群
                if (!cbGame1.isChecked() && !cbGame2.isChecked() && !cbGame3.isChecked()) {
                    ToastUtils.showShort(R.string.select_game_type1);
                    return;
                }

                if (gameType.equals("1")) {
                    commission = "";
                    if (cbGame1.isChecked()) {
                        playId += "9,";
                    }
                    if (cbGame2.isChecked()) {
                        playId += "25,";
                    }
                    if (cbGame3.isChecked()) {
                        playId += "16,";
                    }
                } else if (gameType.equals("2")) {
                    commission = "";
                    if (cbGame1.isChecked()) {
                        playId += "91,";
                    }
                    if (cbGame2.isChecked()) {
                        playId += "107,";
                    }
                    if (cbGame3.isChecked()) {
                        playId += "98,";
                    }
                } else if (gameType.equals("3")) {
                    if (cbGame1.isChecked()) {
                        playId += "255,";
                    }
                    if (cbGame2.isChecked()) {
                        playId += "271,";
                    }
                    if (cbGame3.isChecked()) {
                        playId += "262,";
                    }
                    if (!showFanYong) {
                        commission = "";
                    } else {
                        List<GetGameClassResponse.CommissionConfigBean> temp = new ArrayList<>(response.getCommissionConfig().size());

                        for (int i = 0; i < data.size(); i++) {
                            GetGameClassResponse.CommissionConfigBean t = response.getCommissionConfig().get(i);
                            GetGameClassResponse.CommissionConfigBean commissionConfigBean = new GetGameClassResponse.CommissionConfigBean();
                            commissionConfigBean.setMax(t.getMax());
                            commissionConfigBean.setMin(t.getMin());
                            commissionConfigBean.setGrade(t.getGrade());
                            commissionConfigBean.setCommission(t.getCommission());
                            commissionConfigBean.setRemarks("");
                            temp.add(commissionConfigBean);
                        }

                        commission = GsonUtils.toJson(temp, false);
                    }
                }

                playId = playId.substring(0, playId.length() - 1);

                if (onCreateGameGroupClick != null) {
                    onCreateGameGroupClick.click(gameType, playId, pumpingRate, proportionOfFees, typeName, commission);
                    playId = "";
                }
            });
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

        private void bindData(GetGameClassResponse.CommissionConfigBean bean) {
            tv1.setText(bean.getGrade());
            String max = bean.getMax();
            if (Integer.parseInt(bean.getMax()) >= 10000) {
                max = (Integer.parseInt(bean.getMax()) / 10000) + "万";
            }
            tv2.setText(max);
            tv3.setText("每万返佣" + Float.parseFloat(bean.getCommission()) * 10000);
            if (height == 0) {
                height = CommonUtils.dip2px(context, 40);
            }
            ll.getLayoutParams().height = (showFanYong ? height : 0);
        }
    }
}