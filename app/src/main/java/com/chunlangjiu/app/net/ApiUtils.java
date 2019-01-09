package com.chunlangjiu.app.net;

import com.chunlangjiu.app.amain.bean.AuctionListBean;
import com.chunlangjiu.app.amain.bean.CartCountBean;
import com.chunlangjiu.app.amain.bean.CartListBean;
import com.chunlangjiu.app.amain.bean.CheckUpdateBean;
import com.chunlangjiu.app.amain.bean.HomeListBean;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.fans.bean.FansBean;
import com.chunlangjiu.app.fans.bean.FansItemBean;
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
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.bean.RecommendGoodsBean;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
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
import com.chunlangjiu.app.user.bean.EditGoodsDetailBean;
import com.chunlangjiu.app.user.bean.MyNumBean;
import com.chunlangjiu.app.user.bean.ShopCatIdList;
import com.chunlangjiu.app.user.bean.ShopClassList;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.pkqup.commonlibrary.net.HttpUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe:
 */
public class ApiUtils {

    private volatile static ApiUtils mInstance;
    private ApiService apiService;

    public static ApiUtils getInstance() {
        if (mInstance == null) {
            synchronized (ApiUtils.class) {
                if (mInstance == null) {
                    mInstance = new ApiUtils();
                }
            }
        }
        return mInstance;
    }

    public void init() {
        apiService = HttpUtils.getInstance().getRetrofit().create(ApiService.class);
    }

