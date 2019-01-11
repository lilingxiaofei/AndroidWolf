package com.chunlangjiu.app.user.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtIdCard)
    EditText edtIdCard;
    @BindView(R.id.edtBankCard)
    EditText edtBankCard;
    @BindView(R.id.edtBranch)
    EditText edtBranch;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtCode)
    EditText edtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
    }

    @Override
    public void setTitleView() {
        titleName.setText("添加银行卡");
    }

    @OnClick({R.id.btnBindBankCard, R.id.tvCity, R.id.tvProvince})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.btnBindBankCard:
                break;
            case R.id.tvCity:
                break;
            case R.id.tvProvince:
                break;
        }
    }
}
