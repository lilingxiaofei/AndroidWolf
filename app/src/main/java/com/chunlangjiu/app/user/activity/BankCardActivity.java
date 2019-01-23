package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.activity.WithDrawActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.BankCardListBean;
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
    private CompositeDisposable disposable;
    private List<BankCardListBean.BankCardDetailBean> bankCard = new ArrayList<>();
    private BankCardAdapter bankCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        disposable = new CompositeDisposable();
        initView();
        initData();
    }

    private void initView() {
        bankCardAdapter = new BankCardAdapter(R.layout.bank_card_list_item, bankCard);
        recycleBankCardList.setAdapter(bankCardAdapter);
        recycleBankCardList.setLayoutManager(new LinearLayoutManager(this));
        bankCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(WithDrawActivity.BankCardData, bankCard.get(position));
                setResult(WithDrawActivity.BankCardResultCode, intent);
                finish();
//                deleteBankCard(String.valueOf(bankCard.get(position).getBank_id()));
            }
        });

    }

    private void initData() {
        getBankCardList();
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

    private String formateBankCard(String bankCard) {
        if (TextUtils.isEmpty(bankCard)) {
            return "";
        }
        //6214 8378 0811 7523
        String end = bankCard.substring(bankCard.length()-4,bankCard.length());
        String newStr = bankCard.replace(bankCard.substring(bankCard.length()-4,bankCard.length()),"");
        char[] bankCardChar = newStr.toCharArray();
        String bankCardData = "";
        for (int i = 0; i < bankCardChar.length; i++) {
            if (i % 4 == 0 && i > 0) {
                bankCardData += "\u3000";
            }
            bankCardData+="*";

        }
        return String.format("%s\u3000%s",bankCardData,end);

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

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    public void deleteBankCard(String id) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().deleteBankCard((String) SPUtils.get("token", ""), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {
                        hideLoadingDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    public class BankCardAdapter extends BaseQuickAdapter<BankCardListBean.BankCardDetailBean, BaseViewHolder> {

        public BankCardAdapter(int layoutResId, @Nullable List<BankCardListBean.BankCardDetailBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BankCardListBean.BankCardDetailBean item) {
            helper.setText(R.id.tvBankName, item.getName());
            helper.setText(R.id.tvBankCardNumber, formateBankCard(item.getCard()));

        }
    }

}
