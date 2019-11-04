package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AuthInfoBean;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.noober.api.NeedSave;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.`
 * @Describe: 企业认证页面
 */
public class CompanyAuthActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_ONE = 103;
    public static final int REQUEST_CODE_SELECT_TWO = 104;
    public static final int REQUEST_CODE_SELECT_ALLOW_CARD = 105;
    public static final int REQUEST_CODE_SELECT_FOOD_LISENCE = 106;

    @BindView(R.id.etCompany)
    EditText etCompany;
    @BindView(R.id.etPersonName)
    EditText etPersonName;
    @BindView(R.id.edtIdCard)
    EditText edtIdCard;
    @BindView(R.id.imgLicense)
    ImageView imgLicense;
    @BindView(R.id.imgIdCardFront)
    ImageView imgIdCardFront;
    @BindView(R.id.imgIdCardBehind)
    ImageView imgIdCardBehind;
    @BindView(R.id.imgFoodCertificate)
    ImageView imgFoodCertificate;

    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private CompositeDisposable disposable;
    private ChoicePhotoDialog photoDialog;

    @NeedSave
    int codeType;
    @NeedSave
    ArrayList<ImageItem> ZhiZhaoLists;
    @NeedSave
    ArrayList<ImageItem> cardFLists;
    @NeedSave
    ArrayList<ImageItem> cardBLists;
    @NeedSave
    ArrayList<ImageItem> foodLisenceLists;
    @NeedSave
    String base64ZhiZhao;
    @NeedSave
    String urlZhiZhao;
    @NeedSave
    String base64IdCardF;
    @NeedSave
    String urlIdCardF;
    @NeedSave
    String base64IdCardB;
    @NeedSave
    String urlIdCardB;
    @NeedSave
    String base64FoodLicense;
    @NeedSave
    String urlFoodLicense;


    @Override
    public void setTitleView() {
        titleName.setText("企业认证");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_company_auth);
        initImagePicker();
        initView();
        initData();
        setImgPic();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(1000);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(625);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(625);                         //保存文件的高度。单位像素
    }

    private void initView() {
        disposable = new CompositeDisposable();
        String str = getString(R.string.person_one_tips);
        String key = "3.5%";
        int color = ContextCompat.getColor(this, R.color.t_red);
        tvTips.setText(CommonUtils.setSpecifiedTextsColor(str, key, color));
    }

    @OnClick({R.id.img_title_left, R.id.btnLicense, R.id.btnIdCardFront, R.id.btnIdCardBehind, R.id.tvCommit, R.id.btnFoodCertificate})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btnLicense:
                showPhotoDialog(REQUEST_CODE_SELECT_ONE);
                break;
            case R.id.btnIdCardFront:
                showPhotoDialog(REQUEST_CODE_SELECT_TWO);
                break;
            case R.id.btnIdCardBehind:
                showPhotoDialog(REQUEST_CODE_SELECT_ALLOW_CARD);
                break;
            case R.id.btnFoodCertificate:
                showPhotoDialog(REQUEST_CODE_SELECT_FOOD_LISENCE);
                break;
            case R.id.tvCommit:
                checkData();
                break;

        }

    }


    private void initData() {
        getAuthStatus();
    }

    private void getUserInfo() {
        disposable.add(ApiUtils.getInstance().getCompanyAuthInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthInfoBean>>() {
                    @Override
                    public void accept(ResultBean<AuthInfoBean> userInfoBeanResultBean) throws Exception {
                        AuthInfoBean authInfoBean = userInfoBeanResultBean.getData();
                        if (authInfoBean != null) {
                            etCompany.setText(authInfoBean.getCompany_name());
                            etPersonName.setText(authInfoBean.getRepresentative());
                            edtIdCard.setText(authInfoBean.getIdcard());

                            urlZhiZhao = authInfoBean.getLicense_img();
                            urlIdCardF = authInfoBean.getShopuser_identity_img_z();
                            urlIdCardB = authInfoBean.getShopuser_identity_img_f();
                            urlFoodLicense = authInfoBean.getFood_or_wine_img();
                            if (CommonUtils.isNetworkPic(urlZhiZhao)) {
                                GlideUtils.loadImageOptions(CompanyAuthActivity.this, urlZhiZhao, imgLicense);
                            }
                            if (CommonUtils.isNetworkPic(urlIdCardF)) {
                                GlideUtils.loadImageOptions(CompanyAuthActivity.this, urlIdCardF, imgIdCardFront);
                            }
                            if (CommonUtils.isNetworkPic(urlIdCardB)) {
                                GlideUtils.loadImageOptions(CompanyAuthActivity.this, urlIdCardB, imgIdCardBehind);
                            }
                            if (CommonUtils.isNetworkPic(urlFoodLicense)) {
                                GlideUtils.loadImageOptions(CompanyAuthActivity.this, urlFoodLicense, imgFoodCertificate);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getAuthStatus() {
        disposable.add(ApiUtils.getInstance().getCompanyAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        getStatusSuccess(authStatusBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getStatusSuccess(AuthStatusBean data) {
        if (!"active".equals(data.getStatus())) {
            getUserInfo();
        }

        if ("active".equals(data.getStatus())) {
            //未认证
            tvCommit.setText("提交审核");
        } else if ("locked".equals(data.getStatus())) {
            tvCommit.setText("审核中");
            tvCommit.setClickable(false);
            updateView(data);
        } else if ("failing".equals(data.getStatus())) {
            tvCommit.setText("审核未通过，请重新提交资料审核");
            tvCommit.setClickable(true);
            updateView(data);
        } else if (AuthStatusBean.AUTH_MODIFIER.equals(data.getStatus())) {
            tvCommit.setText("更新审核资料");
            tvCommit.setClickable(true);
            updateView(data);
        } else if ("finish".equals(data.getStatus())) {
            tvCommit.setText("更新审核资料");
            tvCommit.setClickable(true);
            updateView(data);
        }
    }

    private void updateView(AuthStatusBean data) {

    }

    private void showPhotoDialog(int requestCode) {
        codeType = requestCode;
        if (photoDialog == null) {
            photoDialog = new ChoicePhotoDialog(this);
            photoDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    openCamera(codeType);
                }

                @Override
                public void toPhotoAlbum() {
                    openAlbum(codeType);
                }
            });
        }
        photoDialog.show();
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, requestCode);
    }

    private void openAlbum(int requestCode) {
        Intent intent1 = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent1, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null) {
                if (requestCode == REQUEST_CODE_SELECT_ONE) {
                    ZhiZhaoLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = ZhiZhaoLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64ZhiZhao = FileUtils.imgToBase64(ZhiZhaoLists.get(0).path);
                    urlZhiZhao = "";
                    setImgPic();
                } else if (requestCode == REQUEST_CODE_SELECT_TWO) {
                    cardFLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = cardFLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64IdCardF = FileUtils.imgToBase64(cardFLists.get(0).path);
                    urlIdCardF = "";
                    setImgPic();
                } else if (requestCode == REQUEST_CODE_SELECT_ALLOW_CARD) {
                    cardBLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = cardBLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64IdCardB = FileUtils.imgToBase64(cardBLists.get(0).path);
                    urlIdCardB = "";
                    setImgPic();
                } else if (requestCode == REQUEST_CODE_SELECT_FOOD_LISENCE) {
                    foodLisenceLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = foodLisenceLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64FoodLicense = FileUtils.imgToBase64(foodLisenceLists.get(0).path);
                    urlFoodLicense = "";
                    setImgPic();
                }
            }
        }
    }

    private void setImgPic() {
        if (!TextUtils.isEmpty(base64ZhiZhao)) {
            GlideUtils.loadImage(CompanyAuthActivity.this, base64ZhiZhao, imgLicense);
        }
        if (!TextUtils.isEmpty(base64IdCardF)) {
            GlideUtils.loadImage(CompanyAuthActivity.this, base64IdCardF, imgIdCardFront);
        }
        if (!TextUtils.isEmpty(base64IdCardB)) {
            GlideUtils.loadImage(CompanyAuthActivity.this, base64IdCardB, imgIdCardBehind);
        }
        if (!TextUtils.isEmpty(base64FoodLicense)) {
            GlideUtils.loadImage(CompanyAuthActivity.this, base64FoodLicense, imgFoodCertificate);
        }
    }

    private void checkData() {
        if (TextUtils.isEmpty(etCompany.getText().toString().trim())) {
            ToastUtils.showShort("请输入企业名称");
        } else if (TextUtils.isEmpty(etPersonName.getText().toString().trim())) {
            ToastUtils.showShort("请输入法人名称");
        } else if (TextUtils.isEmpty(edtIdCard.getText().toString().trim())) {
            ToastUtils.showShort("请输入身份证号");
        }
//        else if (TextUtils.isEmpty(etCardNum.getText().toString().trim())) {
//            ToastUtils.showShort("请输入营业执照");
//        } else if (TextUtils.isEmpty(tvCreateTime.getText().toString())) {
//            ToastUtils.showShort("请选择成立时间");
//        } else if (TextUtils.isEmpty(tvSellArea.getText().toString())) {
//            ToastUtils.showShort("请选择经营区域");
//        } else if (TextUtils.isEmpty(etAddress.getText().toString())) {
//            ToastUtils.showShort("请输入详情地址");
//        } else if (TextUtils.isEmpty(etPhone.getText().toString())) {
//            ToastUtils.showShort("请输入固定电话");
//        }
        else if (base64ZhiZhao == null && !CommonUtils.isNetworkPic(urlZhiZhao)) {
            ToastUtils.showShort("请上传营业执照图片");
        } else if (base64IdCardF == null && !CommonUtils.isNetworkPic(urlIdCardF)) {
            ToastUtils.showShort("请上传法人身份证正面图片");
        } else if (base64IdCardB == null && !CommonUtils.isNetworkPic(urlIdCardB)) {
            ToastUtils.showShort("请上传法人身份证反面图片");
        } else if (base64FoodLicense == null && !CommonUtils.isNetworkPic(urlFoodLicense)) {
            ToastUtils.showShort("请上传食品流通许可证/酒类经营许可证");
        } else {
            uploadImage();

        }
    }

    private void uploadImage() {
        showLoadingDialog();
        List<Observable> uploadList = new ArrayList();
        Observable<ResultBean<UploadImageBean>> front = null;
        if (!TextUtils.isEmpty(base64ZhiZhao) && ZhiZhaoLists != null && ZhiZhaoLists.size() > 0) {
            front = ApiUtils.getInstance().userUploadImage(base64ZhiZhao, ZhiZhaoLists.get(0).name, "rate");

        } else {
            front = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlZhiZhao);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        uploadList.add(front);

        Observable<ResultBean<UploadImageBean>> behind = null;
        if (!TextUtils.isEmpty(base64IdCardF) && cardFLists != null && cardFLists.size() > 0) {
            behind = ApiUtils.getInstance().userUploadImage(base64IdCardF, cardFLists.get(0).name, "rate");
        } else {
            behind = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlIdCardF);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        uploadList.add(behind);


        Observable<ResultBean<UploadImageBean>> allow = null;
        if (!TextUtils.isEmpty(base64IdCardB) && cardBLists != null && cardBLists.size() > 0) {
            allow = ApiUtils.getInstance().userUploadImage(base64IdCardB, cardBLists.get(0).name, "rate");
        } else {
            allow = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlIdCardB);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        uploadList.add(allow);

        Observable<ResultBean<UploadImageBean>> food = null;
        if (!TextUtils.isEmpty(base64FoodLicense) && foodLisenceLists != null && foodLisenceLists.size() > 0) {
            food = ApiUtils.getInstance().userUploadImage(base64FoodLicense, foodLisenceLists.get(0).name, "rate");
        } else {
            food = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlFoodLicense);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        uploadList.add(food);

        disposable.add(Observable.zip(front, behind, allow, food, new Function4<ResultBean<UploadImageBean>, ResultBean<UploadImageBean>, ResultBean<UploadImageBean>, ResultBean<UploadImageBean>, List<String>>() {
            @Override
            public List<String> apply(ResultBean<UploadImageBean> uploadImageBeanResultBean, ResultBean<UploadImageBean> uploadImageBeanResultBean2, ResultBean<UploadImageBean> uploadImageBeanResultBean3, ResultBean<UploadImageBean> uploadImageBeanResultBean4) throws Exception {
                List<String> imageLists = new ArrayList<>();
                imageLists.add(uploadImageBeanResultBean.getData().getUrl());
                imageLists.add(uploadImageBeanResultBean2.getData().getUrl());
                imageLists.add(uploadImageBeanResultBean3.getData().getUrl());
                imageLists.add(uploadImageBeanResultBean4.getData().getUrl());
                return imageLists;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        commitAuth(strings);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("上传图片失败");
                    }
                }));
    }

    private void commitAuth(List<String> strings) {
        disposable.add(ApiUtils.getInstance().companyAuth((String) SPUtils.get("token", ""), etCompany.getText().toString().trim(), etPersonName.getText().toString().trim(), edtIdCard.getText().toString().trim()
                , strings.get(0), strings.get(1), strings.get(2), strings.get(3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("提交成功，请耐心等待审核");
                        EventManager.getInstance().notify(null, ConstantMsg.PERSON_COMPANY_AUTH_SUCCESS);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
