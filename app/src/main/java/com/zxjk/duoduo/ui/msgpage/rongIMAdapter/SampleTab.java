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

import androidx.recyclerview.widget.RecyclerView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.R.drawable;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.view.HorizontalPageLayoutManager;
import com.zxjk.duoduo.view.PagingScrollHelper;

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

public class SampleTab implements IEmoticonTab, PagingScrollHelper.onPageChangeListener, View.OnClickListener {


    private PagingScrollHelper scrollHelper = new PagingScrollHelper();
    private RecyclerView.ItemDecoration lastItemDecoration = null;
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


    public SampleTab() {

    }


    public Drawable obtainTabDrawable(Context context) {
        return context.getResources().getDrawable(drawable.ic_001);
    }

    public View obtainTabPager(Context context) {
//        RongIMClient.getInstance().getCurrentUserId();
        return this.initView(context);
    }


    public void onTableSelected(int position) {
    }

    private View initView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, (ViewGroup) null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);
        recyclerView.setHorizontalScrollBarEnabled(true);
        HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 4);
        RecyclerView.LayoutManager layoutManager = horizontalPageLayoutManager;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.removeItemDecoration(lastItemDecoration);
        scrollHelper.updateLayoutManger();
        scrollHelper.scrollToPosition(0);
        return view;

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageChange(int index) {

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<Integer> data = new ArrayList<>();

        public MyAdapter() {
            setData();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }


        private void setData() {
            for (int i = 0; i < images.length; i++) {
                data.add(images[i]);
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.iv_image.setImageResource(data.get(position));
            holder.itemView.setOnClickListener(v -> {
                Bitmap image = ((BitmapDrawable) holder.iv_image.getDrawable()).getBitmap();
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

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_image;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv_image = itemView.findViewById(R.id.iv_image);
            }
        }


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