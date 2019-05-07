package com.chunlangjiu.app.appraise.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class AppraiseGoodsAdapter extends BaseQuickAdapter<AppraiseGoodsBean, BaseViewHolder> {
    public static final int LIST_LINEAR = 0;
    public static final int LIST_GRID = 1;
    private int itemType = LIST_GRID;

    private Context context;
    private boolean isShowStoreView = true;

    public AppraiseGoodsAdapter(Context context, List<AppraiseGoodsBean> data) {
        super(R.layout.appraise_item_goods_grid,data);
        this.context = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, AppraiseGoodsBean item) {
        setItemContent(helper, item);

    }

    private void setItemContent(BaseViewHolder helper, AppraiseGoodsBean item) {
        try {
            ImageView imgPic = helper.getView(R.id.imgPic);
            //网格布局的时候设置图片大小
            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
            if (layoutParams.width == layoutParams.height && layoutParams.width > 0) {
                int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(25)) / 2;
                layoutParams.width = picWidth;
                layoutParams.height = picWidth;
                imgPic.setLayoutParams(layoutParams);
            }
            GlideUtils.loadImage(context, item.getImg(), imgPic);
            helper.setText(R.id.tvName, item.getTitle());
            helper.setText(R.id.tvAppraise,"鉴定报告");
//            helper.addOnClickListener(R.id.tvAppraise);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}