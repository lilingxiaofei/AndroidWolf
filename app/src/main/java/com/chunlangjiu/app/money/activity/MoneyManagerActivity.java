package com.chunlangjiu.app.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.bean.UserMoneyBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoneyManagerActivity extends BaseActivity {

    @BindView(R.id.relSecurityDeposit)
    RelativeLayout relSecurityDeposit;
    @BindView(R.id.relBalance)
    RelativeLayout relBalance;
    @BindView(R.id.vLineBalance)
    View vLineBalance;
    @BindView(R.id.vLineSecurityDeposit)
    View vLineSecurityDeposit;
    @BindView(R.id.tvSecurityDeposit)
    TextView tvSecurityDeposit;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvAvailableBalance)
    TextView tvAvailableBalance;
    @BindView(R.id.tvFreezeBalance)
    TextView tvFreezeBalance;

    private CompositeDisposable disposable;
    private UserMoneyBean userMoneyBean;


    public enum MoneyType {
        Balance, SecurityDeposit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_manager);
        disposable = new CompositeDisposable();
        setViewByType(MoneyType.Balance);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    private void initData() {
        disposable.add(ApiUtils.getInstance().getUserMoney((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UserMoneyBean>>() {
                    @Override
                    public void accept(ResultBean<UserMoneyBean> resultBean) throws Exception {
                        userMoneyBean = resultBean.getData();
                        if (null != userMoneyBean)
                            tvFreezeBalance.setText(userMoneyBean.getFreeze_money() == null ? "" : userMoneyBean.getFreeze_money());
                        tvAvailableBalance.setText(userMoneyBean.getMoney() == null ? "" : userMoneyBean.getMoney());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    @Override
    public void setTitleView() {
        titleName.setText("资金管理");
        setRightText();
    }

    @OnClick({R.id.img_title_left, R.id.relSelectBalance, R.id.relSelectSecurityDeposit, R.id.btnWithdraw, R.id.btnRecharge, R.id.btnPaySecurityDeposit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.relSelectBalance:
                setViewByType(MoneyType.Balance);
                break;
            case R.id.relSelectSecurityDeposit:
                setViewByType(MoneyType.SecurityDeposit);
                break;
            case R.id.btnWithdraw:
                startActivity(new Intent(this, WithDrawActivity.class));
                break;
            case R.id.btnRecharge:
                startActivity(new Intent(this, ReChargeActivity.class));
                break;
            case R.id.btnPaySecurityDeposit:
                Intent intent = new Intent(this, ReChargeActivity.class);
                intent.putExtra(ReChargeActivity.ReChargeType,ReChargeActivity.RechargeType.SecurityDeposit);
                startActivity(intent);
                break;

        }

    }

    private void setRightText() {
        titleView.removeView(titleSearchView);
        TextView rightTv = new TextView(this);
        rightTv.setText("明细");
        rightTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        rightTv.setTextColor(getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.icon_size_16dp);
        titleView.addView(rightTv, layoutParams);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyManagerActivity.this, FundDetailsListActivity.class));
            }
        });
    }

    private void setViewByType(MoneyType type) {
        switch (type) {
            case Balance:
                relSecurityDeposit.setVisibility(View.GONE);
                relBalance.setVisibility(View.VISIBLE);
                vLineBalance.setVisibility(View.VISIBLE);
                vLineSecurityDeposit.setVisibility(View.GONE);
                tvSecurityDeposit.setSelected(false);
                tvBalance.setSelected(true);
                break;
            case SecurityDeposit:
                relSecurityDeposit.setVisibility(View.VISIBLE);
                relBalance.setVisibility(View.GONE);
                vLineBalance.setVisibility(View.GONE);
                vLineSecurityDeposit.setVisibility(View.VISIBLE);
                tvSecurityDeposit.setSelected(true);
                tvBalance.setSelected(false);
                break;
        }

    }

}
