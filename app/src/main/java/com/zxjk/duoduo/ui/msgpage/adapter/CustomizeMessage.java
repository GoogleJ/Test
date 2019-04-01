package com.zxjk.duoduo.ui.msgpage.adapter;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * @author Administrator
 * @// TODO: 2019\4\1 0001 融云发消息的自定义实现
 */
//    MessageTag.NONE	为空值，不表示任何意义，发送的自定义消息不会在会话页面和会话列表中展示。
//    MessageTag.ISCOUNTED	表示客户端收到消息后，要进行未读消息计数（未读消息数增加 1），所有内容型消息都应该设置此值。非内容类消息暂不支持消息计数。
//    MessageTag.ISPERSISTED	表示客户端收到消息后，要进行存储，并在之后可以通过接口查询，存储后会在会话界面中显示。
//    MessageTag.STATUS	在本地不存储，不计入未读数，并且如果对方不在线，服务器会直接丢弃该消息，对方如果之后再上线也不会再收到此消息(聊天室类型除外，此类消息聊天室会视为普通消息)。
@SuppressLint("ParcelCreator")
@MessageTag(value = "app:custom", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomizeMessage extends MessageContent {

    //自定义的属性
    private String title;
    private String storeName;
    private String desc1;
    private String desc2;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    /*
     *
     * 实现 encode() 方法，该方法的功能是将消息属性封装成 json 串，
     * 再将 json 串转成 byte 数组，该方法会在发消息时调用，如下面示例代码：
     * */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("title", this.getTitle());
            jsonObj.put("storeName",this.getStoreName());
            jsonObj.put("desc1",this.getDesc1());
            jsonObj.put("desc2",this.getDesc2());

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

    /*
     * 覆盖父类的 MessageContent(byte[] data) 构造方法，该方法将对收到的消息进行解析，
     * 先由 byte 转成 json 字符串，再将 json 中内容取出赋值给消息属性。
     * */
    public CustomizeMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("title")) {
                setTitle(jsonObj.optString("title"));
            }

            if (jsonObj.has("storeName")) {
                setStoreName(jsonObj.optString("storeName"));
            }

            if (jsonObj.has("desc1")) {
                setDesc1(jsonObj.optString("desc1"));
            }

            if (jsonObj.has("desc2")) {
                setDesc2(jsonObj.optString("desc2"));
            }

        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }
    //给消息赋值。
    public CustomizeMessage(Parcel in) {

        setTitle(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        //这里可继续增加你消息的属性
        setStoreName(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setDesc1(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
        setDesc2(ParcelUtils.readFromParcel(in));//该类为工具类，消息属性
    }
    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<CustomizeMessage> CREATOR = new Creator<CustomizeMessage>() {

        @Override
        public CustomizeMessage createFromParcel(Parcel source) {
            return new CustomizeMessage(source);
        }

        @Override
        public CustomizeMessage[] newArray(int size) {
            return new CustomizeMessage[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, getTitle());
        ParcelUtils.writeToParcel(dest, getStoreName());
        ParcelUtils.writeToParcel(dest, getDesc1());
        ParcelUtils.writeToParcel(dest, getDesc2());
    }



}
