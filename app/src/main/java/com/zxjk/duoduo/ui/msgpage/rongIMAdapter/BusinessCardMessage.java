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
 * @// TODO: 2019\4\3 0003 名片分享 
 */
@SuppressLint("ParcelCreator")
@MessageTag(value = "app:business", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class BusinessCardMessage extends MessageContent {

    private String headerUrl;
    private String userName;
    private String duoduoId;
    //这里是userId或者friendId
    private String userId;

    public BusinessCardMessage() {

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("headerUrl", getHeaderUrl());
            jsonObj.put("userName",getUserName());
            jsonObj.put("duoduoId",getDuoduoId());
            jsonObj.put("userId",getUserId());
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
    public BusinessCardMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("headerUrl")) {
                setHeaderUrl(jsonObj.optString("headerUrl"));
            }

            if (jsonObj.has("userName")) {
                setUserName(jsonObj.optString("userName"));
            }

            if (jsonObj.has("duoduoId")) {
                setDuoduoId(jsonObj.optString("duoduoId"));
            }

            if (jsonObj.has("userId")) {
                setUserId(jsonObj.optString("userId"));
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
        ParcelUtils.writeToParcel(dest,headerUrl);
        ParcelUtils.writeToParcel(dest,userName);
        ParcelUtils.writeToParcel(dest,duoduoId);
        ParcelUtils.writeToParcel(dest,userId);
    }

    //给消息赋值。
    public BusinessCardMessage(Parcel in) {
        setHeaderUrl(ParcelUtils.readFromParcel(in));
        //这里可继续增加你消息的属性
        setUserName(ParcelUtils.readFromParcel(in));
        setDuoduoId(ParcelUtils.readFromParcel(in));
        setUserId(ParcelUtils.readFromParcel(in));
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

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDuoduoId() {
        return duoduoId;
    }

    public void setDuoduoId(String duoduoId) {
        this.duoduoId = duoduoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
