package com.chunlangjiu.app.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.bean.DepositBean;
import com.chunlangjiu.app.money.bean.UserMoneyBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
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
    @BindView(R.id.tvDepositCount)
    TextView tvDepositCount;
    @BindView(R.id.btnPaySecurityDeposit)
    Button btnPaySecurityDeposit;
    @BindView(R.id.btnDepositCancel)
    Button btnDepositCancel;

    private CompositeDisposable disposable;
    private UserMoneyBean userMoneyBean;
    private DepositBean depositBean;


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
        initEvent();
    }

    private void initEvent() {
        EventManager.getInstance().registerListener(onNotifyListener);

    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            switch (eventTag) {
                case ConstantMsg.RECHARGE:
                case ConstantMsg.WITHDRAW_DEPOSIT_CASH:
                    getUserMoney();
                    break;
                case ConstantMsg.DEPOSIT_CREATE:
                case ConstantMsg.WITHDRAW_DEPOSIT_REFUND:
                    getDepositMoney();
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void initData() {
        btnPaySecurityDeposit.setEnabled(false);
        getUserMoney();
        getDepositMoney();

    }

    private void getUserMoney() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getUserMoney((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UserMoneyBean>>() {
                    @Override
                    public void accept(ResultBean<UserMoneyBean> resultBean) throws Exception {
                        hideLoadingDialog();
                        userMoneyBean = resultBean.getData();
                        if (null != userMoneyBean)
                            tvFreezeBalance.setText(userMoneyBean.getFreeze_money() == null ? "0.00" : userMoneyBean.getFreeze_money());
                        tvAvailableBalance.setText(userMoneyBean.getMoney() == null ? "0.00" : userMoneyBean.getMoney());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    private void getDepositMoney() {
        disposable.add(ApiUtils.getInstance().getDeposit((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<DepositBean>>() {
                    @Override
                    public void accept(ResultBean<DepositBean> resultBean) throws Exception {
                        depositBean = resultBean.getData();
                        if (null != depositBean) {
                            tvDepositCount.setText(depositBean.getDeposit() == null ? "" : depositBean.getDeposit());
                            if ("1".equals(depositBean.getDeposit_status())) { //deposit_status 1.交纳 2.撤销中 3.撤销
                                btnPaySecurityDeposit.setText("撤销保证金");
                                btnPaySecurityDeposit.setEnabled(true);
                                btnPaySecurityDeposit.setBackgroundResource(R.drawable.bg_gray_rectangle);
                                btnDepositCancel.setVisibility(View.GONE);
                            } else if ("2".equals(depositBean.getDeposit_status())) {
                                btnDepositCancel.setVisibility(View.VISIBLE);
                                btnPaySecurityDeposit.setText("撤销保证金中");
                                btnPaySecurityDeposit.setEnabled(false);
                                btnPaySecurityDeposit.setBackgroundResource(R.drawable.bg_gray_rectangle);
                            } else if ("0".equals(depositBean.getDeposit_status())) {
                                btnDepositCancel.setVisibility(View.GONE);
                                btnPaySecurityDeposit.setText("缴纳保证金");
                                btnPaySecurityDeposit.setEnabled(true);
                                btnPaySecurityDeposit.setBackgroundResource(R.drawable.bg_red_rectangle);
                            }
                        }
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

    @OnClick({R.id.img_title_left, R.id.relSelectBalance, R.id.relSelectSecurityDeposit, R.id.btnWithdraw, R.id.btnRecharge, R.id.btnPaySecurityDeposit,R.id.btnDepositCancel})
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
            case R.id.btnWithdraw: {
                Intent intent = new Intent(this, WithDrawActivity.class);
                intent.putExtra(WithDrawActivity.WithDrawType, WithDrawActivity.DepositCash);
                intent.putExtra(WithDrawActivity.MoneyCount,userMoneyBean.getMoney());
                startActivity(intent);
            }
            break;
            case R.id.btnRecharge:
                startActivity(new Intent(this, ReChargeActivity.class));
                break;
            case R.id.btnPaySecurityDeposit:
                String status = depositBean.getDeposit_status();
                if ("1".equals(status)) {
                    Intent intent = new Intent(this, SecurityDepositManagerActivity.class);
                    intent.putExtra(SecurityDepositManagerActivity.SECURITY_DEPOSIT_TYPE, SecurityDepositManagerActivity.REFUND_DEPOSIT);
                    startActivity(intent);

//                    Intent intent = new Intent(this, WithDrawActivity.class);
//                    intent.putExtra(WithDrawActivity.WithDrawType, WithDrawActivity.DepositRefund);
//                    startActivity(intent);
                    btnDepositCancel.setVisibility(View.GONE);
                } else if ("2".equals(status)) {
                    btnDepositCancel.setVisibility(View.VISIBLE);

                } else if ("0".equals(status)) {
                    btnDepositCancel.setVisibility(View.GONE);
                    Intent intent = new Intent(this, ReChargeActivity.class);
                    intent.putExtra(ReChargeActivity.ReChargeType, ReChargeActivity.RechargeType.SecurityDeposit);
//                    intent.putExtra(ReChargeActivity.ReChargeType,depositBean.getDeposit());
                    intent.putExtra(ReChargeActivity.DepositMoney, depositBean.getDeposit());
                    startActivity(intent);

                }
                break;
            case R.id.btnDepositCancel:
                depositCancel();
                break;

        }

    }
    private void depositCancel(){
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().cancelDeposit((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("取消撤销成功");
                        getDepositMoney();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
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
