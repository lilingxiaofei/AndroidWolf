package com.chunlangjiu.app.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.activity.MainActivity;
import com.chunlangjiu.app.money.bean.DepositBean;
import com.chunlangjiu.app.money.bean.DepositCashBean;
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

public class SecurityDepositManagerActivity extends BaseActivity {

    //    @BindView(R.id.bankCardList)
//    RecyclerView bankCardList;
    @BindView(R.id.lineaCancelDeposit)
    LinearLayout lineaCancelDeposit;
    @BindView(R.id.lineaPayDepositSuccess)
    LinearLayout lineaPayDepositSuccess;
    @BindView(R.id.lineaRefundDeposit)
    LinearLayout lineaRefundDeposit;


    public static final String SECURITY_DEPOSIT_TYPE = "SECURITY_DEPOSIT_TYPE";
    public static final String PAYING_DEPOSIT = "PAYING_DEPOSIT";
    public static final String REFUND_DEPOSIT = "REFUND_DEPOSIT";
    public static final String STATUS = "status";
    public static final String PAY_SUCCESS = "pay_success";
    public static final String REFUND_SUCCESS = "refund_success";
    private String depositStatus = "";


    private String securityDepositType = PAYING_DEPOSIT;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_deposit_manager);
        disposable = new CompositeDisposable();
        securityDepositType = getIntent().getStringExtra(SECURITY_DEPOSIT_TYPE);
        if (null == securityDepositType) {
            securityDepositType = PAYING_DEPOSIT;
        }
        depositStatus = getIntent().getStringExtra(STATUS);
        if (null==depositStatus){
            depositStatus ="";
        }
        setViewByType();
    }

    private void setViewByType() {
        if (depositStatus.equals(PAY_SUCCESS)) {
            lineaCancelDeposit.setVisibility(View.GONE);
            lineaPayDepositSuccess.setVisibility(View.VISIBLE);
            lineaRefundDeposit.setVisibility(View.GONE);
        } else if (depositStatus.equals(REFUND_SUCCESS)) {
            lineaCancelDeposit.setVisibility(View.GONE);
            lineaPayDepositSuccess.setVisibility(View.GONE);
            lineaRefundDeposit.setVisibility(View.VISIBLE);
        } else {
            lineaCancelDeposit.setVisibility(View.VISIBLE);
            lineaPayDepositSuccess.setVisibility(View.GONE);
            lineaRefundDeposit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.img_title_left, R.id.btnNext, R.id.btnCancel, R.id.btnGoPrivilege, R.id.btnGoVipCenter, R.id.btnCancelApply, R.id.btnGoVipCenterS})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btnNext: {
                depositRefund();
//                depositRefund();
//                Intent intent = new Intent(this, WithDrawActivity.class);
//                intent.putExtra(WithDrawActivity.WithDrawType, WithDrawActivity.DepositRefund);
//                startActivity(intent);
//                finish();
            }
            break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnGoPrivilege: {
                Intent intent = new Intent(this, StarSellerPrivilegeActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btnGoVipCenter: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            break;
            case R.id.btnCancelApply:
                depositCancel();
                break;
            case R.id.btnGoVipCenterS: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void setTitleView() {
        securityDepositType = getIntent().getStringExtra(SECURITY_DEPOSIT_TYPE);
        if (null == securityDepositType) {
            securityDepositType = PAYING_DEPOSIT;
        }
        switch (securityDepositType) {
            case PAYING_DEPOSIT:
                titleName.setText(R.string.payment_deposit);
                break;
            case REFUND_DEPOSIT:
                titleName.setText("撤销保证金");
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
                        EventManager.getInstance().notify(null, ConstantMsg.WITHDRAW_DEPOSIT_REFUND);
                        ToastUtils.showShort("取消撤销成功");
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
    /**
     * 撤销保证金
     *
     * @param
     */

    private void depositRefund( ) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().depositRefund((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<DepositCashBean>>() {
                    @Override
                    public void accept(ResultBean<DepositCashBean> resultBean) throws Exception {
                        hideLoadingDialog();
                        DepositCashBean depositCashBean = resultBean.getData();
                        if (null != depositCashBean) {
                            ToastUtils.showShort(depositCashBean.getMessage());
                            EventManager.getInstance().notify(null, ConstantMsg.WITHDRAW_DEPOSIT_REFUND);
                            depositStatus = REFUND_SUCCESS;
                            setViewByType();
//                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=disposable)
            disposable.dispose();
    }
}
