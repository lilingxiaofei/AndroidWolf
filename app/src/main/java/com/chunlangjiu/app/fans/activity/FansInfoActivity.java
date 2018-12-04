package com.chunlangjiu.app.fans.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;

public class FansInfoActivity extends BaseActivity {
    @BindView(R.id.tv_invite_fans)
    TextView tvInviteFans;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_invite_person)
    TextView tvInvitePerson;
    @BindView(R.id.btn_invite_fans)
    Button btnInviteFans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_info);
        updateView();
        tvCopy.setOnClickListener(onClickListener);
        btnInviteFans.setOnClickListener(onClickListener);
    }

    private void updateView(){
        tvInviteFans.setText("");
        tvInvitePerson.setText("");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId() ;
            if(resId == R.id.tv_copy){
                copyStr();
            }else if(resId == R.id.btn_invite_fans){
                startInviteFansActivity();
            }
        }
    };

    /**
     * 复制邀请码
     */
    public void copyStr(){
        String copyContent = tvInviteFans.getText().toString();
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        int sdk=android.os.Build.VERSION.SDK_INT;
        if(sdk>=11){
            cm.setPrimaryClip(ClipData.newPlainText(copyContent,copyContent));
        }else{
            cm.setText(copyContent);
        }
        ToastUtils.showShort("复制成功"+copyContent);
    }

    /**
     * 启动邀请码Activity
     */
    public void startInviteFansActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansInviteActivity.class);
        startActivity(intent);
    }

    @Override
    public void setTitleView() {
        hideTitleView();
    }
}
