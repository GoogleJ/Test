package com.zxjk.duoduo.network;

import com.zxjk.duoduo.network.response.AllGroupMembersResponse;
import com.zxjk.duoduo.network.response.BaseResponse;
import com.zxjk.duoduo.network.response.CreateWalletResponse;
import com.zxjk.duoduo.network.response.FriendInfoResponse;
import com.zxjk.duoduo.network.response.GetBalanceHkResponse;
import com.zxjk.duoduo.network.response.GetNumbeOfTransactionResponse;
import com.zxjk.duoduo.network.response.GetOverOrderResponse;
import com.zxjk.duoduo.network.response.GetRedPackageRecordResponse;
import com.zxjk.duoduo.network.response.GetReleasePurchaseResponse;
import com.zxjk.duoduo.network.response.GetTransferAllResponse;
import com.zxjk.duoduo.network.response.GetTransferEthResponse;
import com.zxjk.duoduo.network.response.GroupChatResponse;
import com.zxjk.duoduo.network.response.GroupResponse;
import com.zxjk.duoduo.network.response.LoginResponse;
import com.zxjk.duoduo.network.response.PayInfoResponse;
import com.zxjk.duoduo.network.response.PayTypeResponse;
import com.zxjk.duoduo.network.response.RedPackageResponse;
import com.zxjk.duoduo.network.response.ReleaseSaleResponse;

