package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class MyInfoEditActivity extends BaseActivity implements View.OnClickListener {
    //个人信息
    @BindView(R.id.etContent)
    TextView etContent;
    @BindView(R.id.rgSex)
    RadioGroup rgSex ;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    private String type ;
    private String content;
    private CompositeDisposable disposable;

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(this);
    }

    /**
     * shopname	 青青贸易测试 string	店铺名称
     bulletin	string	企业简介
     sex	string	性别 0女 1男 2保密
     area	解放路测试  string	店铺地址
     phone
     * @param activity
     * @param type
     * @param content
     */
    public static void startActivity(Activity activity,String title,String type,String content,int requestCode){
        if(activity != null ){
            Intent intent  = new Intent(activity,MyInfoEditActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("type",type);
            intent.putExtra("content",content);
            activity.startActivityForResult(intent,requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_my_info_edit_activity);
        disposable = new CompositeDisposable();
        initData();;
    }

    private void initData(){
        String title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        content = getIntent().getStringExtra("content");
        if("sex".equals(type)){
            rgSex.setVisibility(View.VISIBLE);
            etContent.setVisibility(View.GONE);
            if("0".equals(content)){
                rgSex.check(R.id.rbWoman);
            }else if("1".equals(content)){
                rgSex.check(R.id.rbMan);
            }else{
                rgSex.check(R.id.rbSecrecy);
            }
        }else{
            rgSex.setVisibility(View.GONE);
            etContent.setVisibility(View.VISIBLE);
            etContent.setText(content);
        }


        titleName.setText(title);
    }

    private void editUserInfo() {
        if(etContent.getVisibility() ==View.VISIBLE && TextUtils.isEmpty(etContent.getText().toString())){
            ToastUtils.showShort("修改信息不能为空");
            return;
        }
        String shopname = null , bulletin  = null , sex  = null , area  = null , phone  = null ;
        type = type == null ?"" :type;
        switch (type){
            case "shopname":
                shopname = etContent.getText().toString();
                break;
            case "bulletin":
                bulletin = etContent.getText().toString();
                break;
            case "sex":
                RadioButton button = findViewById(rgSex.getCheckedRadioButtonId());
                sex= button.getTag().toString();
                break;
            case "area":
                area = etContent.getText().toString();
                break;
            case "phone":
                phone = etContent.getText().toString();
                break;
        }

        disposable.add(ApiUtils.getInstance().editUserInfo(shopname,bulletin,sex,area,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        if(resultBean.getErrorcode() == 0){
                            EventManager.getInstance().notify(null, ConstantMsg.USER_DATA_CHANGE);
                            ToastUtils.showShort("修改成功");
                            setResult(RESULT_OK);
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

//    private void getShopInfo(String shopId) {
//        disposable.add(ApiUtils.getInstance().getShopInfo(shopId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResultBean<ShopInfoBean>>() {
//                    @Override
//                    public void accept(ResultBean<ShopInfoBean> shopInfoBeanResultBean) throws Exception {
//                        getShopInfoSuccess(shopInfoBeanResultBean.getData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                    }
//                }));
//    }


    @OnClick({R.id.img_title_left, R.id.tvConfirm})
    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if (resId == R.id.img_title_left) {
            finish();
        } else if (resId == R.id.tvConfirm) {
            editUserInfo();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
