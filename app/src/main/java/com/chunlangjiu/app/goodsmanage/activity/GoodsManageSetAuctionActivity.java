package com.chunlangjiu.app.goodsmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
import com.chunlangjiu.app.goodsmanage.bean.GoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.user.bean.MyNumBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

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
public class GoodsManageSetAuctionActivity extends BaseActivity {


    @BindView(R.id.imgPic)
    ImageView imgPic;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.llLabel)
    LinearLayout llLabel;
    @BindView(R.id.tvSellPrice)
    TextView tvSellPrice;
    @BindView(R.id.tvStock)
    TextView tvStock;

    @BindView(R.id.rbBrightAuction)
    RadioButton rbBrightAuction;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.etStartPrice)
    TextView etStartPrice;
    @BindView(R.id.etNum)
    TextView etNum;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;


    private CompositeDisposable disposable;

    private GoodsBean goodsBean;

    private TimePickerDialog startTimeDialog;
    private long startTime ;
    private TimePickerDialog endTimeDialog;
    private long endTime ;


    public static void startSetAuctionActivity(Activity activity, GoodsBean goodsBean) {
        Intent intent = new Intent(activity, GoodsManageSetAuctionActivity.class);
        intent.putExtra("goodsBean", goodsBean);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivBack:
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvStartTime:
                    showStartTime();
                    break;
                case R.id.tvEndTime:
                    showEndTime();
                    break;
                case R.id.tvSubmit:
                    setAuctionGoods();
                    break;

            }
        }
    };


    public void showStartTime() {
        if (startTimeDialog == null) {
            startTimeDialog = new TimePickerDialog.Builder().setCallBack(startDataSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12).build();
        }
        startTimeDialog.show(getSupportFragmentManager(), "all");
    }


    public void showEndTime() {
        if (endTimeDialog == null) {
            endTimeDialog = new TimePickerDialog.Builder().setCallBack(endDataSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12).build();
        }
        endTimeDialog.show(getSupportFragmentManager(), "all");
    }

    private OnDateSetListener startDataSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            startTime = millSeconds / 1000;
            tvStartTime.setText(TimeUtils.millisToDate(startTime+""));
        }
    };
    private OnDateSetListener endDataSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            endTime = millSeconds / 1000;
            tvEndTime.setText(TimeUtils.millisToDate(endTime+""));
        }
    };

    @Override
    public void setTitleView() {
        titleName.setText("设置竞拍商品");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_goods_manage_set_auction);
        initView();
        initData();
    }


    private void initView() {
        tvSubmit.setOnClickListener(onClickListener);
        tvEndTime.setOnClickListener(onClickListener);
        tvStartTime.setOnClickListener(onClickListener);

        goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodsBean");
        if(goodsBean != null){
            GlideUtils.loadImage(this,goodsBean.getImage_default_id(),imgPic);
            tv_name.setText(goodsBean.getTitle());
            tvSellPrice.setText(goodsBean.getPrice());
            tvStock.setText(goodsBean.getStore());
        }
    }

    private void initData() {
        disposable = new CompositeDisposable();
    }


    private void setAuctionGoods() {
        if(startTime<=0){
            ToastUtils.showShort("请选择竞拍开始时间");
            return ;
        }else if(endTime<=0){
            ToastUtils.showShort("请选择竞拍结束时间");
            return ;
        }else if(endTime<startTime){
            ToastUtils.showShort("开始时间必须早于结束时间");
            return ;
        }else if(BigDecimalUtils.objToBigDecimal(etStartPrice.getText().toString()).doubleValue()<1d){
            ToastUtils.showShort("竞拍金额必须大于1元");
            return ;
        }else if(TextUtils.isEmpty(etNum.getText().toString())){
            ToastUtils.showShort("请输入商品的竞拍数量");
            return ;
        }

        String status = rbBrightAuction.isChecked()?"true":"false";
        disposable.add(ApiUtils.getInstance().setAuctionGoods(goodsBean.getItem_id(), etStartPrice.getText().toString(),status,etNum.getText().toString(),startTime,endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean result) throws Exception {
                        if(result.getErrorcode() ==0){
                            EventManager.getInstance().notify(null, ConstantMsg.SHOP_DATA_CHANGE);
                            ToastUtils.showShort("设置竞拍成功");
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


}
