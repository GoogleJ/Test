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
 * @// TODO: 2019\4\3 0003 关于转账的自定义消息
 */
@SuppressLint("ParcelCreator")
@MessageTag(value = "app:transfer", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class TransferMessage extends MessageContent {

    /**
     * 里头的所有字段实体可根据需求自己定义
     *toCustomerId 接受者用户Id
     * hk 转账金额
     *payPwd 支付密码
     * remarks 转账说明
     */
    private String toCustomerId;
    private String hk;
    private String payPwd;
    private String remarks;
    private String type;
    private String transferId;

    public TransferMessage(){}

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("toCustomerId", getToCustomerId());
            jsonObj.put("hk",getHk());
            jsonObj.put("payPwd",getPayPwd());
            jsonObj.put("remarks",getRemarks());
            jsonObj.put("type",getType());
            jsonObj.put("transferId",getTransferId());
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
    public TransferMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("toCustomerId")) {
                setToCustomerId(jsonObj.optString("toCustomerId"));
            }

            if (jsonObj.has("hk")) {
                setHk(jsonObj.optString("hk"));
            }

            if (jsonObj.has("payPwd")) {
                setPayPwd(jsonObj.optString("payPwd"));
            }

            if (jsonObj.has("remarks")) {
                setRemarks(jsonObj.optString("remarks"));
            }

            if (jsonObj.has("type")) {
                setType(jsonObj.optString("type"));
            }

            if (jsonObj.has("transferId")) {
                setTransferId(jsonObj.optString("transferId"));
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
        ParcelUtils.writeToParcel(dest,toCustomerId);
        ParcelUtils.writeToParcel(dest,hk);
        ParcelUtils.writeToParcel(dest,payPwd);
        ParcelUtils.writeToParcel(dest,remarks);
        ParcelUtils.writeToParcel(dest,type);
        ParcelUtils.writeToParcel(dest,transferId);
    }
    //给消息赋值。
    public TransferMessage(Parcel in) {
        setToCustomerId(ParcelUtils.readFromParcel(in));
        //这里可继续增加你消息的属性
        setHk(ParcelUtils.readFromParcel(in));
        setPayPwd(ParcelUtils.readFromParcel(in));
        setRemarks(ParcelUtils.readFromParcel(in));
        setType(ParcelUtils.readFromParcel(in));
        setTransferId(ParcelUtils.readFromParcel(in));
    }
    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<TransferMessage> CREATOR = new Creator<TransferMessage>() {

        @Override
        public TransferMessage createFromParcel(Parcel source) {
            return new TransferMessage(source);
        }

        @Override
        public TransferMessage[] newArray(int size) {
            return new TransferMessage[size];
        }
    };

    public String getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(String toCustomerId) {
        this.toCustomerId = toCustomerId;
    }

    public String getHk() {
        return hk;
    }

    public void setHk(String hk) {
        this.hk = hk;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}