import com.zxjk.duoduo.network.response.SignHkbOrHkExchangeResponse;
import com.zxjk.duoduo.network.response.SignTransactionResponse;
import com.zxjk.duoduo.network.response.TransferResponse;

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
    Observable<BaseResponse<LoginResponse>> register(
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
            @Field("mobile") String phone,
            @Field("type") String type
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
    Observable<BaseResponse<List<FriendInfoResponse>>> getFriendListById();

    /**
     * 模糊搜索好友
     *
     * @param data
     * @return
     */
    @POST("duoduo/friend/searchFriend")
    @FormUrlEncoded
    Observable<BaseResponse<List<FriendInfoResponse>>> searchFriend(
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
    Observable<BaseResponse<List<FriendInfoResponse>>> searchCustomerInfo(
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
    Observable<BaseResponse<List<FriendInfoResponse>>> getMyFirendsWaiting();

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
     * 修改好友备注
     *
     * @param friendId
     * @param remark
     * @return
     */
    @POST("duoduo/friend/updateRemark")
    @FormUrlEncoded
    Observable<BaseResponse<FriendInfoResponse>> updateRemark(
            @Field("friendId") String friendId,
            @Field("remark") String remark
    );


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
            @Field("payType") String payType
    );

    /**
     * 获取用户实名认证状态
     *
     * @return
     */
    @POST("duoduo/customer/getCustomerAuth")
    Observable<BaseResponse<String>> getCustomerAuth();

    /**
     * 获取用户已完成订单
     *
     * @return
     */
    @POST("duoduo/exchange/getOverOrder")
    Observable<BaseResponse<List<GetOverOrderResponse>>> getOverOrder();

    /***
     * 获取用户发布的出售列表
     * @return
     */
    @POST("duoduo/exchange/getReleasePurchase")
    Observable<BaseResponse<List<GetReleasePurchaseResponse>>> getReleasePurchase();

    /**
     * 查看钱包地址及余额
     *
     * @param customerDuoDuoId
     * @return
     */
    @POST("duoduo/wallet/getWallet")
    @FormUrlEncoded
    Observable<BaseResponse<CreateWalletResponse>> getWallet(@Field("customerDuoDuoId") String customerDuoDuoId);

    /**
     * 找回支付密码
     *
     * @return
     */
    @POST("duoduo/customer/fandPayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<String>> fandPayPwd(
            @Field("number") String number,
            @Field("securityCode") String securityCode,
            @Field("newPayPwd") String newPayPwd,
            @Field("newPayPwdTwo") String newPayPwdTwo

    );

    /**
     * 创建群
     *
     * @param groupOwnerId
     * @param customerIds
     * @return
     */
    @POST("duoduo/group/makeGroup")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse>> makeGroup(
            @Field("groupOwnerId") String groupOwnerId,
            @Field("customerIds") String customerIds
    );

    /**
     * 查看群成员
     */
    @POST("duoduo/group/getGroupMemByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<List<AllGroupMembersResponse>>> getGroupMemByGroupId(
            @Field("groupId") String groupId
    );

    /**
     * 同意加入群聊
     *
     * @param groupId
     * @param inviterId
     * @param customerIds
     * @return
     */

    @POST("duoduo/group/enterGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> enterGroup(
            @Field("groupId") String groupId,
            @Field("inviterId") String inviterId,
            @Field("customerIds") String customerIds
    );

    /**
     * 根据groupId查看群信息
     *
     * @param groupId
     * @return
     */
    @POST("duoduo/group/getGroupByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse>> getGroupByGroupId(
            @Field("groupId") String groupId
    );

    /**
     * 修改群信息
     *
     * @param groupInfo
     * @return
     */
    @POST("duoduo/group/updateGroupInfo")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse>> updateGroupInfo(
            @Field("groupInfo") String groupInfo
    );

    /**
     * 解散群
     *
     * @param groupId
     * @param groupOwnerId
     * @return
     */
    @POST("duoduo/group/disBandGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> disBandGroup(
            @Field("groupId") String groupId,
            @Field("groupOwnerId") String groupOwnerId
    );

    /**
     * 退出群组
     *
     * @param groupId
     * @param customerId
     * @return
     */
    @POST("duoduo/group/exitGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> exitGroup(
            @Field("groupId") String groupId,
            @Field("customerId") String customerId
    );

    /**
     * 移除群组
     *
     * @param groupId
     * @param customerIds
     * @return
     */
    @POST("duoduo/group/moveOutGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> moveOutGroup(
            @Field("groupId") String groupId,
            @Field("customerIds") String customerIds
    );

    /**
     * 群主转让
     *
     * @param groupId
     * @param customerId
     * @return
     */
    @POST("duoduo/group/updateGroupOwner")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateGroupOwner(
            @Field("groupId") String groupId,
            @Field("customerId") String customerId
    );

    /**
     * 创建钱包
     *
     * @param customerDuoDuoId
     * @return
     */
    @POST("duoduo/wallet/createWallet")
    @FormUrlEncoded
    Observable<BaseResponse<CreateWalletResponse>> createWallet(@Field("customerDuoDuoId") String customerDuoDuoId);

    /**
     * 查看eth转账记录
     *
     * @param address
     * @param page
     * @param offset
     * @return
     */
    @POST("duoduo/wallet/getTransferEth")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferEthResponse>> getTransferEth(@Field("address") String address, @Field("page") String page, @Field("offset") String offset);


    /**
     * 转账
     *
     * @param payPwd
     * @param type
     * @param fromaddress
     * @param toaddress
     * @param gasPrice
     * @param number
     * @param keyStore
     * @param duoduoId
     * @return
     */
    @POST("duoduo/wallet/signTransaction")
    @FormUrlEncoded
    Observable<BaseResponse<SignTransactionResponse>>
    signTransaction(@Field("payPwd") String payPwd,
                    @Field("type") String type,
                    @Field("fromaddress") String fromaddress,
                    @Field("toaddress") String toaddress,
                    @Field("gasPrice") String gasPrice,
                    @Field("number") String number,
                    @Field("keyStore") String keyStore,
                    @Field("duoduoId") String duoduoId);

    @POST("duoduo/wallet/sendTransaction")
    @FormUrlEncoded
    Observable<BaseResponse<String>> sendTransaction(@Field("transactionHash") String arg1, @Field("rawTransaction") String arg2);

    @POST("duoduo/wallet/signHkbOrHkExchange")
    @FormUrlEncoded
    Observable<BaseResponse<SignHkbOrHkExchangeResponse>> signHkbOrHkExchange(@Field("payPwd") String payPwd, @Field("type") String type, @Field("address") String address,
                                                                              @Field("gasPrice") String gasPrice, @Field("number") String number,
                                                                              @Field("keyStore") String keyStore, @Field("duoduoId") String duoduoId);

    @POST("duoduo/wallet/sendHkbOrHkExchange")
    @FormUrlEncoded
    Observable<BaseResponse<String>> sendHkbOrHkExchange(@Field("type") String type, @Field("number") String number,
                                                         @Field("transactionHash") String transactionHash, @Field("rawTransaction") String rawTransaction);

    /**
     * 个人对个人进行发送红包
     *
     * @param data
     * @return
     */
    @POST("duoduo/redPackage/sendSingleRedPackage")
    @FormUrlEncoded
    Observable<BaseResponse<RedPackageResponse>> sendSingleRedPackage(
            @Field("data") String data
    );

    @POST("duoduo/wallet/getTransferAll")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferAllResponse>> getTransferAll(
            @Field("address") String address, @Field("page") String page, @Field("offset") String offset
    );

    @POST("duoduo/wallet/getTransfer")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferAllResponse>> getTransfer(
            @Field("address") String address, @Field("page") String page, @Field("offset") String offset
    );

    @POST("duoduo/wallet/getTransferOut")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferAllResponse>> getTransferOut(
            @Field("address") String address, @Field("page") String page, @Field("offset") String offset
    );

    @POST("duoduo/wallet/getTransferIn")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferAllResponse>> getTransferIn(
            @Field("address") String address, @Field("page") String page, @Field("offset") String offset
    );

    @POST("duoduo/wallet/getSerialsFail")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferAllResponse>> getSerialsFail(
            @Field("address") String address, @Field("page") String page, @Field("offset") String offset
    );

    @POST("duoduo/customer/getCustomerInfoById")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> getCustomerInfoById(@Field("id") String id);

    @POST("duoduo/customer/transfer")
    @FormUrlEncoded
    Observable<BaseResponse<TransferResponse>> transfer(@Field("toCustomerId") String toCustomerId,
                                                        @Field("hk") String hk, @Field("payPwd") String payPwd, @Field("remarks") String remarks);

    @POST("duoduo/customer/collect")
    @FormUrlEncoded
    Observable<BaseResponse<TransferResponse>> collect(@Field("transferId") String transferId);

    @POST("duoduo/customer/getTransferInfo")
    @FormUrlEncoded
    Observable<BaseResponse<TransferResponse>> getTransferInfo(@Field("transferId") String transferId);

    @POST("duoduo/redPackage/getRedPackageRecord")
    @FormUrlEncoded
    Observable<BaseResponse<GetRedPackageRecordResponse>> getRedPackageRecord(@Field("type") String type);


}
