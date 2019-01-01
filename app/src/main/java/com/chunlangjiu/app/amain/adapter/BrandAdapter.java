package com.chunlangjiu.app.amain.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/21.
 * @Describe: 品牌推荐
 */
public class BrandAdapter extends BaseQuickAdapter<HomeModulesBean.Pic, BaseViewHolder> {

    private Context context;

    public BrandAdapter(Context context, int layoutResId, List<HomeModulesBean.Pic> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeModulesBean.Pic item) {
        ImageView imgPic = helper.getView(R.id.imgPic);

        //网格布局的时候设置图片大小
        ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
        if(layoutParams.width == layoutParams.height && layoutParams.width>0){
            int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(46)) / 3;
            layoutParams.width = picWidth;
            layoutParams.height = picWidth;
            imgPic.setLayoutParams(layoutParams);
        }

        GlideUtils.loadImage(context,item.getImage(),imgPic);
        helper.setText(R.id.tvBrandName,item.getBrandname());
        helper.setText(R.id.tvDesc,item.getLinkinfo());
    }


}
