package com.chunlangjiu.app.amain.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.bean.FirstClassBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.amain.bean.SecondClassBean;
import com.chunlangjiu.app.amain.bean.ThirdClassBean;
import com.chunlangjiu.app.goods.activity.GoodsListNewActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class ClassifyFragment extends BaseFragment {

    private Activity activity;
    //分类列表
    private RecyclerView recyclerViewClass;
    private ClassAdapter classAdapter;
    private List<ThirdClassBean> classLists;
    private String selectClassId = "";

    private CompositeDisposable disposable;



    public static ClassifyFragment newInstance() {
        Bundle bundle = new Bundle();
        ClassifyFragment goodsFragment = new ClassifyFragment();
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity)context;
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.goods_fragment_classify, container, true);
    }

    private void initTitleView() {
        titleView.setVisibility(View.VISIBLE);
        tvTitleF.setText(R.string.classify);
    }

    @Override
    public void initView() {
        MyStatusBarUtils.setHomeFragment(getActivity(),rootView.findViewById(R.id.title_view));
        initTitleView();

        disposable = new CompositeDisposable();

        recyclerViewClass = rootView.findViewById(R.id.recyclerViewClass);
        classLists = new ArrayList<>();
//        ThirdClassBean thirdClassBean = new ThirdClassBean();
//        thirdClassBean.setCat_id("");
//        thirdClassBean.setCat_name("全部");
//        classLists.add(thirdClassBean);
        classAdapter = new ClassAdapter(R.layout.amain_item_classify, classLists);
        recyclerViewClass.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerViewClass.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(activity, 10), false));
        recyclerViewClass.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UmengEventUtil.search_category(getActivity(), classLists.get(position).getCat_name());
                selectClassId = classLists.get(position).getCat_id();
                String classifyName = classLists.get(position).getCat_name();
                GoodsListNewActivity.startGoodsListNewActivity(activity,selectClassId,classifyName,"","","");
//                classAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void initData() {
        getClassData();
    }


    private void getClassData() {
        disposable.add(ApiUtils.getInstance().getMainClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<MainClassBean>>() {
                    @Override
                    public void accept(ResultBean<MainClassBean> mainClassBean) throws Exception {
                        classLists.clear();
//                        ThirdClassBean thirdClassBean = new ThirdClassBean();
//                        thirdClassBean.setCat_id("");
//                        thirdClassBean.setCat_name("全部");
//                        classLists.add(thirdClassBean);
                        List<FirstClassBean> categorys = mainClassBean.getData().getCategorys();
                        for (int i = 0; i < categorys.size(); i++) {
                            List<SecondClassBean> lv2 = categorys.get(i).getLv2();
                            for (int j = 0; j < lv2.size(); j++) {
                                List<ThirdClassBean> lv3 = lv2.get(j).getLv3();
                                classLists.addAll(lv3);
                            }
                        }
                        classAdapter.setNewData(classLists);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }



    public class ClassAdapter extends BaseQuickAdapter<ThirdClassBean, BaseViewHolder> {
        public ClassAdapter(int layoutResId, List<ThirdClassBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ThirdClassBean item) {
            RoundedImageView rivPic = helper.getView(R.id.riv_pic);
            GlideUtils.loadImage(activity, item.getCat_logo(), rivPic);
            setPicSize(rivPic);

            TextView tvClass = helper.getView(R.id.tvClass);
            tvClass.setText(item.getCat_name());
            tvClass.setSelected(item.getCat_id().equals(selectClassId));
        }

        private void setPicSize(RoundedImageView rivPic){
            int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(30)) / 2;
            int picSize = rivPic.getWidth();
            if(picSize!=picWidth){
                ViewGroup.LayoutParams layoutParams = rivPic.getLayoutParams();
                layoutParams.width = picWidth;
                layoutParams.height = picWidth;
                rivPic.setLayoutParams(layoutParams);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
