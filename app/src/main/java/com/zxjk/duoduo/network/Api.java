package com.zxjk.duoduo.network;

import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    /**
     * 这里是登录
     *
     * @param phone
     * @param pwd
     * @return
     */
    @POST("duoduo/login")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> login(
            @Field("mobile") String phone,
            @Field("pwd") String pwd
    );

    //更新用户信息
    @POST("duoduo/customer/updateCustomerInfo")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> updateUserInfo(@Field("customerInfo") String customerInfo);

}
