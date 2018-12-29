package com.chunlangjiu.app.fans.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.fans.bean.FansBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.dialog.CommonLoadingDialog;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;

import org.w3c.dom.Text;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/10/24
 * @Describe:
 */
public class InviteCodeDialog extends Dialog {

    private Context context;
    private CallBack callBack;

    private EditText etName;
    private TextView tvConfirm;
    private TextView tvCancel ;
    private CommonLoadingDialog loadingDialog ;

    public InviteCodeDialog(Context context) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void initView() {
        loadingDialog = new CommonLoadingDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.fans_dialog_invite_code, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);

        setContentView(view);// 设置布局

        etName = findViewById(R.id.etName);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvCancel = findViewById(R.id.tvCancel);

        tvConfirm.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId() ;
            if(resId == R.id.tvConfirm){
                checkInviteCode();
            }else if(resId == R.id.tvCancel){
                dismiss();
            }
        }
    };

    /**
     * 提交邀请码到后台服务器
     */
    private void checkInviteCode(){
        String inviteCode = etName.getText().toString();
        if(TextUtils.isEmpty(inviteCode)){
            ToastUtils.showShort("请填写邀请人的邀请码~~~");
            return ;
        }
        submitInviteCode(inviteCode);
    }

    private void submitInviteCode(String inviteCode) {
        loadingDialog.show();
        ApiUtils.getInstance().submitInviteCode(inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean mainClassBeanResultBean) throws Exception {
                        ToastUtils.showShort("邀请码上传成功");
                        loadingDialog.dismiss();
                        dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loadingDialog.dismiss();
                    }
                });
    }

    public void clearName(){
        etName.setText("");
    }


    public interface CallBack {
        void onConfirm(String name);
    }
}
