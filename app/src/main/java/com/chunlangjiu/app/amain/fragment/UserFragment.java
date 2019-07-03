package com.chunlangjiu.app.amain.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.activity.LoginMainActivity;
import com.chunlangjiu.app.appraise.activity.AppraiserBuyerHomeActivity;
import com.chunlangjiu.app.appraise.activity.AppraiserSellerHomeActivity;
import com.chunlangjiu.app.fans.activity.FansHomeActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.dialog.EditAccountNameDialog;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageHomeActivity;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageListActivity;
import com.chunlangjiu.app.money.activity.MoneyManagerActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderMainNewActivity;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.user.activity.CompanyAuthActivity;
import com.chunlangjiu.app.user.activity.PersonAuthActivity;
import com.chunlangjiu.app.user.activity.ServiceActivity;
import com.chunlangjiu.app.user.activity.SettingActivity;
import com.chunlangjiu.app.user.activity.VerifiedActivity;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.user.bean.MyNumBean;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.chunlangjiu.app.user.dialog.UserCheckAuthDialog;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.util.Rotate3dAnimation;
import com.chunlangjiu.app.util.ShareUtils;
import com.chunlangjiu.app.web.WebViewActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: liucun on 2018/6/16. 13824404512  15919823747
 * @Describe: 我的
 */
public class UserFragment extends BaseFragment {

    private LinearLayout llNotLogin;
    private TextView tvToLogin;
    private RelativeLayout rlHead;
    private RelativeLayout rlContentLayout;

    private TextView tvMyTitle;
    private ImageView ivSetting;
    private RelativeLayout rlAuthStatus;
    private ImageView imgAuthStatus;
    private TextView tvAuthStatus;

    private CircleImageView imgHead;
    private TextView tvName;
    private ImageView imgEditName;
    private TextView tvChangeType;
    private TextView tvAuthPerson;
    private TextView tvAuthCompany;

    private LinearLayout llCanUseMoney;
    private TextView tvCanUseMoney;
    private LinearLayout llNotUseMoney;
    private TextView tvNotUseMoney;
    private LinearLayout llMessageNum;
    private TextView tvMessageNum;


    //切换用户布局
    private LinearLayout llSwitchLayout;
    private ImageView ivSwitchIcon;
    private TextView tvSwitchText;


    /*订单管理*/
    private RelativeLayout rlOrderManager;
    //-----------买家中心------------//
    private LinearLayout llBuyOrder;
    private RelativeLayout rlOrderOne;
    private TextView tvOrderOneNum;
    private RelativeLayout rlOrderTwo;
    private TextView tvOrderTwoNum;
    private RelativeLayout rlOrderThree;
    private TextView tvOrderThreeNum;
    private RelativeLayout rlOrderFour;
    private TextView tvOrderFourNum;
    private RelativeLayout rlOrderFive;
    private TextView tvOrderFiveNum;
    //-----------买家中心------------//

    //-----------卖家中心------------//
    private LinearLayout llSellOrder;
    private RelativeLayout rlSellOrderOne;
    private TextView tvSellOrderOneNum;
    private RelativeLayout rlSellOrderTwo;
    private TextView tvSellOrderTwoNum;
    private RelativeLayout rlSellOrderThree;
    private TextView tvSellOrderThreeNum;
    private RelativeLayout rlSellOrderFour;
    private TextView tvSellOrderFourNum;
    private RelativeLayout rlSellOrderFive;
    private TextView tvSellOrderFiveNum;
    //-----------卖家中心------------//
    /*订单管理*/

    /*竞拍订单管理*/
    private LinearLayout llAuctionContent;
    private RelativeLayout rlAuctionManager;
    private LinearLayout llBuyAuction;
    private RelativeLayout rlAuctionOne;
    private TextView tvAuctionOneNum;
    private RelativeLayout rlAuctionTwo;
    private TextView tvAuctionTwoNum;
    private RelativeLayout rlAuctionThree;
    private TextView tvAuctionThreeNum;
    private RelativeLayout rlAuctionFour;
    private TextView tvAuctionFourNum;
    private RelativeLayout rlAuctionFive;
    private TextView tvAuctionFiveNum;

    private LinearLayout llSellAuction;
    private RelativeLayout rlSellAuctionOne;
    private TextView tvSellAuctionOneNum;
    private RelativeLayout rlSellAuctionTwo;
    private TextView tvSellAuctionTwoNum;
    private RelativeLayout rlSellAuctionThree;
    private TextView tvSellAuctionThreeNum;
    private RelativeLayout rlSellAuctionFour;
    private TextView tvSellAuctionFourNum;
    /*竞拍订单管理*/

    /*商品管理*/
    private RelativeLayout rlGoodsManager;
    private LinearLayout llGoodsContent;
    private RelativeLayout rlAddGoods;
    private RelativeLayout rlSellGoods;
    private RelativeLayout rlAuctionGoods;
    private RelativeLayout rlWareHouseGoods;
    private RelativeLayout rlCheckGoods;
    private RelativeLayout rlGoodsManagerPlace;
    private TextView tvCheckGoodsNum;
    private TextView tvWareHouseGoodsNum;
    private TextView tvAuctionGoodsNum;
    private TextView tvSellGoodsNum;

    /*商品管理*/

    /*我的管理*/
    FlowLayout flowLayout;
    private LinearLayout rlMoneyManager;
    private LinearLayout rlCollect;
    private LinearLayout rlShare;
    private LinearLayout rlShop;
    private LinearLayout llMyManagerSecond;
    private LinearLayout rlMyEvaluate;
    private LinearLayout rlFansManage;
    private LinearLayout rlSetting;
    private LinearLayout rlService;
    private LinearLayout rlAppraiseReport ;
    private LinearLayout rlAppraiser ;
    private ImageView img_appraiser;
    /*我的管理*/

    public static final int TYPE_BUYER = 1;//买家中心
    public static final int TYPE_SELLER = 0;//卖家中心
    public static int userType = TYPE_BUYER;
    private String companyStatus;
    private String personStatus;

    private UserCheckAuthDialog userCheckAuthDialog ;
    private ChoicePhotoDialog photoDialog;
    private EditAccountNameDialog editAccountNameDialog;
    public static final int REQUEST_CODE_CHOICE_HEAD = 1001;

