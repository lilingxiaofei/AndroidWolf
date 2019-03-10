package com.chunlangjiu.app.user.activity;

import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageHomeActivity;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageListActivity;
import com.chunlangjiu.app.util.CommonUtils;

/**
 * @CreatedbBy: liucun on 2018/9/17
 * @Describe:
 */
public class AddGoodsSuccessActivity extends BaseActivity {

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvGoodsManager:
                    GoodsManageHomeActivity.startShopMainActivity(AddGoodsSuccessActivity.this);
//                    WebViewActivity.startWebViewActivity(AddGoodsSuccessActivity.this, ConstantMsg.WEB_URL_GOODS_MANAGER + BaseApplication.getToken(), "商品管理");
                    break;
                case R.id.tvCheckGoodsManager:
                    GoodsManageListActivity.startGoodsManageActivity(AddGoodsSuccessActivity.this, CommonUtils.GOODS_STATUS_AUDIT_PENDING);
//                    WebViewActivity.startWebViewActivity(AddGoodsSuccessActivity.this, ConstantMsg.WEB_URL_AUTH_GOODS + BaseApplication.getToken(), "审核商品");
                    break;
            }
        }
    };

    @Override
    public void setTitleView() {
        titleName.setText("商品添加");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_add_goods_success);
        findViewById(R.id.tvGoodsManager).setOnClickListener(onClickListener);
        findViewById(R.id.tvCheckGoodsManager).setOnClickListener(onClickListener);
    }

}
