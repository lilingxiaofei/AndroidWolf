package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.ThirdClassBean;
import com.chunlangjiu.app.goods.adapter.GoodsAdapter;
import com.chunlangjiu.app.goods.bean.AlcListBean;
import com.chunlangjiu.app.goods.bean.AreaListBean;
import com.chunlangjiu.app.goods.bean.BrandsListBean;
import com.chunlangjiu.app.goods.bean.GoodsListBean;
import com.chunlangjiu.app.goods.bean.GoodsListDetailBean;
import com.chunlangjiu.app.goods.bean.OrdoListBean;
import com.chunlangjiu.app.goods.bean.PriceBean;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.user.dialog.ChoiceAlcPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceAreaPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceBrandPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceOrdoPopWindow;
import com.chunlangjiu.app.user.dialog.ChoicePricePopWindow;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.lzy.widget.HeaderViewPager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.KeyBoardUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.MyHeaderRecycleView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/5
 * @Describe: 卖家主页
 */
public class FestivalActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private View notDataView;

    private CompositeDisposable disposable;
    private List<GoodsListDetailBean> lists;
    private GoodsAdapter goodsAdapter;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, FestivalActivity.class);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivBack:
                    finish();
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        hideTitleView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_festival);
        initFilterView();
        initView();
        initData();
    }

    private void initFilterView() {
        findViewById(R.id.ivBack).setOnClickListener(onClickListener);
    }


    private void initView() {
        MyStatusBarUtils.setStatusBar(this,ContextCompat.getColor(this, R.color.bg_red));
        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.llRootLayout),true);

        lists = new ArrayList<>();
        goodsAdapter = new GoodsAdapter(this, lists);
        goodsAdapter.setShowStoreView(false);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListDetailBean details = lists.get(position);
                GoodsDetailslNewActivity.startActivity(FestivalActivity.this,details.getItem_id());
            }
        });

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        GridSpacingItemDecoration decoration2 = new GridSpacingItemDecoration(1, Utils.dp2px(this, 5), false);
        recycleView.addItemDecoration(decoration2);
        recycleView.setAdapter(goodsAdapter);

        notDataView = getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recycleView.getParent(), false);
    }

    private void initData() {
        disposable = new CompositeDisposable();
    }


    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
