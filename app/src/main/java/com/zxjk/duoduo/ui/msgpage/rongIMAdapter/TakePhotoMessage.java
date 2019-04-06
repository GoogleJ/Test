package com.zxjk.duoduo.ui.msgpage.rongIMAdapter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.model.MessageContent;

/**
 * @author Administrator
 * @// TODO: 2019\4\6 0006 发送拍到的照片的信息
 */
@SuppressLint("ParcelCreator")
public class TakePhotoMessage extends MessageContent {

    private String userId;
    private String sendId;
    private String filePath;

    public TakePhotoMessage(){}


    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId",getUserId());
            jsonObj.put("sendId",getSendId());
            jsonObj.put("filePath",getFilePath());

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
    public TakePhotoMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("userId")) {
                setUserId(jsonObj.optString("userId"));
            }

            if (jsonObj.has("sendId")) {
                setSendId(jsonObj.optString("sendId"));
            }

            if (jsonObj.has("filePath")) {
                setFilePath(jsonObj.optString("filePath"));
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
        ParcelUtils.writeToParcel(dest,userId);
        ParcelUtils.writeToParcel(dest,sendId);
        ParcelUtils.writeToParcel(dest,filePath);

    }
    //给消息赋值。
    public TakePhotoMessage(Parcel in) {
        setUserId(ParcelUtils.readFromParcel(in));
        //这里可继续增加你消息的属性
        setSendId(ParcelUtils.readFromParcel(in));
        setFilePath(ParcelUtils.readFromParcel(in));
    }
    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<BusinessCardMessage> CREATOR = new Creator<BusinessCardMessage>() {

        @Override
        public BusinessCardMessage createFromParcel(Parcel source) {
            return new BusinessCardMessage(source);
        }

        @Override
        public BusinessCardMessage[] newArray(int size) {
            return new BusinessCardMessage[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
