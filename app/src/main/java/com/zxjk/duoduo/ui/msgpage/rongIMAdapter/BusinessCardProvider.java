package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.msgpage.AddFriendDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.FriendDetailsActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = BusinessCardMessage.class)
public class BusinessCardProvider extends IContainerItemProvider.MessageProvider<BusinessCardMessage> {

    class ViewHolder {
        TextView userName;
        TextView duoduoId;
        ImageView heardImage;
        ConstraintLayout sendLayout;
    }

    Context context;

    @Override
    public void bindView(View view, int i, BusinessCardMessage businessCardMessage, UIMessage uiMessage) {
        if (context == null) {
            context = view.getContext();
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
            holder.sendLayout.setBackgroundResource(R.drawable.icon_business_card_user);
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_business_card_friend);
        }

        holder.userName.setText(businessCardMessage.getName());
        holder.duoduoId.setText(businessCardMessage.getDuoduo());
        GlideUtil.loadCornerImg(holder.heardImage, businessCardMessage.getIcon(), 2);
    }

    @Override
    public Spannable getContentSummary(BusinessCardMessage businessCardMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, BusinessCardMessage businessCardMessage, UIMessage uiMessage) {
        if (null == Constant.friendsList) {
            getFriendListById(businessCardMessage.getUserId());
        } else {
            handleFriendList(businessCardMessage.getUserId());
        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business_card_send, null);
        ViewHolder holder = new ViewHolder();
        holder.userName = (TextView) view.findViewById(R.id.business_card_user_name);
        holder.duoduoId = (TextView) view.findViewById(R.id.business_card_duoduo_id);
        holder.heardImage = (ImageView) view.findViewById(R.id.business_card_header);
        holder.sendLayout = (ConstraintLayout) view.findViewById(R.id.send_red_packet_layout);
        view.setTag(holder);
        return view;
    }

    @SuppressLint("CheckResult")
    public void getFriendListById(String userId) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(context)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> {
                    if (null == Constant.friendsList) {
                        Constant.friendsList = friendInfoResponses;
                        handleFriendList(userId);
                    }
                }, throwable -> LogUtils.d(throwable.getMessage()));
    }

    private void handleFriendList(String userId) {
        if (userId.equals(Constant.userId)) {
            //扫到了自己
            Intent intent = new Intent(context, FriendDetailsActivity.class);
            intent.putExtra("friendId", userId);
            context.startActivity(intent);
            return;
        }
        for (FriendInfoResponse f : Constant.friendsList) {
            if (f.getId().equals(userId)) {
                //扫到了自己的好友，进入详情页（可聊天）
                Intent intent = new Intent(context, FriendDetailsActivity.class);
                intent.putExtra("searchFriendDetails", f);
                context.startActivity(intent);
                return;
            }
        }

        //扫到了陌生人，进入加好友页面
        Intent intent = new Intent(context, AddFriendDetailsActivity.class);
        intent.putExtra("newFriendId", userId);
        context.startActivity(intent);
    }
}
