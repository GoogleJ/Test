package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;
import com.zxjk.duoduo.ui.msgpage.TransferInfoActivity;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 完成转账的自定义Provider
 */
@ProviderTag(messageContent = TransferMessage.class)
public class TransferProvider extends IContainerItemProvider.MessageProvider<TransferMessage> {
    class ViewHolder {
        TextView message;
        TextView transferMoney;
        ImageView transferIconType;
        LinearLayout sendLayout;
    }

    @Override
    public void bindView(View view, int i, TransferMessage transferMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.sendLayout.setBackgroundResource(R.drawable.ic_zhuanzhang_send_bg);
        } else {
            holder.sendLayout.setBackgroundResource(R.drawable.icon_send_red_packet_friend);
        }

        holder.message.setText(transferMessage.getRemarks());
        holder.transferMoney.setText(transferMessage.getHk());

    }

    @Override
    public Spannable getContentSummary(TransferMessage transferMessage) {
        return new SpannableString("收到一条转账消息");
    }

    @Override
    public void onItemClick(View view, int i, TransferMessage transferMessage, UIMessage uiMessage) {
        //这里只需要把transferMessage的值拿过来，传到TransferInfoActivity里头了
        Context context = view.getContext();
        Intent intent = new Intent(context, TransferInfoActivity.class);
        intent.putExtra("msg", transferMessage);

        boolean fromSelf = false;
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            fromSelf = true;
        }
        intent.putExtra("fromSelf", fromSelf);
        context.startActivity(intent);
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_send, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        holder.transferMoney = (TextView) view.findViewById(R.id.transfer_money);
        holder.transferIconType = (ImageView) view.findViewById(R.id.transfer_type_icon);
        holder.sendLayout = (LinearLayout) view.findViewById(R.id.transfer_send_layout);
        view.setTag(holder);
        return view;
    }
}
