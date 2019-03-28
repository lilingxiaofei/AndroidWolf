package com.chunlangjiu.app.goods.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.pkqup.commonlibrary.util.ToastUtils;

/**
 * @CreatedbBy: liucun on 2018/10/24
 * @Describe:
 */
public class EditAccountNameDialog extends Dialog {

    private Context context;
    private CallBack callBack;

    private EditText etName;
    private TextView tv_confirm;
    private TextView tv_cancel ;
    private ImageView ivClose;

    public EditAccountNameDialog(Context context) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initView();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.my_dialog_edit_name, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);

        setContentView(view);// 设置布局

        etName = findViewById(R.id.etName);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_cancel = findViewById(R.id.tv_cancel);
        ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(onClickListener);
        tv_cancel.setOnClickListener(onClickListener);
        tv_confirm.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tv_confirm){
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name) || name.length() < 2) {
                    ToastUtils.showShort("请输入2-7个字的名称");
                }else{
                    if (null != callBack) {
                        dismiss();
                        callBack.onConfirm(name);
                    }
                }
            }
            dismiss();
        }
    };

    public void clearName(){
        etName.setText("");
    }

    public void setNameContent(String name){
        etName.setText(name);
    }


    public interface CallBack {
        void onConfirm(String name);
    }
}
