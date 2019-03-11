package com.chunlangjiu.app.util;

import com.pkqup.commonlibrary.net.HttpUtils;

/**
 * @CreatedbBy: liucun on 2018/8/9
 * @Describe:
 */
public class ConstantMsg {

    public static final String MSG_PAGE_CLASS = "msg_page_class";//首页切换到分类
    public static final String MSG_PAGE_AUCTION = "msg_page_auction";//首页切换到竞拍专区
    public static final String MSG_PAGE_MY = "msg_page_my";//首页切换到我的
    public static final String MSG_PAGE_CART = "msg_page_cart";//首页切换到购物车
    public static final String SHOP_DATA_CHANGE = "shop_data_change";//商家数据变化
    public static final String USER_DATA_CHANGE = "user_data_change";//商家数据变化
    public static final String LOGIN_SUCCESS = "msg_login_success";//登录成功
    public static final String LOGOUT_SUCCESS = "msg_logout_success";//登出成功
    public static final String PERSON_COMPANY_AUTH_SUCCESS = "person_company_auth_success";//个人或者企业认证成功
    public static final String ADD_ADDRESS_SUCCESS = "msg_add_address_success";//添加地址成功
    public static final String CHANGE_TO_EVALUATE = "msg_change_to_evaluate";//切换到评论tab
    public static final String WEIXIN_PAY_CALLBACK = "weixin_pay_success";//微信支付成功
    public static final String UPDATE_CART_LIST = "update_cart_list";//添加到购物车、支付成功等操作后更新购物车
    public static final String UPDATE_WEBVIEW = "update_webview";//刷新网页

    public static final String AUCTION_SLIDE_CHANGE = "auction_slide_change";//竞拍商品详情滑动变化
    public static final String GOODS_SLIDE_CHANGE = "goods_slide_change";//普通商品详情滑动变化

    public static final String APPLY_LOCATION_SUCCESS = "apply_location_success" ;//申请定位成功
    public static final String SET_INVITATION_CODE = "setInvitationCode";//设置邀请码
    public static final String HOME_COUNT_END = "home_count_end";//首页倒计时完成
    public static final String AUCTION_COUNT_END = "auction_count_end";//竞拍倒计时完成
    public static final String DETAIL_COUNT_END = "detail_count_end";//详情倒计时完成

    public static final String RECHARGE ="recharge_success";//充值变化
    public static final String DEPOSIT_CREATE="deposit_create";//交纳保证金变化
    public static final String WITHDRAW_DEPOSIT_CASH="withdraw_deposit_cash";//提现
    public static final String WITHDRAW_DEPOSIT_REFUND="withdraw_deposit_refund";//撤销保证金
    public static final String BANKCARD_CHANGE="bank_card_change";//撤销保证金

    public static final String WEB_URL_MESSAGE = HttpUtils.BASE_URL+ "index.php/wap/app-information.html?apitoken=";//消息
    public static final String WEB_URL_SETTING = HttpUtils.BASE_URL+ "index.php/wap/app-setting.html?apitoken=";//设置
    public static final String WEB_URL_LICENSE = HttpUtils.BASE_URL+ "index.php/wap/app-license.html";//协议

    public static final String WEB_URL_GOODS_MANAGER = HttpUtils.BASE_URL+ "index.php/wap/app-itemCenter.html?apitoken=";//商品管理
    public static final String WEB_URL_SELL_GOODS = HttpUtils.BASE_URL+ "index.php/wap/onsale.html?apitoken=";//在售商品
    public static final String WEB_URL_ACTION_GOODS = HttpUtils.BASE_URL+ "index.php/wap/auctionList.html?apitoken=";//竞拍商品
    public static final String WEB_URL_STORE_GOODS = HttpUtils.BASE_URL+ "index.php/wap/instock.html?apitoken=";//仓库商品
    public static final String WEB_URL_AUTH_GOODS = HttpUtils.BASE_URL+ "index.php/wap/app-pending.html?apitoken=";//审核商品

    public static final String WEB_URL_MONEY_MANAGER = HttpUtils.BASE_URL+ "index.php/wap/app-capital.html?apitoken=";//资金管理
    public static final String WEB_URL_COLLECT = HttpUtils.BASE_URL+ "index.php/wap/app-collect.html?apitoken=";//我的收藏
    public static final String WEB_URL_VIP_INFO = HttpUtils.BASE_URL+ "index.php/wap/app-storeinfo.html?apitoken=";//个人会员资料
    public static final String WEB_URL_SHOP_INFO = HttpUtils.BASE_URL+ "index.php/wap/app-shopinfo.html?apitoken=";//商家会员资料
    public static final String WEB_URL_BANK_CARD = HttpUtils.BASE_URL+ "index.php/wap/app-bank.html?apitoken=";//银行卡管理
    public static final String WEB_URL_EVALUATE = HttpUtils.BASE_URL+ "index.php/wap/app-evaluate.html?apitoken=";//我的估值



   /* public static final String WEB_URL_MESSAGE = "http://mall.chunlangjiu.com/index.php/wap/app-information.html?apitoken=";//消息
    public static final String WEB_URL_SETTING = "http://mall.chunlangjiu.com/index.php/wap/app-setting.html?apitoken=";//设置
    public static final String WEB_URL_LICENSE = "http://mall.chunlangjiu.com/index.php/wap/app-license.html";//协议

    public static final String WEB_URL_GOODS_MANAGER = "http://mall.chunlangjiu.com/index.php/wap/app-itemCenter.html?apitoken=";//商品管理
    public static final String WEB_URL_SELL_GOODS = "http://mall.chunlangjiu.com/index.php/wap/onsale.html?apitoken=";//在售商品
    public static final String WEB_URL_ACTION_GOODS = "http://mall.chunlangjiu.com/index.php/wap/auctionList.html?apitoken=";//竞拍商品
    public static final String WEB_URL_STORE_GOODS = "http://mall.chunlangjiu.com/index.php/wap/instock.html?apitoken=";//仓库商品
    public static final String WEB_URL_AUTH_GOODS = "http://mall.chunlangjiu.com/index.php/wap/app-pending.html?apitoken=";//审核商品

    public static final String WEB_URL_MONEY_MANAGER = "http://mall.chunlangjiu.com/index.php/wap/app-capital.html?apitoken=";//资金管理
    public static final String WEB_URL_COLLECT = "http://mall.chunlangjiu.com/index.php/wap/app-collect.html?apitoken=";//我的收藏
    public static final String WEB_URL_VIP_INFO = "http://mall.chunlangjiu.com/index.php/wap/app-storeinfo.html?apitoken=";//个人会员资料
    public static final String WEB_URL_SHOP_INFO = "http://mall.chunlangjiu.com/index.php/wap/app-shopinfo.html?apitoken=";//商家会员资料
    public static final String WEB_URL_BANK_CARD = "http://mall.chunlangjiu.com/index.php/wap/app-bank.html?apitoken=";//银行卡管理
    public static final String WEB_URL_EVALUATE = "http://mall.chunlangjiu.com/index.php/wap/app-evaluate.html?apitoken=";//我的估值
*/
}
