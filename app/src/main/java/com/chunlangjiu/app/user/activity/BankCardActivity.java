package com.chunlangjiu.app.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.activity.WithDrawActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.BankCardInfoBean;
import com.chunlangjiu.app.user.bean.BankCardListBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BankCardActivity extends BaseActivity {

    @BindView(R.id.recycleBankCardList)
    RecyclerView recycleBankCardList;

    public static final String FromType = "FromType";
    public static final String ClassWithDrawActivity = "ClassWithDrawActivity";
    private CompositeDisposable disposable;
    private List<BankCardListBean.BankCardDetailBean> bankCard = new ArrayList<>();
    private BankCardAdapter bankCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        disposable = new CompositeDisposable();
        initEvent();
        initView();
        initData();
    }

    private void initView() {
        bankCardAdapter = new BankCardAdapter(R.layout.bank_card_list_item, bankCard);
        recycleBankCardList.setAdapter(bankCardAdapter);
        recycleBankCardList.setLayoutManager(new LinearLayoutManager(this));
        bankCardAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (ClassWithDrawActivity.equals(getIntent().getStringExtra(FromType))) {
                    Intent intent = new Intent();
                    intent.putExtra(WithDrawActivity.BankCardData, bankCard.get(position));
                    setResult(WithDrawActivity.BankCardResultCode, intent);
                    finish();
                }

            }
        });
//        bankCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (ClassWithDrawActivity.equals(getIntent().getStringExtra(FromType))) {
//                    Intent intent = new Intent();
//                    intent.putExtra(WithDrawActivity.BankCardData, bankCard.get(position));
//                    setResult(WithDrawActivity.BankCardResultCode, intent);
//                    finish();
//                }
////                deleteBankCard(String.valueOf(bankCard.get(position).getBank_id()));
//            }
//        });
        bankCardAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recycleBankCardList.getParent(), false));

    }
    private Drawable getDrawable(String color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        //设置圆角大小
        drawable.setCornerRadius(dip2px(this,4));
        //设置边缘线的宽以及颜色
//        drawable.setStroke(1, Color.parseColor(#FF00FF));
        //设置shape背景色
        drawable.setColor(Color.parseColor(color));
        //设置到TextView中
        return drawable;

    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void initData() {
        getBankCardList();
    }

    private void test() {
        bankCard.add(new BankCardListBean.BankCardDetailBean());
        bankCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleView() {
        titleName.setText("银行卡");
    }

    @OnClick({R.id.btnAddBankCard, R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddBankCard:
                startActivity(new Intent(this, AddBankCardActivity.class));
                break;
            case R.id.img_title_left:
                finish();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    private void initEvent() {
        EventManager.getInstance().registerListener(onNotifyListener);

    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            switch (eventTag) {
                case ConstantMsg.BANKCARD_CHANGE:
                    getBankCardList();
                    break;

            }

        }
    };

    private String formateBankCard(String bankCard) {
        if (TextUtils.isEmpty(bankCard)) {
            return "";
        }
        //6214 8378 0811 7523
        String end = bankCard.substring(bankCard.length() - 4, bankCard.length());
        String newStr = bankCard.replace(bankCard.substring(bankCard.length() - 4, bankCard.length()), "");
        char[] bankCardChar = newStr.toCharArray();
        String bankCardData = "";
        for (int i = 0; i < bankCardChar.length; i++) {
            if (i % 4 == 0 && i > 0) {
                bankCardData += "\u3000";
            }
            bankCardData += "*";

        }
        return String.format("%s\u3000%s", bankCardData, end);

    }

    public void getBankCardList() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getBankCardList((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<BankCardListBean>>() {
                    @Override
                    public void accept(ResultBean<BankCardListBean> bankCardListBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        bankCard.clear();
                        bankCard.addAll(bankCardListBeanResultBean.getData().getList());
                        bankCardAdapter.notifyDataSetChanged();
                        getBankCardListInfo();
//                        test();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    public void deleteBankCard(final String id) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().deleteBankCard((String) SPUtils.get("token", ""), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        removeLocalData(id);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    private void getBankCardListInfo() {
        for (BankCardListBean.BankCardDetailBean bankCardDetailBean : bankCard) {
            if (null != bankCardDetailBean) {
                getBankCardInfo(bankCardDetailBean.getCard(), bankCardDetailBean);
            }
        }


    }

    private void getBankCardInfo(final String cardId, final BankCardListBean.BankCardDetailBean bankCardDetailBean) {
        disposable.add(ApiUtils.getInstance().getBankCardInfo((String) SPUtils.get("token", ""), cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<BankCardInfoBean>>() {
                    @Override
                    public void accept(ResultBean<BankCardInfoBean> resultBean) throws Exception {
                        if (null != resultBean && null != resultBean.getData()) {
                            BankCardInfoBean bankCardInfoBean = resultBean.getData();
                            bankCardDetailBean.setBankCardInfoBean(bankCardInfoBean);
                            bankCardAdapter.notifyDataSetChanged();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }


    private void removeLocalData(String id) {
        for (BankCardListBean.BankCardDetailBean bankCardDetailBean : bankCard) {
            if (id.equals(String.valueOf(bankCardDetailBean.getBank_id()))) {
                bankCard.remove(bankCardDetailBean);
                break;
            }
        }
        bankCardAdapter.notifyDataSetChanged();

    }

    public class BankCardAdapter extends BaseQuickAdapter<BankCardListBean.BankCardDetailBean, BaseViewHolder> {

        public BankCardAdapter(int layoutResId, @Nullable List<BankCardListBean.BankCardDetailBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final BankCardListBean.BankCardDetailBean item) {
            BankCardInfoBean bankCardInfoBean = item.getBankCardInfoBean();
            helper.getView(R.id.linearBankCard).setBackground(getDrawable("#ffb31f3f"));
            helper.setText(R.id.tvBankName, item.getBank());
            helper.setText(R.id.tvCardType, "");
            helper.setText(R.id.tvBankCardNumber, formateBankCard(item.getCard()));
            helper.setOnClickListener(R.id.tvDelete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBankCard(String.valueOf(item.getBank_id()));

                }
            });
            helper.addOnClickListener(R.id.linearBankCard);
            if (null != bankCardInfoBean) {
                GlideUtils.loadImageHead(BankCardActivity.this, bankCardInfoBean.getBankimage(), (ImageView) helper.getView(R.id.imgBank));
                helper.setText(R.id.tvCardType,bankCardInfoBean.getCardtype());
            }

        }
    }

}
