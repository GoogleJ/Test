package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;

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
public class BusinessCardProvider extends IContainerItemProvider.MessageProvider<BusinessCardMessage>{

    class ViewHolder {
        TextView userName;
        TextView duoduoId;
        ImageView heardImage;
        ConstraintLayout sendLayout;
    }

    @Override
    public void bindView(View view, int i, BusinessCardMessage businessCardMessage, UIMessage uiMessage) {
        ViewHolder holder =(ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_send_red_packet_friend);
        }
        holder.userName.setText(businessCardMessage.getUserId());
        holder.duoduoId.setText(businessCardMessage.getDuoduoId());
    }

    @Override
    public Spannable getContentSummary(BusinessCardMessage transferMessage) {
        return new SpannableString("");
    }

    @Override
    public void onItemClick(View view, int i, BusinessCardMessage transferMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_business_card_send,null);
        ViewHolder holder = new ViewHolder();
        holder.userName = (TextView) view.findViewById(R.id.business_card_user_name);
        holder.duoduoId=(TextView) view.findViewById(R.id.business_card_duoduo_id);
        holder.heardImage=(ImageView)view.findViewById(R.id.business_card_header);
        holder.sendLayout = (ConstraintLayout) view.findViewById(R.id.send_red_packet_layout);
        view.setTag(holder);
        return view;
    }
}
