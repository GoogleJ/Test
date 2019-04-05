package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.PeopleRedEnvelopesActivity;
import com.zxjk.duoduo.weight.dialog.RedEvelopesDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.ConnectChangeReceiver;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\4 0004 关于红包的provider
 */
@ProviderTag(messageContent = RedPacketMessage.class)
public class RedPacketProvider extends IContainerItemProvider.MessageProvider<RedPacketMessage> {

    private Context context;

    class ViewHolder {
        TextView message;
        ConstraintLayout sendLayout;
    }

    @Override
    public void bindView(View view, int position, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
        if (context == null) {
            context = view.getContext();
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
            holder.sendLayout.setBackgroundResource(R.drawable.icon_red_packet_user);
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_send_red_packet_friend);
        }
        if (TextUtils.isEmpty(redPacketMessage.getRemark())) {
            holder.message.setText(context.getResources().getString(R.string.m_red_envelopes_label));
        } else {
            holder.message.setText(redPacketMessage.getRemark());
        }
    }

    @Override
    public Spannable getContentSummary(RedPacketMessage redPacketMessage) {
        return new SpannableString("");
    }

    @Override
    public void onItemClick(View view, int i, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
    }


    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_packet_send, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.remark);
        view.setTag(holder);
        holder.sendLayout = (ConstraintLayout) view.findViewById(R.id.send_red_packet_layout);
        return view;
    }
}
