package com.chunlangjiu.app.goods.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goods.bean.PayBalanceBean;
import com.chunlangjiu.app.money.activity.ReChargeActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.SetPayPasswordActivity;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class BalancePayDialog extends Dialog {

    private Activity context;
    private TextView tvMoney;
    private TextView etPsd;
    private TextView tvCancel;
    private TextView tvConfirm;
    private ImageView imgClose;


    private PayBalanceBean balanceBean ;
    private String payMoney;


    public BalancePayDialog(Activity context, String payMoney) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        this.payMoney = payMoney;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
        getBalanceInfo();
    }

    private void getBalanceInfo() {
        ApiUtils.getInstance().getBalanceInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PayBalanceBean>>() {
                    @Override
                    public void accept(ResultBean<PayBalanceBean> resultBean) throws Exception {
                        balanceBean = resultBean.getData();
                        updateBalanceUI();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("获取余额信息失败");
                    }
                });
    }

    private void updateBalanceUI(){
        if(null != balanceBean){
            BigDecimal bigDecimal = BigDecimalUtils.substractObj(balanceBean.getDeposit(),payMoney);
            if(!balanceBean.isPassword()){
                etPsd.setHint("未设置支付密码，请先设置支付密码");
                etPsd.setEnabled(false);
                tvConfirm.setText("设置密码");
            }else if(bigDecimal!=null && bigDecimal.doubleValue()<=0){
                etPsd.setHint("余额不足，请先去充值");
                etPsd.setEnabled(false);
                tvConfirm.setText("余额充值");
            }else{
                etPsd.setEnabled(true);
            }
        }else{
            etPsd.setEnabled(false);
        }
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_dialog_pay_balance, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(view);// 设置布局

        tvMoney = findViewById(R.id.tvMoney);
        etPsd = findViewById(R.id.etPsd);
        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);
        imgClose = findViewById(R.id.imgClose);

        tvMoney.setText(context.getString(R.string.rmb)+payMoney);
        imgClose.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);
        tvConfirm.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tvConfirm ) {
                confirm();
            }else{
                cancelPay();
            }
            dismiss();
        }
    };

    private void cancelPay(){
        if(callBack!=null){
            callBack.cancelPay();
        }
    }

    private void confirm(){
        BigDecimal bigDecimal = BigDecimalUtils.substractObj(balanceBean.getDeposit(),payMoney);
        if(!balanceBean.isPassword()){
            SetPayPasswordActivity.startActivity(context);
            return ;
        }else if(bigDecimal!=null && bigDecimal.doubleValue()<=0){
            context.startActivity(new Intent(context, ReChargeActivity.class));
            return ;
        }else if(TextUtils.isEmpty(etPsd.getText().toString())){
            ToastUtils.showShort("请输入支付密码");
            return ;
        }

        if(callBack!=null){
            callBack.confirmPay(etPsd.getText().toString());
        }
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void cancelPay();
        void confirmPay(String payPwd);
    }
}
