package com.zxjk.duoduo.network;

import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.UpdateCustomerInfoResponse;

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

    /**
     * 这里是注册接口
     *
     * @param phone
     * @param code
     * @param pwd
     * @return
     */
    @POST("duoduo/customer/appUserRegister")
    @FormUrlEncoded
    Observable<BaseResponse<String>> register(
            @Field("mobile") String phone,
            @Field("securityCode") String code,
            @Field("pwd") String pwd
    );

    /**
     * 这里是短信验证码接口
     *
     * @param phone
     * @return
     */
    @POST("duoduo/getCode")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getCode(
            @Field("mobile") String phone
    );

    /**
     * 这里是忘记密码
     *
     * @param phone
     * @param pwd
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("duoduo/customer/forgetPwd")
    Observable<BaseResponse<String>> forgetPwd(
            @Field("mobile") String phone,
            @Field("pwd") String pwd,
            @Field("securityCode") String code
    );


    //更新用户信息
    @POST("duoduo/customer/updateCustomerInfo")
    @FormUrlEncoded
    Observable<BaseResponse<UpdateCustomerInfoResponse>> updateUserInfo(@Field("customerInfo") String customerInfo);

    /**
     * 修改支付密码
     * @param oldPwd
     * @param newPwdOne
     * @param newPwdTwo
     * @return
     */
    @POST("duoduo/customer/updatePayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> updatePwd(
            @Field("oldPayPwd")String oldPwd,
            @Field("newPayPwd")String newPwdOne,
            @Field("newPayPwdTwo")String newPwdTwo
    );



}
