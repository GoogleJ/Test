package com.zxjk.duoduo.ui.msgpage.rongIM.message;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@SuppressLint("ParcelCreator")
@MessageTag(value = "duoduo:system:system", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class DuoDuoMessage extends MessageContent {
    private String content;
    private String extra;

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("content", getContent());
            jsonObj.put("extra", getExtra());
        } catch (JSONException e) {
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public DuoDuoMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content")) {
                setContent(jsonObj.optString("content"));
            }

            if (jsonObj.has("extra")) {
                setExtra(jsonObj.optString("extra"));
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
    public void writeToParcel(Parcel dest, int i) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extra);
    }

    //给消息赋值。
    public DuoDuoMessage(Parcel in) {
        //这里可继续增加你消息的属性
        setContent(ParcelUtils.readFromParcel(in));
        setExtra(ParcelUtils.readFromParcel(in));
    }

    public static final Creator<DuoDuoMessage> CREATOR = new Creator<DuoDuoMessage>() {

        @Override
        public DuoDuoMessage createFromParcel(Parcel source) {
            return new DuoDuoMessage(source);
        }

        @Override
        public DuoDuoMessage[] newArray(int size) {
            return new DuoDuoMessage[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public static Creator<DuoDuoMessage> getCREATOR() {
        return CREATOR;
    }
}
