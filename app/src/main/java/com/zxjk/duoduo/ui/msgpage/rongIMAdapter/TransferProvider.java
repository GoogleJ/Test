package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxjk.duoduo.R;

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
        TextView remark;
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

        if (!TextUtils.isEmpty(uiMessage.getExtra())) {
            holder.remark.setText("已被领取");
        } else {
            holder.remark.setText(transferMessage.getRemark());
        }
        holder.transferMoney.setText(transferMessage.getMoney() + "HK");
    }

    @Override
    public Spannable getContentSummary(TransferMessage transferMessage) {
        return null;
    }


    @Override
    public void onItemClick(View view, int i, TransferMessage transferMessage, UIMessage uiMessage) {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transfer_send, null);
        ViewHolder holder = new ViewHolder();
        holder.remark = (TextView) view.findViewById(R.id.remark);
        holder.transferMoney = (TextView) view.findViewById(R.id.transfer_money);
        holder.transferIconType = (ImageView) view.findViewById(R.id.transfer_type_icon);
        holder.sendLayout = (LinearLayout) view.findViewById(R.id.transfer_send_layout);
        view.setTag(holder);
        return view;
    }
}
