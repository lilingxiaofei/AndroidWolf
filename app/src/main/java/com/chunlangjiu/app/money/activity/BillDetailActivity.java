package com.chunlangjiu.app.money.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.bean.DepositBean;
import com.chunlangjiu.app.money.bean.FundInfoBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BillDetailActivity extends BaseActivity {//1551084183 //1551084183
    @BindView(R.id.amount)
    TextView tv_amount;

    @BindView(R.id.lineGoodsName)
    LinearLayout lineGoodsName;

    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;

    @BindView(R.id.lineaBusinessStatus)
    LinearLayout lineaBusinessStatus;

    @BindView(R.id.tvBusinessStatus)
    TextView tvBusinessStatus;

    @BindView(R.id.LineaBusinessType)
    LinearLayout LineaBusinessType;

    @BindView(R.id.tvBusinessType)
    TextView tvBusinessType;

    @BindView(R.id.lineaPayWay)
    LinearLayout lineaPayWay;

    @BindView(R.id.tvPayWay)
    TextView tvPayWay;

    @BindView(R.id.lineWithDrawBank)
    LinearLayout lineWithDrawBank;

    @BindView(R.id.tvWithDrawBank)
    TextView tvWithDrawBank;

    @BindView(R.id.lineaOrderNumber)
    LinearLayout lineaOrderNumber;

    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;

    @BindView(R.id.lineaCreateTime)
    LinearLayout lineaCreateTime;

    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime;

    @BindView(R.id.lineaApplyTime)
    LinearLayout lineaApplyTime;

    @BindView(R.id.tvApplyTime)
    TextView tvApplyTime;

    @BindView(R.id.lineaArrivalTime)
    LinearLayout lineaArrivalTime;

    @BindView(R.id.tvArrivalTime)
    TextView tvArrivalTime;

    private CompositeDisposable disposable;
    private String log_id = "";
    private String amount = "";
    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        disposable = new CompositeDisposable();
        log_id = getIntent().getStringExtra("log_id");
        amount = getIntent().getStringExtra("amount");
        type = getIntent().getStringExtra("type");
        tv_amount.setText(String.format("¥ %s", amount));
        setView();
        initData();

    }

    private void setView() {
        if (TextUtils.isEmpty(type)) return;
        if ("sell".equals(type)) {
            lineGoodsName.setVisibility(View.GONE);
            lineaBusinessStatus.setVisibility(View.GONE);
            LineaBusinessType.setVisibility(View.VISIBLE);
            lineaPayWay.setVisibility(View.GONE);
            lineWithDrawBank.setVisibility(View.GONE);
            lineaOrderNumber.setVisibility(View.VISIBLE);
            lineaCreateTime.setVisibility(View.VISIBLE);
            lineaApplyTime.setVisibility(View.GONE);
            lineaArrivalTime.setVisibility(View.GONE);
        } else if ("add".equals(type)) {
            lineGoodsName.setVisibility(View.GONE);
            lineaBusinessStatus.setVisibility(View.VISIBLE);
            LineaBusinessType.setVisibility(View.VISIBLE);
            lineaPayWay.setVisibility(View.VISIBLE);
            lineWithDrawBank.setVisibility(View.GONE);
            lineaOrderNumber.setVisibility(View.VISIBLE);
            lineaCreateTime.setVisibility(View.VISIBLE);
            lineaApplyTime.setVisibility(View.GONE);
            lineaArrivalTime.setVisibility(View.GONE);
        } else if ("expense".equals(type)) {
            lineGoodsName.setVisibility(View.GONE);
            lineaBusinessStatus.setVisibility(View.VISIBLE);
            LineaBusinessType.setVisibility(View.VISIBLE);
            lineaPayWay.setVisibility(View.GONE);
            lineWithDrawBank.setVisibility(View.VISIBLE);
            lineaOrderNumber.setVisibility(View.VISIBLE);
            lineaCreateTime.setVisibility(View.GONE);
            lineaApplyTime.setVisibility(View.VISIBLE);
            lineaArrivalTime.setVisibility(View.GONE);
        } else if ("refund".equals(type)) {
            lineGoodsName.setVisibility(View.GONE);
            lineaBusinessStatus.setVisibility(View.VISIBLE);
            LineaBusinessType.setVisibility(View.VISIBLE);
            lineaPayWay.setVisibility(View.GONE);
            lineWithDrawBank.setVisibility(View.GONE);
            lineaOrderNumber.setVisibility(View.VISIBLE);
            lineaCreateTime.setVisibility(View.VISIBLE);
            lineaApplyTime.setVisibility(View.GONE);
            lineaArrivalTime.setVisibility(View.GONE);
        }
    }

    private void initData() {
        getFundInfo();
    }

    @Override
    public void setTitleView() {
        titleName.setText("账单详情");
    }

    @OnClick({R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
        }
    }

    private void getFundInfo() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getfundInfo((String) SPUtils.get("token", ""), log_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FundInfoBean>>() {
                    @Override
                    public void accept(ResultBean<FundInfoBean> resultBean) throws Exception {
                        hideLoadingDialog();
                        FundInfoBean fundInfoBean = resultBean.getData();
                        setFundInfoData(fundInfoBean);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    private void setFundInfoData(FundInfoBean fundInfoBean) {
        if (null == fundInfoBean) return;
        if ("sell".equals(type)) {
            tvGoodsName.setText("");
            tvBusinessType.setText(fundInfoBean.getMemo());
            tvOrderNumber.setText(fundInfoBean.getId());
            tvCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fundInfoBean.getTime() * 1000)));
        } else if ("add".equals(type)) {
            tvBusinessStatus.setText(fundInfoBean.getStatus());
            tvBusinessType.setText(fundInfoBean.getMemo());
            tvPayWay.setText(fundInfoBean.getPay_name());
            tvOrderNumber.setText(fundInfoBean.getId());
            tvCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fundInfoBean.getTime() * 1000)));
        } else if ("expense".equals(type)) {
            tvBusinessStatus.setText(fundInfoBean.getStatus());
            tvBusinessType.setText(fundInfoBean.getMemo());
            tvWithDrawBank.setText(fundInfoBean.getBank_name());
            tvOrderNumber.setText(fundInfoBean.getId());
            tvApplyTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fundInfoBean.getTime() * 1000)));
//            tvArrivalTime.setText("");
        } else if ("refund".equals(type)) {
            tvBusinessStatus.setText(fundInfoBean.getStatus());
            tvBusinessType.setText(fundInfoBean.getMemo());
            tvOrderNumber.setText(fundInfoBean.getId());
            tvCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fundInfoBean.getTime() * 1000)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable)
            disposable.dispose();
    }
}
