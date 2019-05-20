package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.zxjk.duoduo.R;

import java.util.ArrayList;
import java.util.Locale;

import io.rong.callkit.CallSelectMemberActivity;
import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongVoIPIntent;
import io.rong.callkit.util.CallKitUtils;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.common.RLog;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;


/**
 * *********************
 * Administrator
 * *********************
 * 2019/5/20
 * *********************
 * 音视频
 * *********************
 */
public class AudioVideoPlugin implements IPluginModule, IPluginRequestPermissionResultCallback {
    private static final String TAG = "VideoPlugin";
    private ArrayList<String> allMembers;
    private Context context;
    private Conversation.ConversationType conversationType;
    private String targetId;
    private static final int REQEUST_CODE_RECORD_AUDIO_PERMISSION = 101;

    private boolean isType;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_audio);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.audio_video);
    }

    @Override
    public void onClick(Fragment currentFragment, RongExtension extension) {
        NiceDialog.init().setLayoutId(R.layout.layout_general_dialog8).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                //视频通话
                holder.setOnClickListener(R.id.tv_videoCall, v -> {
                    dialog.dismiss();
                    isType = false;
                    video(currentFragment, extension);

                });
                //语音通话
                holder.setOnClickListener(R.id.tv_voiceCall, v -> {
                    dialog.dismiss();
                    isType = true;
                    voice(currentFragment, extension);

                });
            }
        }).setShowBottom(true)
                .setOutCancel(true)
                .setDimAmount(0.5f)
                .show(currentFragment.getActivity().getSupportFragmentManager());
    }

    /**
     * 视频通话
     *
     * @param currentFragment
     * @param extension
     */
    private void video(Fragment currentFragment, RongExtension extension) {
        context = currentFragment.getActivity().getApplicationContext();
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();

        String[] permissions = CallKitUtils.getCallpermissions();
        if (PermissionCheckUtil.checkPermissions(currentFragment.getActivity(), permissions)) {
            startVideoActivity(extension);
        } else {
            extension.requestPermissionForPluginResult(permissions, IPluginRequestPermissionResultCallback.REQUEST_CODE_PERMISSION_PLUGIN, this);
        }

    }

    private void startVideoActivity(final RongExtension extension) {

        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getStartTime() > 0) {
            Toast.makeText(context,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            context.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                            context.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(context, context.getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
            intent.putExtra("conversationType", conversationType.getName().toLowerCase(Locale.US));
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.getApplicationContext().startActivity(intent);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {

                    Intent intent = new Intent(context, CallSelectMemberActivity.class);
                    allMembers = (ArrayList<String>) discussion.getMemberIdList();
                    intent.putStringArrayListExtra("allMembers", allMembers);
                    String myId = RongIMClient.getInstance().getCurrentUserId();
                    ArrayList<String> invited = new ArrayList<>();
                    invited.add(myId);
                    intent.putStringArrayListExtra("invitedMembers", invited);
                    intent.putExtra("conversationType", conversationType.getValue());
                    intent.putExtra("mediaType", RongCallCommon.CallMediaType.VIDEO.getValue());
                    extension.startActivityForPluginResult(intent, 110, AudioVideoPlugin.this);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.d(TAG, "get discussion errorCode = " + e.getValue());
                }
            });
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            Intent intent = new Intent(context, CallSelectMemberActivity.class);
            String myId = RongIMClient.getInstance().getCurrentUserId();
            ArrayList<String> invited = new ArrayList<>();
            invited.add(myId);
            intent.putStringArrayListExtra("invitedMembers", invited);
            intent.putExtra("groupId", targetId);
            intent.putExtra("conversationType", conversationType.getValue());
            intent.putExtra("mediaType", RongCallCommon.CallMediaType.VIDEO.getValue());
            extension.startActivityForPluginResult(intent, 110, this);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (isType) {
            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_MULTIAUDIO);
            ArrayList<String> userIds = data.getStringArrayListExtra("invited");
            ArrayList<String> observers = data.getStringArrayListExtra("observers");
            userIds.add(RongIMClient.getInstance().getCurrentUserId());
            intent.putExtra("conversationType", conversationType.getName().toLowerCase());
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.putStringArrayListExtra("invitedUsers", userIds);
            intent.putStringArrayListExtra("observers", observers);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.getApplicationContext().startActivity(intent);
        } else {
            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_MULTIVIDEO);
            ArrayList<String> userIds = data.getStringArrayListExtra("invited");
            ArrayList<String> observerIds = data.getStringArrayListExtra("observers");
            userIds.add(RongIMClient.getInstance().getCurrentUserId());
            intent.putExtra("conversationType", conversationType.getName().toLowerCase(Locale.US));
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.putStringArrayListExtra("invitedUsers", userIds);
            intent.putStringArrayListExtra("observerUsers", observerIds);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.getApplicationContext().startActivity(intent);
        }


    }


    @Override
    public boolean onRequestPermissionResult(Fragment fragment, RongExtension extension, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionCheckUtil.checkPermissions(fragment.getActivity(), permissions)) {
            startVideoActivity(extension);
        } else {
            extension.showRequestPermissionFailedAlter(PermissionCheckUtil.getNotGrantedPermissionMsg(context, permissions, grantResults));
        }
        return true;
    }
