package com.chunlangjiu.app.appraise.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.Iterator;
import java.util.List;

public class AppraiserAdapter extends BaseQuickAdapter<AppraiseBean, BaseViewHolder> {
    private Context context;

    public AppraiserAdapter(Context context, List<AppraiseBean> data) {
        super(data);
        this.context = context;

        setMultiTypeDelegate(new MultiTypeDelegate<AppraiseBean>() {
            @Override
            protected int getItemType(AppraiseBean item) {
                return item.isAdd()?1:0 ;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.appraise_activity_main_item)
                .registerItemType(1, R.layout.appraise_activity_main_item_add);
    }

    public void setAddNewData(List<AppraiseBean> data){
        if(data!=null){
            Iterator<AppraiseBean> it = data.iterator();
            while(it.hasNext()){
                AppraiseBean item = it.next();
                if(item.isAdd()){
                    it.remove();
                }
            }
            AppraiseBean addBean = new AppraiseBean();
            addBean.setAdd(true);
            data.add(addBean);
        }
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppraiseBean item) {
        int itemType = helper.getItemViewType();
        if(itemType == 0){
            ImageView imgPic = helper.getView(R.id.imgHead);
            GlideUtils.loadImageHead(context, item.getAuthenticate_img(), imgPic);
            helper.setText(R.id.tvName,item.getAuthenticate_name());
            helper.setText(R.id.tvAppraiseScope,CommonUtils.getString(R.string.appraise_scope,item.getAuthenticate_scope()));
            helper.setText(R.id.tvAppraiseRequire,CommonUtils.getString(R.string.appraise_require,item.getAuthenticate_require()));
            helper.setText(R.id.tvTipsOne,"累计鉴定"+item.getLine());
            helper.setText(R.id.tvTipsTwo,"完成率"+item.getRate());
            String queueUp = item.getDay();
            helper.setText(R.id.tvTipsThree,CommonUtils.setSpecifiedTextsColor("今日鉴定"+queueUp,queueUp, ContextCompat.getColor(context,R.color.t_red)));
        }


    }
}