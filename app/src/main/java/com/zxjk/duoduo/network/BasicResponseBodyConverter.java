package com.zxjk.duoduo.network;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import org.json.JSONException;
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
            if (TextUtils.isEmpty(data) || data.equals("")) {
                throw new IOException(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adapter.fromJson(json);
    }
}
