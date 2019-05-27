package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.stx.xhb.pagemenulibrary.PageMenuLayout;
import com.stx.xhb.pagemenulibrary.holder.AbstractHolder;
import com.stx.xhb.pagemenulibrary.holder.PageMenuViewHolderCreator;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.R.drawable;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.view.IndicatorView;
import com.zxjk.duoduo.view.ModelHomeEntrance;
import com.zxjk.duoduo.view.ScreenUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

public class SampleTab implements IEmoticonTab {
    private List<ModelHomeEntrance> homeEntrances;
    private IndicatorView entranceIndicatorView;
    private PageMenuLayout<ModelHomeEntrance> mPageMenuLayout;
    private int[] images = {R.drawable.ic_001, R.drawable.ic_002, drawable.ic_003,
            drawable.ic_004, drawable.ic_005, drawable.ic_006,
            drawable.ic_007, drawable.ic_008, drawable.ic_009};

    private String targetId;
    private String conversationType;

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public Drawable obtainTabDrawable(Context context) {
        return context.getResources().getDrawable(drawable.ic_001);
    }

    public View obtainTabPager(Context context) {
        return this.initView(context);
    }

    public void onTableSelected(int position) {
    }

    private View initView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, (ViewGroup) null);
        entranceIndicatorView = view.findViewById(R.id.main_home_entrance_indicator);
        mPageMenuLayout = view.findViewById(R.id.pagemenu);
        initData();
        return view;

    }

    private void initData() {
        homeEntrances = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            homeEntrances.add(new ModelHomeEntrance(images[i]));
        }

        mPageMenuLayout.setPageDatas(homeEntrances, new PageMenuViewHolderCreator() {
            @Override
            public AbstractHolder createHolder(View itemView) {
                return new AbstractHolder<ModelHomeEntrance>(itemView) {
                    private TextView entranceNameTextView;
                    private ImageView entranceIconImageView;

                    @Override
                    protected void initView(View itemView) {
                        entranceIconImageView = itemView.findViewById(R.id.entrance_image);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) ScreenUtil.getScreenWidth() / 4.0f));
                        itemView.setLayoutParams(layoutParams);
                    }

                    @Override
                    public void bindView(RecyclerView.ViewHolder holder, final ModelHomeEntrance data, int pos) {
                        entranceIconImageView.setImageResource(data.getImage());
                        holder.itemView.setOnClickListener(v -> {
                            Bitmap image = ((BitmapDrawable) entranceIconImageView.getDrawable()).getBitmap();

                            saveBitmapFile(v.getContext(), image);
                            Uri uri = Uri.fromFile(new File(v.getContext().getExternalCacheDir(), "1.jpg"));
                            ImageMessage obtain = ImageMessage.obtain(uri, uri, true);
                            Message obtain1;
                            if (conversationType.equals("private")) {
                                obtain1 = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, obtain);
                            } else {
                                obtain1 = Message.obtain(targetId, Conversation.ConversationType.GROUP, obtain);
                            }

                            RongIM.getInstance().sendImageMessage(obtain1, null, null, new RongIMClient.SendImageMessageCallback() {
                                        @Override
                                        public void onAttached(Message message) {

                                        }

                                        @Override
                                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                            CommonUtils.destoryDialog();
                                        }

                                        @Override
                                        public void onSuccess(Message message) {
                                            CommonUtils.destoryDialog();
                                        }

                                        @Override
                                        public void onProgress(Message message, int i) {

                                        }
                                    });
                        });
                    }
                };
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_home_entrance;
            }
        });
        entranceIndicatorView.setIndicatorCount(mPageMenuLayout.getPageCount());
        mPageMenuLayout.setOnPageListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                entranceIndicatorView.setCurrentIndicator(position);
            }
        });

    }


    public void saveBitmapFile(Context context, Bitmap bitmap) {
        File file = new File(context.getExternalCacheDir(), "1.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}