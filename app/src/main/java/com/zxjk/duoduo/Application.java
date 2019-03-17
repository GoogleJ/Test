package com.zxjk.duoduo;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.blankj.utilcode.util.Utils;

public class Application extends android.app.Application {

    public static OSS oss;

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

        initOSS();

    }

    //初始化阿里云OSS上传服务
    private void initOSS() {
        String AK = "LTAI8wpEjXW0r2y9";
        String SK = "4Dv0UhSB16KLPKC8GR6DuH9GTEGil7";
        OSSPlainTextAKSKCredentialProvider ossPlainTextAKSKCredentialProvider =
                new OSSPlainTextAKSKCredentialProvider(AK, SK);
        String endpoint = "oss-cn-hongkong.aliyuncs.com";
        oss = new OSSClient(this, endpoint, ossPlainTextAKSKCredentialProvider);
    }
}
