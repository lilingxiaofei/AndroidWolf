package com.chunlangjiu.app.fans.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.fans.bean.FansItemBean;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/21.
 * @Describe: 品牌推荐
 */
public class FansAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {

    private Context context;

    public FansAdapter(Context context, int layoutResId, List<FansItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FansItemBean item) {
        helper.setText(R.id.tv_fans_name,item.getFansName());
        helper.setText(R.id.tv_fans_phone,item.getPhone());
        helper.setText(R.id.tv_register_time,item.getRegisterTime());
        helper.setText(R.id.tv_money,item.getTotalMoney());
    }


}
