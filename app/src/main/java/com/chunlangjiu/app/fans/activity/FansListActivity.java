package com.chunlangjiu.app.fans.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.bean.ListBean;
import com.chunlangjiu.app.fans.bean.FansBean;
import com.chunlangjiu.app.fans.bean.FansCodeBean;
import com.chunlangjiu.app.fans.bean.FansItemBean;
import com.chunlangjiu.app.fans.bean.FansNumBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ShareUtils;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FansListActivity extends BaseActivity {

    private View llHead;
    private TextView tvFansNum;
    private TextView tvTotalPrice;
    private RecyclerView rvFansList;
    private TextView tvShare;

    private List<FansItemBean> list;
    private FansListAdapter fansAdapter;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_list);
        initView();
        initData();
    }

    private void initView() {
        disposable = new CompositeDisposable();


        llHead = getLayoutInflater().inflate(R.layout.fans_list_head, null);
        tvFansNum = llHead.findViewById(R.id.tvFansNum);
        tvTotalPrice = llHead.findViewById(R.id.tvTotalPrice);
        rvFansList = findViewById(R.id.rv_fans_list);
        tvShare = findViewById(R.id.tvShare);
        tvShare.setOnClickListener(onClickListener);
        rvFansList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list = new ArrayList<>();
        fansAdapter = new FansListAdapter(R.layout.fans_list_item, list);
        fansAdapter.addHeaderView(llHead);
        rvFansList.setAdapter(fansAdapter);
        fansAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvFansList.getParent(), false));
    }

    private void initData() {
        loadFansSum();
        loadFansList();
    }

    private void loadFansList() {
        //获取粉丝信息
        disposable.add(ApiUtils.getInstance().getFansList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ListBean<FansItemBean>>>() {
                    @Override
                    public void accept(ResultBean<ListBean<FansItemBean>> result) throws Exception {
                        if (result != null && result.getData() != null && null != result.getData().getList()) {
                            list.clear();
                            list.addAll(result.getData().getList());
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
                        if (result != null && result.getData() != null) {
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
            helper.setText(R.id.tv_fans_name, item.getName());
            helper.setText(R.id.tv_fans_phone, item.getMobile());
            helper.setText(R.id.tv_register_time, TimeUtils.millisToYearMD(item.getCreatetime() + "000"));
            helper.setText(R.id.tv_money, item.getCommission());
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if (resId == R.id.img_title_left) {
                finish();
            } else if (resId == R.id.tvShare) {
                showShare();
            }
        }
    };

    private void startFansInviteActivity() {
        String url = getIntent().getStringExtra("shareUrl");
        String title = getString(R.string.fans_register_app);
        WebViewActivity.startWebViewActivity(this, url, title);
    }

    private void showShare() {
        FansCodeBean fansBean = (FansCodeBean) getIntent().getSerializableExtra("fansBean");
        if (null != fansBean) {
            UMImage thumb = new UMImage(this, R.mipmap.launcher);
            UMWeb web = new UMWeb(fansBean.getUrl());
            web.setTitle(fansBean.getTitle());//标题
            web.setThumb(thumb);  //缩略图
            web.setDescription(fansBean.getSub_title());//描述

            ShareUtils.shareLink(this, web, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                }
            });

        }
    }

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
