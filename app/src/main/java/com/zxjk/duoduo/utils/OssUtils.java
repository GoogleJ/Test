package com.zxjk.duoduo.utils;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zxjk.duoduo.Application;
import com.zxjk.duoduo.Constant;

public class OssUtils {

    public interface OssCallBack {
        void onSuccess();

        void onError();
    }

    public static void uploadFile(String filePath, OssCallBack callBack) {

        PutObjectRequest put = new PutObjectRequest("zhongxingjike", "upload/" +
                Constant.userId + System.currentTimeMillis(), filePath);

        Application.oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                if (callBack != null) {
                    callBack.onError();
                }
            }
        });
    }
}
