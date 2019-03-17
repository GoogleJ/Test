package com.zxjk.duoduo;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class TestBodyConverter<T> implements Converter<ResponseBody, T> {

    Type type;

    public TestBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Reader reader = value.charStream();
        String targetString;

        targetString = IOUtils.toString(reader);

        reader = new StringReader(targetString);

        try {
            JSONObject jsonObject = new JSONObject(targetString);
            String data = jsonObject.optString("data");
            if (data.equals("")) {
                String msg = jsonObject.optString("msg");
                int code = jsonObject.optInt("code");
                jsonObject = new JSONObject(new Gson().toJson(new EmptyC()));
                jsonObject.put("msg",msg);
                jsonObject.put("code",code);
                LogUtils.e("ss",jsonObject.toString());
                return new Gson().fromJson(jsonObject.toString(), type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(targetString, type);
    }
}
