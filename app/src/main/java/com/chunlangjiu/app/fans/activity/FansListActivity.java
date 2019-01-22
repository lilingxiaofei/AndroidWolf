package com.chunlangjiu.app.fans.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.fans.bean.FansItemBean;
import com.chunlangjiu.app.fans.bean.FansNumBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FansListActivity extends BaseActivity {

    private View llHead ;
    private TextView tvFansNum ;
    private TextView tvTotalPrice ;
    private RecyclerView rvFansList;

    private List<FansItemBean> list ;
    private FansListAdapter fansAdapter  ;

    private CompositeDisposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_list);
        initView();
        initData();
    }
    private void initView(){
        disposable = new CompositeDisposable();


        llHead = getLayoutInflater().inflate(R.layout.fans_list_head,null);
        tvFansNum = llHead.findViewById(R.id.tvFansNum);
        tvTotalPrice = llHead.findViewById(R.id.tvTotalPrice);
        rvFansList = findViewById(R.id.rv_fans_list);
        rvFansList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = new ArrayList<>();
        fansAdapter = new FansListAdapter(R.layout.fans_list_item,list);
        fansAdapter.addHeaderView(llHead);
        rvFansList.setAdapter(fansAdapter);
    }

    private void initData(){
        loadFansSum();
        loadFansList();
    }

    private void loadFansList() {
        //获取粉丝信息
        disposable.add(ApiUtils.getInstance().getFansList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<FansItemBean>>>() {
                    @Override
                    public void accept(ResultBean<List<FansItemBean>> result) throws Exception {
                        if(result!=null && result.getData()!=null){
                            list.clear();
                            list.addAll(result.getData());
                            fansAdapter.setNewData(list);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }


    private void loadFansSum() {
        //获取粉丝信息
        disposable.add(ApiUtils.getInstance().getFansInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FansNumBean>>() {
                    @Override
                    public void accept(ResultBean<FansNumBean> result) throws Exception {
                        if(result!=null && result.getData()!=null){
                            tvFansNum.setText(result.getData().getFans_sum());
                            tvTotalPrice.setText(result.getData().getCommission_sum());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }


    class FansListAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {
        public FansListAdapter(int layoutResId, List<FansItemBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FansItemBean item) {
            helper.setText(R.id.tv_fans_name,item.getName());
            helper.setText(R.id.tv_fans_phone,item.getMobile());
            helper.setText(R.id.tv_register_time,item.getCreatetime());
            helper.setText(R.id.tv_money,item.getCommission());
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if(resId == R.id.img_title_left){
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText(R.string.fans_list);
    }
}
