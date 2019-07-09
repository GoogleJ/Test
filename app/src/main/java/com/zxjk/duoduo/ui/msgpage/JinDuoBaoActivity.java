package com.zxjk.duoduo.ui.msgpage;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.bean.request.GroupGamebettingForDuobaoRequest;
import com.zxjk.duoduo.bean.response.DuobaoParameterResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.base.BaseActivity;
import com.zxjk.duoduo.ui.msgpage.adapter.ChineseZodiacAdapter;
import com.zxjk.duoduo.ui.widget.dialog.SelectPopupWindow;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.MD5Utils;
import com.zxjk.duoduo.utils.RecyclerItemAverageDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JinDuoBaoActivity extends BaseActivity implements SelectPopupWindow.OnPopWindowClickListener {
    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //期数
    @BindView(R.id.tvDuoBaoQiShu)
    TextView tvDuoBaoQiShu;
    //01-24
    @BindView(R.id.tvDuoBaoNum1)
    TextView tvDuoBaoNum1;
    //25-49
    @BindView(R.id.tvDuoBaoNum2)
    TextView tvDuoBaoNum2;
    //
    @BindView(R.id.recycler)
    RecyclerView recycler;
    //12生肖
    @BindView(R.id.recyclerViewZodiac)
    RecyclerView recyclerViewZodiac;
    //输入金额
    @BindView(R.id.tv_input_money)
    TextView tvInputMoney;
    //赔率
    @BindView(R.id.tv_odds)
    TextView tvOdds;
    //选中的号码数量
    @BindView(R.id.tv_db_number)
    TextView tvDbNumber;
    //下注总额
    @BindView(R.id.tv_db_totalMoney)
    TextView tvDbTotalMoney;
    //红波
    @BindView(R.id.tv_db_red)
    TextView tvDbRed;
    //篮波
    @BindView(R.id.tv_db_blue)
    TextView tvDbBlue;
    //绿波
    @BindView(R.id.tv_db_green)
    TextView tvDbGreen;

    //是否选中红
    private boolean isDbRed;
    //是否选中蓝
    private boolean isDbBlue;
    //是否选中绿
    private boolean isDbGreen;

    private int redCurrentMoney = 0;
    private int blueCurrentMoney = 0;
    private int greenCurrentMoney = 0;

    private SelectPopupWindow selectPopupWindow;

    private BaseQuickAdapter adapter;
    private ChineseZodiacAdapter zodiacAdapter;

    //红波
    private String red = "01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";
    //蓝波
    private String blue = "03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";
    //绿波
    private String green = "05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";

    private String da = "25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49";

    private String xiao = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24";

    private String dan = "01,03,05,07,09,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49";

    private String shuang = "02,04,06,08,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48";

    private List<DuobaoParameterResponse.ChineseZodiacBean> chineseZodiac = new ArrayList<>();

    private ArrayList<XiaZhuBean> data = new ArrayList<>(49);
    private List<XiaZhuBean> data1 = new ArrayList<>();
    private List<XiaZhuBean> data2 = new ArrayList<>();

    private BaseNiceDialog dialog;

    private DuobaoParameterResponse response;

    private int currentCount = 0;
    private int currentMoney = 0;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin_duo_bao);
        ButterKnife.bind(this);

        selectPopupWindow = new SelectPopupWindow(this, this);

        ServiceFactory.getInstance().getBaseService(Api.class)
                .duobaoParameter(getIntent().getStringExtra("groupId"))
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                .compose(RxSchedulers.normalTrans())
                .compose(bindToLifecycle())
                .subscribe(response -> {
                    this.response = response;
                    chineseZodiac = response.getChineseZodiac();
                    DuobaoParameterResponse.ChineseZodiacBean zodiacBean1 = new DuobaoParameterResponse.ChineseZodiacBean();
                    DuobaoParameterResponse.ChineseZodiacBean zodiacBean2 = new DuobaoParameterResponse.ChineseZodiacBean();
                    DuobaoParameterResponse.ChineseZodiacBean zodiacBean3 = new DuobaoParameterResponse.ChineseZodiacBean();
                    DuobaoParameterResponse.ChineseZodiacBean zodiacBean4 = new DuobaoParameterResponse.ChineseZodiacBean();
                    zodiacBean1.setZodiac("大");
                    zodiacBean1.setCode(da);
                    zodiacBean2.setZodiac("小");
                    zodiacBean2.setCode(xiao);
                    zodiacBean3.setZodiac("单");
                    zodiacBean3.setCode(dan);
                    zodiacBean4.setZodiac("双");
                    zodiacBean4.setCode(shuang);
                    chineseZodiac.add(zodiacBean1);
                    chineseZodiac.add(zodiacBean2);
                    chineseZodiac.add(zodiacBean3);
                    chineseZodiac.add(zodiacBean4);

                    initZodiac();
                    initView();
                }, this::handleApiError);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvTitle.setText(R.string.jinduobao);
        tvOdds.setText(response.getDuobaoMultiple());

        while (data.size() != 50) {
            data.add(new XiaZhuBean(data.size() > 9 ? (data.size() + "") : ("0" + data.size()), "0"));
        }
        data.remove(0);

        for (XiaZhuBean b : data.subList(0, 24)) {
            data1.add((XiaZhuBean) b.clone());
        }
        for (XiaZhuBean b : data.subList(24, data.size())) {
            data2.add((XiaZhuBean) b.clone());
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 6));

        int decoration = ScreenUtils.getScreenWidth() / 6 / 7;
        int itemHeight = (ScreenUtils.getScreenWidth() - decoration / 2 * 2 * 7) / 6;

        adapter = new BaseQuickAdapter<XiaZhuBean, BaseViewHolder>(R.layout.item_xiazhu_duobao, data1) {
            @Override
            protected void convert(BaseViewHolder helper, XiaZhuBean item) {
                TextView tv1 = helper.getView(R.id.tv1);
                ViewGroup.LayoutParams layoutParams = tv1.getLayoutParams();
                layoutParams.height = itemHeight;
                tv1.setLayoutParams(layoutParams);
                tv1.setText(item.betCode);

                if (!TextUtils.isEmpty(item.hue)) {
                    tv1.setBackgroundResource(R.drawable.shape_db_theme_circular);
                    tv1.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.white));
                    String[] strings = item.hue.split("-");
                    if (strings[strings.length - 1].equals("red")) {
                        tv1.setBackgroundResource(R.drawable.shape_db_red_circular);
                    } else if (strings[strings.length - 1].equals("blue")) {
                        tv1.setBackgroundResource(R.drawable.shape_db_blue_circular);
                    } else if (strings[strings.length - 1].equals("green")) {
                        tv1.setBackgroundResource(R.drawable.shape_db_green_circular);
                    }
                } else {
                    if (!item.betMoney.equals("0")) {
                        tv1.setBackgroundResource(R.drawable.shape_db_theme_circular);
                        tv1.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.white));
                    } else {
                        tv1.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.black));
                        tv1.setBackgroundResource(R.drawable.shape_db_white_circular);
                    }
                }

                TextView tv2 = helper.getView(R.id.tv2);
                if (item.betMoney.equals("0")) {
                    tv2.setText("");
                } else {
                    tv2.setText(item.betMoney);
                }

            }
        };

        adapter.setOnItemClickListener((adapter, view, position) -> {
            XiaZhuBean bean = (XiaZhuBean) adapter.getData().get(position);
            dialog = NiceDialog.init().setLayoutId(R.layout.layout_dialog_duobaoxiazhu)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, BaseNiceDialog baseNiceDialog) {
                            if (!TextUtils.isEmpty(response.getMaximumBetAmount())) {
                                holder.setText(R.id.tvRate, "下注范围为" + response.getMinimumBetAmount() + "-" + response.getMaximumBetAmount() + "\n" + "金额为空时，点击确认取消该号码下注。");
                            } else {
                                holder.setText(R.id.tvRate, "最小下注" + response.getMinimumBetAmount() + "\n" + "金额为空时，点击确认取消该号码下注。");
                            }
                            holder.setText(R.id.tv, bean.betCode);
                            EditText et = holder.getView(R.id.et);

                            holder.setOnClickListener(R.id.ivXiaZhu1, v -> {
                                //10
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 10 + "");
                                } else {
                                    et.setText(10 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu2, v -> {
                                //50
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 50 + "");
                                } else {
                                    et.setText(50 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu3, v -> {
                                //100
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 100 + "");
                                } else {
                                    et.setText(100 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu4, v -> {
                                //200
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 200 + "");
                                } else {
                                    et.setText(200 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.ivXiaZhu5, v -> {
                                //500
                                String trim = et.getText().toString().trim();
                                if (!TextUtils.isEmpty(trim)) {
                                    et.setText(Integer.parseInt(trim) + 500 + "");
                                } else {
                                    et.setText(500 + "");
                                }
                            });

                            holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());

                            holder.setOnClickListener(R.id.tv_determine, v -> {
                                String trim = et.getText().toString().trim();

                                if (TextUtils.isEmpty(trim)) {
                                    dialog.dismiss();
                                    currentMoney -= Integer.parseInt(bean.betMoney);
                                    bean.betMoney = "0";
                                    XiaZhuBean xiaZhuBean;
                                    if (adapter.getData().size() == 24) {
                                        xiaZhuBean = data1.get(position);
                                    } else {
                                        xiaZhuBean = data2.get(position);
                                    }
                                    if (!TextUtils.isEmpty(xiaZhuBean.hue)) {
                                        String[] split = xiaZhuBean.hue.split("-");
//                                        for (int i = 0; i < split.length; i++) {
//                                            if (!split[i].equals("red") && !split[i].equals("blue") && !split[i].equals("green")) {
//                                                DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(Integer.parseInt(split[i].substring(1)));
//                                                zodiacBean.setMoney(zodiacBean.getMoney() / zodiacBean.getCode().split(",").length);
//                                            }
//                                        }
                                        xiaZhuBean.hue = "";
                                    }
                                    if (currentCount != 0) {
                                        currentCount -= 1;
                                    }
                                    adapter.notifyItemChanged(position);
                                    tvDbTotalMoney.setText(currentMoney + "HK");
                                    tvDbNumber.setText(currentCount + "");
                                    return;
                                }

                                int i = Integer.parseInt(trim);
                                if (i < Float.parseFloat(response.getMinimumBetAmount()) || (!TextUtils.isEmpty(response.getMaximumBetAmount()) && i > Float.parseFloat(response.getMaximumBetAmount()))) {
                                    ToastUtils.showShort(R.string.duobaoxiazhufail);
                                    return;
                                }
                                dialog.dismiss();
                                currentMoney -= Integer.parseInt(bean.betMoney);
                                bean.betMoney = trim;
                                currentMoney += Integer.parseInt(bean.betMoney);
                                XiaZhuBean xiaZhuBean;
                                if (adapter.getData().size() == 24) {
                                    xiaZhuBean = data1.get(position);
                                } else {
                                    xiaZhuBean = data2.get(position);
                                }
                                if (!TextUtils.isEmpty(xiaZhuBean.hue)) {
                                    String[] split = xiaZhuBean.hue.split("-");
//                                    for (int j = 0; j < split.length; j++) {
//                                        if (!split[j].equals("red") && !split[j].equals("blue") && !split[j].equals("green")) {
//                                            DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(Integer.parseInt(split[j].substring(1)));
//                                            zodiacBean.setMoney(zodiacBean.getMoney() / zodiacBean.getCode().split(",").length);
//                                        }
//                                    }
                                    xiaZhuBean.hue = "";
                                } else {
                                    currentCount += 1;
                                }

                                adapter.notifyItemChanged(position);
                                tvDbTotalMoney.setText(currentMoney + "HK");
                                tvDbNumber.setText(currentCount + "");
                            });
                        }
                    })
                    .setDimAmount(0.5f)
                    .setOutCancel(false);
            dialog.show(getSupportFragmentManager());
        });

        recycler.addItemDecoration(new RecyclerItemAverageDecoration(decoration / 2 * 2, decoration, 6));
        recycler.setAdapter(adapter);

        tvDuoBaoQiShu.setText("期数：" + response.getExpect());
    }

    //12生肖
    private void initZodiac() {
        int decoration = ScreenUtils.getScreenWidth() / 6 / 7;
        ArrayList<DuobaoParameterResponse.ChineseZodiacBean> data = new ArrayList<>(chineseZodiac.size());
        for (DuobaoParameterResponse.ChineseZodiacBean b : chineseZodiac) {
            data.add((DuobaoParameterResponse.ChineseZodiacBean) b.clone());
        }
        zodiacAdapter = new ChineseZodiacAdapter(R.layout.item_db_zodiac, data);
        zodiacAdapter.setNewData(data);
        recyclerViewZodiac.setLayoutManager(new GridLayoutManager(this, 6));
        recyclerViewZodiac.addItemDecoration(new RecyclerItemAverageDecoration(decoration / 2 * 2, decoration, 6));
        recyclerViewZodiac.setAdapter(zodiacAdapter);
        zodiacAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (zodiacAdapter.getData().get(position).isChecked()) {
                zodiacAdapter.getData().get(position).setChecked(false);
                select("J" + position, false, position);
            } else {
                zodiacAdapter.getData().get(position).setChecked(true);
                select("J" + position, true, position);
            }
            zodiacAdapter.notifyItemChanged(position);
        });
    }

    private ArrayList<XiaZhuBean> finalData11;
    private String total11;


    @SuppressLint("CheckResult")
    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            GroupGamebettingForDuobaoRequest request = new GroupGamebettingForDuobaoRequest();
            request.setExpect(response.getExpect());
            request.setCountMoney(total11);
            request.setPayPwd(MD5Utils.getMD5(psw));
            request.setGroupId(getIntent().getStringExtra("groupId"));
            request.setDuoBaoBetInfoBeanList(finalData11);

            ServiceFactory.getInstance().getBaseService(Api.class)
                    .groupGamebettingForDuobao(GsonUtils.toJson(request, false))
                    .compose(bindToLifecycle())
                    .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(this)))
                    .compose(RxSchedulers.normalTrans())
                    .subscribe(s -> {
                        ToastUtils.showShort(R.string.xiazhuchenggong);
                        finish();
                    }, this::handleApiError);
        }
    }

    @OnClick({R.id.rl_back, R.id.tvDuoBaoNum1, R.id.tvDuoBaoNum2, R.id.tv_db_red, R.id.tv_db_blue, R.id.tv_db_green, R.id.iv_db_reset, R.id.iv_db_10, R.id.iv_db_50, R.id.iv_db_100, R.id.iv_db_200, R.id.iv_db_500, R.id.tv_input_money, R.id.tv_bottomPour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tvDuoBaoNum1:
                tvDuoBaoNum1.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.black));
                tvDuoBaoNum2.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.colorDuoBao));
                adapter.setNewData(data1);
                break;
            case R.id.tvDuoBaoNum2:
                tvDuoBaoNum1.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.colorDuoBao));
                tvDuoBaoNum2.setTextColor(ContextCompat.getColor(JinDuoBaoActivity.this, R.color.black));
                adapter.setNewData(data2);
                break;
            case R.id.tv_db_red:
                if (isDbRed) {
                    tvDbRed.setBackgroundResource(R.drawable.shape_db_white);
                    tvDbRed.setTextColor(Color.BLACK);
                    isDbRed = false;
                    select("red", false, -1);
                } else {
                    tvDbRed.setBackgroundResource(R.drawable.shape_db_red);
                    tvDbRed.setTextColor(Color.WHITE);
                    isDbRed = true;
                    select("red", true, -1);
                }
                break;
            case R.id.tv_db_blue:
                if (isDbBlue) {
                    tvDbBlue.setBackgroundResource(R.drawable.shape_db_white);
                    tvDbBlue.setTextColor(Color.BLACK);
                    isDbBlue = false;
                    select("blue", false, -1);
                } else {
                    tvDbBlue.setBackgroundResource(R.drawable.shape_db_blue);
                    tvDbBlue.setTextColor(Color.WHITE);
                    isDbBlue = true;
                    select("blue", true, -1);
                }
                break;
            case R.id.tv_db_green:
                if (isDbGreen) {
                    tvDbGreen.setBackgroundResource(R.drawable.shape_db_white);
                    tvDbGreen.setTextColor(Color.BLACK);
                    isDbGreen = false;
                    select("green", false, -1);
                } else {
                    tvDbGreen.setBackgroundResource(R.drawable.shape_db_green);
                    tvDbGreen.setTextColor(Color.WHITE);
                    isDbGreen = true;
                    select("green", true, -1);
                }
                break;

            case R.id.iv_db_reset:
                currentCount = 0;
                currentMoney = 0;
                tvDbNumber.setText("0");
                tvInputMoney.setText("0");
                tvDbTotalMoney.setText("0HK");
                data1.clear();
                data2.clear();
                for (XiaZhuBean b : data.subList(0, 24)) {
                    data1.add((XiaZhuBean) b.clone());
                }
                for (XiaZhuBean b : data.subList(24, data.size())) {
                    data2.add((XiaZhuBean) b.clone());
                }
                if (adapter.getData().size() == 24) {
                    adapter.setNewData(data1);
                } else {
                    adapter.setNewData(data2);
                }

                tvDbRed.setBackgroundResource(R.drawable.shape_db_white);
                tvDbRed.setTextColor(Color.BLACK);
                tvDbGreen.setBackgroundResource(R.drawable.shape_db_white);
                tvDbGreen.setTextColor(Color.BLACK);
                tvDbBlue.setBackgroundResource(R.drawable.shape_db_white);
                tvDbBlue.setTextColor(Color.BLACK);

                ArrayList<DuobaoParameterResponse.ChineseZodiacBean> bottomData = new ArrayList<>();
                for (DuobaoParameterResponse.ChineseZodiacBean b : chineseZodiac) {
                    bottomData.add((DuobaoParameterResponse.ChineseZodiacBean) b.clone());
                }
                zodiacAdapter.setNewData(bottomData);

                isDbRed = false;
                isDbBlue = false;
                isDbGreen = false;
                redCurrentMoney = 0;
                blueCurrentMoney = 0;
                greenCurrentMoney = 0;
                break;
            case R.id.iv_db_10:
                setMoney(10);
                break;
            case R.id.iv_db_50:
                setMoney(50);
                break;
            case R.id.iv_db_100:
                setMoney(100);
                break;
            case R.id.iv_db_200:
                setMoney(200);
                break;
            case R.id.iv_db_500:
                setMoney(500);
                break;
            case R.id.tv_input_money:
                NiceDialog.init().setLayoutId(R.layout.layout_general_dialog3).setConvertListener(new ViewConvertListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_title, "下注金额");
                        TextView textView = holder.getView(R.id.tv_rate);
                        textView.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(response.getMaximumBetAmount())) {
                            textView.setText("下注范围为" + response.getMinimumBetAmount() + "-" + response.getMaximumBetAmount());
                        } else {
                            textView.setText("最小下注" + response.getMinimumBetAmount());
                        }
                        holder.setText(R.id.tv_cancel, "取消");
                        holder.setText(R.id.tv_notarize, "确认");
                        EditText editText = holder.getView(R.id.et_content);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        holder.setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                        holder.setOnClickListener(R.id.tv_notarize, v -> {
                            if (!TextUtils.isEmpty(editText.getText().toString())) {
                                if (Integer.parseInt(editText.getText().toString()) >= Integer.parseInt(response.getMinimumBetAmount())) {
                                    dialog.dismiss();
                                    tvInputMoney.setText(editText.getText().toString());
                                    setMoney(Integer.parseInt(tvInputMoney.getText().toString()));
                                } else {
                                    ToastUtils.showShort(R.string.duobaoxiazhufail);
                                }
                            }


                        });
                    }
                }).setDimAmount(0.5f).setOutCancel(false).show(getSupportFragmentManager());
                break;
            //下注
            case R.id.tv_bottomPour:
                finalData11 = new ArrayList<>();
                total11 = "0";
                for (XiaZhuBean bean : data1) {
                    if (!TextUtils.isEmpty(bean.betMoney) && !bean.betMoney.equals("0")) {
                        finalData11.add(bean);
                        total11 = Integer.parseInt(bean.betMoney) + Integer.parseInt(total11) + "";
                    }
                }
                for (XiaZhuBean bean : data2) {
                    if (!TextUtils.isEmpty(bean.betMoney) && !bean.betMoney.equals("0")) {
                        finalData11.add(bean);
                        total11 = Integer.parseInt(bean.betMoney) + Integer.parseInt(total11) + "";
                    }
                }

                if (finalData11.size() == 0) {
                    ToastUtils.showShort(R.string.qingxiazhu);
                    return;
                }

                KeyboardUtils.hideSoftInput(this);
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int winHeight = getWindow().getDecorView().getHeight();
                selectPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
                break;
        }
    }

    private void setMoney(int money) {
        currentCount = 0;
        currentMoney = 0;
        boolean temp1 = false;
        boolean temp2 = false;
        boolean temp3 = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data1.size(); i++) {
            XiaZhuBean b = data1.get(i);
            if (!TextUtils.isEmpty(b.hue)) {
                String[] split = b.hue.split("-");
                for (String str : split) {
                    if (str.equals("red")) {
                        temp1 = true;
                    } else if (str.equals("blue")) {
                        temp2 = true;
                    } else if (str.equals("green")) {
                        temp3 = true;
                    } else {
                        if (sb.indexOf(str + ",") == -1) {
                            sb.append(str + ",");
                        }
                    }
                    if (!TextUtils.isEmpty(b.betMoney)) {
                        b.betMoney = String.valueOf(Integer.parseInt(b.betMoney) + money);
                    } else {
                        b.betMoney = String.valueOf(money);
                    }
                }
                if (adapter.getData().size() == 24) {
                    adapter.notifyItemChanged(i);
                }
            }
            if (!b.betMoney.equals("0")) {
                currentMoney += Integer.parseInt(b.betMoney);
                currentCount += 1;
            }
        }
        for (int i = 0; i < data2.size(); i++) {
            XiaZhuBean b = data2.get(i);
            if (!TextUtils.isEmpty(b.hue)) {
                String[] split = b.hue.split("-");
                for (String str : split) {
                    if (str.equals("red")) {
                        temp1 = true;
                    } else if (str.equals("blue")) {
                        temp2 = true;
                    } else if (str.equals("green")) {
                        temp3 = true;
                    } else {
                        if (sb.indexOf(str + ",") == -1) {
                            sb.append(str + ",");
                        }
                    }
                    if (!TextUtils.isEmpty(b.betMoney)) {
                        b.betMoney = String.valueOf(Integer.parseInt(b.betMoney) + money);
                    } else {
                        b.betMoney = String.valueOf(money);
                    }
                }
                if (adapter.getData().size() == 25) {
                    adapter.notifyItemChanged(i);
                }
            }
            if (!b.betMoney.equals("0")) {
                currentMoney += Integer.parseInt(b.betMoney);
                currentCount += 1;
            }
        }
        if (temp1) {
            redCurrentMoney += money;
        }
        if (temp2) {
            blueCurrentMoney += money;
        }
        if (temp3) {
            greenCurrentMoney += money;
        }
        if (sb.length() != 0) {
            String[] split = sb.toString().split(",");
            for (String s : split) {
                DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(Integer.parseInt(s.substring(1)));
                zodiacBean.setMoney(zodiacBean.getMoney() + money);
            }
        }
        tvDbTotalMoney.setText(currentMoney + "HK");
        tvDbNumber.setText(currentCount + "");
    }

    //选中
    private void select(String type, boolean isCheck, int position) {
        currentCount = 0;
        currentMoney = 0;
        boolean isSelect;
        List<DuobaoParameterResponse.ChineseZodiacBean> bottomData = zodiacAdapter.getData();
        if (position == -1) {
            isSelect = isCheck;
        } else {
            isSelect = bottomData.get(position).isChecked();
        }

        for (int i = 0; i < data1.size(); i++) {
            String betCode = data1.get(i).betCode;
            if ((type.equals("red") && red.contains(betCode)) ||
                    (type.equals("blue") && blue.contains(betCode)) ||
                    (type.equals("green") && green.contains(betCode)) ||
                    (position != -1 && bottomData.get(position).getCode().contains(betCode))) {
                if (isSelect) {
                    data1.get(i).hue = data1.get(i).hue + type + "-";
                } else {
                    //反选
                    boolean contains = data1.get(i).hue.contains(type);
                    int i1 = 0;
                    if (!TextUtils.isEmpty(data1.get(i).betMoney)) {
                        i1 = Integer.parseInt(data1.get(i).betMoney);
                    }
                    if (type.equals("red") && contains) {
                        i1 -= redCurrentMoney;
                    } else if (type.equals("blue") && contains) {
                        i1 -= blueCurrentMoney;
                    } else if (type.equals("green") && contains) {
                        i1 -= greenCurrentMoney;
                    } else if (contains) {
                        int i2 = Integer.parseInt(type.substring(1));
                        DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(i2);
                        i1 -= zodiacBean.getMoney();
                    }
                    data1.get(i).betMoney = String.valueOf(i1);
                    data1.get(i).hue = data1.get(i).hue.replace(type + "-", "");
                }
                if (adapter.getData().size() == 24) {
                    adapter.notifyItemChanged(i);
                }
            }
            if (!data1.get(i).betMoney.equals("0")) {
                currentMoney += Integer.parseInt(data1.get(i).betMoney);
                currentCount += 1;
            }
        }
        for (int i = 0; i < data2.size(); i++) {
            String betCode = data2.get(i).betCode;
            if ((type.equals("red") && red.contains(betCode)) ||
                    (type.equals("blue") && blue.contains(betCode)) ||
                    (type.equals("green") && green.contains(betCode)) ||
                    (position != -1 && bottomData.get(position).getCode().contains(betCode))) {
                if (isSelect) {
                    data2.get(i).hue = data2.get(i).hue + type + "-";
                } else {
                    //反选
                    boolean contains = data2.get(i).hue.contains(type);
                    int i1 = 0;
                    if (!TextUtils.isEmpty(data2.get(i).betMoney)) {
                        i1 = Integer.parseInt(data2.get(i).betMoney);
                    }
                    if (type.equals("red") && contains) {
                        i1 -= redCurrentMoney;
                    } else if (type.equals("blue") && contains) {
                        i1 -= blueCurrentMoney;
                    } else if (type.equals("green") && contains) {
                        i1 -= greenCurrentMoney;
                    } else if (contains) {
                        int i2 = Integer.parseInt(type.substring(1));
                        DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(i2);
                        i1 -= zodiacBean.getMoney();
                    }
                    data2.get(i).betMoney = String.valueOf(i1);
                    data2.get(i).hue = data2.get(i).hue.replace(type + "-", "");
                }
                if (adapter.getData().size() == 25) {
                    adapter.notifyItemChanged(i);
                }
            }
            if (!data2.get(i).betMoney.equals("0")) {
                currentMoney += Integer.parseInt(data2.get(i).betMoney);
                currentCount += 1;
            }
        }

        if (type.equals("red")) {
            redCurrentMoney = 0;
        } else if (type.equals("blue")) {
            blueCurrentMoney = 0;
        } else if (type.equals("green")) {
            greenCurrentMoney = 0;
        } else {
            int i2 = Integer.parseInt(type.substring(1));
            DuobaoParameterResponse.ChineseZodiacBean zodiacBean = zodiacAdapter.getData().get(i2);
            zodiacBean.setMoney(0);
        }
        tvDbTotalMoney.setText(currentMoney + "HK");
        tvDbNumber.setText(currentCount + "");
    }

    public class XiaZhuBean implements Cloneable {
        String betCode;
        String betMoney;
        String hue = "";

        public Object clone() {
            XiaZhuBean xiaZhuBean = null;
            try {
                xiaZhuBean = (XiaZhuBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return xiaZhuBean;
        }

        XiaZhuBean(String number, String money) {
            this.betCode = number;
            this.betMoney = money;
        }
    }
}
