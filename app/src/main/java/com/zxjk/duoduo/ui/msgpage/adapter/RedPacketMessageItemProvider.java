package com.zxjk.duoduo.ui.msgpage.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxjk.duoduo.R;

import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 */
public class RedPacketMessageItemProvider extends IContainerItemProvider.MessageProvider<RedPacketMessage>{

    class ViewHolder{
        TextView message;
    }

    @Override
    public void bindView(View view, int i, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        //消息方向，自己发送的
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource( R.drawable.icon_red_packet_user);
        } else {
            holder.message.setBackgroundResource(R.drawable.icon_send_red_packet_friend);
        }
        holder.message.setText(redPacketMessage.getMessage());
    }

    @Override
    public Spannable getContentSummary(RedPacketMessage redPacketMessage) {
        return new SpannableString(redPacketMessage.getMessage());
    }

    @Override
    public void onItemClick(View view, int i, RedPacketMessage redPacketMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_red_packet_send,null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        view.setTag(holder);
        return view;
    }

}
