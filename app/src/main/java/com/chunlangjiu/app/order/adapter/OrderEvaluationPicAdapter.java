package com.chunlangjiu.app.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.order.bean.OrderEvaluationPicBean;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.List;

public class OrderEvaluationPicAdapter extends BaseAdapter {
    private Context context;
    private List<OrderEvaluationPicBean> list;
    private LayoutInflater inflater;

    public OrderEvaluationPicAdapter(Context context, List<OrderEvaluationPicBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void updateData(List<OrderEvaluationPicBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    // 得到总的数量
    @Override
    public int getCount() {
        int size = list == null ? 0 : list.size() ;
        if(size<3){
            size = size+1;
        }
        return size;
//        return list == null ? 0 : list.size();
    }

    // 根据ListView位置返回View
    @Override
    public OrderEvaluationPicBean getItem(int position) {
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
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_adapter_evaluationpic, null);
            holder.imgOrderEvaluation =
                    convertView.findViewById(R.id.imgOrderEvaluation);
            holder.tvOrderEvaluation = convertView.findViewById(R.id.tvOrderEvaluation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderEvaluationPicBean orderEvaluationPicBean = getItem(position);
        if (orderEvaluationPicBean!=null) {
            GlideUtils.loadImage(context, orderEvaluationPicBean.getPicPath(), holder.imgOrderEvaluation);
            holder.tvOrderEvaluation.setVisibility(View.GONE);
        } else {
            GlideUtils.loadImage(context, R.mipmap.grid_camera, holder.imgOrderEvaluation);
            holder.tvOrderEvaluation.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imgOrderEvaluation;
        TextView tvOrderEvaluation;
    }
}

