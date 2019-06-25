package com.zxjk.duoduo.ui.msgpage.rongIM;


import android.view.KeyEvent;
import android.widget.EditText;

import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.AudioVideoPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.BusinessCardPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.CollectionPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.MyCombineLocationPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.PhotoSelectorPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.RedPacketPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.SightPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.TakePhotoPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.TransferPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.plugin.VoiceCallsPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIM.rongTab.SampleTab;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * @author Administrator
 */
public class BasePluginExtensionModule extends DefaultExtensionModule {
    private EditText mEditText;
    private List<IEmoticonTab> list;

    public List<IEmoticonTab> getList() {
        return list;
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        RedPacketPlugin packetPlugin = new RedPacketPlugin();
        PhotoSelectorPlugin photoSelectorPlugin = new PhotoSelectorPlugin();
        TakePhotoPlugin takePhotoPlugin = new TakePhotoPlugin();
        TransferPlugin transferPlugin = new TransferPlugin();
        VoiceCallsPlugin voiceCallsPlugin = new VoiceCallsPlugin();
        BusinessCardPlugin businessCardPlugin = new BusinessCardPlugin();
        MyCombineLocationPlugin locationPlugin = new MyCombineLocationPlugin();
        CollectionPlugin collectionPlugin = new CollectionPlugin();
        //音视频
        AudioVideoPlugin audioVideoPlugin = new AudioVideoPlugin();

        List<IPluginModule> list = super.getPluginModules(conversationType);

        if (list != null) {
            list.clear();
            list.add(photoSelectorPlugin);
            list.add(takePhotoPlugin);
            list.add(transferPlugin);
            list.add(new SightPlugin());
            list.add(audioVideoPlugin);
//            list.add(voiceCallsPlugin);
            list.add(packetPlugin);
            list.add(businessCardPlugin);
            list.add(locationPlugin);
//            list.add(collectionPlugin);

        }

        return list;
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        mEditText = extension.getInputEditText();
    }

    @Override
    public void onDetachedFromExtension() {
        mEditText = null;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            public void onEmojiClick(String emoji) {
                int start = BasePluginExtensionModule.this.mEditText.getSelectionStart();
                BasePluginExtensionModule.this.mEditText.getText().insert(start, emoji);
            }

            public void onDeleteClick() {
                BasePluginExtensionModule.this.mEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            }
        });
        list = new ArrayList<>();
        list.add(emojiTab);
        list.add(new SampleTab());
        return list;
    }
}
