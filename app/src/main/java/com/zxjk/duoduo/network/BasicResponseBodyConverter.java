package com.zxjk.duoduo.network;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import com.zxjk.duoduo.network.rx.RxException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BasicResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final TypeAdapter<T> adapter;

    BasicResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String data = jsonObject.optString("data");
            String msg = jsonObject.optString("msg");
            int code = jsonObject.optInt("code");
            if (TextUtils.isEmpty(data)) {
                throw new RxException.EmptyDataExceptioin(msg, code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adapter.fromJson(json);
    }
}