    private CompositeDisposable disposable;

    private UserInfoBean userInfo ;
    private String loginAccount;
    private String personName;
    private String companyName;
    private String shopName;
    private String shopId;


    Rotate3dAnimation rotateStart;
    Rotate3dAnimation rotateEnd;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!BaseApplication.isLogin()) {
                LoginMainActivity.startLoginActivity(activity);
                return;
            }
            switch (view.getId()) {
                case R.id.llSwitchLayout:
                    checkStatus();// 切换买/卖家中心
                    break;
                case R.id.tvToLogin:
                    break;
                case R.id.ivSetting:
                case R.id.rl_setting:
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_SETTING + BaseApplication.getToken(), "设置");
                    SettingActivity.startActivity(getActivity());
                    break;
                case R.id.imgHead:
                    setHeadIcon();
                    break;
                case R.id.imgEditName://编辑昵称或者店铺名
                    showEditNameDialog();
                    break;
                case R.id.rlAuthStatus:
                    if (!AuthStatusBean.AUTH_SUCCESS.equals(personStatus) && !AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
                        startActivity(new Intent(getActivity(), VerifiedActivity.class));
                    }
                    break;
                case R.id.tvAuthRealName:// 个人认证
                    checkPersonStatus();
                    break;
                case R.id.tvAuthCompany:// 升级为企业(进入企业认证)
                    checkCompanyStatus();
                    break;
                case R.id.llMessageNum:// 我的消息
                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_MESSAGE + BaseApplication.getToken(), "消息");
                    break;
                case R.id.rlOrderManager:// 订单管理
                    if (llSellOrder.isShown()) {
                        toOrderMainActivity(3, 0);
                    } else {
                        toOrderMainActivity(0, 0);
                    }
                    break;
                case R.id.rlOrderOne:// 买家待付款
                    toOrderMainActivity(0, 1);
                    break;
                case R.id.rlOrderTwo:// 买家待收货
                    toOrderMainActivity(0, 2);
                    break;
                case R.id.rlOrderThree:// 买家待发货
                    toOrderMainActivity(0, 3);
                    break;
                case R.id.rlOrderFour:// 买家售后订单
                    toOrderMainActivity(2, 0);
                    break;
                case R.id.rlOrderFive:// 买家全部订单
                    toOrderMainActivity(0, 0);
                    break;
                case R.id.rlSellOrderOne:// 卖家待付款
                    toOrderMainActivity(3, 1);
                    break;
                case R.id.rlSellOrderTwo:// 卖家待发货
                    toOrderMainActivity(3, 2);
                    break;
                case R.id.rlSellOrderThree:// 卖家待收货
                    toOrderMainActivity(3, 3);
                    break;
                case R.id.rlSellOrderFour:// 卖家售后订单
                    toOrderMainActivity(4, 0);
                    break;
                case R.id.rlSellOrderFive://  卖家取消订单
                    toOrderMainActivity(5, 0);
                    break;
                case R.id.rlAuctionManager:// 竞拍订单管理
                    toOrderMainActivity(1, 0);
                    break;
                case R.id.rlAuctionOne:// 买家竞拍订单-待付定金
                    toOrderMainActivity(1, 1);
                    break;
                case R.id.rlAuctionTwo:// 买家竞拍订单-竞拍中
                    toOrderMainActivity(1, 2);
                    break;
                case R.id.rlAuctionThree:// 买家竞拍订单-已中标
                    toOrderMainActivity(1, 3);
                    break;
                case R.id.rlAuctionFour:// 买家竞拍订单-落标
                    toOrderMainActivity(1, 4);
                    break;
                case R.id.rlAuctionFive:// 买家竞拍订单-全部订单
                    toOrderMainActivity(1, 0);
                    break;
                case R.id.rlSellAuctionOne:// 卖家竞拍订单-待付款
                    break;
                case R.id.rlSellAuctionTwo:// 卖家竞拍订单-待发货
                    break;
                case R.id.rlSellAuctionThree:// 卖家竞拍订单-待收货
                    break;
                case R.id.rlSellAuctionFour:// 卖家竞拍订单-全部订单
                    break;
                case R.id.rlGoodsManager:// 商品管理
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_GOODS_MANAGER + BaseApplication.getToken(), "商品管理");
                    GoodsManageHomeActivity.startShopMainActivity(activity);
                    break;
                case R.id.rlAddGoods:// 添加商品
                    startActivity(new Intent(getActivity(), AddGoodsActivity.class));
                    break;
                case R.id.rlSellGoods:// 在售商品
                    GoodsManageListActivity.startGoodsManageActivity(activity, CommonUtils.GOODS_STATUS_SELL);
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_SELL_GOODS + BaseApplication.getToken(), "在售商品");
                    break;
                case R.id.rlAuctionGoods:// 竞拍商品
                    GoodsManageListActivity.startGoodsManageActivity(activity, CommonUtils.GOODS_STATUS_AUCTION_ACTIVE);
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_ACTION_GOODS + BaseApplication.getToken(), "竞拍商品");
                    break;
                case R.id.rlWareHouseGoods:// 仓库商品
                    GoodsManageListActivity.startGoodsManageActivity(activity, CommonUtils.GOODS_STATUS_INSTOCK);
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_STORE_GOODS + BaseApplication.getToken(), "仓库商品");
                    break;
                case R.id.rlCheckGoods:// 审核商品
                    GoodsManageListActivity.startGoodsManageActivity(activity, CommonUtils.GOODS_STATUS_AUDIT_PENDING);
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_AUTH_GOODS + BaseApplication.getToken(), "审核商品");
                    break;
                case R.id.rlMoneyManager:// 资金管理
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_MONEY_MANAGER + BaseApplication.getToken(), "资金管理");
                    startActivity(new Intent(getActivity(), MoneyManagerActivity.class));
                    break;
                case R.id.rlShare:// 分享
                    showShareDialog();
                    break;
                case R.id.rlCollect:// 我的收藏
                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_COLLECT + BaseApplication.getToken(), "我的收藏");
                    break;
                case R.id.rlMyEvaluate:// 我的估价
