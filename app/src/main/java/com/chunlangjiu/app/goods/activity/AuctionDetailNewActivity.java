package com.chunlangjiu.app.goods.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.amain.activity.LoginActivity;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.CallDialog;
import com.chunlangjiu.app.goods.dialog.InputPriceDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.goods.fragment.AuctionDetailFragment;
import com.chunlangjiu.app.goods.fragment.GoodsCommentFragment;
import com.chunlangjiu.app.goods.fragment.GoodsWebFragment;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderMainActivity;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.chunlangjiu.app.util.ShareUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/10
 * @Describe:
 */
public class AuctionDetailNewActivity extends BaseActivity {

    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tvPayMoney)
    TextView tvPayMoney;
    @BindView(R.id.tvBuy)
    TextView tvBuy;

    @BindView(R.id.rlChat)
    RelativeLayout rlChat;
    @BindView(R.id.rlCollect)
    RelativeLayout rlCollect;
    @BindView(R.id.imgCollect)
    ImageView imgCollect;

    private BaseFragmentAdapter fragmentAdapter;
    private final String[] mTitles = {"商品", "详情", "评价"};
    private List<Fragment> mFragments;

    private CompositeDisposable disposable;
    private String itemId;
    private GoodsDetailBean goodsDetailBean;
    private String skuId;

    private InputPriceDialog inputPriceDialog;
    private CallDialog callDialog;

    private AuctionDetailFragment auctionDetailFragment;
    private boolean isFavorite = false;//是否收藏

    private IWXAPI wxapi;
    private String payment_id;
    private List<PaymentBean.PaymentInfo> payList;
    private PayDialog payDialog;
    private int payMethod;//默认微信支付
    private String payMethodId;//支付方式类型

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.img_share://分享
                    showShare();
                    break;
            }
        }
    };

    private View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (BaseApplication.isLogin()) {
                switch (view.getId()) {
                    case R.id.rlChat://聊天
                        showCallDialog();
                        break;
                    case R.id.rlCollect://收藏
                        changeCollectStatus();
                        break;
                    case R.id.tvBuy://立即购买
                        toConfirmOrder();
                        break;
                }
            } else {
                startActivity(new Intent(AuctionDetailNewActivity.this, LoginActivity.class));
            }
        }
    };



    public static void startAuctionDetailsActivity(Activity activity, String itemId) {
        Intent intent = new Intent(activity, AuctionDetailNewActivity.class);
        intent.putExtra("itemId", itemId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_auction_detail);
        EventManager.getInstance().registerListener(onNotifyListener);
        initView();
        initData();
    }

    @Override
    public void setTitleView() {
        hideTitleView();
    }

    private void initView() {
        tab.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        rlBottom.setVisibility(View.GONE);

        imgBack.setOnClickListener(onClickListener);
        imgShare.setOnClickListener(onClickListener);
        tvBuy.setOnClickListener(onClickListenerLogin);
        rlChat.setOnClickListener(onClickListenerLogin);
        rlCollect.setOnClickListener(onClickListenerLogin);

        itemId = getIntent().getStringExtra("itemId");
        disposable = new CompositeDisposable();
        initPay();
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    private void initData() {
        getGoodsDetail();
        getPaymentList();
    }

    private void getGoodsDetail() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getGoodsDetailWithToken(itemId, (String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<GoodsDetailBean>>() {
                    @Override
                    public void accept(ResultBean<GoodsDetailBean> goodsDetailBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        goodsDetailBean = goodsDetailBeanResultBean.getData();
                        skuId = goodsDetailBean.getItem().getDefault_sku_id();
                        updateView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void updateView() {
        String check = goodsDetailBean.getItem().getAuction().getCheck();
        String isPay = goodsDetailBean.getItem().getAuction().getIs_pay();
        if ("true".equals(check)) {
            if ("true".equals(isPay)) {
                //已经出过价并且已支付
                tvBuy.setText("修改出价");
                tvPayMoney.setText("已付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
            } else {
                //已经出过价但是未支付
                tvBuy.setText("去支付");
                tvPayMoney.setText("应付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
                payment_id = goodsDetailBean.getItem().getAuction().getPayment_id();
            }
        } else {
            tvBuy.setText("立即出价");
            tvPayMoney.setText("应付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
        }

        if ("true".equals(goodsDetailBean.getItem().getIs_collect())) {
            isFavorite = true;
            imgCollect.setBackgroundResource(R.mipmap.collect_true);
        } else {
            isFavorite = false;
            imgCollect.setBackgroundResource(R.mipmap.collect_false);
        }

        tab.setVisibility(View.VISIBLE);
        imgShare.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        rlBottom.setVisibility(View.VISIBLE);
        auctionDetailFragment = AuctionDetailFragment.newInstance(goodsDetailBean);
        mFragments = new ArrayList<>();
        mFragments.add(auctionDetailFragment);
        mFragments.add(GoodsWebFragment.newInstance(goodsDetailBean.getDesc()));
        mFragments.add(GoodsCommentFragment.newInstance(itemId));
        fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setLists(mFragments);
        viewPager.setAdapter(fragmentAdapter);
        tab.setViewPager(viewPager, mTitles);
        viewPager.setCurrentItem(0);
    }


    private void showShare() {
        UMImage thumb = new UMImage(this, goodsDetailBean.getShare().getImage());
        UMWeb web = new UMWeb(goodsDetailBean.getShare().getH5href());
        web.setTitle(goodsDetailBean.getItem().getTitle());//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(goodsDetailBean.getItem().getSub_title());//描述

        ShareUtils.shareLink(this, web, new UMShareListener() {
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

    private void showCallDialog() {
        if (callDialog == null) {
            callDialog = new CallDialog(this, goodsDetailBean.getShop().getMobile());
            callDialog.setCallBack(new CallDialog.CallBack() {
                @Override
                public void onConfirm() {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + goodsDetailBean.getShop().getMobile()));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }

                @Override
                public void onCancel() {
                }
            });
        }
        callDialog.show();
    }

    private void changeCollectStatus() {
        if (isFavorite) {
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().favoriteCancelGoods(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            isFavorite = false;
                            imgCollect.setBackgroundResource(R.mipmap.collect_false);
                            ToastUtils.showShort("取消收藏成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("取消收藏失败");
                        }
                    }));
        } else {
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().favoriteAddGoods(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            isFavorite = true;
                            imgCollect.setBackgroundResource(R.mipmap.collect_true);
                            ToastUtils.showShort("收藏成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("收藏失败");
                        }
                    }));
        }
    }


    private void toConfirmOrder() {
        String check = goodsDetailBean.getItem().getAuction().getCheck();
        String isPay = goodsDetailBean.getItem().getAuction().getIs_pay();
        if ("true".equals(check)) {
            if ("true".equals(isPay)) {
                if (inputPriceDialog == null) {
                    inputPriceDialog = new InputPriceDialog(this, goodsDetailBean.getItem().getAuction().getMax_price(),
                            goodsDetailBean.getItem().getAuction().getOriginal_bid());
                    inputPriceDialog.setCallBack(new InputPriceDialog.CallBack() {
                        @Override
                        public void editPrice(String price) {
                            editGivePrice(price);
                        }
                    });
                }
                inputPriceDialog.show();
            } else {
                showPayMethodDialog();
            }
        } else {
            AuctionConfirmOrderActivity.startConfirmOrderActivity(this, goodsDetailBean);
        }
    }


    private void editGivePrice(String price) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().auctionAddPrice(goodsDetailBean.getItem().getAuction().getAuctionitem_id(), price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("修改出价成功");
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void getPaymentList() {
        disposable.add(ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        payList = paymentBeanResultBean.getData().getList();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }


    private void showPayMethodDialog() {
        if (payList == null & payList.size() == 0) {
            ToastUtils.showShort("获取支付方式失败");
        } else {
            if (payDialog == null) {
                payDialog = new PayDialog(this, payList);
                payDialog.setCallBack(new PayDialog.CallBack() {
                    @Override
                    public void choicePayMethod(int payMethod, String payMethodId) {
                        payMoney(payMethod, payMethodId);
                    }
                });
            }
            payDialog.show();
        }
    }

    private void payMoney(final int payMethod, String payMethodId) {
        disposable.add(ApiUtils.getInstance().payDo(payment_id, payMethodId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        invokePay(payMethod,resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void invokePay(int payMethod,ResultBean data) {
        switch (payMethod) {
            case 0:
                invokeWeixinPay(data);
                break;
            case 1:
                invokeZhifubaoPay(data);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void invokeWeixinPay(ResultBean data) {
        PayReq request = new PayReq();
        request.appId = "wx0e1869b241d7234f";
        request.partnerId = data.getPartnerid();
        request.prepayId = data.getPrepayid();
        request.packageValue = data.getPackageName();
        request.nonceStr = data.getNoncestr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        wxapi.sendReq(request);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(AuctionDetailNewActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(AuctionDetailNewActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                toOrderMainActivity(1, 0);
            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AuctionDetailNewActivity.this);
                Map<String, String> stringStringMap = alipay.payV2(url, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = stringStringMap;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            changeToEvaluate(eventTag);
            changeSlide(object, eventTag);
            detailCountEnd(eventTag);
            weixinPaySuccess(object, eventTag);
        }
    };

    private void detailCountEnd(String eventTag) {
        if (eventTag.equals(ConstantMsg.DETAIL_COUNT_END)) {
            finish();
        }
    }

    private void weixinPaySuccess(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.WEIXIN_PAY_CALLBACK)) {
            int code = (int) object;
            if (code == 0) {
                //支付成功
                ToastUtils.showShort("支付成功");
                EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
            } else if (code == -1) {
                //支付错误
                ToastUtils.showShort("支付失败");
            } else if (code == -2) {
                //支付取消
                ToastUtils.showShort("支付取消");
            }
            finish();
            toOrderMainActivity(1, 0);
        }
    }

    /**
     * 跳转到评价tab
     *
     * @param eventTag 标识
     */
    private void changeToEvaluate(String eventTag) {
        if (eventTag.equals(ConstantMsg.CHANGE_TO_EVALUATE)) {
            viewPager.setCurrentItem(2);
        }
    }


    /**
     * 图文详情滑动变化
     *
     * @param object
     * @param eventTag
     */
    private void changeSlide(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.AUCTION_SLIDE_CHANGE)) {
            int pageType = (int) object;
            if (pageType == 0) {
                viewPager.setCurrentItem(1);
                auctionDetailFragment.goTop();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void toOrderMainActivity(int type, int target) {
        Intent intent = new Intent(this, OrderMainActivity.class);
        intent.putExtra(OrderParams.TYPE, type);
        intent.putExtra(OrderParams.TARGET, target);
        startActivity(intent);
    }
}

