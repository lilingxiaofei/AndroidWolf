package com.chunlangjiu.app.order.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.pkqup.commonlibrary.util.SystemUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

public class RefundAmountDialog extends Dialog {
    private Window window;
    private Context context;// 上下文
    private LayoutInflater inflater;

    private TextView tvOk;
    private TextView tvCancel;
    private EditText etContent;

    private CallBack callBack;

    /**
     * Creates a new instance of ChooseShopDialog.
     *
     * @param context 上下文
     */
    public RefundAmountDialog(Context context) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
        initData();
    }

    /**
     * init:【初始化dialog】. <br/>
     * ..<br/>
     */
    private void initView() {
        setContentView(R.layout.order_dialog_refund_aftersale);
        etContent = findViewById(R.id.etContent);
        tvOk = findViewById(R.id.tvOk);
        tvCancel = findViewById(R.id.tvCancel);
        tvOk.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);
    }

    /**
     * initData:【初始化数据】. <br/>
     * ..<br/>
     */
    private void initData() {
        etContent.setText("");
        if (null == window) {
            window = getWindow();
        }
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); // 消除边距

            WindowManager.LayoutParams lp = window.getAttributes();
            int[] deviceWh = SystemUtils.getDeviceWh(getContext());
            lp.width = (int) (deviceWh[0] / 1.5); // 设置宽度充满屏幕
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvOk:
                    String s = etContent.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        callBack.confirm(s);
                        dismiss();
                    } else {
                        ToastUtils.showShort("请输入退款金额");
                    }
                    break;
                case R.id.tvCancel:
                    dismiss();
                    break;
            }
        }
    };

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void confirm(String money);
    }
}