    public ApiService getApiService() {
        if (null == apiService) {
            apiService = HttpUtils.getInstance().getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

    public Flowable<ResultBean<CheckUpdateBean>> checkUpdate() {
        return apiService.checkUpdate("app.versions", "v1", "android","chunlang");
    }


    public Flowable<ResultBean> getAuthSms(String mobile) {
        return apiService.getAuthSms("user.sendSms", "v1", mobile);
    }

    public Flowable<ResultBean<LoginBean>> login(String account, String code) {
        return apiService.login("user.oauthlogin", "v2", account, code);
    }

    public Flowable<ResultBean<LoginBean>> psdLogin(String account, String psd, String deviceId) {
        return apiService.psdLogin("user.login", "v2", account, psd, deviceId, deviceId, "android", "igexin");
    }

    public Flowable<ResultBean> setPsd(String account, String vcode, String psd) {
        return apiService.setPsd("user.forgot.resetpassword", "v2", account, vcode, psd);
    }

    public Flowable<ResultBean> updateLoginPassword (String newPwd, String confirmPwd, String pwd) {
        return apiService.updateLoginPassword("member.security.updateLoginPassword", "v1", newPwd,confirmPwd, pwd);
    }
    public Flowable<ResultBean> updatePayPassword (String newPwd, String confirmPwd, String pwd) {
        return apiService.setPayPwd("member.security.updatePayPassword", "v1", newPwd,confirmPwd, pwd);
    }

    public Flowable<ResultBean> sendSmsCode (String pwd, String confirmPwd, String code) {
        return apiService.sendSmsCode("user.resetSendSms", "v1", pwd,confirmPwd, code);
    }



    public Flowable<ResultBean> logout() {
        return apiService.logout("user.logout", "v1");
    }

    public Flowable<ResultBean<LoginBean>> shopLogin(String mobile, String password) {
        return apiService.shopLogin("user.login", "v1", mobile, password);
    }

    public Flowable<ResultBean<UserInfoBean>> getUserInfo() {
        return apiService.getUserInfo("member.basics.get", "v1");
    }

    public Flowable<ResultBean> setHeadImg(String url) {
        return apiService.setHeadImg("member.setImg", "v1", url);
    }

    public Flowable<ResultBean> personAuth(String name, String idcard, String dentity, String dentity_front, String dentity_reverse) {
        return apiService.personAuth("member.autonym", "v1", name, idcard, dentity, dentity_front, dentity_reverse);
    }

    public Flowable<ResultBean> companyAuth(String company_name, String representative, String license_num, String establish_date, String area,
                                            String address, String company_phone, String license_img, String shopuser_identity_img_z, String food_or_wine_img) {
        return apiService.companyAuth("member.enterprise", "v1", company_name, representative, license_num, establish_date, area,
                address, company_phone, license_img, shopuser_identity_img_z,food_or_wine_img);
    }

    public Observable<ResultBean<AuthStatusBean>> getPersonAuthStatus() {
        return apiService.getPersonAuthStatus("member.get.autonym", "v1");
    }

    public Observable<ResultBean<AuthStatusBean>> getCompanyAuthStatus() {
        return apiService.getCompanyAuthStatus("member.get.enterprise", "v1");
    }


    public Flowable<ResultBean> valuationGoods(String title, String name, String imgs, String series) {
        return apiService.valuationGoods("member.evaluate", "v1", title, name, imgs, series);
    }

    public Flowable<ResultBean<AuctionListBean>> getAuctionList() {
        return apiService.getAuctionList("item.auction.list", "v1");
    }

    public Flowable<ResultBean> auctionGivePrice(String itemId, String price) {
        return apiService.auctionGivePrice("item.auction.userAdd", "v1", itemId, price);
    }

    public Flowable<ResultBean<MainClassBean>> getMainClass() {
        return apiService.getGoodsClass("category.itemCategory", "v1");
    }

    public Flowable<ResultBean<GoodsListBean>> getGoodsList(String cat_id, int page_no, String search_keywords, String shop_id,
                                                            String brand_id, String area_id, String odor_id, String alcohol_id, String min_price, String max_price) {
        return apiService.getGoodsList("item.search", "v1", cat_id, page_no, 20, search_keywords, shop_id,
                brand_id, area_id, odor_id, alcohol_id, min_price, max_price);
    }

    public Flowable<ResultBean<GoodsDetailBean>> getGoodsDetail(String item_id) {
        return apiService.getGoodsDetail("item.detail", "v1", item_id);
    }

    public Flowable<ResultBean<GoodsDetailBean>> getGoodsDetailWithToken(String item_id, String token) {
        return apiService.getGoodsDetailWithToken("item.detail", "v1", token, item_id);
    }

    public Flowable<ResultBean<GoodsDetailBean>> getAuctionGoodsDetail(String auctionitem_id) {
        return apiService.getAuctionGoodsDetail("item.auction.detail", "v1", auctionitem_id);
    }

    public Flowable<ResultBean<List<GivePriceBean>>> getAuctionPriceList(String auctionitem_id) {
        return apiService.getAuctionPriceList("item.auction.get", "v1", auctionitem_id);
    }

    public Flowable<ResultBean<FilterListBean>> getFilterData(String cat_id) {
        return apiService.getFilterData("item.filterItems", "v1", cat_id);
    }


    public Flowable<ResultBean<EvaluateListBean>> getEvaluateList(String item_id, int page_no) {
        return apiService.getEvaluateList("item.rate.list", "v1", 0, item_id, page_no, 10);
    }

    public Flowable<ResultBean<ShopInfoBean>> getShopInfo(String shopId) {
        return apiService.getShopInfo("shop.basic", "v1", shopId);
    }


    public Flowable<ResultBean> newAddress(String name, String mobile, String area, String addr, String def_addr) {
        return apiService.newAddress("member.address.create", "v1", name, mobile, area, addr, def_addr);
    }

    public Flowable<ResultBean<AddressListBean>> getAddressList() {
        return apiService.getAddressList("member.address.list", "v1");
    }

    public Flowable<ResultBean> editAddress(String name, String mobile, String area, String addr, String def_addr, String addressId) {
        return apiService.editAddress("member.address.update", "v1", name, mobile, area, addr, def_addr, addressId);
    }

    public Flowable<ResultBean> deleteAddress(String addressId) {
        return apiService.deleteAddress("member.address.delete", "v1", addressId);
    }

    public Flowable<ResultBean> setDefault(String addressId) {
        return apiService.setDefault("member.address.setDefault", "v1", addressId);
    }

    //获取我的模块的数量上标
    public Flowable<ResultBean<MyNumBean>> getMyNumFlag(String type) {
        return apiService.getMyNumFlag("member.index", "v1",type);
    }

    public Flowable<ResultBean<StoreClassListBean>> getStoreClass() {
        return apiService.getStoreClass("category.chateauCat", "v1");
    }

    public Flowable<ResultBean<StoreListBean>> getStoreList(String id, int pageNum) {
        return apiService.getStoreList("category.chateauList", "v1", id, pageNum, 100);
    }

    public Flowable<ResultBean<StoreDetailBean>> getStoreDetail(String id) {
        return apiService.getStoreDetail("category.chateauDetail", "v1", id);
    }


    public Flowable<ResultBean<CartListBean>> getCartList() {
        return apiService.getCartList("cart.get", "v1", "cart", "wap");
    }

    public Flowable<ResultBean> addGoodsToCart(int num, String sku_id) {
        return apiService.addGoodsToCart("cart.add", "v1", num, sku_id, "item", "cart");
    }

    public Flowable<ResultBean> addGoodsToCartBuyNow(int num, String sku_id) {
        return apiService.addGoodsToCart("cart.add", "v1", num, sku_id, "item", "fastbuy");
    }

    public Flowable<ResultBean<ConfirmOrderBean>> buyNowConfirmOrder() {
        return apiService.confirmOrder("cart.checkout", "v1", "fastbuy");
    }

    public Flowable<ResultBean<ConfirmOrderBean>> cartConfirmOrder() {
        return apiService.confirmOrder("cart.checkout", "v1", "cart");
    }

    public Flowable<ResultBean> deleteCartItem(String cart_id) {
        return apiService.deleteCartItem("cart.del", "v1", cart_id, "cart");
    }

    //cart_params: 	[{"cart_id":1,"is_checked":1,"selected_promotion":13,"totalQuantity":2},{"cart_id":7,"is_checked":1,"selected_promotion":0,"totalQuantity":1}]
    public Flowable<ResultBean> updateCartData(String cart_params) {
        return apiService.updateCartData("cart.update", "v1", "item", cart_params);
    }

    public Flowable<ResultBean<CartCountBean>> getCartCount() {
        return apiService.getCartCount("cart.count", "v1");
    }

    public Flowable<ResultBean<PaymentBean>> getPayment() {
        return apiService.getPayment("payment.pay.paycenter", "v1");
    }

    public Flowable<ResultBean<CreateOrderBean>> createOrder(String mode, String md5, String addrId, String paymentId, String shippingType,
                                                             String mark) {
        return apiService.createOrder("trade.create", "v1", mode, md5, addrId, paymentId, "app",
                shippingType, mark, "notuse", "", "");
    }

    public Flowable<ResultBean> payDo(String payment_id, String payment_type) {
        return apiService.payDo("payment.pay.do", "v1", payment_id, payment_type);
    }

    public Flowable<ResultBean<CreateAuctionBean>> createAuctionOrder(String auctionitem_id, String addr_id, String price) {
        return apiService.createAuctionOrder("payment.pay.auctionCreate", "v1", auctionitem_id, addr_id, price);
    }

    public Flowable<ResultBean> auctionAddPrice(String auctionitem_id, String price) {
        return apiService.auctionAddPrice("item.auction.userAdd", "v1", auctionitem_id, price);
    }


    public Flowable<ResultBean<HomeModulesBean>> getHomeModules() {
        return apiService.getHomeModules("theme.modules", "v1", "index");
    }

    public Flowable<ResultBean<HomeListBean>> getHomeLists(int pageNo) {
        return apiService.getHomeLists("theme.pull.goods", "v1", "index", pageNo, 10);
    }

    // image_type —— complaints 用户投诉商家图片, aftersales售后图片, rate 评价图片
    public Observable<ResultBean<UploadImageBean>> userUploadImage(String imgBase64, String imageName, String image_type) {
        return apiService.userUploadImage("image.upload", "v1", "base64", imgBase64, imageName, image_type);
    }

    public Observable<ResultBean<UploadImageBean>> shopUploadImage(String imgBase64, String imageName) {
        return apiService.shopUploadImage("image.upload", "v1", "base64", imgBase64, imageName, "item", "0");
    }

    public Flowable<ResultBean<ShopClassList>> getShopClassList() {
        return apiService.getShopClassList("category.platform.get", "v1");
    }

    public Flowable<ResultBean<ShopCatIdList>> getStoreClassList() {
        return apiService.getStoreClassList("category.shop.get", "v1");
    }


    public Observable<ResultBean> addGoods(String cat_id, String brand_id, String shop_cat_id, String title, String sub_title, String weight, String list_image,
                                           String price, String dlytmpl_id, String sku, String label, String explain, String parameter,
                                           String area_id, String odor_id, String alcohol_id, String store) {
        return apiService.addGoods("item.create", "v1", cat_id, brand_id, shop_cat_id, title, sub_title, weight, list_image,
                price, dlytmpl_id, sku, label, explain, parameter, "瓶", "1", area_id, odor_id, alcohol_id, store);
    }

    public Flowable<ResultBean<OrderListBean>> getOrderLists(String status, int pageNo) {
        return apiService.getOrderLists("trade.list", "v1", status, pageNo, 10);
    }

    public Flowable<ResultBean<List<AuctionOrderListBean>>> getAuctionOrderLists(String status, int pageNo) {
        return apiService.getAuctionOrderLists("trade.auction.list", "v1", status, pageNo, 10, "*");
    }

    public Flowable<ResultBean<OrderDetailBean>> getAuctionOrderDetail(String auctionitem_id) {
        return apiService.getAuctionOrderDetail("item.auction.detail", "v1", auctionitem_id);
    }

    public Flowable<ResultBean<OrderListBean>> getAfterSaleOrderList(String status, String progress, int pageNo) {
        return apiService.getAfterSaleOrderList("member.aftersales.list", "v1", status, progress, pageNo, 10);
    }

    public Flowable<ResultBean<OrderDetailBean>> getOrderDetail(String tid) {
        return apiService.getOrderDetail("trade.get", "v1", tid);
    }

    public Flowable<ResultBean<OrderDetailBean>> getAfterSaleOrderDetail(String aftersales_bn, String oid) {
        return apiService.getAfterSaleOrderDetail("member.aftersales.get", "v1", aftersales_bn, oid);
    }

    public Flowable<ResultBean> confirmReceipt(String tid) {
        return apiService.confirmReceipt("trade.confirm", "v1", tid);
    }

    public Flowable<ResultBean<CancelReasonBean>> getCancelReason() {
        return apiService.getCancelReason("trade.cancel.reason.get", "v1");
    }

    public Flowable<ResultBean<CancelOrderResultBean>> cancelOrder(String tid, String reason) {
        return apiService.cancelOrder("trade.cancel.create", "v1", tid, reason);
    }

    public Flowable<ResultBean> addRate(String tid, JSONArray rateData, boolean anony, int tallyScore, int attitudeScore, int deliverySpeedScore) {
        return apiService.addRate("member.rate.add", "v1", tid, rateData, anony, tallyScore, attitudeScore, deliverySpeedScore);
    }

    public Flowable<ResultBean<OrderAfterSaleReasonBean>> getAfterSaleReason(String oid) {
        return apiService.getAfterSaleReason("member.aftersales.applyInfo.get", "v1", oid);
    }

    public Flowable<ResultBean> applyAfterSaleReason(String tid, String oid, String reason, String description, String evidence_pic) {
        return apiService.applyAfterSaleReason("member.aftersales.apply", "v1", tid, oid, reason, description, "REFUND_GOODS", evidence_pic);
    }

    public Flowable<ResultBean> delete(String tid) {
        return apiService.delete("trade.delete", "v1", tid);
    }

    public Flowable<ResultBean<LogisticsBean>> getLogisticsList() {
        return apiService.getLogisticsList("logistics.list.get", "v1");
    }

    public Flowable<ResultBean> sendLogistics(String aftersales_bn, String corp_code, String logi_name, String logi_no) {
        return apiService.sendLogistics("logistics.send", "v1", aftersales_bn, corp_code, logi_name, logi_no);
    }

    public Flowable<ResultBean<CreateOrderBean>> repay(String tid, String merge) {
        return apiService.repay("payment.pay.create", "v1", tid, merge);
    }

    public Flowable<ResultBean<OrderListBean>> getSellerOrderLists(String status, int pageNo) {
        return apiService.getSellerOrderLists("trade.list", "v1", status, pageNo, 10, "*");
    }

    public Flowable<ResultBean<SellerOrderDetailBean>> getSellerOrderDetail(String tid) {
        return apiService.getSellerOrderDetail("trade.info", "v1", tid, "*");
    }

    public Flowable<ResultBean> sendSellerLogistics(String tid, String corp_code, String logi_no) {
        return apiService.sendSellerLogistics("trade.delivery.shop", "v1", tid, corp_code, logi_no);
    }

    public Flowable<ResultBean<CancelReasonBean>> getSellerCancelReason() {
        return apiService.getSellerCancelReason("trade.cancel.shop.reason", "v1");
    }

    public Flowable<ResultBean<CancelOrderResultBean>> sellerCancelOrder(String tid, String reason) {
        return apiService.sellerCancelOrder("trade.close.shop", "v1", tid, reason);
    }

    public Flowable<ResultBean<OrderListBean>> getSellerAfterSaleOrderList(String status, String progress, int pageNo) {
        return apiService.getSellerAfterSaleOrderList("aftersales.list", "v1", status, progress, pageNo, 10, "*");
    }

    public Flowable<ResultBean<OrderDetailBean>> getSellerAfterSaleOrderDetail(String aftersales_bn, String oid) {
        return apiService.getSellerAfterSaleOrderDetail("aftersales.get", "v1", aftersales_bn, oid, "*");
    }

    public Flowable<ResultBean<OrderListBean>> getSellerAfterSaleCencelOrderList(int pageNo) {
        return apiService.getSellerAfterSaleCencelOrderList("aftersales.cencel.list", "v1", pageNo, 10, "*");
    }

    public Flowable<ResultBean<OrderDetailBean>> getSellerAfterSaleCencelOrderDetail(String cancel_id) {
        return apiService.getSellerAfterSaleCencelOrderDetail("aftersales.cencel.get", "v1", cancel_id, "*");
    }

    public Flowable<ResultBean> applySellerCancelOrder(String cancel_id, String status, String reason) {
        return apiService.applySellerCancelOrder("trade.cencel.shop.check", "v1", cancel_id, status, reason);
    }

    public Flowable<ResultBean> applySellerAfterSale(String aftersales_bn, String check_result, String total_price, String refunds_reason) {
        return apiService.applySellerAfterSale("trade.cancel.shop.check", "v1", aftersales_bn, check_result, total_price, refunds_reason);
    }


    public Flowable<ResultBean<BrandsListBean>> getUserBrandList(String cat_id) {
        return apiService.getUserBrandList("category.brand.get", "v1", cat_id);
    }

    public Flowable<ResultBean<AreaListBean>> getUserAreaList(String cat_id) {
        return apiService.getUserAreaList("category.area.get", "v1", cat_id);
    }

    public Flowable<ResultBean<OrdoListBean>> getUserOrdoList(String cat_id) {
        return apiService.getUserOrdoList("category.odor.get", "v1", cat_id);
    }

    public Flowable<ResultBean<AlcListBean>> getUserAlcList(String cat_id) {
        return apiService.getUserAlcList("category.alcohol.get", "v1", cat_id);
    }

    public Flowable<ResultBean<BrandsListBean>> getAddShopBrandList(String cat_id) {
        return apiService.getAddShopBrandList("category.platform.brand.get", "v1", cat_id);
    }

    public Flowable<ResultBean<AreaListBean>> getShopAreaList(String cat_id) {
        return apiService.getShopAreaList("category.platform.area.get", "v1", cat_id);
    }

    public Flowable<ResultBean<OrdoListBean>> getShopOrdoList(String cat_id) {
        return apiService.getShopOrdoList("category.platform.odor.get", "v1", cat_id);
    }

    public Flowable<ResultBean<AlcListBean>> getShopAlcList(String cat_id) {
        return apiService.getShopAlcList("category.platform.alcohol.get", "v1", cat_id);
    }

    public Flowable<ResultBean> favoriteAddGoods(String item_id) {
        return apiService.favoriteAddGoods("member.favorite.item.add", "v1", item_id);
    }

    public Flowable<ResultBean> favoriteCancelGoods(String item_id) {
        return apiService.favoriteCancelGoods("member.favorite.item.remove", "v1", item_id);
    }

    public Flowable<ResultBean<EditGoodsDetailBean>> editGoodsDetail(String item_id) {
        return apiService.editGoodsDetail("item.detail", "v1", item_id);
    }

    public Observable<ResultBean> commitEditGoodsDetail(String cat_id, String brand_id, String shop_cat_id, String title, String sub_title, String weight, String list_image,
                                                        String price, String dlytmpl_id, String sku, String label, String explain, String parameter,
                                                        String area_id, String odor_id, String alcohol_id, String store, String itemId) {
        return apiService.commitEditGoodsDetail("item.save", "v1", cat_id, brand_id, shop_cat_id, title, sub_title, weight, list_image,
                price, dlytmpl_id, sku, label, explain, parameter, "瓶", "1", area_id, odor_id, alcohol_id, store, itemId);
    }

    public Flowable<ResultBean<List<RecommendGoodsBean>>> getRecommendGoods(String item_id) {
        return apiService.getRecommendGoods("item.recommend", "v1", item_id);
    }

    public Flowable<ResultBean> editShopName(String token, String name) {
        return apiService.editShopName("user.update.shop", "v1", token, name);
    }

    public Flowable<ResultBean<List<FansItemBean>>> getFansList() {
        return apiService.getFansList("user.update.shop", "v1");
    }
    public Flowable<ResultBean<FansBean>> getFansInfo() {
        return apiService.getFansInfo("user.update.shop", "v1");
    }
    public Flowable<ResultBean> submitInviteCode(String inviteCode) {
        return apiService.submitInviteCode("user.update.shop", "v1",inviteCode);
    }
}
