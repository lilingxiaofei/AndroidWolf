package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsPicAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定结果
 */
public class AppraiseAssessActivity extends BaseActivity {


    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etColour)
    EditText etColour;
    @BindView(R.id.etFlaw)
    EditText etFlaw;
    @BindView(R.id.etAccessory)
    EditText etAccessory;
    @BindView(R.id.etOtherHelp)
    EditText etOtherHelp;

    //商品UI

    @BindView(R.id.tvGoodsTitle)
    TextView tvGoodsTitle;
    @BindView(R.id.tvGoodsSeries)
    TextView tvGoodsSeries;
    @BindView(R.id.tvGoodsYear)
    TextView tvGoodsYear;
    @BindView(R.id.tvGoodsExplain)
    TextView tvGoodsExplain;
    @BindView(R.id.rvPicList)
    RecyclerView rvPicList;

    //快速提现UI
    @BindView(R.id.llCommit)
    LinearLayout llCommit;


    private List<String> picList = new ArrayList<>();
    AppraiseGoodsPicAdapter picAdapter;

    private String appraiseGoodsId;
    private AppraiseGoodsBean appraiseGoodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraiser_activity_to_appraise);
        initView();
        initData();
    }


    public static void startAppraiserAssessActivity(Activity activity, String appraiseGoodsId) {
        if (activity != null) {
            Intent intent = new Intent(activity, AppraiseAssessActivity.class);
            intent.putExtra("appraiseGoodsId", appraiseGoodsId);
            activity.startActivity(intent);
        }
    }

    private void initView() {
        //判断不能以0开头的就截取
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String temp = charSequence.toString();
                if (temp.startsWith("0") && !temp.startsWith("0.") && temp.length()>1) {
                    temp = temp.substring(1);
                    etPrice.setText(temp);
                    CommonUtils.requestFocus(etPrice);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        llCommit.setOnClickListener(onClickListener);
    }

    private void initData() {
        appraiseGoodsId = getIntent().getStringExtra("appraiseGoodsId");
        loadAppraiserGoodsDetails();
    }


    private void loadAppraiserGoodsDetails() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getAppraiseGoodsDetails(appraiseGoodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AppraiseGoodsBean>>() {
                    @Override
                    public void accept(ResultBean<AppraiseGoodsBean> result) throws Exception {
                        hideLoadingDialog();
                        appraiseGoodsBean = result.getData();
                        updateView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    @Override
    public void setTitleView() {
        titleName.setText("名酒鉴别");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private void updateView() {
        if (appraiseGoodsBean != null) {
            //商品UI
            tvGoodsTitle.setText(CommonUtils.getString(R.string.appraise_goods_title, appraiseGoodsBean.getTitle()));
            tvGoodsSeries.setText(CommonUtils.getString(R.string.appraise_goods_series, appraiseGoodsBean.getSeries()));
            tvGoodsYear.setText(CommonUtils.getString(R.string.appraise_goods_year, appraiseGoodsBean.getYear()));
            tvGoodsExplain.setText(CommonUtils.getString(R.string.appraise_goods_other, appraiseGoodsBean.getDetails()));
            if (!TextUtils.isEmpty(appraiseGoodsBean.getImg())) {
                picList = Arrays.asList(appraiseGoodsBean.getImg().split(","));
                picAdapter = new AppraiseGoodsPicAdapter(this, picList);
                rvPicList.setLayoutManager(new GridLayoutManager(this, 3));
                rvPicList.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 3), false));
                rvPicList.setAdapter(picAdapter);
            }

//            //快速提现UI
//            if("true".equals(appraiseGoodsBean.getStatus())){
//                llAppraiseSource.setVisibility(View.VISIBLE);
//                llAppraisePrice.setVisibility(View.VISIBLE);
//                llAppraiseDetails.setVisibility(View.VISIBLE);
//                tvAppraiseTips.setVisibility(View.VISIBLE);
//                llCommit.setVisibility(View.VISIBLE);
//                llCommit.setOnClickListener(onClickListener);
//                tvCommitPrice.setText(CommonUtils.getString(R.string.rmb_two,appraiseGoodsBean.getPrice()));
//            }else{
//                llCommit.setVisibility(View.GONE);
//                llAppraiseSource.setVisibility(View.GONE);
//                llAppraisePrice.setVisibility(View.GONE);
//                llAppraiseDetails.setVisibility(View.GONE);
//                tvAppraiseTips.setVisibility(View.GONE);
//            }

        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.img_title_left) {
                finish();
            } else if (view.getId() == R.id.llCommit) {
                commitAssess();
            }
        }
    };

    private void commitAssess() {

        String price = etPrice.getText().toString();
        String colour = etColour.getText().toString();
        String flaw = etFlaw.getText().toString();
        String accessory = etAccessory.getText().toString();
        String otherHelp = etOtherHelp.getText().toString();
        if (TextUtils.isEmpty(price)) {
            ToastUtils.showShort("请输入价格");
            return;
        } else if (BigDecimalUtils.objToBigDecimal(price).doubleValue() <= 0) {
            ToastUtils.showShort("价格必须大于0");
            return;
        } else if (TextUtils.isEmpty(colour)) {
            ToastUtils.showShort("请输入酒成色");
            return;
        } else if (TextUtils.isEmpty(flaw)) {
            ToastUtils.showShort("请输入瑕疵情况");
            return;
        } else if (TextUtils.isEmpty(accessory)) {
            ToastUtils.showShort("请输入附近情况");
            return;
        } else if (TextUtils.isEmpty(otherHelp)) {
            ToastUtils.showShort("请输入其内容");
            return;
        }


        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().appraiserGoods(appraiseGoodsId, price, colour, flaw, accessory, otherHelp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (resultBean.getErrorcode() == 0) {
                            ToastUtils.showShort("鉴别成功");
                            EventManager.getInstance().notify(null, ConstantMsg.APPRAISE_GOODS_SUCCESS);
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    private void quickCash() {
        startActivity(new Intent(AppraiseAssessActivity.this, QuickCashActivity.class));
    }
}
