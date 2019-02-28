package com.chunlangjiu.app.order.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.bean.LogisticsBean;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SystemUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChooseExpressSellerDialog extends Dialog {
    private Window window;
    private BaseActivity baseActivity;// 上下文
    private List<LogisticsBean.ListBean> data;
    private LayoutInflater inflater;

    private TextView tvConfirm;
    private TextView tvCancel;
    private CompositeDisposable disposable;
    private String tid;

    private CallBack callBack;
    private Spinner spExpressCompany;
    private EditText etExpressCode;
    private ArrayAdapter<String> adapter;
    private List<String> spinnerData;

    /**
     * Creates a new instance of ChooseShopDialog.
     *
     * @param context 上下文
     */
    public ChooseExpressSellerDialog(Context context, List<LogisticsBean.ListBean> data, String tid) {
        super(context, R.style.dialog_choose_express);
        this.baseActivity = (BaseActivity) context;
        this.data = data;
        this.tid = tid;
        inflater = LayoutInflater.from(context);
        initView();
        initData();
    }

    /**
     * init:【初始化dialog】. <br/>
     * ..<br/>
     */
    private void initView() {
        Window window = getWindow();
        int padding = (int)baseActivity.getResources().getDimension(R.dimen.layout_padding_12dp);
        window.getDecorView().setPadding(padding, 0, padding, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(R.layout.order_dialog_choose_express);

        tvConfirm = findViewById(R.id.tvConfirm);
        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);
        spExpressCompany = findViewById(R.id.spExpressCompany);
        etExpressCode = findViewById(R.id.etExpressCode);

        disposable = new CompositeDisposable();
    }

    /**
     * initData:【初始化数据】. <br/>
     * ..<br/>
     */
    private void initData() {
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

        if (null == spinnerData) {
            spinnerData = new ArrayList<>();
        } else {
            spinnerData.clear();
        }
        for (LogisticsBean.ListBean listBean : data) {
            spinnerData.add(listBean.getCorp_name());
        }

        if (null == adapter) {
            //将可选内容与ArrayAdapter连接起来
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerData);
            //设置下拉列表的风格
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spExpressCompany.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void setData(List<LogisticsBean.ListBean> data, String tid) {
        this.data = data;
        this.tid = tid;
        etExpressCode.setText("");
        initData();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvConfirm:
                    sendLogistics();
                    break;
                case R.id.tvCancel:
                    dismiss();
                    break;
            }
        }
    };

    private void sendLogistics() {
        String expressCode = etExpressCode.getText().toString();
        LogisticsBean.ListBean listBean = data.get(spExpressCompany.getSelectedItemPosition());
        if (TextUtils.isEmpty(expressCode)) {
            ToastUtils.showShort("请输入快递单号后重试");
        } else {
            baseActivity.showLoadingDialog();
            disposable.add(ApiUtils.getInstance().sendSellerLogistics(tid, listBean.getCorp_code(), expressCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean orderListBeanResultBean) throws Exception {
                            baseActivity.hideLoadingDialog();
                            if (0 == orderListBeanResultBean.getErrorcode()) {
                                ToastUtils.showShort("发货成功");
                                if (null != callBack) {
                                    callBack.sendExpressSuccess();
                                }
                                dismiss();
                            } else {
                                if (TextUtils.isEmpty(orderListBeanResultBean.getMsg())) {
                                    ToastUtils.showShort("发货失败");
                                } else {
                                    ToastUtils.showShort(orderListBeanResultBean.getMsg());
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            baseActivity.hideLoadingDialog();
                            if (TextUtils.isEmpty(throwable.getMessage())) {
                                ToastUtils.showShort("发货失败");
                            } else {
                                ToastUtils.showShort(throwable.getMessage());
                            }
                        }
                    }));
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void sendExpressSuccess();
    }
}
