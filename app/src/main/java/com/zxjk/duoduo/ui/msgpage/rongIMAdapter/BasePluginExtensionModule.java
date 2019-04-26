package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;


import com.zxjk.duoduo.Constant;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameDownScorePlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameDuobaoPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRecordPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameRulesPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameStartPlugin;
import com.zxjk.duoduo.ui.msgpage.rongIMAdapter.gameplugin.GameUpScorePlugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * @author Administrator
 */
public class BasePluginExtensionModule extends DefaultExtensionModule {

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

        List<IPluginModule> list = super.getPluginModules(conversationType);

        if (list != null) {
            list.clear();
            list.add(photoSelectorPlugin);
            list.add(takePhotoPlugin);
            list.add(transferPlugin);
//            list.add(voiceCallsPlugin);
            list.add(packetPlugin);
            list.add(businessCardPlugin);
            list.add(locationPlugin);
//            list.add(collectionPlugin);

        }

        return list;
    }
}
