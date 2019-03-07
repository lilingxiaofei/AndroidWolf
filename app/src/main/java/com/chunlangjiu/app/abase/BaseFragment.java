package com.chunlangjiu.app.abase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.pkqup.commonlibrary.dialog.CommonLoadingDialog;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {

    public View rootView;//Fragment的根view
    public RelativeLayout titleView;//
    public ImageView imgTitleLeftF;
    public ImageView imgTitleRightOneF;
    public ImageView imgTitleRightTwoF;
    public TextView tvTitleF;
    public RelativeLayout titleSearchView;
    public EditText titleSearchEdit;

    public RelativeLayout contentView;//内容
    public RelativeLayout loadingView;//加载
    public RelativeLayout emptyView;//空态
    public RelativeLayout errorView;//加载失败

    public CommonLoadingDialog loadingDialog;


    public Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName()); //统计页面("MainScreen"为页面名称，可自定义)
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.abase_fragment, null);
            findView();
            getContentView(inflater, contentView);
            initView();
            initData();
        }
        return rootView;
    }

    private void findView() {
        titleView = rootView.findViewById(R.id.title_view);
        tvTitleF = rootView.findViewById(R.id.tv_title_f);
        imgTitleLeftF = rootView.findViewById(R.id.img_title_left_f);
        imgTitleRightOneF = rootView.findViewById(R.id.img_title_right_one_f);
        imgTitleRightTwoF = rootView.findViewById(R.id.img_title_right_two_f);
        titleSearchView = rootView.findViewById(R.id.rl_title_search);
        titleSearchEdit = rootView.findViewById(R.id.et_title_search);
        titleView.setVisibility(View.GONE);

        contentView = rootView.findViewById(R.id.content_view);
        loadingView = rootView.findViewById(R.id.loading_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        errorView = rootView.findViewById(R.id.error_view);
    }

    public abstract void getContentView(LayoutInflater inflater, ViewGroup container);

    public abstract void initView();

    public abstract void initData();

    public void showContentView() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showLoadingView() {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showEmptyView() {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    public void showErrorView() {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    public void showLoadingDialog() {
        if (null == loadingDialog) {
            loadingDialog = new CommonLoadingDialog(getActivity());
        }
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }
}
