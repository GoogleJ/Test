package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * @author Administrator
 * @// TODO: 2019\4\3 0003 关于红包的自定义消息 
 */
@SuppressLint("ParcelCreator")
@MessageTag(value = "app:custom", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class RedPacketMessage extends MessageContent {
    /**
     * sendUserId : 14
     * receiveUserId : 15
     * money : 0.001
     * message : 恭喜发财，大吉大利
     * paypwd : 123456
     */

    private String sendUserId;
    private String receiveUserId;
    private double money;
    private String message;
    private String paypwd;

    public RedPacketMessage() {

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("sendUserId", getSendUserId());
            jsonObj.put("receiveUserId",getReceiveUserId());
            jsonObj.put("money",getMoney());
            jsonObj.put("paypwd",getPaypwd());
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RedPacketMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("sendUserId")) {
                setSendUserId(jsonObj.optString("sendUserId"));
            }

            if (jsonObj.has("receiveUserId")) {
                setReceiveUserId(jsonObj.optString("receiveUserId"));
            }

            if (jsonObj.has("money")) {
                setMoney(jsonObj.optDouble("money"));
            }

            if (jsonObj.has("paypwd")) {
                setPaypwd(jsonObj.optString("paypwd"));
            }

        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest,sendUserId);
        ParcelUtils.writeToParcel(dest,receiveUserId);
        ParcelUtils.writeToParcel(dest,money);
        ParcelUtils.writeToParcel(dest,message);
        ParcelUtils.writeToParcel(dest,paypwd);

    }

    //给消息赋值。
    public RedPacketMessage(Parcel in) {
        setSendUserId(ParcelUtils.readFromParcel(in));
        //这里可继续增加你消息的属性
        setReceiveUserId(ParcelUtils.readFromParcel(in));
        setMoney(ParcelUtils.readDoubleFromParcel(in));
        setMessage(ParcelUtils.readFromParcel(in));
        setPaypwd(ParcelUtils.readFromParcel(in));



        //这里可继续增加你消息的属性
    }
    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RedPacketMessage> CREATOR = new Creator<RedPacketMessage>() {

        @Override
        public RedPacketMessage createFromParcel(Parcel source) {
            return new RedPacketMessage(source);
        }

        @Override
        public RedPacketMessage[] newArray(int size) {
            return new RedPacketMessage[size];
        }
    };



    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }
}