//                    WebViewActivity.startWebViewActivity(getActivity(), ConstantMsg.WEB_URL_EVALUATE + BaseApplication.getToken(), "我的估价");
//                    AppraiserMainActivity.
                    break;
                case R.id.rl_fans_manage:// 粉丝管理
                    startActivity(new Intent(getActivity(), FansHomeActivity.class));
                    break;
                case R.id.rl_service:// 粉丝管理
                    ServiceActivity.startActivity(activity);
                    break;
                case R.id.rlShop:
                    ShopMainActivity.startShopMainActivity(activity, shopId);
                    break;
                case R.id.rlAppraiseReport:
                    startActivity(new Intent(getActivity(), AppraiserBuyerHomeActivity.class));
                    break;
                case R.id.rlAppraiser:
                    if(userInfo != null && "true".equals(userInfo.getAuthenticate()) && !TextUtils.isEmpty(userInfo.getAuthenticate_id())){
                        AppraiserSellerHomeActivity.startAppraiserInfoActivity(activity,userInfo.getAuthenticate_id());
                    }
//                    else{
//                        startActivity(new Intent(activity, AppraiserInfoEditActivity.class));
//                    }
                    break;

            }
        }
    };

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.amain_fragment_user, container, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
        if (BaseApplication.isLogin()) {
            getUserInfo();
            getBuyerOrderNumIndex();
            getSellerOrderNumIndex();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void initView() {
        MyStatusBarUtils.setTitleBarPadding(getActivity(), rootView.findViewById(R.id.rlUserHead));
        userCheckAuthDialog = new UserCheckAuthDialog(activity);
        rlContentLayout = rootView.findViewById(R.id.rlContentLayout);
        llNotLogin = rootView.findViewById(R.id.llNotLogin);
        tvToLogin = rootView.findViewById(R.id.tvToLogin);
        tvToLogin.setOnClickListener(onClickListener);
        rlHead = rootView.findViewById(R.id.rlHead);
        ivSetting = rootView.findViewById(R.id.ivSetting);
        MyStatusBarUtils.setTitleBarPadding(getActivity(), ivSetting);
        if (Build.VERSION.SDK_INT >= 21) {
            // 获得状态栏高度
            int statusBarHeight = MyStatusBarUtils.getStatusBarHeight(activity);
            int left = ivSetting.getPaddingLeft();
            int top = statusBarHeight + ivSetting.getPaddingTop() - SizeUtils.dp2px(10);
            int right = ivSetting.getPaddingRight();
            int bottom = ivSetting.getPaddingBottom();
            ivSetting.setPadding(left, top, right, bottom);
        }
        ivSetting.setOnClickListener(onClickListener);

        tvMyTitle = rootView.findViewById(R.id.tvUserTitle);
        rlAuthStatus = rootView.findViewById(R.id.rlAuthStatus);
        rlAuthStatus.setOnClickListener(onClickListener);
        rlAuthStatus.setVisibility(View.GONE);
        imgAuthStatus = rootView.findViewById(R.id.imgAuthStatus);
        tvAuthStatus = rootView.findViewById(R.id.tvAuthStatus);

        imgHead = rootView.findViewById(R.id.imgHead);
        imgHead.setOnClickListener(onClickListener);
        tvName = rootView.findViewById(R.id.tvName);
        imgEditName = rootView.findViewById(R.id.imgEditName);
        imgEditName.setOnClickListener(onClickListener);
        imgEditName.setVisibility(View.GONE);

        tvChangeType = rootView.findViewById(R.id.tvChangeType);
        tvChangeType.setOnClickListener(onClickListener);
        tvAuthPerson = rootView.findViewById(R.id.tvAuthRealName);
        tvAuthPerson.setOnClickListener(onClickListener);
        tvAuthCompany = rootView.findViewById(R.id.tvAuthCompany);
        tvAuthCompany.setOnClickListener(onClickListener);
        tvAuthPerson.setVisibility(View.GONE);
        tvAuthCompany.setVisibility(View.GONE);

        llCanUseMoney = rootView.findViewById(R.id.llCanUseMoney);
        llCanUseMoney.setOnClickListener(onClickListener);
        tvCanUseMoney = rootView.findViewById(R.id.tvCanUseMoney);
        llNotUseMoney = rootView.findViewById(R.id.llNotUseMoney);
        llNotUseMoney.setOnClickListener(onClickListener);
        tvNotUseMoney = rootView.findViewById(R.id.tvNotUseMoney);
        llMessageNum = rootView.findViewById(R.id.llMessageNum);
        llMessageNum.setOnClickListener(onClickListener);
        tvMessageNum = rootView.findViewById(R.id.tvMessageNum);

        rlOrderManager = rootView.findViewById(R.id.rlOrderManager);
        rlOrderManager.setOnClickListener(onClickListener);
        llBuyOrder = rootView.findViewById(R.id.llBuyOrder);
        rlOrderOne = rootView.findViewById(R.id.rlOrderOne);
        rlOrderOne.setOnClickListener(onClickListener);
        tvOrderOneNum = rootView.findViewById(R.id.tvOrderOneNum);
        rlOrderTwo = rootView.findViewById(R.id.rlOrderTwo);
        rlOrderTwo.setOnClickListener(onClickListener);
        tvOrderTwoNum = rootView.findViewById(R.id.tvOrderTwoNum);
        rlOrderThree = rootView.findViewById(R.id.rlOrderThree);
        rlOrderThree.setOnClickListener(onClickListener);
        tvOrderThreeNum = rootView.findViewById(R.id.tvOrderThreeNum);
        rlOrderFour = rootView.findViewById(R.id.rlOrderFour);
        rlOrderFour.setOnClickListener(onClickListener);
        tvOrderFourNum = rootView.findViewById(R.id.tvOrderFourNum);
        rlOrderFive = rootView.findViewById(R.id.rlOrderFive);
        rlOrderFive.setOnClickListener(onClickListener);
        tvOrderFiveNum = rootView.findViewById(R.id.tvOrderFiveNum);
        llSellOrder = rootView.findViewById(R.id.llSellOrder);
        rlSellOrderOne = rootView.findViewById(R.id.rlSellOrderOne);
        rlSellOrderOne.setOnClickListener(onClickListener);
        tvSellOrderOneNum = rootView.findViewById(R.id.tvSellOrderOneNum);
        rlSellOrderTwo = rootView.findViewById(R.id.rlSellOrderTwo);
        rlSellOrderTwo.setOnClickListener(onClickListener);
        tvSellOrderTwoNum = rootView.findViewById(R.id.tvSellOrderTwoNum);
        rlSellOrderThree = rootView.findViewById(R.id.rlSellOrderThree);
        rlSellOrderThree.setOnClickListener(onClickListener);
        tvSellOrderThreeNum = rootView.findViewById(R.id.tvSellOrderThreeNum);
        rlSellOrderFour = rootView.findViewById(R.id.rlSellOrderFour);
        rlSellOrderFour.setOnClickListener(onClickListener);
        tvSellOrderFourNum = rootView.findViewById(R.id.tvSellOrderFourNum);
        rlSellOrderFive = rootView.findViewById(R.id.rlSellOrderFive);
        rlSellOrderFive.setOnClickListener(onClickListener);
        tvSellOrderFiveNum = rootView.findViewById(R.id.tvSellOrderFiveNum);

        llAuctionContent = rootView.findViewById(R.id.llAuctionContent);
        rlAuctionManager = rootView.findViewById(R.id.rlAuctionManager);
        llBuyAuction = rootView.findViewById(R.id.llBuyAuction);
        rlAuctionOne = rootView.findViewById(R.id.rlAuctionOne);
        tvAuctionOneNum = rootView.findViewById(R.id.tvAuctionOneNum);
        rlAuctionTwo = rootView.findViewById(R.id.rlAuctionTwo);
        tvAuctionTwoNum = rootView.findViewById(R.id.tvAuctionTwoNum);
        rlAuctionThree = rootView.findViewById(R.id.rlAuctionThree);
        tvAuctionThreeNum = rootView.findViewById(R.id.tvAuctionThreeNum);
        rlAuctionFour = rootView.findViewById(R.id.rlAuctionFour);
        tvAuctionFourNum = rootView.findViewById(R.id.tvAuctionFourNum);
        rlAuctionFive = rootView.findViewById(R.id.rlAuctionFive);
        tvAuctionFiveNum = rootView.findViewById(R.id.tvAuctionFiveNum);
        llSellAuction = rootView.findViewById(R.id.llSellAuction);
        rlSellAuctionOne = rootView.findViewById(R.id.rlSellAuctionOne);
        tvSellAuctionOneNum = rootView.findViewById(R.id.tvSellAuctionOneNum);
        rlSellAuctionTwo = rootView.findViewById(R.id.rlSellAuctionTwo);
        tvSellAuctionTwoNum = rootView.findViewById(R.id.tvSellAuctionTwoNum);
        rlSellAuctionThree = rootView.findViewById(R.id.rlSellAuctionThree);
        tvSellAuctionThreeNum = rootView.findViewById(R.id.tvSellAuctionThreeNum);
        rlSellAuctionFour = rootView.findViewById(R.id.rlSellAuctionFour);
        tvSellAuctionFourNum = rootView.findViewById(R.id.tvSellAuctionFourNum);
        rlAuctionManager.setOnClickListener(onClickListener);
        rlAuctionOne.setOnClickListener(onClickListener);
        rlAuctionTwo.setOnClickListener(onClickListener);
        rlAuctionThree.setOnClickListener(onClickListener);
        rlAuctionFour.setOnClickListener(onClickListener);
        rlAuctionFive.setOnClickListener(onClickListener);
        rlSellAuctionOne.setOnClickListener(onClickListener);
        rlSellAuctionTwo.setOnClickListener(onClickListener);
        rlSellAuctionThree.setOnClickListener(onClickListener);
        rlSellAuctionFour.setOnClickListener(onClickListener);

        rlGoodsManager = rootView.findViewById(R.id.rlGoodsManager);
        llGoodsContent = rootView.findViewById(R.id.llGoodsContent);
        rlAddGoods = rootView.findViewById(R.id.rlAddGoods);
        rlSellGoods = rootView.findViewById(R.id.rlSellGoods);
        rlAuctionGoods = rootView.findViewById(R.id.rlAuctionGoods);
        rlWareHouseGoods = rootView.findViewById(R.id.rlWareHouseGoods);
        rlCheckGoods = rootView.findViewById(R.id.rlCheckGoods);
        rlGoodsManagerPlace = rootView.findViewById(R.id.rlGoodsManagerPlace);
        tvCheckGoodsNum = rootView.findViewById(R.id.tvCheckGoodsNum);
        tvWareHouseGoodsNum = rootView.findViewById(R.id.tvWareHouseGoodsNum);
        tvAuctionGoodsNum = rootView.findViewById(R.id.tvAuctionGoodsNum);
        tvSellGoodsNum = rootView.findViewById(R.id.tvSellGoodsNum);
        rlGoodsManager.setOnClickListener(onClickListener);
        rlAddGoods.setOnClickListener(onClickListener);
        rlSellGoods.setOnClickListener(onClickListener);
        rlAuctionGoods.setOnClickListener(onClickListener);
        rlWareHouseGoods.setOnClickListener(onClickListener);
        rlCheckGoods.setOnClickListener(onClickListener);

        flowLayout = rootView.findViewById(R.id.flowLayout);
        rlMoneyManager = rootView.findViewById(R.id.rlMoneyManager);
        rlCollect = rootView.findViewById(R.id.rlCollect);
        rlShare = rootView.findViewById(R.id.rlShare);
        rlShop = rootView.findViewById(R.id.rlShop);
        rlMoneyManager.setOnClickListener(onClickListener);
        rlCollect.setOnClickListener(onClickListener);
        rlShare.setOnClickListener(onClickListener);
        rlShop.setOnClickListener(onClickListener);
        int width = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(20)) / 3;
        for (int i = 0; i < flowLayout.getChildCount(); i++) {
            View view = flowLayout.getChildAt(i);
            FlowLayout.LayoutParams layoutParams = (FlowLayout.LayoutParams) view.getLayoutParams();
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
            view.setMinimumWidth(width);
        }


        llMyManagerSecond = rootView.findViewById(R.id.llMyManagerSecond);
        rlMyEvaluate = rootView.findViewById(R.id.rlMyEvaluate);
        rlFansManage = rootView.findViewById(R.id.rl_fans_manage);
        rlSetting = rootView.findViewById(R.id.rl_setting);
        rlService = rootView.findViewById(R.id.rl_service);
        rlAppraiser = rootView.findViewById(R.id.rlAppraiser);
        img_appraiser = rootView.findViewById(R.id.img_appraiser);
        rlAppraiseReport = rootView.findViewById(R.id.rlAppraiseReport);
        rlMyEvaluate.setOnClickListener(onClickListener);
        rlFansManage.setOnClickListener(onClickListener);
        rlSetting.setOnClickListener(onClickListener);
        rlService.setOnClickListener(onClickListener);
        rlAppraiser.setOnClickListener(onClickListener);
        rlAppraiseReport.setOnClickListener(onClickListener);


        //切换用户布局
        llSwitchLayout = rootView.findViewById(R.id.llSwitchLayout);
        ivSwitchIcon = rootView.findViewById(R.id.ivSwitchIcon);
        tvSwitchText = rootView.findViewById(R.id.tvSwitchText);
        llSwitchLayout.setOnClickListener(onClickListener);

//        if (BaseApplication.HIDE_AUCTION) {
//            llAuctionContent.setVisibility(View.GONE);
//            rlAuctionGoods.setVisibility(View.GONE);
//            rlGoodsManagerPlace.setVisibility(View.VISIBLE);
//        } else {
//            llAuctionContent.setVisibility(View.VISIBLE);
//            rlAuctionGoods.setVisibility(View.VISIBLE);
//            rlGoodsManagerPlace.setVisibility(View.GONE);
//        }

        checkLogin();
    }

    private void checkLogin() {
        if (BaseApplication.isLogin()) {
            llNotLogin.setVisibility(View.GONE);
//            rlHead.setVisibility(View.VISIBLE);
            showUserTypeView();
        } else {
            llNotLogin.setVisibility(View.VISIBLE);
//            rlHead.setVisibility(View.GONE);
        }
    }

    private void showUserTypeView() {
        if (userType == TYPE_BUYER) {
            //买家中心
            imgEditName.setVisibility(View.GONE);
            llNotUseMoney.setVisibility(View.GONE);
            tvName.setText(loginAccount);

            tvMyTitle.setText("买家中心");
            ivSwitchIcon.setImageResource(R.mipmap.center_seller);
            tvSwitchText.setText("进入卖家中心  >>");
            rlAppraiseReport.setVisibility(View.VISIBLE);
            rlAppraiser.setVisibility(View.GONE);

            llBuyOrder.setVisibility(View.VISIBLE);
            llSellOrder.setVisibility(View.GONE);
            rlAuctionManager.setVisibility(View.VISIBLE);
            llBuyAuction.setVisibility(View.VISIBLE);
            llSellAuction.setVisibility(View.GONE);

            rlGoodsManager.setVisibility(View.GONE);
            llGoodsContent.setVisibility(View.GONE);

            rlCollect.setVisibility(View.VISIBLE);
            rlShop.setVisibility(View.GONE);
            llMyManagerSecond.setVisibility(View.VISIBLE);
        } else {
            //卖家中心
            imgEditName.setVisibility(View.VISIBLE);
            llNotUseMoney.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(shopName)) {
                if (!TextUtils.isEmpty(companyName)) {
                    tvName.setText(companyName);
                } else if(!TextUtils.isEmpty(personName)){
                    tvName.setText(personName);
                }else{
                    tvName.setText("设置店铺名称");
                }
            } else {
                tvName.setText(shopName);
            }


            tvMyTitle.setText("卖家中心");
            ivSwitchIcon.setImageResource(R.mipmap.center_buyer);
            tvSwitchText.setText("进入买家中心 >>");


            llBuyOrder.setVisibility(View.GONE);
            llSellOrder.setVisibility(View.VISIBLE);
            rlAuctionManager.setVisibility(View.GONE);
            llBuyAuction.setVisibility(View.GONE);
            llSellAuction.setVisibility(View.GONE);

            rlGoodsManager.setVisibility(View.VISIBLE);
            llGoodsContent.setVisibility(View.VISIBLE);
            rlShop.setVisibility(View.VISIBLE);
            rlCollect.setVisibility(View.VISIBLE);
            rlMyEvaluate.setVisibility(View.GONE);
            rlAppraiseReport.setVisibility(View.GONE);
            rlAppraiser.setVisibility(View.VISIBLE);
            llMyManagerSecond.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        disposable = new CompositeDisposable();
        EventManager.getInstance().registerListener(onNotifyListener);
        initImagePicker();
        if (BaseApplication.isLogin()) {
            getUserInfo();
//            getBuyerOrderNumIndex();
//            getSellerOrderNumIndex();
            getPersonAndCompanyAuthStatus();
        }
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(300);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(300);                         //保存文件的高度。单位像素
    }

    private void getUserInfo() {
        disposable.add(ApiUtils.getInstance().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UserInfoBean>>() {
                    @Override
                    public void accept(ResultBean<UserInfoBean> userInfoBeanResultBean) throws Exception {
                        userInfo = userInfoBeanResultBean.getData();
                        GlideUtils.loadImageHead(getActivity(), userInfo.getHead_portrait(), imgHead);
                        loginAccount = userInfo.getLogin_account();
                        personName = userInfo.getName();
                        companyName = userInfo.getCompany_name();
                        shopName = userInfo.getShop_name();
                        shopId = userInfo.getShop_id();
                        tvName.setText(loginAccount);
                        SPUtils.put("account", loginAccount);
                        SPUtils.put("avator", userInfo.getHead_portrait());
                        if("true".equals(userInfo.getAuthenticate()) && !TextUtils.isEmpty(userInfo.getAuthenticate_id())){
                            img_appraiser.setImageResource(R.mipmap.appraise_icon);
                        }else{
                            img_appraiser.setImageResource(R.mipmap.appraise_grey);
                        }
                        setLoginAccountAuth();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

        private void getBuyerOrderNumIndex() {
            disposable.add(ApiUtils.getInstance().getMyNumFlag("user")
                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<MyNumBean>>() {
                    @Override
                    public void accept(ResultBean<MyNumBean> myNumBeanResultBean) throws Exception {
                        MyNumBean data = myNumBeanResultBean.getData();
                        if (data != null) {
                            tvOrderOneNum.setText(data.getWait_pay_num());
                            tvOrderOneNum.setVisibility(TextUtils.isEmpty(data.getWait_pay_num()) ||
                                    "0".equals(data.getWait_pay_num()) ? View.GONE : View.VISIBLE);

                            tvOrderTwoNum.setText(data.getWait_send_goods_num());
                            tvOrderTwoNum.setVisibility(TextUtils.isEmpty(data.getWait_send_goods_num()) ||
                                    "0".equals(data.getWait_send_goods_num()) ? View.GONE : View.VISIBLE);

                            tvOrderThreeNum.setText(data.getWait_confirm_goods_num());
                            tvOrderThreeNum.setVisibility(TextUtils.isEmpty(data.getWait_confirm_goods_num()) ||
                                    "0".equals(data.getWait_confirm_goods_num()) ? View.GONE : View.VISIBLE);

                            tvOrderFourNum.setText(data.getAfter_sale_num());
                            tvOrderFourNum.setVisibility(TextUtils.isEmpty(data.getAfter_sale_num()) ||
                                    "0".equals(data.getAfter_sale_num()) ? View.GONE : View.VISIBLE);

                            tvCanUseMoney.setText(TextUtils.isEmpty(data.getMoney()) ? "0" : data.getMoney());
                            tvNotUseMoney.setText(TextUtils.isEmpty(data.getMoney_frozen()) ? "0" : data.getMoney_frozen());
                            tvMessageNum.setText(TextUtils.isEmpty(data.getInformation()) ? "0" : data.getInformation());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void updateMyNum() {

    }


    private void getSellerOrderNumIndex() {
        disposable.add(ApiUtils.getInstance().getMyNumFlag("shop")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<MyNumBean>>() {
                    @Override
                    public void accept(ResultBean<MyNumBean> myNumBeanResultBean) throws Exception {
                        MyNumBean data = myNumBeanResultBean.getData();
                        if (data != null) {
                            tvSellOrderOneNum.setText(data.getWait_pay_num());
                            tvSellOrderOneNum.setVisibility(TextUtils.isEmpty(data.getWait_pay_num()) ||
                                    "0".equals(data.getWait_pay_num()) ? View.GONE : View.VISIBLE);

                            tvSellOrderTwoNum.setText(data.getWait_send_goods_num());
                            tvSellOrderTwoNum.setVisibility(TextUtils.isEmpty(data.getWait_send_goods_num()) ||
                                    "0".equals(data.getWait_send_goods_num()) ? View.GONE : View.VISIBLE);

                            tvSellOrderThreeNum.setText(data.getWait_confirm_goods_num());
                            tvSellOrderThreeNum.setVisibility(TextUtils.isEmpty(data.getWait_confirm_goods_num()) ||
                                    "0".equals(data.getWait_confirm_goods_num()) ? View.GONE : View.VISIBLE);

                            tvSellOrderFourNum.setText(data.getAfter_sale_num());
                            tvSellOrderFourNum.setVisibility(TextUtils.isEmpty(data.getAfter_sale_num()) ||
                                    "0".equals(data.getAfter_sale_num()) ? View.GONE : View.VISIBLE);

                            tvSellOrderFiveNum.setText(data.getCanceled_num());
                            tvSellOrderFiveNum.setVisibility(TextUtils.isEmpty(data.getCanceled_num()) ||
                                    "0".equals(data.getCanceled_num()) ? View.GONE : View.VISIBLE);


                            tvCanUseMoney.setText(TextUtils.isEmpty(data.getMoney()) ? "0" : data.getMoney());
                            tvNotUseMoney.setText(TextUtils.isEmpty(data.getMoney_frozen()) ? "0" : data.getMoney_frozen());
                            tvMessageNum.setText(TextUtils.isEmpty(data.getInformation()) ? "0" : data.getInformation());

                            tvCheckGoodsNum.setText(data.getPending_num());
                            tvWareHouseGoodsNum.setText(data.getInstock_num());
                            tvSellGoodsNum.setText(data.getOnsale_num());
                            tvAuctionGoodsNum.setText(data.getAuction_num());

                            tvCheckGoodsNum.setVisibility(BigDecimalUtils.objToBigDecimal(data.getPending_num()).intValue() > 0 ? View.VISIBLE : View.GONE);
                            tvWareHouseGoodsNum.setVisibility(BigDecimalUtils.objToBigDecimal(data.getInstock_num()).intValue() > 0 ? View.VISIBLE : View.GONE);
                            tvAuctionGoodsNum.setVisibility(BigDecimalUtils.objToBigDecimal(data.getAuction_num()).intValue() > 0 ? View.VISIBLE : View.GONE);
                            tvSellGoodsNum.setVisibility(BigDecimalUtils.objToBigDecimal(data.getOnsale_num()).intValue() > 0 ? View.VISIBLE
                                    : View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void getPersonAndCompanyAuthStatus() {
        Observable<ResultBean<AuthStatusBean>> personAuthStatus = ApiUtils.getInstance().getPersonAuthStatus();
        Observable<ResultBean<AuthStatusBean>> companyAuthStatus = ApiUtils.getInstance().getCompanyAuthStatus();
        disposable.add(Observable.zip(personAuthStatus, companyAuthStatus, new BiFunction<ResultBean<AuthStatusBean>, ResultBean<AuthStatusBean>, List<AuthStatusBean>>() {
            @Override
            public List<AuthStatusBean> apply(ResultBean<AuthStatusBean> uploadImageBeanResultBean, ResultBean<AuthStatusBean> uploadImageBeanResultBean2) throws Exception {
                List<AuthStatusBean> imageLists = new ArrayList<>();
                imageLists.add(uploadImageBeanResultBean.getData());
                imageLists.add(uploadImageBeanResultBean2.getData());
                return imageLists;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AuthStatusBean>>() {
                    @Override
                    public void accept(List<AuthStatusBean> authStatusBeans) throws Exception {
                        personStatus = authStatusBeans.get(0).getStatus();
                        companyStatus = authStatusBeans.get(1).getStatus();
                        setAuthView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void setLoginAccountAuth() {
        userCheckAuthDialog.updateAuthStatus(personStatus,companyStatus,loginAccount);
    }

    private void setAuthView() {
        rlAuthStatus.setVisibility(View.VISIBLE);
        setLoginAccountAuth();
        if ((AuthStatusBean.AUTH_SUCCESS.equals(personStatus) || AuthStatusBean.AUTH_SUCCESS.equals(companyStatus))) {
            imgAuthStatus.setImageResource(R.mipmap.my_auth);
            tvAuthStatus.setText("已认证");
            if ((AuthStatusBean.AUTH_SUCCESS.equals(personStatus) && AuthStatusBean.AUTH_SUCCESS.equals(companyStatus))) {
                tvAuthPerson.setVisibility(View.GONE);
                tvAuthCompany.setVisibility(View.GONE);
            } else if (AuthStatusBean.AUTH_SUCCESS.equals(personStatus)) {
                tvAuthPerson.setVisibility(View.GONE);
                tvAuthCompany.setVisibility(View.VISIBLE);
            } else if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
                tvAuthPerson.setVisibility(View.GONE);
                tvAuthCompany.setVisibility(View.GONE);
            }

        } else {
            imgAuthStatus.setImageResource(R.mipmap.my_no_auth);
            tvAuthStatus.setText("未认证");

            tvAuthPerson.setVisibility(View.VISIBLE);
            tvAuthCompany.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 设置头像
     */
    private void setHeadIcon() {
        if (photoDialog == null) {
            photoDialog = new ChoicePhotoDialog(getActivity());
            photoDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    initImagePicker();
                    openCamera(REQUEST_CODE_CHOICE_HEAD);
                }

                @Override
                public void toPhotoAlbum() {
                    initImagePicker();
                    openAlbum(REQUEST_CODE_CHOICE_HEAD);
                }
            });
        }
        photoDialog.show();
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, requestCode);
    }

    private void openAlbum(int requestCode) {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }

    private void checkStatus() {
        if (userType == TYPE_BUYER) {
            if (AuthStatusBean.AUTH_SUCCESS.equals(personStatus) || AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
                changeUserType();
            } else {
                showLoadingDialog();
                Observable<ResultBean<AuthStatusBean>> personAuthStatus = ApiUtils.getInstance().getPersonAuthStatus();
                Observable<ResultBean<AuthStatusBean>> companyAuthStatus = ApiUtils.getInstance().getCompanyAuthStatus();
                disposable.add(Observable.zip(personAuthStatus, companyAuthStatus, new BiFunction<ResultBean<AuthStatusBean>, ResultBean<AuthStatusBean>, List<AuthStatusBean>>() {
                    @Override
                    public List<AuthStatusBean> apply(ResultBean<AuthStatusBean> uploadImageBeanResultBean, ResultBean<AuthStatusBean> uploadImageBeanResultBean2) throws Exception {
                        List<AuthStatusBean> imageLists = new ArrayList<>();
                        imageLists.add(uploadImageBeanResultBean.getData());
                        imageLists.add(uploadImageBeanResultBean2.getData());
                        return imageLists;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<AuthStatusBean>>() {
                            @Override
                            public void accept(List<AuthStatusBean> authStatusBeans) throws Exception {
                                hideLoadingDialog();
                                personStatus = authStatusBeans.get(0).getStatus();
                                companyStatus = authStatusBeans.get(1).getStatus();
                                setAuthView();

                                if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeans.get(1).getStatus())) {
                                    changeUserType();
                                } else if (AuthStatusBean.AUTH_LOCKED.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_LOCKED.equals(authStatusBeans.get(1).getStatus())) {
                                    ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                                    startActivity(new Intent(getActivity(), VerifiedActivity.class));
                                } else if (AuthStatusBean.AUTH_FAILING.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_FAILING.equals(authStatusBeans.get(1).getStatus())) {
                                    ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                                    startActivity(new Intent(getActivity(), VerifiedActivity.class));
                                } else {
                                    ToastUtils.showShort("您还没有进行实名认证，请先认证");
                                    startActivity(new Intent(getActivity(), VerifiedActivity.class));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hideLoadingDialog();
                            }
                        }));
            }
        } else {
            changeUserType();
        }
    }

    private void changeUserType() {
        initRotateAnimation();
//        Rotate3dAnimation.rotateOnYCoordinate(rlContentLayout);
        userType = userType == TYPE_BUYER ? TYPE_SELLER : TYPE_BUYER;
        rlContentLayout.clearAnimation();
        rlContentLayout.startAnimation(rotateStart);
//        showUserTypeView();
    }

    private void initRotateAnimation() {
        float centerX = rlContentLayout.getWidth() / 2.0f;
        float centerY = rlContentLayout.getHeight() / 2.0f;
        float centerZ = 800f;

        if (rotateStart == null) {
            rotateStart = new Rotate3dAnimation(0, 90, centerX, centerY, centerZ, Rotate3dAnimation.ROTATE_Y_AXIS, true);
            rotateStart.setDuration(500);
            rotateStart.setAnimationListener(animationListener);
        }

        if (rotateEnd == null) {
            rotateEnd = new Rotate3dAnimation(270, 360, centerX, centerY, centerZ, Rotate3dAnimation.ROTATE_Y_AXIS, false);
            rotateEnd.setDuration(500);
            rotateEnd.setAnimationListener(animationListener);
        }

    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation == rotateStart) {
                showUserTypeView();
                rlContentLayout.startAnimation(rotateEnd);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void checkPersonStatus() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getPersonAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        personStatus = authStatusBeanResultBean.getData().getStatus();
                        hideLoadingDialog();
                        if ("active".equals(authStatusBeanResultBean.getData().getStatus())) {
                            //未认证
                            toAuthActivity();
                        } else if ("locked".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                        } else if ("failing".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                            toAuthActivity();
                        } else if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证已成功");
                            tvAuthPerson.setVisibility(View.GONE);
                            imgAuthStatus.setImageResource(R.mipmap.my_auth);
                            tvAuthStatus.setText("已认证");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void toAuthActivity() {
        startActivity(new Intent(getActivity(), PersonAuthActivity.class));
    }


    private void checkCompanyStatus() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getCompanyAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        companyStatus = authStatusBeanResultBean.getData().getStatus();
                        hideLoadingDialog();
                        if ("active".equals(authStatusBeanResultBean.getData().getStatus())) {
                            //未认证
                            toAuthCompanyActivity();
                        } else if ("locked".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                        } else if ("failing".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                            toAuthCompanyActivity();
                        } else if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证已成功");
                            tvAuthPerson.setVisibility(View.GONE);
                            tvAuthCompany.setVisibility(View.GONE);
                            imgAuthStatus.setImageResource(R.mipmap.my_auth);
                            tvAuthStatus.setText("已认证");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void showShareDialog() {
        UMImage thumb = new UMImage(getActivity(), BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.launcher));
        UMWeb web = new UMWeb("http://mall.chunlangjiu.com/appdownload/index.html");
        web.setTitle("给您推荐高端酒综合服务平台-醇狼");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("为行业用户提供高端酒发布、估价、竞拍、鉴定等综合性服务");//描述
        ShareUtils.shareLink(getActivity(), web, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        });
    }

    private void toAuthCompanyActivity() {
        startActivity(new Intent(getActivity(), CompanyAuthActivity.class));
    }


    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            eventTag = eventTag == null ? "" : eventTag;
            switch (eventTag) {
                case ConstantMsg.USER_DATA_CHANGE:
                    getUserInfo();
                    break;
                case ConstantMsg.LOGIN_SUCCESS:
                    loginSuccess(eventTag);
                    break;
                case ConstantMsg.LOGOUT_SUCCESS:
                    logoutSuccess(eventTag);
                    break;
            }


//            authSuccess(eventTag);
        }
    };

    private void authSuccess(String eventTag) {

        if (eventTag.equals(ConstantMsg.PERSON_COMPANY_AUTH_SUCCESS)) {
            Observable<ResultBean<AuthStatusBean>> personAuthStatus = ApiUtils.getInstance().getPersonAuthStatus();
            Observable<ResultBean<AuthStatusBean>> companyAuthStatus = ApiUtils.getInstance().getCompanyAuthStatus();
            disposable.add(Observable.zip(personAuthStatus, companyAuthStatus, new BiFunction<ResultBean<AuthStatusBean>, ResultBean<AuthStatusBean>, List<AuthStatusBean>>() {
                @Override
                public List<AuthStatusBean> apply(ResultBean<AuthStatusBean> uploadImageBeanResultBean, ResultBean<AuthStatusBean> uploadImageBeanResultBean2) throws Exception {
                    List<AuthStatusBean> imageLists = new ArrayList<>();
                    imageLists.add(uploadImageBeanResultBean.getData());
                    imageLists.add(uploadImageBeanResultBean2.getData());
                    return imageLists;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<AuthStatusBean>>() {
                        @Override
                        public void accept(List<AuthStatusBean> authStatusBeans) throws Exception {
                            personStatus = authStatusBeans.get(0).getStatus();
                            companyStatus = authStatusBeans.get(1).getStatus();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    }));
        }
    }


    //登录成功
    private void loginSuccess(String eventTag) {
        if (eventTag.equals(ConstantMsg.LOGIN_SUCCESS)) {
            checkLogin();
            getUserInfo();
//            getBuyerOrderNumIndex();
//            getSellerOrderNumIndex();
            getPersonAndCompanyAuthStatus();
        }
    }

    //退出登录
    private void logoutSuccess(String eventTag) {
        if (eventTag.equals(ConstantMsg.LOGOUT_SUCCESS)) {
            personStatus = "";
            companyStatus = "";

            userInfo = null;
            loginAccount = "";
            personName = "";
            companyName = "";
            shopName = "";
            shopId = "";

            SPUtils.put("token", "");
            BaseApplication.setToken("");
            BaseApplication.initToken();
            checkLogin();
            userType = TYPE_BUYER;
            tvOrderOneNum.setVisibility(View.GONE);
            tvOrderTwoNum.setVisibility(View.GONE);
            tvOrderThreeNum.setVisibility(View.GONE);
            tvOrderFourNum.setVisibility(View.GONE);

            tvSellOrderOneNum.setVisibility(View.GONE);
            tvSellOrderTwoNum.setVisibility(View.GONE);
            tvSellOrderThreeNum.setVisibility(View.GONE);
            tvSellOrderFourNum.setVisibility(View.GONE);
            tvSellOrderFiveNum.setVisibility(View.GONE);
            tvCheckGoodsNum.setVisibility(View.GONE);
            tvWareHouseGoodsNum.setVisibility(View.GONE);
            tvAuctionGoodsNum.setVisibility(View.GONE);
            tvSellGoodsNum.setVisibility(View.GONE);

            llNotUseMoney.setVisibility(View.VISIBLE);
//            disposable.add(ApiUtils.getInstance().logout()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ResultBean>() {
//                        @Override
//                        public void accept(ResultBean loginBeanResultBean) throws Exception {
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                        }
//                    }));
        }
    }

    private void toOrderMainActivity(int type, int target) {
        Intent intent = new Intent(getActivity(), OrderMainNewActivity.class);
        intent.putExtra(OrderParams.TYPE, type);
        intent.putExtra(OrderParams.TARGET, target);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == REQUEST_CODE_CHOICE_HEAD) {
                        ArrayList<ImageItem> mainPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = mainPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        String base64Head = FileUtils.imgToBase64(mainPicLists.get(0).path);
                        uploadHeadIcon(mainPicLists, base64Head);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadHeadIcon(ArrayList<ImageItem> mainPicLists, String base64Head) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().userUploadImage(base64Head, mainPicLists.get(0).name, "rate")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UploadImageBean>>() {
                    @Override
                    public void accept(ResultBean<UploadImageBean> uploadImageBeanResultBean) throws Exception {
                        setHeadUrl(uploadImageBeanResultBean.getData().getUrl());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("上传头像失败");
                    }
                }));
    }

    private void setHeadUrl(final String url) {
        disposable.add(ApiUtils.getInstance().setHeadImg(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        GlideUtils.loadImageHead(getActivity(), url, imgHead);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("设置头像失败");
                    }
                }));
    }


    private void showEditNameDialog() {
        if (editAccountNameDialog == null) {
            editAccountNameDialog = new EditAccountNameDialog(getActivity());
            editAccountNameDialog.setCallBack(new EditAccountNameDialog.CallBack() {
                @Override
                public void onConfirm(String name) {
                    editShopName(name);
                }
            });
        }
        editAccountNameDialog.setNameContent(shopName);
        editAccountNameDialog.show();
    }

    private void editShopName(final String name) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().editShopName(BaseApplication.getToken(), name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("修改成功");
                        shopName = name;
                        tvName.setText(shopName);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("修改失败");
                    }
                }));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

}
