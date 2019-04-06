package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zxjk.duoduo.R;
import com.zxjk.duoduo.network.Api;
import com.zxjk.duoduo.network.ServiceFactory;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.rx.RxSchedulers;
import com.zxjk.duoduo.ui.msgpage.ConversationDetailsActivity;
import com.zxjk.duoduo.ui.msgpage.ConversationForAddActivity;
import com.zxjk.duoduo.utils.CommonUtils;
import com.zxjk.duoduo.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 个人名片的provider
 */
@ProviderTag(messageContent = BusinessCardMessage.class)
public class BusinessCardProvider extends IContainerItemProvider.MessageProvider<BusinessCardMessage> {

    List<FriendInfoResponse> list = new ArrayList<>();

    class ViewHolder {
        TextView userName;
        TextView duoduoId;
        ImageView heardImage;
        ConstraintLayout sendLayout;
    }

    @Override
    public void bindView(View view, int i, BusinessCardMessage businessCardMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_business_card_friend);
        }
        holder.userName.setText(businessCardMessage.getUserId());
        holder.duoduoId.setText(businessCardMessage.getDuoduoId());
        GlideUtil.loadCornerImg(holder.heardImage, businessCardMessage.getHeaderUrl(), 2);
    }

    @Override
    public Spannable getContentSummary(BusinessCardMessage transferMessage) {
        return new SpannableString("对方向您发送了一张名片");
    }

    @Override
    public void onItemClick(View view, int i, BusinessCardMessage businessCardMessage, UIMessage uiMessage) {
        Context context = view.getContext();
        getFriendListById(context);

        if (list.size()>=0){
            for(FriendInfoResponse friendInfoResponse:list){
                businessCardMessage.getDuoduoId();
                if (!friendInfoResponse.getId().equals(businessCardMessage.getUserId())){
                    Intent intent = new Intent(context, ConversationDetailsActivity.class);
                    intent.putExtra("businessCardMessageId", businessCardMessage.getUserId());
                    intent.putExtra("ConstantUserId", 3);
                    context.startActivity(intent);
                    return;
                }else{
                    Intent intent=new Intent(context, ConversationForAddActivity.class);
                    intent.putExtra("friendInfoResponses",friendInfoResponse);
                    context.startActivity(intent);
                    return;

                }
            }

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
    public void getFriendListById(Context context) {
        ServiceFactory.getInstance().getBaseService(Api.class)
                .getFriendListById()
                .compose(RxSchedulers.ioObserver(CommonUtils.initDialog(context)))
                .compose(RxSchedulers.normalTrans())
                .subscribe(friendInfoResponses -> list = friendInfoResponses, throwable -> LogUtils.d(throwable.getMessage()));

    }
}
