package com.zxjk.duoduo.network;

import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.FriendListResponse;
import com.zxjk.duoduo.network.response.GetBalanceHkResponse;
import com.zxjk.duoduo.network.response.GetNumbeOfTransactionResponse;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.network.response.GetReleasePurchaseResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.PayInfoResponse;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;
import com.zxjk.duoduo.network.response.SearchCustomerInfoResponse;
import com.zxjk.duoduo.network.response.SearchResponse;

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
    Observable<BaseResponse<String>> updateUserInfo(@Field("customerInfo") String customerInfo);

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
    Observable<BaseResponse<LoginResponse>> updatePayPwd(
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
    Observable<BaseResponse<String>> applyAddFriend(
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
     * 删除好友申请
     *
     * @param friendId
     * @return
     */
    @POST("duoduo/friend/deleteMyfirendsWaiting")
    @FormUrlEncoded
    Observable<BaseResponse<String>> deleteMyfirendsWaiting(
            @Field("deleteCustomerId") String friendId
    );

    /**
     * 修改手机号
     *
     * @param newMobile
     * @param securityCode
     * @return
     */
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


    /**
     * 个人中心-获取账户信息
     *
     * @return
     */
    @POST("duoduo/exchange/getBalanceHk")
    Observable<BaseResponse<GetBalanceHkResponse>> getBalanceHk();

    /**
     * 获取交易额度
     *
     * @return
     */
    @POST("duoduo/exchange/getNumbeOfTransaction")
    Observable<BaseResponse<GetNumbeOfTransactionResponse>> getNumbeOfTransaction();

    /**
     * 个人发布出售币种
     *
     * @param number
     * @param money
     * @param currency
     * @param paypwd
     * @param payTpye
     * @return
     */
    @POST("duoduo/exchange/releasePurchase")
    @FormUrlEncoded
    Observable<BaseResponse<ReleasePurchase>> releasePurchase(@Field("number") String number,
                                                              @Field("money") String money, @Field("currency") String currency, @Field("payPwd") String paypwd, @Field("payTpye") String payTpye);

    /**
     * 发布购买币种信息
     *
     * @param number
     * @param money
     * @param currency
     * @param payTpye
     * @return
     */
    @POST("duoduo/exchange/releaseSale")
    @FormUrlEncoded
    Observable<BaseResponse<ReleaseSaleResponse>> releaseSale(@Field("number") String number,
                                                              @Field("money") String money,
                                                              @Field("currency") String currency,
                                                              @Field("payTpye") String payTpye);

    /**
     * 获取用户收款方式
     *
     * @return
     */
    @POST("duoduo/customer/getPayInfo")
    Observable<BaseResponse<List<PayInfoResponse>>> getPayInfo();

    /**
     * 删除好友相关
     *
     * @return
     */
    @POST("duoduo/friend/deleteFriend")
    @FormUrlEncoded
    Observable<BaseResponse<String>> deleteFriend(
            @Field("friendId") String friendId
    );

    /**
     * 修改登录密码
     *
     * @param oldPwd
     * @param newPwdOne
     * @param newPwdTwo
     * @return
     */
    @POST("duoduo/exchange/updatePwd")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updatePwd(
            @Field("oldPwd") String oldPwd,
            @Field("newPwdOne") String newPwdOne,
            @Field("newPwdTwo") String newPwdTwo
    );

    /**
     * 退出登录
     *
     * @return
     */
    @POST("duoduo/loginOut")
    Observable<BaseResponse<String>> loginOut();

    /**
     * 新增（修改收款方式）
     *
     * @return
     */
    @POST("duoduo/customer/addPayInfo")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addPayInfo(
            @Field("data") String data
    );

    /**
     * 实名认证
     *
     * @param data
     * @return
     */
    @POST("duoduo/customer/certification")
    @FormUrlEncoded
    Observable<BaseResponse<String>> certification(
            @Field("data") String data
    );

    /**
     * 撤销发布的卖单
     *
     * @param sellOrderId
     * @return
     */
    @POST("duoduo/exchange/closeSellOrder")
    @FormUrlEncoded
    Observable<BaseResponse<String>> closeSellOrder(@Field("sellOrderId") String sellOrderId);

    /**
     * 买家撤销订单
     *
     * @param buyOrderId
     * @param bothOrderId
     * @param sellOrderId
     * @return
     */
    @POST("duoduo/exchange/cancelled")
    @FormUrlEncoded
    Observable<BaseResponse<String>> cancelled(@Field("buyOrderId") String buyOrderId
            , @Field("bothOrderId") String bothOrderId
            , @Field("sellOrderId") String sellOrderId);

    /**
     * 查询用户是否可以进行买单操作
     *
     * @return
     */
    @POST("duoduo/exchange/isConfine")
    Observable<BaseResponse<String>> isConfine();

    /**
     * 完成交易
     *
     * @param buyCustomerId
     * @param buyOrderId
     * @param sellOrderId
     * @param bothOrderId
     * @return
     */
    @POST("duoduo/exchange/overOrder")
    @FormUrlEncoded
    Observable<BaseResponse<String>> overOrder(@Field("buyCustomerId") String buyCustomerId
            , @Field("buyOrderId") String buyOrderId
            , @Field("sellOrderId") String sellOrderId
            , @Field("bothOrderId") String bothOrderId);

    /**
     * 卖家拒绝审核
     *
     * @param buyOrderId
     * @param bothOrderId
     * @param sellOrderId
     * @return
     */
    @POST("duoduo/exchange/rejectAudit")
    @FormUrlEncoded
    Observable<BaseResponse<String>> rejectAudit(
            @Field("buyOrderId") String buyOrderId
            , @Field("bothOrderId") String bothOrderId
            , @Field("sellOrderId") String sellOrderId);

    /**
     * 买家确认付款
     *
     * @param bothOrderId
     * @return
     */
    @POST("duoduo/exchange/updateBuyPayState")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateBuyPayState(@Field("bothOrderId") String bothOrderId);

    /**
     * 判断是否允许修改支付方式
     *
     * @return
     */
    @POST("duoduo/customer/updatePayInfo")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updatePayInfo(
            @Field("payType")String payType
            );

    /**
     * 获取用户实名认证状态
     * @return
     */
    @POST("duoduo/customer/getCustomerAuth")
    Observable<BaseResponse<String>> getCustomerAuth();

    @POST("duoduo/exchange/getOverOrder")
    Observable<BaseResponse<List<GetOverOrderResponse>>> getOverOrder();

    @POST("duoduo/exchange/getReleasePurchase")
    Observable<BaseResponse<List<GetReleasePurchaseResponse>>> getReleasePurchase();

    @POST("duoduo/wallet/getWallet")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getWallet(@Field("customerDuoDuoId") String customerDuoDuoId);

}