//    @Override
//    public boolean onRequestPermissionResult(Fragment fragment, RongExtension extension, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (PermissionCheckUtil.checkPermissions(fragment.getActivity(), permissions)) {
//            startAudioActivity(fragment, extension);
//        } else {
//            extension.showRequestPermissionFailedAlter(PermissionCheckUtil.getNotGrantedPermissionMsg(context, permissions, grantResults));
//        }
//        return true;
//    }

    /**
     * 语音通话
     *
     * @param currentFragment
     * @param extension
     */
    private void voice(Fragment currentFragment, RongExtension extension) {
        context = currentFragment.getActivity().getApplicationContext();
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        Log.i(TAG, "---- targetId==" + targetId);
        String[] permissions = CallKitUtils.getCallpermissions();
        if (PermissionCheckUtil.checkPermissions(currentFragment.getActivity(), permissions)) {
            Log.i(TAG, "---- startAudioActivity ----");
            startAudioActivity(currentFragment, extension);
        } else {
            Log.i(TAG, "---- requestPermissionForPluginResult ----");
            extension.requestPermissionForPluginResult(permissions, REQEUST_CODE_RECORD_AUDIO_PERMISSION, this);
        }
    }


    private void startAudioActivity(Fragment currentFragment, final RongExtension extension) {
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getStartTime() > 0) {
            Toast.makeText(context,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            currentFragment.getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                            currentFragment.getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(context, currentFragment.getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
            intent.putExtra("conversationType", conversationType.getName().toLowerCase());
            Log.i(TAG, "---- conversationType.getName().toLowerCase() =-" + conversationType.getName().toLowerCase());
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            Log.i(TAG, "---- callAction=" + RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i(TAG, "getPackageName===" + context.getPackageName());
            intent.setPackage(context.getPackageName());
            context.getApplicationContext().startActivity(intent);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    Intent intent = new Intent(context, CallSelectMemberActivity.class);
                    allMembers = (ArrayList<String>) discussion.getMemberIdList();
                    intent.putStringArrayListExtra("allMembers", allMembers);
                    intent.putExtra("conversationType", conversationType.getValue());
                    String myId = RongIMClient.getInstance().getCurrentUserId();
                    ArrayList<String> invited = new ArrayList<>();
                    invited.add(myId);
                    intent.putStringArrayListExtra("invitedMembers", invited);
                    intent.putExtra("mediaType", RongCallCommon.CallMediaType.AUDIO.getValue());
                    extension.startActivityForPluginResult(intent, 110, AudioVideoPlugin.this);
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {
                    RLog.d(TAG, "get discussion errorCode = " + e.getValue());
                }
            });
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            Intent intent = new Intent(context, CallSelectMemberActivity.class);
            String myId = RongIMClient.getInstance().getCurrentUserId();
            ArrayList<String> invited = new ArrayList<>();
            invited.add(myId);
            intent.putStringArrayListExtra("invitedMembers", invited);
            intent.putExtra("conversationType", conversationType.getValue());
            intent.putExtra("groupId", targetId);
            intent.putExtra("mediaType", RongCallCommon.CallMediaType.AUDIO.getValue());
            extension.startActivityForPluginResult(intent, 110, this);
        }
    }


}
