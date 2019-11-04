package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe: 个人认证页面
 */
public class PersonAuthActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_FRONT = 100;
    public static final int REQUEST_CODE_SELECT_BEHIND = 101;
    public static final int REQUEST_CODE_SELECT_PERSON = 102;

    @BindView(R.id.etName)
    EditText etName;
    //    @BindView(R.id.rlChoiceClass)
//    RelativeLayout rlChoiceClass;
//    @BindView(R.id.tvClass)
//    TextView tvClass;
    @BindView(R.id.etCardNum)
    EditText etCardNum;
    @BindView(R.id.etPhone)
    EditText etPhone;

//    @BindView(R.id.llPicOne)
//    LinearLayout llPicOne;
//    @BindView(R.id.llPicTwo)
//    LinearLayout llPicTwo;

    //    @BindView(R.id.rlFront)
//    RelativeLayout rlFront;
//    @BindView(R.id.rlBehind)
//    RelativeLayout rlBehind;
//    @BindView(R.id.rlPerson)
//    RelativeLayout rlPerson;
//    @BindView(R.id.rlFour)
//    RelativeLayout rlFour;
//    @BindView(R.id.imgFrontPic)
//    ImageView imgFrontPic;
//    @BindView(R.id.imgBehindPic)
//    ImageView imgBehindPic;
//    @BindView(R.id.imgCardPic)
//    ImageView imgCardPic;
    @BindView(R.id.imgFront)
    ImageView imgFront;
    @BindView(R.id.imgBehind)
    ImageView imgBehind;
    @BindView(R.id.imgPerson)
    ImageView imgPerson;

    @BindView(R.id.tvTips)
    TextView tvTips;

    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private CompositeDisposable disposable;
    private ChoicePhotoDialog frontDialog;
    private ChoicePhotoDialog behindDialog;
    private ChoicePhotoDialog personDialog;

    @NeedSave
    ArrayList<ImageItem> frontLists;
    @NeedSave
    ArrayList<ImageItem> behindLists;
    @NeedSave
     ArrayList<ImageItem> personLists;
    @NeedSave
     String base64HandCard;
    @NeedSave
     String urlHandCard;
    @NeedSave
     String base64Front;
    @NeedSave
     String urlFront;
    @NeedSave
     String base64Reverse;
    @NeedSave
     String urlReverse;
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.img_title_left:
//                    finish();
//                    break;
//                case R.id.rlFront:
//                    showFrontDialog();
//                    break;
//                case R.id.rlBehind:
//                    showBehindDialog();
//                    break;
//                case R.id.rlPerson:
//                    showPersonDialog();
//                    break;
//                case R.id.tvCommit:
//                    checkData();
//                    break;
//            }
//        }
//    };

    @OnClick({R.id.img_title_left, R.id.btnFront, R.id.btnBehind, R.id.btnPerson, R.id.tvCommit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btnFront:
                showFrontDialog();
                break;
            case R.id.btnBehind:
                showBehindDialog();
                break;
            case R.id.btnPerson:
                showPersonDialog();
                break;
            case R.id.tvCommit:
                checkData();
                break;
        }
    }


    @Override
    public void setTitleView() {
        titleName.setText("个人认证信息");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_person_auth);
        initImagePicker();
        initView();
        initData();
        setImgPic();

    }

    private void initImagePicker() {
//        int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(30)) / 2;
//        int picHeight = (picWidth / 8) * 5;
//        ViewGroup.LayoutParams layoutParamsOne = rlFront.getLayoutParams();
//        layoutParamsOne.width = picWidth;
//        layoutParamsOne.height = picHeight;
//        rlFront.setLayoutParams(layoutParamsOne);
//
//        ViewGroup.LayoutParams layoutParamsTwo = rlBehind.getLayoutParams();
//        layoutParamsTwo.width = picWidth;
//        layoutParamsTwo.height = picHeight;
//        rlBehind.setLayoutParams(layoutParamsTwo);
//
//        ViewGroup.LayoutParams layoutParamsThree = rlPerson.getLayoutParams();
//        layoutParamsThree.width = picWidth;
//        layoutParamsThree.height = picHeight;
//        rlPerson.setLayoutParams(layoutParamsThree);
//
//        ViewGroup.LayoutParams layoutParamsFour = rlFour.getLayoutParams();
//        layoutParamsFour.width = picWidth;
//        layoutParamsFour.height = picHeight;
//        rlFour.setLayoutParams(layoutParamsFour);

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
//        rlFront.setOnClickListener(onClickListener);
//        rlBehind.setOnClickListener(onClickListener);
//        rlPerson.setOnClickListener(onClickListener);
//        tvCommit.setOnClickListener(onClickListener);
        String str  = getString(R.string.person_one_tips);
        String key = "3.5%";
        int color = ContextCompat.getColor(this,R.color.t_red);
        tvTips.setText(CommonUtils.setSpecifiedTextsColor(str,key,color));
    }

    private void initData() {
        getStatus();
    }

    private void getUserInfo() {
        disposable.add(ApiUtils.getInstance().getPersonAuthInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthInfoBean>>() {
                    @Override
                    public void accept(ResultBean<AuthInfoBean> userInfoBeanResultBean) throws Exception {
                        AuthInfoBean authInfoBean = userInfoBeanResultBean.getData();
                        if (authInfoBean != null) {
                            etCardNum.setText(authInfoBean.getIdcard());
                            etName.setText(authInfoBean.getName());
                            etPhone.setText(authInfoBean.getMobile());

                            urlHandCard = authInfoBean.getDentity();
                            urlFront = authInfoBean.getDentity_front();
                            urlReverse = authInfoBean.getDentity_reverse();
                            if (CommonUtils.isNetworkPic(urlHandCard)) {
                                GlideUtils.loadImageOptions(PersonAuthActivity.this, urlHandCard, imgPerson);
                            }
                            if (CommonUtils.isNetworkPic(urlFront)) {
                                GlideUtils.loadImageOptions(PersonAuthActivity.this, urlFront, imgFront);
                            }
                            if (CommonUtils.isNetworkPic(urlReverse)) {
                                GlideUtils.loadImageOptions(PersonAuthActivity.this, urlReverse, imgBehind);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getStatus() {
        disposable.add(ApiUtils.getInstance().getPersonAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        getStatusSuccess(authStatusBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws  Exception {
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


    private void showFrontDialog() {
        if (frontDialog == null) {
            frontDialog = new ChoicePhotoDialog(this);
            frontDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    openCamera(REQUEST_CODE_SELECT_FRONT);
                }

                @Override
                public void toPhotoAlbum() {
                    openAlbum(REQUEST_CODE_SELECT_FRONT);
                }
            });
        }
        frontDialog.show();
    }


    private void showBehindDialog() {
        if (behindDialog == null) {
            behindDialog = new ChoicePhotoDialog(this);
            behindDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    openCamera(REQUEST_CODE_SELECT_BEHIND);
                }

                @Override
                public void toPhotoAlbum() {
                    openAlbum(REQUEST_CODE_SELECT_BEHIND);
                }
            });
        }
        behindDialog.show();
    }

    private void showPersonDialog() {
        if (personDialog == null) {
            personDialog = new ChoicePhotoDialog(this);
            personDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    openCamera(REQUEST_CODE_SELECT_PERSON);
                }

                @Override
                public void toPhotoAlbum() {
                    openAlbum(REQUEST_CODE_SELECT_PERSON);
                }
            });
        }
        personDialog.show();
    }

    private void openCamera(int requestCode) {
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, requestCode);
    }

    private void openAlbum(int requestCode) {
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent1 = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent1, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null) {
                if (requestCode == REQUEST_CODE_SELECT_FRONT) {
                    frontLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = frontLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64Front = FileUtils.imgToBase64(frontLists.get(0).path);
//                    GlideUtils.loadImage(PersonAuthActivity.this, frontLists.get(0).path, imgFrontPic);
                    setImgPic();
                } else if (requestCode == REQUEST_CODE_SELECT_BEHIND) {
                    behindLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = behindLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64Reverse = FileUtils.imgToBase64(behindLists.get(0).path);
//                    GlideUtils.loadImage(PersonAuthActivity.this, behindLists.get(0).path, imgBehindPic);
                    setImgPic();
                } else if (requestCode == REQUEST_CODE_SELECT_PERSON) {
                    personLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = personLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64HandCard = FileUtils.imgToBase64(personLists.get(0).path);
//                    GlideUtils.loadImage(PersonAuthActivity.this, personLists.get(0).path, imgCardPic);
                    setImgPic();
                }
            }
        }
    }

    private void setImgPic(){
        if(frontLists!=null && frontLists.get(0).path!=null){
            GlideUtils.loadImage(PersonAuthActivity.this, frontLists.get(0).path, imgFront);
        }
        if(behindLists!=null && behindLists.get(0).path!=null){
            GlideUtils.loadImage(PersonAuthActivity.this, behindLists.get(0).path, imgBehind);
        }
        if(personLists!=null && personLists.get(0).path!=null){
            GlideUtils.loadImage(PersonAuthActivity.this, personLists.get(0).path, imgPerson);
        }
        Log.d("setImgPicbase64Front",base64Front+"-----");
        Log.d("setImgPicbase64HandCard",base64HandCard+"-----");
        Log.d("setImgPicbase64Reverse",base64Reverse+"-----");

    }

    private void checkData() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtils.showShort("请填写名字");
        } else if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtils.showShort("请填手机号码");
        } else if (TextUtils.isEmpty(etCardNum.getText().toString().trim())) {
            ToastUtils.showShort("请填写身份证号码");
        } else if (base64Front == null && !CommonUtils.isNetworkPic(urlFront)) {
            ToastUtils.showShort("请上传身份证正面图片");
        } else if (base64Reverse == null && !CommonUtils.isNetworkPic(urlReverse)) {
            ToastUtils.showShort("请上传身份证反面图片");
        } else if (base64HandCard == null && !CommonUtils.isNetworkPic(urlHandCard)) {
            ToastUtils.showShort("请上传手持身份证图片");
        } else {
            uploadImage();
        }
    }


    private void uploadImage() {
        showLoadingDialog();
        Observable<ResultBean<UploadImageBean>> front;
        if (!TextUtils.isEmpty(base64Front) && frontLists != null && frontLists.size() > 0) {
            front = ApiUtils.getInstance().userUploadImage(base64Front, frontLists.get(0).name, "rate");
        } else {
            front = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlFront);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        Observable<ResultBean<UploadImageBean>> behind;
        if (!TextUtils.isEmpty(base64Reverse) && behindLists != null && behindLists.size() > 0) {
            behind = ApiUtils.getInstance().userUploadImage(base64Reverse, behindLists.get(0).name, "rate");
        } else {
            behind = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlReverse);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        Observable<ResultBean<UploadImageBean>> handCard;
        if (!TextUtils.isEmpty(base64HandCard) && personLists != null && personLists.size() > 0) {
            handCard = ApiUtils.getInstance().userUploadImage(base64HandCard, personLists.get(0).name, "rate");

        } else {
            handCard = Observable.create(new ObservableOnSubscribe<ResultBean<UploadImageBean>>() {
                @Override
                public void subscribe(ObservableEmitter<ResultBean<UploadImageBean>> emitter) throws Exception {
                    ResultBean<UploadImageBean> bean = new ResultBean<>();
                    UploadImageBean uploadImageBean = new UploadImageBean();
                    uploadImageBean.setUrl(urlHandCard);
                    bean.setData(uploadImageBean);
                    emitter.onNext(bean);
                }
            });
        }
        disposable.add(Observable.zip(handCard, front, behind, new Function3<ResultBean<UploadImageBean>, ResultBean<UploadImageBean>,
                ResultBean<UploadImageBean>, List<String>>() {
            @Override
            public List<String> apply(ResultBean<UploadImageBean> uploadImageBeanResultBean, ResultBean<UploadImageBean> uploadImageBeanResultBean2,
                                      ResultBean<UploadImageBean> uploadImageBeanResultBean3) throws Exception {
                List<String> imageLists = new ArrayList<>();
                imageLists.add(uploadImageBeanResultBean.getData().getUrl());
                imageLists.add(uploadImageBeanResultBean2.getData().getUrl());
                imageLists.add(uploadImageBeanResultBean3.getData().getUrl());
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
        disposable.add(ApiUtils.getInstance().personAuth(etName.getText().toString().trim(), etPhone.getText().toString().trim(), etCardNum.getText().toString().trim(),
                strings.get(0), strings.get(1), strings.get(2))
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


}
