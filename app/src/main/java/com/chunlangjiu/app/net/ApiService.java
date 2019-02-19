package com.chunlangjiu.app.net;

import com.chunlangjiu.app.amain.bean.AuctionListBean;
import com.chunlangjiu.app.amain.bean.CartCountBean;
import com.chunlangjiu.app.amain.bean.CartListBean;
import com.chunlangjiu.app.amain.bean.CheckUpdateBean;
import com.chunlangjiu.app.amain.bean.HomeListBean;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.amain.bean.ListBean;
import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.fans.bean.FansBean;
import com.chunlangjiu.app.fans.bean.FansCodeBean;
import com.chunlangjiu.app.fans.bean.FansItemBean;
import com.chunlangjiu.app.fans.bean.FansNumBean;
import com.chunlangjiu.app.goods.bean.AlcListBean;
import com.chunlangjiu.app.goods.bean.AreaListBean;
import com.chunlangjiu.app.goods.bean.BrandsListBean;
import com.chunlangjiu.app.goods.bean.ConfirmOrderBean;
import com.chunlangjiu.app.goods.bean.CreateAuctionBean;
import com.chunlangjiu.app.goods.bean.CreateOrderBean;
import com.chunlangjiu.app.goods.bean.EvaluateListBean;
import com.chunlangjiu.app.goods.bean.FilterListBean;
import com.chunlangjiu.app.goods.bean.GivePriceBean;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.GoodsListBean;
import com.chunlangjiu.app.goods.bean.OrdoListBean;
import com.chunlangjiu.app.goods.bean.PartnerBean;
import com.chunlangjiu.app.goods.bean.PayBalanceBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.bean.RecommendGoodsBean;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
import com.chunlangjiu.app.goods.bean.StoreActivityBean;
import com.chunlangjiu.app.money.bean.CreateRechargeOrderBean;
import com.chunlangjiu.app.money.bean.DepositBean;
import com.chunlangjiu.app.money.bean.DepositCashBean;
import com.chunlangjiu.app.money.bean.FundDetailListBean;
import com.chunlangjiu.app.money.bean.UserMoneyBean;
import com.chunlangjiu.app.order.bean.AuctionOrderListBean;
import com.chunlangjiu.app.order.bean.CancelOrderResultBean;
import com.chunlangjiu.app.order.bean.CancelReasonBean;
import com.chunlangjiu.app.order.bean.LogisticsBean;
import com.chunlangjiu.app.order.bean.OrderAfterSaleReasonBean;
import com.chunlangjiu.app.order.bean.OrderDetailBean;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.chunlangjiu.app.order.bean.SellerOrderDetailBean;
import com.chunlangjiu.app.store.bean.StoreClassListBean;
import com.chunlangjiu.app.store.bean.StoreDetailBean;
import com.chunlangjiu.app.store.bean.StoreListBean;
import com.chunlangjiu.app.user.bean.AddressListBean;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.user.bean.BankCardInfoBean;
import com.chunlangjiu.app.user.bean.BankCardListBean;
import com.chunlangjiu.app.user.bean.CheckGoodsBean;
import com.chunlangjiu.app.user.bean.EditGoodsDetailBean;
import com.chunlangjiu.app.user.bean.MyNumBean;
import com.chunlangjiu.app.user.bean.ShopCatIdList;
import com.chunlangjiu.app.user.bean.ShopClassList;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe: 请求接口
 */
