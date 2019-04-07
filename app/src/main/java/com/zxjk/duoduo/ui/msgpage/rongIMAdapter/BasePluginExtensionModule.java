package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;


import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.CombineLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * @author Administrator
 * @// TODO: 2019\4\2 0002 自定义+号下的plugin
 */
public class BasePluginExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        RedPacketPlugin packetPlugin = new RedPacketPlugin();
        PhotoSelectorPlugin photoSelectorPlugin = new PhotoSelectorPlugin();
        TakePhotoPlugin takePhotoPlugin = new TakePhotoPlugin();
        TransferPlugin transferPlugin = new TransferPlugin();
        VoiceCallsPlugin voiceCallsPlugin = new VoiceCallsPlugin();
        BusinessCardPlugin businessCardPlugin =new BusinessCardPlugin();
        MyCombineLocationPlugin locationPlugin=new MyCombineLocationPlugin();
        CollectionPlugin collectionPlugin=new CollectionPlugin();

        List<IPluginModule> list = super.getPluginModules(conversationType);
        if (list != null) {
            list.clear();
            list.add(photoSelectorPlugin);
            list.add(takePhotoPlugin);
            list.add(transferPlugin);
            list.add(voiceCallsPlugin);
            list.add(packetPlugin);
            list.add(businessCardPlugin);
            list.add(locationPlugin);
            list.add(collectionPlugin);
        }

        return list;
    }
}
