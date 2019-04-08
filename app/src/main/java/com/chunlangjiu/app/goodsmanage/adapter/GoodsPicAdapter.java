package com.chunlangjiu.app.goodsmanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chunlangjiu.app.R;
import com.lzy.imagepicker.bean.ImageItem;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class GoodsPicAdapter extends BaseAdapter {


    private Context context;
    private List<ImageItem> list;
    private LayoutInflater inflater;

    public GoodsPicAdapter(Context context, List<ImageItem> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void updateData(List<ImageItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    // 得到总的数量
    @Override
    public int getCount() {
        int size = list == null ? 0 : list.size() ;
        if(size<5){
            size = size+1;
        }
        return size;
    }

    // 根据ListView位置返回View
    @Override
    public ImageItem getItem(int position) {
        if(list!=null && list.size()>position){
            return list.get(position);
        }else{
            return null ;
        }
    }

    // 根据ListView位置得到List中的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 根据位置得到View对象
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_item_goods_manage_add_pic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.llAdd.setTag(R.id.position,position);
        holder.ivDeleteGoodsPic.setTag(R.id.position,position);
        holder.llAdd.setOnClickListener(onClickListener);
        holder.ivDeleteGoodsPic.setOnClickListener(onClickListener);

        try {
            ImageItem item = getItem(position);
            if(item == null){
                holder.llAdd.setVisibility(View.VISIBLE);
                holder.ivGoodsPic.setVisibility(View.GONE);
                holder.ivDeleteGoodsPic.setVisibility(View.GONE);
            }else{
                GlideUtils.loadImage(context, item.path, holder.ivGoodsPic);
                holder.llAdd.setVisibility(View.GONE);
                holder.ivGoodsPic.setVisibility(View.VISIBLE);
                holder.ivDeleteGoodsPic.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int position = (Integer) view.getTag(R.id.position);
                if(onItemChildClickListener!=null){
                    onItemChildClickListener.onItemChildClick(view,position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    class ViewHolder {
        LinearLayout llAdd;
        ImageView ivGoodsPic;
        ImageView ivDeleteGoodsPic;
        RelativeLayout rlGoods;
        ViewHolder(View view){
            llAdd = view.findViewById(R.id.llAdd);
            ivGoodsPic= view.findViewById(R.id.ivGoodsPic);
            ivDeleteGoodsPic= view.findViewById(R.id.ivDeleteGoodsPic);
            rlGoods = view.findViewById(R.id.rlGoods);
            ViewGroup.LayoutParams layoutParams = rlGoods.getLayoutParams();
            layoutParams.width = (SizeUtils.getScreenWidth()-SizeUtils.dp2px(10))/2;
            layoutParams.height = layoutParams.width;
            rlGoods.setLayoutParams(layoutParams);
        }
    }

    OnItemChildClickListener onItemChildClickListener;

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public interface OnItemChildClickListener{
        public void onItemChildClick(View view, int position);
    }
}