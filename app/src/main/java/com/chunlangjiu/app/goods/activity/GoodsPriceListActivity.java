package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.GivePriceBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.TimeUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/19
 * @Describe:
 */
public class GoodsPriceListActivity extends BaseActivity {

    private String auctionId ;
    private CompositeDisposable disposable;
    private PriceAdapter priceAdapter;
    private List<GivePriceBean> priceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_price_list);
        initView();
        initData();
    }

    public static void startActivity(Activity activity, String auctionId) {
        if (activity != null) {
            Intent intent = new Intent(activity, GoodsPriceListActivity.class);
            intent.putExtra("auctionId",auctionId);
            activity.startActivity(intent);
        }
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText("出价记录");
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        priceAdapter = new PriceAdapter(R.layout.goods_item_auction_price_new, priceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(priceAdapter);
    }

    private void initData(){
        disposable = new CompositeDisposable();
        auctionId = getIntent().getStringExtra("auctionId");
        getPriceList();
    }

    private void getPriceList() {
        disposable.add(ApiUtils.getInstance().getAuctionPriceList(auctionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<GivePriceBean>>>() {
                    @Override
                    public void accept(ResultBean<List<GivePriceBean>> listResultBean) throws Exception {
                        priceList = listResultBean.getData();
                        priceSort();
                        priceAdapter.setNewData(priceList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void priceSort(){
        Collections.sort(priceList, new Comparator<GivePriceBean>() {

            @Override
            public int compare(GivePriceBean o1, GivePriceBean o2) {
                // 按照学生的年龄进行升序排列
                if (BigDecimalUtils.substractObj(o1.getPrice(),o2.getPrice()).doubleValue()>0) {
                    return -1;
                }
                return 1;
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_title_left){
                finish();
            }
        }
    };

    public class PriceAdapter extends BaseQuickAdapter<GivePriceBean, BaseViewHolder> {

        public PriceAdapter(int layoutResId, List<GivePriceBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GivePriceBean item) {
            helper.setText(R.id.tvMobile, item.getMobile());
            if(helper.getLayoutPosition() ==0 ){
                helper.setText(R.id.tvMaxPrice,"领先");
                helper.setTextColor(R.id.tvMaxPrice, ContextCompat.getColor(GoodsPriceListActivity.this,R.color.t_red));
            }else{
                helper.setTextColor(R.id.tvMaxPrice, ContextCompat.getColor(GoodsPriceListActivity.this,R.color.t_gray));
                helper.setText(R.id.tvMaxPrice,"落后");
            }
            helper.setText(R.id.tvTime, TimeUtils.millisToDate(item.getTime()+"000"));
            helper.setText(R.id.tvPrice, "" + "¥" + item.getPrice() + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
