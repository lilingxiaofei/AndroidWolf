package com.chunlangjiu.app.user.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chunlangjiu.app.R;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class StarLevelDialog extends Dialog {

    private Activity context;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTips ;
    private String tips;



    public StarLevelDialog(Activity context) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        initView();
    }



    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_dialog_start_level, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(view);// 设置布局

        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvTips = findViewById(R.id.tvTips);
        tvCancel.setOnClickListener(onClickListener);
        tvConfirm.setOnClickListener(onClickListener);
    }
    public void updateTips(String tips){
        if(null != tvTips){
            tvTips.setText(tips);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tvConfirm ) {
                confirm();
            }else{
                cancelPay();
            }
        }
    };

    private void cancelPay(){
        if(callBack!=null){
            callBack.cancel();
        }
    }

        private void confirm(){

        if(callBack!=null){
            callBack.confirm();
        }
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void cancel();
        void confirm();
    }
}
