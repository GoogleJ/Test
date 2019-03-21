package com.zxjk.duoduo.network;

import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;
import com.zxjk.duoduo.network.response.UpdateCustomerInfoResponse;

import java.util.List;

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
    Observable<BaseResponse<UpdateCustomerInfoResponse>> updateUserInfo(
            @Field("customerInfo") String customerInfo);

    /**
     * 修改支付密码
     *
     * @param oldPwd
     * @param newPwdOne
     * @param newPwdTwo
     * @return
     */
    @POST("duoduo/customer/updatePayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> updatePwd(
            @Field("oldPayPwd") String oldPwd,
            @Field("newPayPwd") String newPwdOne,
            @Field("newPayPwdTwo") String newPwdTwo
    );

    /**
     * 获取好友详情
     *
     * @param friendId
     * @return
     */
    @POST("duoduo/friend/getFriendInfoById")
    @FormUrlEncoded
    Observable<BaseResponse<FriendInfoResponse>> getFriendInfoById(
            @Field("friendId") String friendId
    );

    /**
     * 获取好友列表
     *
     * @return
     */
    @POST("duoduo/friend/getFriendListById")
    Observable<BaseResponse<List<FriendListResponse>>> getFriendListById();

    /**
     * 模糊搜索好友
     *
     * @param data
     * @return
     */
    @POST("duoduo/friend/searchFriend")
    @FormUrlEncoded
    Observable<BaseResponse<List<SearchResponse>>> searchFriend(
            @Field("data") String data
    );

    /**
     * 模糊搜索用户
     *
     * @param data
     * @return
     */
    @POST("duoduo/friend/searchCustomer")
    @FormUrlEncoded
    Observable<BaseResponse<List<SearchCustomerInfoResponse>>> searchCustomerInfo(
            @Field("data") String data
    );

    /**
     * 申请添加好友
     *
     * @param friendId
     * @param remark
     * @return
     */
    @POST("duoduo/friend/applyAddFriend")
    @FormUrlEncoded
    Observable<BaseResponse<List<FriendListResponse>>> applyAddFriend(
            @Field("friendId") String friendId,
            @Field("remark") String remark
    );

    /**
     * 获取待添加的好友列表
     *
     * @return
     */
    @POST("duoduo/friend/getMyfriendsWaiting")
    Observable<BaseResponse<List<FriendListResponse>>> getMyFirendsWaiting();

    /**
     * 同意添加
     *
     * @param friendId
     * @param markName
     * @return
     */
    @FormUrlEncoded
    @POST("duoduo/friend/addFriend")
    Observable<BaseResponse<String>> addFriend(
            @Field("friendId") String friendId,
            @Field("markName") String markName
    );

    /**
     * 删除好友
     *
     * @param friendId
     * @return
     */
    @POST("duoduo/friend/deleteMyfirendsWaiting")
    @FormUrlEncoded
    Observable<BaseResponse<String>> deleteMyfirendsWaiting(
            @Field("deleteCustomerId") String friendId
    );

    @POST("duoduo/customer/updateMobile")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateMobile(
            @Field("newMobile") String newMobile,
            @Field("securityCode") String securityCode
    );

    /**
     * 获取所有群组信息
     */
    @POST("duoduo/group/getGroupByCustomId")
    @FormUrlEncoded
    Observable<BaseResponse<List<GroupChatResponse>>> getMygroupinformation(@Field("customerId") String customerId);

}