public interface ApiService {


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CheckUpdateBean>> checkUpdate(@Field("method") String method, @Field("v") String v, @Field("app_type") String app_type,
                                                      @Field("platform") String platform);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> getAuthSms(@Field("method") String method, @Field("v") String v, @Field("mobile") String mobile);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<LoginBean>> login(@Field("method") String method, @Field("v") String v, @Field("account") String mobile, @Field("verifycode") String verifycode);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<LoginBean>> psdLogin(@Field("method") String method, @Field("v") String v, @Field("account") String mobile, @Field("password") String password,
                                             @Field("deviceid") String deviceid, @Field("clientid") String clientid,
                                             @Field("type") String type, @Field("plugin") String plugin);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> setPsd(@Field("method") String method, @Field("v") String v, @Field("mobile") String mobile, @Field("vcode") String vcode,
                                @Field("password") String password);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> updateLoginPassword(@Field("method") String method, @Field("v") String v, @Field("password") String password,@Field("password_confirmation") String password_confirmation, @Field("login_password") String loginPassword );

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> setPayPwd(@Field("method") String method, @Field("v") String v, @Field("password") String password,@Field("password_confirmation") String password_confirmation, @Field("verifycode") String vCode );

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> sendSmsCode(@Field("method") String method, @Field("v") String v, @Field("password") String password,@Field("password_confirmation") String password_confirmation, @Field("verifycode") String vCode );

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> logout(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<LoginBean>> shopLogin(@Field("method") String method, @Field("v") String v,
                                              @Field("account") String account, @Field("password") String password);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<UserInfoBean>> getUserInfo(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> setHeadImg(@Field("method") String method, @Field("v") String v,
                                    @Field("img_url") String img_url);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> personAuth(@Field("method") String method, @Field("v") String v,
                                    @Field("name") String name,@Field ("mobile")String mobile, @Field("idcard") String idcard,
                                    @Field("dentity") String dentity, @Field("dentity_front") String dentity_front,
                                    @Field("dentity_reverse") String dentity_reverse);

//    @POST("index.php/topapi")
//    @FormUrlEncoded
//    Flowable<ResultBean> companyAuth(@Field("method") String method, @Field("v") String v,
//                                     @Field("company_name") String company_name, @Field("representative") String representative,
//                                     @Field("license_num") String license_num, @Field("establish_date") String establish_date,
//                                     @Field("area") String area, @Field("address") String address,
//                                     @Field("company_phone") String company_phone, @Field("license_img") String license_img,
//                                     @Field("shopuser_identity_img_z") String shopuser_identity_img_z,
//                                     @Field("food_or_wine_img") String food_or_wine_img);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> companyAuth(@Field("method") String method, @Field("v") String v,@Field("accessToken") String token,
                                     @Field("company_name") String company_name, @Field("representative") String representative,
                                     @Field("idcard") String idCard, @Field("license_img") String license_img,
                                     @Field("shopuser_identity_img_z") String shopuser_identity_img_z,
                                     @Field("shopuser_identity_img_f") String shopuser_identity_img_f,
                                     @Field("food_or_wine_img") String food_or_wine_img);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Observable<ResultBean<AuthStatusBean>> getPersonAuthStatus(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Observable<ResultBean<AuthStatusBean>> getCompanyAuthStatus(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> valuationGoods(@Field("method") String method, @Field("v") String v,
                                        @Field("title") String title, @Field("name") String name,
                                        @Field("img") String img, @Field("series") String series);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AuctionListBean>> getAuctionList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> auctionGivePrice(@Field("method") String method, @Field("v") String v,
                                          @Field("item_id") String item_id, @Field("price") String price);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<MainClassBean>> getGoodsClass(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<GoodsListBean>> getGoodsList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id,
                                                     @Field("page_no") int page_no, @Field("page_size") int page_size, @Field("search_keywords") String search_keywords,
                                                     @Field("shop_id") String shop_id, @Field("brand_id") String brand_id, @Field("area_id") String area_id,
                                                     @Field("odor_id") String odor_id, @Field("alcohol_id") String alcohol_id, @Field("min_price") String min_price,
                                                     @Field("max_price") String max_price);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<FilterListBean>> getFilterData(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<GoodsDetailBean>> getGoodsDetail(@Field("method") String method, @Field("v") String v,
                                                         @Field("item_id") String item_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<GoodsDetailBean>> getGoodsDetailWithToken(@Field("method") String method, @Field("v") String v,
                                                                  @Field("token") String token, @Field("item_id") String item_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<GoodsDetailBean>> getAuctionGoodsDetail(@Field("method") String method, @Field("v") String v,
                                                                @Field("auctionitem_id") String auctionitem_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<List<GivePriceBean>>> getAuctionPriceList(@Field("method") String method, @Field("v") String v,
                                                                  @Field("auctionitem_id") String auctionitem_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<EvaluateListBean>> getEvaluateList(@Field("method") String method, @Field("v") String v,
                                                           @Field("rate_type") int rate_type, @Field("item_id") String item_id,
                                                           @Field("page_no") int page_no, @Field("page_size") int page_size);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<ShopInfoBean>> getShopInfo(@Field("method") String method, @Field("v") String v,
                                                   @Field("shop_id") String shop_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AddressListBean>> getAddressList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> newAddress(@Field("method") String method, @Field("v") String v,
                                    @Field("name") String name, @Field("mobile") String mobile,
                                    @Field("area") String area, @Field("addr") String addr, @Field("def_addr") String def_addr);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> editAddress(@Field("method") String method, @Field("v") String v,
                                     @Field("name") String name, @Field("mobile") String mobile,
                                     @Field("area") String area, @Field("addr") String addr,
                                     @Field("def_addr") String def_addr, @Field("addr_id") String addr_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> deleteAddress(@Field("method") String method, @Field("v") String v, @Field("addr_id") String addr_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> setDefault(@Field("method") String method, @Field("v") String v, @Field("addr_id") String addr_id);


    //获取我的页面数据个数的统计
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<MyNumBean>> getMyNumFlag(@Field("method") String method, @Field("v") String v,@Field("type") String type);


    //获取名庄分类
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<StoreClassListBean>> getStoreClass(@Field("method") String method, @Field("v") String v);

    //获取名庄对应分类下的列表
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<StoreListBean>> getStoreList(@Field("method") String method, @Field("v") String v,
                                                     @Field("chateaucat_id") String chateaucat_id,
                                                     @Field("page_no") int page_no, @Field("page_size") int page_size);

    //获取名庄详情
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<StoreDetailBean>> getStoreDetail(@Field("method") String method, @Field("v") String v, @Field("chateau_id") String chateau_id);

    //获取购物车列表
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CartListBean>> getCartList(@Field("method") String method, @Field("v") String v,
                                                   @Field("mode") String mode, @Field("platform") String platform);


    //添加商品到购物车
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> addGoodsToCart(@Field("method") String method, @Field("v") String v, @Field("quantity") int quantity,
                                        @Field("sku_id") String sku_id, @Field("obj_type") String obj_type, @Field("mode") String mode);


    //删除购物车商品 cart_id：购物车id,多个数据用逗号隔开
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> deleteCartItem(@Field("method") String method, @Field("v") String v,
                                        @Field("cart_id") String cart_id, @Field("mode") String mode);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<ConfirmOrderBean>> confirmOrder(@Field("method") String method, @Field("v") String v, @Field("mode") String mode);

    //更新购物车数据
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> updateCartData(@Field("method") String method, @Field("v") String v,
                                        @Field("obj_type") String obj_type, @Field("cart_params") String cart_params);

    //获取购物车数量
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CartCountBean>> getCartCount(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<PaymentBean>> getPayment(@Field("method") String method, @Field("v") String v);

    //创建订单
    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CreateOrderBean>> createOrder(@Field("method") String method, @Field("v") String v,
                                                      @Field("mode") String mode, @Field("md5_cart_info") String md5_cart_info,
                                                      @Field("addr_id") String addr_id, @Field("payment_type") String payment_type,
                                                      @Field("source_from") String source_from, @Field("shipping_type") String shipping_type,
                                                      @Field("mark") String mark, @Field("invoice_type") String invoice_type,
                                                      @Field("invoice_content") String invoice_content, @Field("use_points") String use_points);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> payDo(@Field("method") String method, @Field("v") String v,
                               @Field("payment_id") String payment_id, @Field("pay_app_id") String pay_app_id,@Field("deposit_password") String deposit_password);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<PayBalanceBean>> getBalanceInfo(@Field("method") String method, @Field("v") String v);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CreateAuctionBean>> createAuctionOrder(@Field("method") String method, @Field("v") String v,
                                                               @Field("auctionitem_id") String auctionitem_id, @Field("addr_id") String addr_id,
                                                               @Field("price") String price);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<Map>> appOpenAd(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> auctionAddPrice(@Field("method") String method, @Field("v") String v,
                                         @Field("auctionitem_id") String auctionitem_id, @Field("price") String price);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<HomeModulesBean>> getHomeModules(@Field("method") String method, @Field("v") String v,
                                                         @Field("tmpl") String payment_id);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<HomeListBean>> getHomeLists(@Field("method") String method, @Field("v") String v,
                                                    @Field("tmpl") String payment_id, @Field("page_no") int page_no,
                                                    @Field("pagesize") int pagesize);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<StoreActivityBean>> getStoreActivityLists(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Observable<ResultBean<UploadImageBean>> userUploadImage(@Field("method") String method, @Field("v") String v,
                                                            @Field("upload_type") String upload_type, @Field("image") String image,
                                                            @Field("image_input_title") String image_input_title,
                                                            @Field("image_type") String image_type);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Observable<ResultBean<UploadImageBean>> shopUploadImage(@Field("method") String method, @Field("v") String v,
                                                            @Field("upload_type") String upload_type, @Field("image") String image,
                                                            @Field("image_input_title") String image_input_title,
                                                            @Field("image_type") String image_type, @Field("image_cat_id") String image_cat_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CheckGoodsBean>> checkUploadGoods(@Field("method") String method, @Field("v") String v);


    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<ShopClassList>> getShopClassList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<ShopCatIdList>> getStoreClassList(@Field("method") String method, @Field("v") String v);

    //添加商品
    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Observable<ResultBean> addGoods(@Field("method") String method, @Field("v") String v,
                                    @Field("cat_id") String cat_id, @Field("brand_id") String brand_id,
                                    @Field("shop_cat_id") String shop_cat_id,
                                    @Field("title") String title, @Field("sub_title") String sub_title,
                                    @Field("weight") String weight, @Field("list_image") String list_image, @Field("price") String price,
                                    @Field("dlytmpl_id") String dlytmpl_id, @Field("sku") String sku,
                                    @Field("label") String label, @Field("explain") String explain,
                                    @Field("parameter") String parameter, @Field("unit") String unit, @Field("nospec") String nospec,
                                    @Field("area_id") String area_id, @Field("odor_id") String odor_id, @Field("alcohol_id") String alcohol_id,
                                    @Field("store") String store);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderListBean>> getOrderLists(@Field("method") String method, @Field("v") String v,
                                                      @Field("status") String status, @Field("page_no") int page_no,
                                                      @Field("pagesize") int pagesize);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<List<AuctionOrderListBean>>> getAuctionOrderLists(@Field("method") String method, @Field("v") String v,
                                                                          @Field("status") String status, @Field("page_no") int page_no,
                                                                          @Field("page_size") int pagesize, @Field("fields") String fields);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderDetailBean>> getAuctionOrderDetail(@Field("method") String method, @Field("v") String v,
                                                                @Field("auctionitem_id") String auctionitem_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderListBean>> getAfterSaleOrderList(@Field("method") String method, @Field("v") String v, @Field("status") String status, @Field("progress") String progress,
                                                              @Field("page_no") int page_no, @Field("pagesize") int pagesize);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderDetailBean>> getOrderDetail(@Field("method") String method, @Field("v") String v,
                                                         @Field("tid") String tid);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderDetailBean>> getAfterSaleOrderDetail(@Field("method") String method, @Field("v") String v,
                                                                  @Field("aftersales_bn") String aftersales_bn, @Field("oid") String oid);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> confirmReceipt(@Field("method") String method, @Field("v") String v,
                                        @Field("tid") String tid);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CancelReasonBean>> getCancelReason(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CancelOrderResultBean>> cancelOrder(@Field("method") String method, @Field("v") String v,
                                                            @Field("tid") String tid, @Field("cancel_reason") String reason);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> addRate(@Field("method") String method, @Field("v") String v,
                                 @Field("tid") String tid, @Field("rate_data") JSONArray rateData,
                                 @Field("anony") boolean anony, @Field("tally_score") int tallyScore,
                                 @Field("attitude_score") int attitudeScore, @Field("delivery_speed_score") int deliverySpeedScore);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderAfterSaleReasonBean>> getAfterSaleReason(@Field("method") String method, @Field("v") String v, @Field("oid") String oid);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> applyAfterSaleReason(@Field("method") String method, @Field("v") String v,
                                              @Field("tid") String tid, @Field("oid") String oid,
                                              @Field("reason") String reason, @Field("description") String description,
                                              @Field("aftersales_type") String aftersales_type, @Field("evidence_pic") String evidence_pic);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> delete(@Field("method") String method, @Field("v") String v, @Field("tid") String tid);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<LogisticsBean>> getLogisticsList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> sendLogistics(@Field("method") String method, @Field("v") String v,
                                       @Field("aftersales_bn") String aftersales_bn, @Field("corp_code")
                                               String corp_code, @Field("logi_name") String logi_name, @Field("logi_no") String logi_no);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CreateOrderBean>> repay(@Field("method") String method, @Field("v") String v, @Field("tid") String tid, @Field("merge") String merge);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderListBean>> getSellerOrderLists(@Field("method") String method, @Field("v") String v,
                                                            @Field("status") String status, @Field("page_no") int page_no,
                                                            @Field("page_size") int page_size, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<SellerOrderDetailBean>> getSellerOrderDetail(@Field("method") String method, @Field("v") String v,
                                                                     @Field("tid") String tid, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> sendSellerLogistics(@Field("method") String method, @Field("v") String v,
                                             @Field("tid") String tid, @Field("corp_code")
                                                     String corp_code, @Field("logi_no") String logi_no);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CancelReasonBean>> getSellerCancelReason(@Field("method") String method, @Field("v") String v);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CancelOrderResultBean>> sellerCancelOrder(@Field("method") String method, @Field("v") String v,
                                                                  @Field("tid") String tid, @Field("cancel_reason") String reason);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderListBean>> getSellerAfterSaleOrderList(@Field("method") String method, @Field("v") String v, @Field("status") String status, @Field("progress") String progress,
                                                                    @Field("page_no") int page_no, @Field("pagesize") int pagesize, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderDetailBean>> getSellerAfterSaleOrderDetail(@Field("method") String method, @Field("v") String v,
                                                                        @Field("aftersales_bn") String aftersales_bn, @Field("oid") String oid, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderListBean>> getSellerAfterSaleCencelOrderList(@Field("method") String method, @Field("v") String v,
                                                                          @Field("page_no") int page_no, @Field("pagesize") int pagesize, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrderDetailBean>> getSellerAfterSaleCencelOrderDetail(@Field("method") String method, @Field("v") String v,
                                                                              @Field("cancel_id") String cancel_id, @Field("fields") String fields);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> applySellerCancelOrder(@Field("method") String method, @Field("v") String v,
                                                @Field("cancel_id") String cancel_id, @Field("status") String status,
                                                @Field("reason") String reason);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> applySellerAfterSale(@Field("method") String method, @Field("v") String v,
                                              @Field("aftersales_bn") String aftersales_bn, @Field("check_result") String check_result,
                                              @Field("total_price") String total_price, @Field("refunds_reason") String refunds_reason);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<BrandsListBean>> getUserBrandList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AreaListBean>> getUserAreaList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrdoListBean>> getUserOrdoList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AlcListBean>> getUserAlcList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);


    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<BrandsListBean>> getAddShopBrandList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AreaListBean>> getShopAreaList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);


    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<OrdoListBean>> getShopOrdoList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AlcListBean>> getShopAlcList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> favoriteAddGoods(@Field("method") String method, @Field("v") String v, @Field("item_id") String item_id);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> favoriteCancelGoods(@Field("method") String method, @Field("v") String v, @Field("item_id") String item_id);


    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<EditGoodsDetailBean>> editGoodsDetail(@Field("method") String method, @Field("v") String v, @Field("item_id") String item_id);


    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Observable<ResultBean> commitEditGoodsDetail(@Field("method") String method, @Field("v") String v,
                                                 @Field("cat_id") String cat_id, @Field("brand_id") String brand_id,
                                                 @Field("shop_cat_id") String shop_cat_id,
                                                 @Field("title") String title, @Field("sub_title") String sub_title,
                                                 @Field("weight") String weight, @Field("list_image") String list_image, @Field("price") String price,
                                                 @Field("dlytmpl_id") String dlytmpl_id, @Field("sku") String sku,
                                                 @Field("label") String label, @Field("explain") String explain,
                                                 @Field("parameter") String parameter, @Field("unit") String unit, @Field("nospec") String nospec,
                                                 @Field("area_id") String area_id, @Field("odor_id") String odor_id, @Field("alcohol_id") String alcohol_id,
                                                 @Field("store") String store, @Field("item_id") String item_id);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<List<RecommendGoodsBean>>> getRecommendGoods(@Field("method") String method, @Field("v") String v,
                                                                     @Field("item_id") String item_id);

    @POST("index.php/shop/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> editShopName(@Field("method") String method, @Field("v") String v,
                                      @Field("accessToken") String accessToken, @Field("company_name") String company_name);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<List<PartnerBean>>>  getPartnerList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<ListBean<FansItemBean>>>  getFansList(@Field("method") String method, @Field("v") String v);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<FansNumBean>> getFansInfo(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<FansCodeBean>> getMyInvitationCode(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<FansBean>> setInvitationCode(@Field("method") String method, @Field("v") String v,@Field("referrer") String inviteCode);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> submitInviteCode(@Field("method") String method, @Field("v") String v,@Field("inviteCode") String inviteCode);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<BankCardListBean>> getBankCardList(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> addBankCardList(@Field("method")String method,@Field("v")String v,@Field("accessToken")String token,@Field("name")String name,@Field("bank")String bank,@Field("card")String card
    ,@Field("bank_branch")String bank_branch,@Field("idcard")String idcard,@Field("mobile")String mobile,@Field("verifycode") String verifycode);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> deleteBankCard(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token, @Field("bank_id")String bankId);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<DepositCashBean>> depositCash(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token, @Field("bank_id")String bankId , @Field("amount")String amount);

   @POST("index.php/topapi")
   @FormUrlEncoded
   Flowable<ResultBean<UserMoneyBean>> getUserMoney(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<FundDetailListBean>> getFundDetails(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token,@Field("type")String type);


    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> sendSms(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CreateRechargeOrderBean>> reCharge(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token, @Field("money")String money);

    @POST("index.php/api")//payment.stored.pay
    @FormUrlEncoded
    Flowable<ResultBean> storedPay(@Field("method")String method, @Field("v")String v,@Field("payment_id")String payment_id,@Field("pay_app_id")String pay_app_id ,@Field("platform")String platform,@Field("money")String money,@Field("type")String type);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<CreateRechargeOrderBean>> depositCreate(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<DepositBean>> getDeposit(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<DepositCashBean>> depositRefund(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token,@Field("bank_id")String bank_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> depositPay(@Field("method")String method, @Field("v")String v,@Field("payment_id")String payment_id,@Field("pay_app_id")String pay_app_id ,@Field("platform")String platform,@Field("money")String money,@Field("type")String type);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<BankCardInfoBean>> bankCardGet(@Field("method")String method, @Field("v")String v, @Field("accessToken")String token, @Field("card")String bank_id);
}
