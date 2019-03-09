package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;

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
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    //个人信息
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.rlStoreIntro)
    RelativeLayout rlStoreIntro;
    @BindView(R.id.tvStoreName)
    TextView tvStoreName;
    @BindView(R.id.etStoreIntro)
    TextView etStoreIntro;
    @BindView(R.id.rlStoreAddress)
    RelativeLayout rlStoreAddress;
    @BindView(R.id.etStoreAddress)
    TextView etStoreAddress;
    @BindView(R.id.tvTel)
    TextView tvTel;

    //个人认证
    @BindView(R.id.llPersonInfo)
    LinearLayout llPersonInfo;
    @BindView(R.id.tvRealName)
    TextView tvRealName;
    @BindView(R.id.tvIDType)
    TextView tvIDType;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.tvNotPersonInfo)
    TextView tvNotPersonInfo;

    //企业认证资料
    @BindView(R.id.llCompanyInfo)
    LinearLayout llCompanyInfo;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvLegalPerson)
    TextView tvLegalPerson;
    @BindView(R.id.tvBusinessLicense)
    TextView tvBusinessLicense;
    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime;
    @BindView(R.id.tvBusinessAddress)
    TextView tvBusinessAddress;
    @BindView(R.id.tvCompanyPhone)
    TextView tvCompanyPhone;
    @BindView(R.id.tvNotCompanyInfo)
    TextView tvNotCompanyInfo;

    public final int REQUEST_CODE_CHOICE_HEAD = 1001;

    private ChoicePhotoDialog photoDialog;
    private CompositeDisposable disposable;
    private UserInfoBean userInfoBean;

    @Override
    public void setTitleView() {
        titleName.setText("我的资料");
        titleImgLeft.setOnClickListener(this);
    }


    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MyInfoActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_my_info_activity);
        disposable = new CompositeDisposable();
        getUserInfo();
    }

    private void getUserInfo() {
        disposable.add(ApiUtils.getInstance().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UserInfoBean>>() {
                    @Override
                    public void accept(ResultBean<UserInfoBean> userInfoBeanResultBean) throws Exception {
                        userInfoBean = userInfoBeanResultBean.getData();
                        updateUserView();
//                        getShopInfo(userInfoBean.getShop_id());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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

    private void updateUserView() {
        if (userInfoBean != null) {
            GlideUtils.loadImageHead(this, userInfoBean.getHead_portrait(), imgHead);
            tvUsername.setText(userInfoBean.getLogin_account());
            tvSex.setText(CommonUtils.getSexStr(userInfoBean.getSex()));
            tvStoreName.setText(userInfoBean.getShop_name());
            etStoreIntro.setText(userInfoBean.getBulletin());
            etStoreAddress.setText(userInfoBean.getArea());
            tvTel.setText(userInfoBean.getPhone());
            SPUtils.put("account", userInfoBean.getLogin_account());
            SPUtils.put("avator", userInfoBean.getHead_portrait());

            //个人认证
            tvRealName.setText(userInfoBean.getName());
            tvIDType.setText("身份证");
            tvIdCard.setText(userInfoBean.getIdcard());
            if (!TextUtils.isEmpty(userInfoBean.getIdcard())) {
                llPersonInfo.setVisibility(View.VISIBLE);
                tvNotPersonInfo.setVisibility(View.GONE);
            } else {
                llPersonInfo.setVisibility(View.GONE);
                tvNotPersonInfo.setVisibility(View.VISIBLE);
            }

            //企业认证资料
            tvCompanyName.setText(userInfoBean.getCompany_name());
            tvLegalPerson.setText(userInfoBean.getRepresentative());
            tvBusinessLicense.setText(userInfoBean.getCompany_area());
            tvCreateTime.setText(TimeUtils.millisToDate(userInfoBean.getEstablish_date()));
            tvBusinessAddress.setText(userInfoBean.getCompany_area());
            tvCompanyPhone.setText(userInfoBean.getCompany_phone());
            if (BigDecimalUtils.objToBigDecimal(userInfoBean.getEstablish_date()).longValue() > 0 && !TextUtils.isEmpty(userInfoBean.getCompany_name())) {
                llCompanyInfo.setVisibility(View.VISIBLE);
                tvNotCompanyInfo.setVisibility(View.GONE);
            } else {
                llCompanyInfo.setVisibility(View.GONE);
                tvNotCompanyInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.img_title_left, R.id.imgHead, R.id.tvSex, R.id.tvStoreName, R.id.rlStoreIntro, R.id.rlStoreAddress, R.id.tvTel})
    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if (resId == R.id.img_title_left) {
            finish();
        } else if (resId == R.id.imgHead) {
            if (photoDialog == null) {
                photoDialog = new ChoicePhotoDialog(MyInfoActivity.this);
                photoDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                    @Override
                    public void takePhoto() {
                        initImagePicker();
                        openCamera(REQUEST_CODE_CHOICE_HEAD);
                    }

                    @Override
                    public void toPhotoAlbum() {
                        initImagePicker();
                        openAlbum(REQUEST_CODE_CHOICE_HEAD);
                    }
                });
            }
            photoDialog.show();
        } else if (resId == R.id.tvSex) {
            MyInfoEditActivity.startActivity(MyInfoActivity.this,"修改性别","sex",userInfoBean.getSex(),1000);
        } else if (resId == R.id.tvStoreName) {
            MyInfoEditActivity.startActivity(MyInfoActivity.this,"修改店铺名称","shopname",userInfoBean.getShop_name(),1000);
        } else if (resId == R.id.rlStoreIntro) {
            MyInfoEditActivity.startActivity(MyInfoActivity.this,"修改店铺简介","bulletin",userInfoBean.getBulletin(),1000);
        } else if (resId == R.id.rlStoreAddress) {
            MyInfoEditActivity.startActivity(MyInfoActivity.this,"修改店铺地址","area",userInfoBean.getArea(),1000);
        } else if (resId == R.id.tvTel) {
            MyInfoEditActivity.startActivity(MyInfoActivity.this,"修改联系方式","phone",userInfoBean.getPhone(),1000);
        }

    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(300);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(300);                         //保存文件的高度。单位像素
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, requestCode);
    }

    private void openAlbum(int requestCode) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(resultCode == Activity.RESULT_OK && requestCode == 1000){
                getUserInfo();
            }else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == REQUEST_CODE_CHOICE_HEAD) {
                        ArrayList<ImageItem> mainPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = mainPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        String base64Head = FileUtils.imgToBase64(mainPicLists.get(0).path);
                        uploadHeadIcon(mainPicLists, base64Head);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadHeadIcon(ArrayList<ImageItem> mainPicLists, String base64Head) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().userUploadImage(base64Head, mainPicLists.get(0).name, "rate")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UploadImageBean>>() {
                    @Override
                    public void accept(ResultBean<UploadImageBean> uploadImageBeanResultBean) throws Exception {
                        setHeadUrl(uploadImageBeanResultBean.getData().getUrl());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("上传头像失败");
                    }
                }));
    }

    private void setHeadUrl(final String url) {
        disposable.add(ApiUtils.getInstance().setHeadImg(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        GlideUtils.loadImageHead(MyInfoActivity.this, url, imgHead);
                        EventManager.getInstance().notify(null, ConstantMsg.USER_DATA_CHANGE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("设置头像失败");
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
