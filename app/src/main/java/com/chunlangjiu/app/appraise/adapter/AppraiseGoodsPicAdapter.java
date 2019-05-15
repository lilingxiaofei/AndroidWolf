package com.chunlangjiu.app.appraise.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class AppraiseGoodsPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    public AppraiseGoodsPicAdapter(Context context, List<String> data) {
        super(R.layout.appraise_item_goods_pic,data);
        this.context = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        try {
            ImageView imgPic = helper.getView(R.id.imgPic);
            //网格布局的时候设置图片大小
            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
            if (layoutParams.width == layoutParams.height && layoutParams.width > 0) {
                int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(38)) / 3;
                layoutParams.width = picWidth;
                layoutParams.height = picWidth;
                imgPic.setLayoutParams(layoutParams);
            }
            GlideUtils.loadImage(context, item, imgPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}