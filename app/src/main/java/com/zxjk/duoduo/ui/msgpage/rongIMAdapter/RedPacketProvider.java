package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.PeopleRedEnvelopesActivity;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.RedPacketMessage;
import com.zxjk.duoduo.weight.dialog.RedEvelopesDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 */
@ProviderTag(messageContent = RedPacketMessage.class)
public class RedPacketProvider extends IContainerItemProvider.MessageProvider<RedPacketMessage> {

    class ViewHolder {
        TextView message;
        ConstraintLayout sendLayout;
    }

    @Override
    public void bindView(View view, int position, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            //消息方向，自己发送的
            holder.sendLayout.setBackgroundResource(R.drawable.icon_red_packet_user);
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_send_red_packet_friend);
        }
        holder.message.setText(redPacketMessage.getMessage());
    }

    @Override
    public Spannable getContentSummary(RedPacketMessage redPacketMessage) {


        return new SpannableString("");

//        return new SpannableString(myCustomizeMessage.getContent());
    }

    @Override
    public void onItemClick(View view, int i, RedPacketMessage redPacketMessage, UIMessage uiMessage) {
        Context context = view.getContext();
        RedEvelopesDialog dialog = new RedEvelopesDialog(context);
        dialog.setOnClickListener(new RedEvelopesDialog.OnClickListener() {
            @Override
            public void onOpen() {
                context.startActivity(new Intent(context, PeopleRedEnvelopesActivity.class));
                dialog.dismiss();
            }

        });
        dialog.show();
    }


    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_packet_send, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        view.setTag(holder);
        holder.sendLayout = (ConstraintLayout) view.findViewById(R.id.send_red_packet_layout);
        return view;
    }
}
