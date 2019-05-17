package com.chunlangjiu.app.appraise.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class AppraiseGoodsAdapter extends BaseQuickAdapter<AppraiseGoodsBean, BaseViewHolder> {

    private Context context;
    private String appraiseRole ;

    public AppraiseGoodsAdapter(Context context,String appraiseRole, List<AppraiseGoodsBean> data) {
        super(R.layout.appraise_item_goods_grid,data);
        this.appraiseRole = appraiseRole;
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
            String img = "" ;
            if(!TextUtils.isEmpty(item.getImg())){
                String[] imgList = item.getImg().split(",");
                img = imgList[0];
            }
            GlideUtils.loadImage(context, img, imgPic);
            helper.setText(R.id.tvName, item.getTitle());

            if("true".equals(item.getStatus())){
                helper.setText(R.id.tvAppraise,"鉴定报告");
            }else{
                if(CommonUtils.APPRAISE_ROLE_VERIFIER.equals(appraiseRole)){
                    helper.setText(R.id.tvAppraise,"去鉴定");
                }else{
                    helper.setText(R.id.tvAppraise,"鉴定中");
                }
            }

//            helper.addOnClickListener(R.id.tvAppraise);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}