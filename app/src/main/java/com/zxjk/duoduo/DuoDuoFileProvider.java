//package com.zxjk.duoduo;
//
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.zxjk.duoduo.ui.msgpage.adapter.CustomizeMessage;
//
//import io.rong.imkit.RongIM;
//import io.rong.imkit.emoticon.AndroidEmoji;
//import io.rong.imkit.model.ProviderTag;
//import io.rong.imkit.model.UIMessage;
//import io.rong.imkit.utilities.OptionsPopupDialog;
//import io.rong.imkit.widget.provider.IContainerItemProvider;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Message;
//import io.rong.imlib.model.MessageContent;
//
///**
// * @author Administrator
// * （这里是你自定义的消息实体）
// */
//@ProviderTag(
//        messageContent = CustomizeMessage.class,
//        showReadState = true
//)
//public class DuoDuoFileProvider extends  IContainerItemProvider.MessageProvider<CustomizeMessage> {
//
//    public DuoDuoFileProvider() {
//    }
//
//
//    @Override
//    public void bindView(View view, int i, CustomizeMessage redPackageMessage, UIMessage message) {
//
//        //根据需求，适配数据
//        ViewHolder holder = (ViewHolder) view.getTag();
//
//        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
//            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
//        } else {
//            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
//        }
//        //AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
//        //holder.tvTitle.setText(redPackageMessage.getTitle());
//        holder.tvStoreName.setText(redPackageMessage.getStoreName());
//        //holder.tvDesc1.setText(redPackageMessage.getDesc1());
//        //holder.tvDesc2.setText(redPackageMessage.getDesc2());
//    }
//
//
//
//
//
//
//    @Override
//    public View newView(Context context, ViewGroup viewGroup) {
//        //这就是展示在会话界面的自定义的消息的布局
//        View view = LayoutInflater.from(context).inflate(R.layout.item_redpackage_message, null);
//        ViewHolder holder = new ViewHolder();
//        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        holder.tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
//        holder.tvDesc1 = (TextView) view.findViewById(R.id.tv_desc1);
//        holder.tvDesc2 = (TextView) view.findViewById(R.id.tv_desc2);
//        view.setTag(holder);
//        return view;
//    }
//    @Override
//    public Spannable getContentSummary(CustomizeMessage redPackageMessage) {
//        return new SpannableString(redPackageMessage.getDesc1());
//    }
//
//    @Override
//    public void onItemClick(View view, int i, CustomizeMessage redPackageMessage, UIMessage uiMessage) {
//
//    }
//
//    @Override
//    public void onItemLongClick(View view, int i, CustomizeMessage redPackageMessage, UIMessage uiMessage) {
//        //实现长按删除等功能，咱们直接复制融云其他provider的实现
//        String[] items1;//复制，删除
//        items1 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete)};
//
//        OptionsPopupDialog.newInstance(view.getContext(), items1).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
//            @Override
//            public void onOptionsItemClicked(int which) {
//                if (which == 0) {
//                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboard.setText("ssss");//这里是自定义消息的消息属性
//                } else if (which == 1) {
//                    RongIM.getInstance().deleteMessages(new int[]{uiMessage.getMessageId()}, (RongIMClient.ResultCallback) null);
//                }
//            }
//        }).show();
//
//    }
//
//    private static class ViewHolder {
//        TextView tvTitle, tvStoreName, tvDesc1, tvDesc2;
//    }
//
//
//
//
//}
