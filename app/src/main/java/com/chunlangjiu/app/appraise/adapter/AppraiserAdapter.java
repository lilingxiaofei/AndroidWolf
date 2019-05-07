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
        GlideUtils.loadImageHead(context, item.getAuthenticate_img(), imgPic);
        helper.setText(R.id.tvName,item.getAuthenticate_name());
        helper.setText(R.id.tvAppraiseScope,context.getString(R.string.appraise_scope,item.getAuthenticate_scope()));
        helper.setText(R.id.tvAppraiseRequire,context.getString(R.string.appraise_require,item.getAuthenticate_require()));
        helper.setText(R.id.tvTipsOne,"日均"+item.getDay());
        helper.setText(R.id.tvTipsTwo,"完成率"+item.getRate());
        String queueUp = item.getLine();
        helper.setText(R.id.tvTipsThree,CommonUtils.setSpecifiedTextsColor("排队"+queueUp,queueUp, ContextCompat.getColor(context,R.color.t_red)));

    }
}