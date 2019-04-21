package com.chunlangjiu.app.appraise.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.List;

public class AppraiserAdapter extends BaseQuickAdapter<AppraiseBean, BaseViewHolder> {
    private Context context;

    public AppraiserAdapter(Context context, List<AppraiseBean> data) {
        super(R.layout.appraise_activity_main_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppraiseBean item) {
        ImageView imgPic = helper.getView(R.id.imgHead);
        GlideUtils.loadImageHead(context, "", imgPic);

        helper.setText(R.id.tvName,"鉴定师名称");
        helper.setText(R.id.tvAppraiseScope,context.getString(R.string.appraise_scope,"茅台，五粮液，各种皮牌老酒"));
        helper.setText(R.id.tvAppraiseRequire,context.getString(R.string.appraise_require,"拍摄物品图清晰，有主题"));
        helper.setText(R.id.tvTipsOne,"日均"+12);
        helper.setText(R.id.tvTipsTwo,"完成率"+"%80");
        String queueUp = "5";
        helper.setText(R.id.tvTipsThree,CommonUtils.setSpecifiedTextsColor("排队"+queueUp,queueUp, ContextCompat.getColor(context,R.color.t_red)));

    }
}