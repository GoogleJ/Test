package com.zxjk.duoduo.network;

import com.zxjk.duoduo.bean.AuditCertificationBean;
import com.zxjk.duoduo.bean.CardBackBean;
import com.zxjk.duoduo.bean.CardFaceBean;
import com.zxjk.duoduo.bean.response.AllGroupMembersResponse;
import com.zxjk.duoduo.bean.response.BaseResponse;
import com.zxjk.duoduo.bean.response.CreateWalletResponse;
import com.zxjk.duoduo.bean.response.DetailListResposne;
import com.zxjk.duoduo.bean.response.DuobaoParameterResponse;
import com.zxjk.duoduo.bean.response.FriendInfoResponse;
import com.zxjk.duoduo.bean.response.GetAllPlayGroupResponse;
import com.zxjk.duoduo.bean.response.GetAppVersionResponse;
import com.zxjk.duoduo.bean.response.GetBalanceHkResponse;
import com.zxjk.duoduo.bean.response.GetBetConutBygroupIdResponse;
import com.zxjk.duoduo.bean.response.GetBetInfoDetailsResponse;
import com.zxjk.duoduo.bean.response.GetDuoBaoIntegralDetailsResponse;
import com.zxjk.duoduo.bean.response.GetExtractRecordResponse;
import com.zxjk.duoduo.bean.response.GetFriendsByMobilesResponse;
import com.zxjk.duoduo.bean.response.GetGameClassResponse;
import com.zxjk.duoduo.bean.response.GetGameInfoByGroupIdResponse;
import com.zxjk.duoduo.bean.response.GetGroupGameParameterResponse;
import com.zxjk.duoduo.bean.response.GetGroupMemberDuoBaoBetInfoResponse;
import com.zxjk.duoduo.bean.response.GetGroupMemberPointsResponse;
import com.zxjk.duoduo.bean.response.GetGroupOwnerDuoBaoBetInfoResponse;
import com.zxjk.duoduo.bean.response.GetGroupOwnerTrendResponse;
import com.zxjk.duoduo.bean.response.GetGroupRedPackageInfoResponse;
import com.zxjk.duoduo.bean.response.GetIntegralDetailsResponse;
import com.zxjk.duoduo.bean.response.GetNumbeOfTransactionResponse;
import com.zxjk.duoduo.bean.response.GetOverOrderResponse;
import com.zxjk.duoduo.bean.response.GetRebateByIdResponse;
import com.zxjk.duoduo.bean.response.GetRebateDetailsResponse;
import com.zxjk.duoduo.bean.response.GetRebateInfoResponse;
import com.zxjk.duoduo.bean.response.GetRebatePayRecordResponse;
import com.zxjk.duoduo.bean.response.GetRedPackageRecordResponse;
import com.zxjk.duoduo.bean.response.GetRedPackageStatusResponse;
import com.zxjk.duoduo.bean.response.GetTeamInfoResponse;
import com.zxjk.duoduo.bean.response.GetThirdPartyPaymentOrderResponse;
import com.zxjk.duoduo.bean.response.GetTransferAllResponse;
import com.zxjk.duoduo.bean.response.GetTransferEthResponse;
import com.zxjk.duoduo.bean.response.GetVicinityResponse;
import com.zxjk.duoduo.bean.response.GroupChatResponse;
import com.zxjk.duoduo.bean.response.GroupResponse;
import com.zxjk.duoduo.bean.response.LoginResponse;
import com.zxjk.duoduo.bean.response.MakeGameGroupResponse;
import com.zxjk.duoduo.bean.response.PayInfoResponse;
import com.zxjk.duoduo.bean.response.PersonalRedPackageInfoResponse;
import com.zxjk.duoduo.bean.response.ReceiveGroupRedPackageResponse;
import com.zxjk.duoduo.bean.response.ReceivePersonalRedPackageResponse;
import com.zxjk.duoduo.bean.response.RedPackageResponse;
import com.zxjk.duoduo.bean.response.ReleasePurchaseResponse;
import com.zxjk.duoduo.bean.response.ReleaseSaleResponse;
import com.zxjk.duoduo.bean.response.SendGroupRedPackageResponse;
import com.zxjk.duoduo.bean.response.SignHkbOrHkExchangeResponse;
import com.zxjk.duoduo.bean.response.SignTransactionResponse;
import com.zxjk.duoduo.bean.response.ThirdPartyPaymentResponse;
import com.zxjk.duoduo.bean.response.TransferResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("duoduo/login")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> login(
            @Field("mobile") String phone,
            @Field("pwd") String pwd
    );

    @POST("duoduo/customer/appUserRegister")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> register(
            @Field("mobile") String phone,
            @Field("securityCode") String code,
            @Field("pwd") String pwd
    );

    @POST("duoduo/getCode")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getCode(
            @Field("mobile") String phone,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("duoduo/customer/forgetPwd")
    Observable<BaseResponse<String>> forgetPwd(
            @Field("mobile") String phone,
            @Field("pwd") String pwd,
            @Field("securityCode") String code
    );

    @POST("duoduo/customer/updateCustomerInfo")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> updateUserInfo(@Field("customerInfo") String customerInfo);

    @POST("duoduo/customer/updatePayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<LoginResponse>> updatePayPwd(
            @Field("oldPayPwd") String oldPwd,
            @Field("newPayPwd") String newPwdOne,
            @Field("newPayPwdTwo") String newPwdTwo
    );

    @POST("duoduo/friend/getFriendInfoById")
    @FormUrlEncoded
    Observable<BaseResponse<FriendInfoResponse>> getFriendInfoById(
            @Field("friendId") String friendId
    );

    @POST("duoduo/friend/getFriendListById")
    Observable<BaseResponse<List<FriendInfoResponse>>> getFriendListById();

    @POST("duoduo/friend/searchCustomer")
    @FormUrlEncoded
    Observable<BaseResponse<List<FriendInfoResponse>>> searchCustomerInfo(
            @Field("data") String data
    );

    @POST("duoduo/friend/applyAddFriend")
    @FormUrlEncoded
    Observable<BaseResponse<String>> applyAddFriend(
            @Field("friendId") String friendId,
            @Field("remark") String remark
    );

    @POST("duoduo/friend/getMyfriendsWaiting")
    Observable<BaseResponse<List<FriendInfoResponse>>> getMyfriendsWaiting();

    @FormUrlEncoded
    @POST("duoduo/friend/addFriend")
    Observable<BaseResponse<String>> addFriend(
            @Field("friendId") String friendId,
            @Field("markName") String markName
    );

    @POST("duoduo/customer/updateMobile")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateMobile(
            @Field("newMobile") String newMobile,
            @Field("securityCode") String securityCode
    );

    @POST("duoduo/group/getGroupByCustomId")
    @FormUrlEncoded
    Observable<BaseResponse<List<GroupChatResponse>>> getMygroupinformation(@Field("customerId") String customerId);

    @POST("duoduo/exchange/getBalanceHk")
    Observable<BaseResponse<GetBalanceHkResponse>> getBalanceHk();

    @POST("duoduo/exchange/getNumbeOfTransaction")
    Observable<BaseResponse<GetNumbeOfTransactionResponse>> getNumbeOfTransaction();

    @POST("duoduo/exchange/releasePurchase")
    @FormUrlEncoded
    Observable<BaseResponse<ReleasePurchaseResponse>> releasePurchase(@Field("number") String number,
                                                                      @Field("money") String money, @Field("currency") String currency, @Field("payPwd") String paypwd, @Field("payTpye") String payTpye,
                                                                      @Field("minNum") String minNum, @Field("maxNum") String maxNum, @Field("exchangeFee") String exchangeFee);

    @POST("duoduo/exchange/releaseSale")
    @FormUrlEncoded
    Observable<BaseResponse<ReleaseSaleResponse>> releaseSale(@Field("number") String number,
                                                              @Field("money") String money,
                                                              @Field("currency") String currency,
                                                              @Field("payTpye") String payTpye);

    @POST("duoduo/customer/getPayInfo")
    Observable<BaseResponse<List<PayInfoResponse>>> getPayInfo();

    @POST("duoduo/friend/deleteFriend")
    @FormUrlEncoded
    Observable<BaseResponse<String>> deleteFriend(
            @Field("friendId") String friendId
    );

    @POST("duoduo/customer/updatePwd")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updatePwd(
            @Field("oldPwd") String oldPwd,
            @Field("newPwdOne") String newPwdOne,
            @Field("newPwdTwo") String newPwdTwo
    );

    @POST("duoduo/loginOut")
    Observable<BaseResponse<String>> loginOut();

    @POST("duoduo/customer/addPayInfo")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addPayInfo(
            @Field("data") String data
    );

    @POST("duoduo/customer/certification")
    @FormUrlEncoded
    Observable<BaseResponse<String>> certification(
            @Field("data") String data
    );

    @POST("duoduo/exchange/closeSellOrder")
    @FormUrlEncoded
    Observable<BaseResponse<String>> closeSellOrder(@Field("sellOrderId") String sellOrderId);

    @POST("duoduo/exchange/cancelled")
    @FormUrlEncoded
    Observable<BaseResponse<String>> cancelled(@Field("buyOrderId") String buyOrderId
            , @Field("bothOrderId") String bothOrderId
            , @Field("sellOrderId") String sellOrderId);

    @POST("duoduo/exchange/isConfine")
    Observable<BaseResponse<String>> isConfine();

    @POST("duoduo/exchange/overOrder")
    @FormUrlEncoded
    Observable<BaseResponse<String>> overOrder(@Field("buyCustomerId") String buyCustomerId
            , @Field("buyOrderId") String buyOrderId
            , @Field("sellOrderId") String sellOrderId
            , @Field("bothOrderId") String bothOrderId
            , @Field("payPwd") String payPwd);

    @POST("duoduo/exchange/rejectAudit")
    @FormUrlEncoded
    Observable<BaseResponse<String>> rejectAudit(
            @Field("buyOrderId") String buyOrderId
            , @Field("bothOrderId") String bothOrderId
            , @Field("sellOrderId") String sellOrderId);

    @POST("duoduo/friend/updateRemark")
    @FormUrlEncoded
    Observable<BaseResponse<FriendInfoResponse>> updateRemark(
            @Field("friendId") String friendId,
            @Field("remark") String remark
    );

    @POST("duoduo/exchange/updateBuyPayState")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateBuyPayState(@Field("bothOrderId") String bothOrderId
            , @Field("picture") String picture);

    @POST("duoduo/customer/updatePayInfo")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updatePayInfo(
            @Field("payType") String payType
    );

    @POST("duoduo/customer/getCustomerAuth")
    Observable<BaseResponse<String>> getCustomerAuth();

    @POST("duoduo/exchange/getOverOrder")
    Observable<BaseResponse<List<GetOverOrderResponse>>> getOverOrder();

    @POST("duoduo/wallet/getWallet")
    @FormUrlEncoded
    Observable<BaseResponse<CreateWalletResponse>> getWallet(@Field("customerDuoDuoId") String customerDuoDuoId);

    @POST("duoduo/customer/fandPayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<String>> fandPayPwd(
            @Field("number") String number,
            @Field("securityCode") String securityCode,
            @Field("newPayPwd") String newPayPwd,
            @Field("newPayPwdTwo") String newPayPwdTwo

    );

    @POST("duoduo/group/makeGroup")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse.GroupInfoBean>> makeGroup(
            @Field("groupOwnerId") String groupOwnerId,
            @Field("customerIds") String customerIds
    );

    @POST("duoduo/group/getGroupMemByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<List<AllGroupMembersResponse>>> getGroupMemByGroupId(
            @Field("groupId") String groupId
    );

    @POST("duoduo/group/enterGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> enterGroup(
            @Field("groupId") String groupId,
            @Field("inviterId") String inviterId,
            @Field("customerIds") String customerIds
    );

    @POST("duoduo/group/getGroupByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse>> getGroupByGroupId(
            @Field("groupId") String groupId
    );

    @POST("duoduo/group/updateGroupInfo")
    @FormUrlEncoded
    Observable<BaseResponse<GroupResponse.GroupInfoBean>> updateGroupInfo(
            @Field("groupInfo") String groupInfo
    );

    @POST("duoduo/group/disBandGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> disBandGroup(
            @Field("groupId") String groupId,
            @Field("groupOwnerId") String groupOwnerId
    );

    @POST("duoduo/group/exitGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> exitGroup(
            @Field("groupId") String groupId,
            @Field("customerId") String customerId
    );

    @POST("duoduo/group/moveOutGroup")
    @FormUrlEncoded
    Observable<BaseResponse<String>> moveOutGroup(
            @Field("groupId") String groupId,
            @Field("customerIds") String customerIds
    );

    @POST("duoduo/group/updateGroupOwner")
    @FormUrlEncoded
    Observable<BaseResponse<String>> updateGroupOwner(
            @Field("groupId") String groupId,
            @Field("customerId") String customerId
    );

    @POST("duoduo/wallet/createWallet")
    @FormUrlEncoded
    Observable<BaseResponse<CreateWalletResponse>> createWallet(@Field("customerDuoDuoId") String customerDuoDuoId);

    @POST("duoduo/wallet/getTransferEth")
    @FormUrlEncoded
    Observable<BaseResponse<GetTransferEthResponse>> getTransferEth(@Field("address") String address, @Field("page") String page, @Field("offset") String offset);

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

    @POST("duoduo/redPackage/receivePersonalRedPackage")
    @FormUrlEncoded
    Observable<BaseResponse<ReceivePersonalRedPackageResponse>> receivePersonalRedPackage(@Field("redPackageId") String redPackageId);

    @POST("duoduo/redPackage/getRedPackageStatus")
    @FormUrlEncoded
    Observable<BaseResponse<GetRedPackageStatusResponse>> getRedPackageStatus(@Field("redPackageId") String redPackageId
            , @Field("isGame") String isGame);

    @POST("duoduo/redPackage/personalRedPackageInfo")
    @FormUrlEncoded
    Observable<BaseResponse<PersonalRedPackageInfoResponse>> personalRedPackageInfo(@Field("redPackageId") String redPackageId,
                                                                                    @Field("customerId") int customerId);

    @POST("duoduo/redPackage/getGroupRedPackageInfo")
    @FormUrlEncoded
    Observable<BaseResponse<GetGroupRedPackageInfoResponse>> getGroupRedPackageInfo(@Field("redPackageId") String redPackageId);

    @POST("duoduo/customer/verifyPayPwd")
    @FormUrlEncoded
    Observable<BaseResponse<String>> verifyPayPwd(@Field("payPwd") String payPwd);

    @POST("duoduo/exchange/getDetailList")
    @FormUrlEncoded
    Observable<BaseResponse<List<DetailListResposne>>> getDetailList(@Field("type") String type);

    @POST("duoduo/customer/verifyPaperworkNumber")
    @FormUrlEncoded
    Observable<BaseResponse<String>> verifyPaperworkNumber(@Field("number") String number);

    @POST("duoduo/exchange/addAppeal")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addAppeal(@Field("orderAppealStr") String orderAppealStr);

    @POST("duoduo/redPackage/sendGroupRedPackage")
    @FormUrlEncoded
    Observable<BaseResponse<SendGroupRedPackageResponse>> sendGroupRedPackage(@Field("data") String data);

    @POST("duoduo/redPackage/receiveGroupRedPackage")
    @FormUrlEncoded
    Observable<BaseResponse<ReceiveGroupRedPackageResponse>> receiveGroupRedPackage(@Field("redPackageId") String redPackageId
            , @Field("isGame") String isGame);

    @POST("duoduo/group/game/getGroupGameParameter")
    @FormUrlEncoded
    Observable<BaseResponse<GetGroupGameParameterResponse>> getGroupGameParameter(@Field("groupId") String groupId);

    @POST("duoduo/group/game/getGroupMemberPoints")
    @FormUrlEncoded
    Observable<BaseResponse<GetGroupMemberPointsResponse>> getGroupMemberPoints(@Field("groupId") String groupId);

    @POST("duoduo/group/game/upPoints")
    @FormUrlEncoded
    Observable<BaseResponse<String>> upPoints(@Field("groupId") String groupId
            , @Field("hk") String hk
            , @Field("pwd") String pwd);

    @POST("duoduo/group/game/onPoints")
    @FormUrlEncoded
    Observable<BaseResponse<String>> onPoints(@Field("groupId") String groupId
            , @Field("hk") String hk
            , @Field("pwd") String pwd);

    @POST("duoduo/group/game/getIntegralDetails")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetIntegralDetailsResponse>>> getIntegralDetails(@Field("groupId") String groupId);

    @POST("duoduo/playRebate/getRebateById")
    @FormUrlEncoded
    Observable<BaseResponse<GetRebateByIdResponse>> getRebateById(@Field("groupId") String groupId);

    @POST("duoduo/group/getAllPlayGroup")
    @FormUrlEncoded
    Observable<BaseResponse<GetAllPlayGroupResponse>> getAllPlayGroup(@Field("customerId") String customerId);

    @POST("duoduo/group/game/beforeBet")
    @FormUrlEncoded
    Observable<BaseResponse<String>> beforeBet(@Field("groupId") String groupId);

    @POST("duoduo/group/game/groupGamebetting")
    @FormUrlEncoded
    Observable<BaseResponse<String>> groupGamebetting(@Field("data") String data);

    @POST("duoduo/group/game/getBetConutBygroupId")
    @FormUrlEncoded
    Observable<BaseResponse<GetBetConutBygroupIdResponse>> getBetConutBygroupId(@Field("groupId") String groupId);

    @POST("duoduo/redPackage/settlementGame")
    @FormUrlEncoded
    Observable<BaseResponse<String>> settlementGame(@Field("redPackageId") String redPackageId,
                                                    @Field("groupId") String groupId);

    @POST("duoduo/group/game/getBetInfolDetails")
    @FormUrlEncoded
    Observable<BaseResponse<GetBetInfoDetailsResponse>> getBetInfolDetails(@Field("redPackageId") String redPackageId,
                                                                           @Field("groupId") String groupId);

    @POST("duoduo/playRebate/getTeamInfo")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetTeamInfoResponse>>> getTeamInfo(@Field("groupId") String groupId);


    @POST("duoduo/playRebate/getRebateInfoByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetRebateInfoResponse>>> getRebateInfoByGroupId(@Field("groupId") String groupId);


    @POST("duoduo/playRebate/getRebateAmount")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getRebateAmount(@Field("groupId") String groupId);

    @POST("duoduo/playRebate/getRebateDetails")
    @FormUrlEncoded
    Observable<BaseResponse<GetRebateDetailsResponse>> getRebateDetails(@Field("groupId") String groupId, @Field("page") int page, @Field("offset") int offset);


    @POST("duoduo/playRebate/getExtractRecord")
    @FormUrlEncoded
    Observable<BaseResponse<GetExtractRecordResponse>> getExtractRecord(@Field("groupId") String groupId, @Field("page") int page, @Field("offset") int offset);


    @POST("duoduo/playRebate/withdrawalAmount")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getWithdrawalAmount(@Field("groupId") String groupId, @Field("groupOwnerId") String groupOwnerId, @Field("num") String num, @Field("payPwd") String payPwd);

    @POST("duoduo/customer/getAppVersion")
    Observable<BaseResponse<GetAppVersionResponse>> getAppVersion();

    @POST("duoduo/playRebate/getRebatePayRecord")
    @FormUrlEncoded
    Observable<BaseResponse<GetRebatePayRecordResponse>> getRebatePayRecord(@Field("groupId") String groupId,
                                                                            @Field("page") int page,
                                                                            @Field("offset") int offset);

    @POST("duoduo/group/game/getGameClass")
    Observable<BaseResponse<GetGameClassResponse>> getGameClass();

    @POST("duoduo/group/makeGameGroup")
    @FormUrlEncoded
    Observable<BaseResponse<MakeGameGroupResponse>> makeGameGroup(@Field("gameType") String gameType, @Field("playId") String playId, @Field("pumpingRate") String pumpingRate,
                                                                  @Field("payPwd") String payPwd, @Field("proportionOfFees") String proportionOfFees, @Field("typeName") String typeName,
                                                                  @Field("commission") String commission, @Field("duobaoMultiple") String duobaoMultiple);

    @POST("duoduo/group/game/duobaoParameter")
    @FormUrlEncoded
    Observable<BaseResponse<DuobaoParameterResponse>> duobaoParameter(@Field("groupId") String groupId);

    @POST("duoduo/group/game/groupGamebettingForDuobao")
    @FormUrlEncoded
    Observable<BaseResponse<String>> groupGamebettingForDuobao(@Field("data") String data);

    @POST("rest/160601/ocr/ocr_idcard.json")
    Observable<CardFaceBean> getOCRResult(@Body RequestBody body);

    @POST("rest/160601/ocr/ocr_idcard.json")
    Observable<CardBackBean> getOCRBackResult(@Body RequestBody body);

    @GET("mobileCheck")
    Observable<AuditCertificationBean> getCertification(@Query("idCard") String idCard, @Query("mobile") String mobile, @Query("name") String name);


    @POST("duoduo/group/game/getDuoBaoIntegralDetails")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetDuoBaoIntegralDetailsResponse>>> getDuoBaoIntegralDetails(@Field("groupId") String groupId);


    @POST("duoduo/group/game/getGroupOwnerDuoBaoBetInfo")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetGroupOwnerDuoBaoBetInfoResponse>>> getGroupOwnerDuoBaoBetInfo(@Field("groupId") String groupId, @Field("expect") String expect);

    @POST("duoduo/group/game/getGroupMemberDuoBaoBetInfo")
    @FormUrlEncoded
    Observable<BaseResponse<GetGroupMemberDuoBaoBetInfoResponse>> getGroupMemberDuoBaoBetInfo(@Field("groupId") String groupId, @Field("expect") String expect, @Field("customerId") String customerId);

    @POST("duoduo/group/game/getGameInfoByGroupId")
    @FormUrlEncoded
    Observable<BaseResponse<GetGameInfoByGroupIdResponse>> getGameInfoByGroupId(@Field("groupId") String groupId);

    @POST("duoduo/customer/getVicinity")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetVicinityResponse>>> getVicinity(@Field("lon") String lon, @Field("lat") String lat);

    @POST("duoduo/customer/operateOpenPhone")
    @FormUrlEncoded
    Observable<BaseResponse<String>> operateOpenPhone(@Field("openPhone") String openPhone);

    @POST("duoduo/customer/operateRealName")
    @FormUrlEncoded
    Observable<BaseResponse<String>> operateRealName(@Field("isShowRealname") String isShowRealname);

    @POST("duoduo/customer/getThirdPartyPaymentOrder")
    @FormUrlEncoded
    Observable<BaseResponse<GetThirdPartyPaymentOrderResponse>> getThirdPartyPaymentOrder(@Field("orderId") String orderId);

    @POST("duoduo/customer/thirdPartyPayment")
    @FormUrlEncoded
    Observable<BaseResponse<ThirdPartyPaymentResponse>> thirdPartyPayment(@Field("data") String data, @Field("payPwd") String payPwd);

    @POST("duoduo/group/game/getGroupOwnerTrend")
    @FormUrlEncoded
    Observable<BaseResponse<GetGroupOwnerTrendResponse>> getGroupOwnerTrend(@Field("groupId") String groupId);

    @POST("duoduo/friend/getFriendsByMobiles")
    @FormUrlEncoded
    Observable<BaseResponse<List<GetFriendsByMobilesResponse>>> getFriendsByMobiles(@Field("mobiles") String mobiles, @Field("data") String data);
}
