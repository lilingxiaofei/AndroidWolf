package com.chunlangjiu.app.appraise.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;

import java.util.List;

public class AppraiserAdapter extends BaseQuickAdapter<AppraiseBean, BaseViewHolder> {
        public AppraiserAdapter(int layoutResId, List<AppraiseBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AppraiseBean item) {
//            ImageView imgPic = helper.getView(R.id.img_pic);
//            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
//            int screenWidth = SizeUtils.getScreenWidth();
//            int picWidth = (screenWidth - SizeUtils.dp2px(25)) / 2;
//            layoutParams.height = picWidth;
//            layoutParams.width = picWidth;
//            imgPic.setLayoutParams(layoutParams);
//
//            TextView tv_name = helper.getView(R.id.tv_name);
//            ViewGroup.LayoutParams nameLayoutParams = tv_name.getLayoutParams();
//            nameLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            nameLayoutParams.width = picWidth;
//            tv_name.setLayoutParams(nameLayoutParams);
//            helper.setText(R.id.tv_name, item.getTitle());
//            helper.setText(R.id.tv_price, "¥" + item.getPrice());
//            GlideUtils.loadImage(GoodsDetailslNewActivity.this, item.getImage_default_id(), imgPic);
        }
    }