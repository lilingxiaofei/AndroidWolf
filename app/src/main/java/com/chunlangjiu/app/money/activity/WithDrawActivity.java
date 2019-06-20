package com.chunlangjiu.app.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.bean.DepositCashBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.BankCardActivity;
import com.chunlangjiu.app.user.bean.BankCardListBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WithDrawActivity extends BaseActivity {//最多可提（¥1250.00）

    @BindView(R.id.edtAmount)
    EditText edtAmount;
    @BindView(R.id.tvBankCard)
    TextView tvBankCard;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.lineMoney)
    LinearLayout lineMoney;
    @BindView(R.id.btnOk)
    Button btnOk;

    public static final String WithDrawType = "WithDrawType";
    public static final String DepositCash = "DepositCash";
    public static final String DepositRefund = "DepositRefund";
    public static final String BankCardData = "BankCardData";
    public static final String MoneyCount = "MoneyCount";

    public static final int BankCardRequestCode = 188;
    public static final int BankCardResultCode = 200;

    private CompositeDisposable disposable;
    private String bankCardId = "";
    private BankCardListBean.BankCardDetailBean bankCardDetailBean;

    private CommonConfirmDialog confirmDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        disposable = new CompositeDisposable();
        initView();

    }

    private void initView() {
        String type = getIntent().getStringExtra(WithDrawType);
        if (type.equals(DepositRefund)) {
            lineMoney.setVisibility(View.GONE);
            tvTips.setText(getResources().getString(R.string.refund_deposit_security_tips));
            btnOk.setText("撤销保证金");
        } else {
            tvTips.setText(getResources().getString(R.string.refund_tips));
            confirmDialog = new CommonConfirmDialog(this,"您好本次提现周期为1-2个工作日，具体时间以银行为准，提现记录详见“资金管理-明细”，如有疑问可致电400-189-0095，谢谢！");
            String count =getIntent().getStringExtra(MoneyCount);
            edtAmount.setHint(String.format("可提余额（¥%s）", BigDecimalUtils.objToStr(count)));
            btnOk.setText("确认提现");
        }
    }

    @Override
    public void setTitleView() {
        String type = getIntent().getStringExtra(WithDrawType);
        if (type.equals(DepositCash)) {
            titleName.setText("提现");
        } else {
            titleName.setText("撤销保证金");
        }
    }

    @OnClick({R.id.img_title_left, R.id.reBankCard, R.id.btnOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.reBankCard:
                Intent intent = new Intent(this, BankCardActivity.class);
                intent.putExtra(BankCardActivity.FromType, BankCardActivity.ClassWithDrawActivity);
                startActivityForResult(intent, BankCardRequestCode);
                break;
            case R.id.btnOk:
                final String amount = edtAmount.getText().toString().trim();
                if (TextUtils.isEmpty(bankCardId)) {
                    ToastUtils.showShort("请选择银行卡");
                    return;
                }
                String type = getIntent().getStringExtra(WithDrawType);
                if (DepositCash.equals(type)) {
                    if (TextUtils.isEmpty(amount)) {
                        ToastUtils.showShort("请输入提现金额");
                        return;
                    }
                    confirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                        @Override
                        public void onConfirm() {
                            depositCash(bankCardId, amount);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    confirmDialog.show();
                } else if (DepositRefund.equals(type)) {
                    depositRefund();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }

    /**
     * 余额提现
     *
     * @param bankCardId
     * @param amount
     */
    private void depositCash(String bankCardId, String amount) {
        disposable.add(ApiUtils.getInstance().depositCash((String) SPUtils.get("token", ""), bankCardId, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<DepositCashBean>>() {
                    @Override
                    public void accept(ResultBean<DepositCashBean> resultBean) throws Exception {
                        DepositCashBean depositCashBean = resultBean.getData();
                        if (null != depositCashBean) {
                            ToastUtils.showShort(depositCashBean.getMessage());
                            EventManager.getInstance().notify(null, ConstantMsg.WITHDRAW_DEPOSIT_CASH);
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    /**
     * 撤销保证金
     *
     * @param
     */

    private void depositRefund() {
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
//                            finish();
                            Intent intent = new Intent(WithDrawActivity.this, SecurityDepositManagerActivity.class);
                            intent.putExtra(SecurityDepositManagerActivity.SECURITY_DEPOSIT_TYPE, SecurityDepositManagerActivity.REFUND_DEPOSIT);
                            intent.putExtra(SecurityDepositManagerActivity.STATUS,SecurityDepositManagerActivity.REFUND_SUCCESS);
                            startActivity(intent);
                            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BankCardRequestCode && resultCode == BankCardResultCode) {
            bankCardDetailBean = (BankCardListBean.BankCardDetailBean) data.getSerializableExtra(BankCardData);
            if (null != bankCardDetailBean) {
                bankCardId = String.valueOf(bankCardDetailBean.getBank_id());
                tvBankCard.setText(bankCardDetailBean.getCard());
            }

        }
    }
}
