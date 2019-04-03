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
        PersonalBusinessCardPlugin personalBusinessCardPlugin=new PersonalBusinessCardPlugin();
        MyCombineLocationPlugin locationPlugin=new MyCombineLocationPlugin();
        CollectionPlugin collectionPlugin=new CollectionPlugin();

        List<IPluginModule> list = super.getPluginModules(conversationType);


        IPluginModule temp = null;
        IPluginModule temp1=null;
        IPluginModule temp2=null;
        for (IPluginModule module : list) {
            if (module instanceof FilePlugin) {
                temp = module;
                break;
            }

        }
        for (IPluginModule module1:list){
            if (module1 instanceof CombineLocationPlugin) {
                temp1 = module1;
                break;
            }
        }
       for (IPluginModule module2:list){
           if (module2 instanceof ImagePlugin) {
               temp2 = module2;
           }
       }
        list.remove(temp);
        list.remove(temp1);
        list.remove(temp2);
        list.add(photoSelectorPlugin);
        list.add(takePhotoPlugin);
        list.add(transferPlugin);
        list.add(voiceCallsPlugin);
        list.add(packetPlugin);
        list.add(personalBusinessCardPlugin);
        list.add(locationPlugin);
        list.add(collectionPlugin);
        return list;
    }
}
