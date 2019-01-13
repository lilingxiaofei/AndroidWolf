package com.chunlangjiu.app.goods.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.fans.bean.FansItemBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PartnerListActivity extends BaseActivity {

    private RecyclerView rvFansList;

    private List<FansItemBean> list ;
    private FansListAdapter fansAdapter  ;

    private CompositeDisposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_activity_list);
        initView();
        initData();
    }
    private void initView(){
        disposable = new CompositeDisposable();
        rvFansList = findViewById(R.id.rv_fans_list);
        list = new ArrayList<>();
        fansAdapter = new FansListAdapter(R.layout.fans_list_item,list);
        rvFansList.setAdapter(fansAdapter);
    }

    private void initData(){
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
        testData();
    }

    private void testData(){
        for (int i = 0; i < 7; i++) {
            FansItemBean bean = new FansItemBean();
            bean.setFansName("李伟"+i);
            bean.setPhone("1234567899"+i);
            bean.setRegisterTime("2018-12-0"+i);
            bean.setTotalMoney("88"+i);
            list.add(bean);
        }
        fansAdapter.setNewData(list);
    };


    class FansListAdapter extends BaseQuickAdapter<FansItemBean, BaseViewHolder> {
        public FansListAdapter(int layoutResId, List<FansItemBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FansItemBean item) {
            helper.setText(R.id.tv_fans_name,item.getFansName());
            helper.setText(R.id.tv_fans_phone,item.getPhone());
            helper.setText(R.id.tv_register_time,item.getRegisterTime());
            helper.setText(R.id.tv_money,item.getTotalMoney());
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
