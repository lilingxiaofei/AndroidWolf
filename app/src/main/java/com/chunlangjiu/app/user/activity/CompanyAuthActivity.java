package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AuthInfoBean;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.user.bean.LocalAreaBean;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.chunlangjiu.app.util.AreaUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
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
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.choicearea.BottomDialog;
import com.pkqup.commonlibrary.view.choicearea.DataProvider;
import com.pkqup.commonlibrary.view.choicearea.ISelectAble;
import com.pkqup.commonlibrary.view.choicearea.SelectedListener;
import com.pkqup.commonlibrary.view.choicearea.Selector;
import com.umeng.socialize.sina.auth.AuthInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.internal.operators.observable.ObservableRefCount;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe: 企业认证页面
 */
public class CompanyAuthActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_ONE = 103;
    public static final int REQUEST_CODE_SELECT_TWO = 104;
    public static final int REQUEST_CODE_SELECT_ALLOW_CARD = 105;
    public static final int REQUEST_CODE_SELECT_FOOD_LISENCE = 106;
    private int codeType;

    @BindView(R.id.etCompany)
    EditText etCompany;
    @BindView(R.id.etPersonName)
    EditText etPersonName;
    @BindView(R.id.edtIdCard)
    EditText edtIdCard;
//    @BindView(R.id.etCardNum)
//    EditText etCardNum;

//    @BindView(R.id.rlCreateTime)
//    RelativeLayout rlCreateTime;
//    @BindView(R.id.tvCreateTime)
//    TextView tvCreateTime;
//    @BindView(R.id.rlSellArea)
//    RelativeLayout rlSellArea;
//    @BindView(R.id.tvSellArea)
//    TextView tvSellArea;
//
//    @BindView(R.id.etAddress)
//    EditText etAddress;
//    @BindView(R.id.etPhone)
//    EditText etPhone;

    //    @BindView(R.id.rlOne)
//    RelativeLayout rlOne;
//    @BindView(R.id.rlTwo)
//    RelativeLayout rlTwo;
//    @BindView(R.id.rlAllowCard)
//    RelativeLayout rlAllowCard;
//    @BindView(R.id.imgSellCard)
//    ImageView imgSellCard;
//    @BindView(R.id.imgIDCard)
//    ImageView imgIDCard;
//    @BindView(R.id.imgAllowCard)
//    ImageView imgAllowCard;
    @BindView(R.id.imgLicense)
    ImageView imgLicense;
    @BindView(R.id.imgIdCardFront)
    ImageView imgIdCardFront;
    @BindView(R.id.imgIdCardBehind)
    ImageView imgIdCardBehind;
    @BindView(R.id.imgFoodCertificate)
    ImageView imgFoodCertificate;

    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private CompositeDisposable disposable;
    private List<LocalAreaBean.ProvinceData> areaLists;
    private BottomDialog areaDialog;
    private String addressId;
    private String provinceId;
    private String cityId;
    private String districtId;
    private int provinceIndex;
    private int cityIndex;

    private TimePickerDialog timePickerDialog;
    private long selectTime = 0;

    private ChoicePhotoDialog photoDialog;
    private ArrayList<ImageItem> ZhiZhaoLists;
    private ArrayList<ImageItem> cardFLists;
    private ArrayList<ImageItem> cardBLists;
    private ArrayList<ImageItem> foodLisenceLists;
    private String base64ZhiZhao;
    private String urlZhiZhao;
    private String base64IdCardF;
    private String urlIdCardF;
    private String base64IdCardB;
    private String urlIdCardB;
    private String base64FoodLicense;
    private String urlFoodLicense;
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.img_title_left:
//                    finish();
//                    break;
//                case R.id.rlCreateTime:
//                    showTimeDialog();
//                    break;
//                case R.id.rlSellArea:
//                    showAreaDialog();
//                    break;
//                case R.id.rlOne:
//                    showPhotoDialog(REQUEST_CODE_SELECT_ONE);
//                    break;
//                case R.id.rlTwo:
//                    showPhotoDialog(REQUEST_CODE_SELECT_TWO);
//                    break;
//                case R.id.rlAllowCard:
//                    showPhotoDialog(REQUEST_CODE_SELECT_ALLOW_CARD);
//                    break;
//                case R.id.tvCommit:
//                    checkData();
//                    break;
//            }
//        }
//    };


    @Override
    public void setTitleView() {
        titleName.setText("企业认证");
//        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_company_auth);
        initImagePicker();
        initView();
        initData();
    }

    private void initImagePicker() {
//        int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(30)) / 2;
//        int picHeight = (picWidth / 8) * 5;
//        ViewGroup.LayoutParams layoutParamsOne = rlOne.getLayoutParams();
//        layoutParamsOne.width = picWidth;
//        layoutParamsOne.height = picHeight;
//        rlOne.setLayoutParams(layoutParamsOne);
//
//        ViewGroup.LayoutParams layoutParamsTwo = rlTwo.getLayoutParams();
//        layoutParamsTwo.width = picWidth;
//        layoutParamsTwo.height = picHeight;
//        rlTwo.setLayoutParams(layoutParamsTwo);
//
//        ViewGroup.LayoutParams layoutParamsAllow = rlAllowCard.getLayoutParams();
//        layoutParamsAllow.width = picWidth;
//        layoutParamsAllow.height = picHeight;
//        rlAllowCard.setLayoutParams(layoutParamsAllow);

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
//        rlOne.setOnClickListener(onClickListener);
//        rlTwo.setOnClickListener(onClickListener);
//        rlAllowCard.setOnClickListener(onClickListener);
//        rlCreateTime.setOnClickListener(onClickListener);
//        rlSellArea.setOnClickListener(onClickListener);
//        tvCommit.setOnClickListener(onClickListener);
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
//            case R.id.rlCreateTime:
//                showTimeDialog();
//                break;
//            case R.id.rlSellArea:
//                showAreaDialog();
//                break;

        }

    }


    private void initData() {
//        getAreaData();
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
        if(!"active".equals(data.getStatus())){
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
        } else if(AuthStatusBean.AUTH_MODIFIER.equals(data.getStatus())){
            tvCommit.setText("更新审核资料");
            tvCommit.setClickable(true);
            updateView(data);
        }else if ("finish".equals(data.getStatus())) {
            tvCommit.setText("更新审核资料");
            tvCommit.setClickable(true);
            updateView(data);
        }
    }

    private void updateView(AuthStatusBean data) {

    }

    private void getAreaData() {
        disposable.add(Observable.create(new ObservableOnSubscribe<List<LocalAreaBean.ProvinceData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalAreaBean.ProvinceData>> e) throws Exception {
                List<LocalAreaBean.ProvinceData> json = AreaUtils.getJson(CompanyAuthActivity.this);
                e.onNext(json);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalAreaBean.ProvinceData>>() {
                    @Override
                    public void accept(List<LocalAreaBean.ProvinceData> provinceData) throws Exception {
                        areaLists = provinceData;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void showTimeDialog() {
        if (timePickerDialog == null) {
            long hundredYears = 100L * 365 * 1000 * 60 * 60 * 24L;
            timePickerDialog = new TimePickerDialog.Builder().setCallBack(onDateSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis() - hundredYears)
                    .setMaxMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.YEAR_MONTH_DAY)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12).build();
        }

        timePickerDialog.show(getSupportFragmentManager(), "year_month_day");
    }

    private OnDateSetListener onDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
//            tvCreateTime.setText(TimeUtils.millisToYearMD(String.valueOf(millSeconds)));
        }
    };

    private void showAreaDialog() {
        Selector selector = new Selector(this, 3);
        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int index, DataReceiver receiver) {
                ArrayList<ISelectAble> list = new ArrayList<>();
                if (currentDeep == 0) {
                    list = getProvince();
                } else if (currentDeep == 1) {
                    list = getCity(index);
                } else if (currentDeep == 2) {
                    list = getDistrict(index);
                }
                receiver.send(list);//根据层级获取数据
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                String areaName = "";
                if (selectAbles.get(0).getName().equals(selectAbles.get(1).getName())) {
                    areaName = selectAbles.get(1).getName() + " " + selectAbles.get(2).getName();
                } else {
                    areaName = selectAbles.get(0).getName() + " " + selectAbles.get(1).getName() + " " + selectAbles.get(2).getName();
                }
                provinceId = ((LocalAreaBean.ProvinceData) (selectAbles.get(0).getArg())).getId();
                cityId = ((LocalAreaBean.ProvinceData.City) (selectAbles.get(1).getArg())).getId();
                districtId = ((LocalAreaBean.ProvinceData.City.District) (selectAbles.get(2).getArg())).getId();
//                tvSellArea.setText(areaName);
                areaDialog.dismiss();
            }
        });

        if (areaDialog == null) {
            areaDialog = new BottomDialog(this);
            areaDialog.init(this, selector);
        }
        areaDialog.show();
    }

    private ArrayList<ISelectAble> getProvince() {
        ArrayList<ISelectAble> data = new ArrayList<>();
        for (int j = 0; j < areaLists.size(); j++) {
            final int finalJ = j;
            data.add(new ISelectAble() {
                @Override
                public String getName() {
                    return areaLists.get(finalJ).getValue();
                }

                @Override
                public int getId() {
                    return finalJ;
                }

                @Override
                public Object getArg() {
                    return areaLists.get(finalJ);
                }
            });
        }
        return data;
    }

    private ArrayList<ISelectAble> getCity(int index) {
        this.provinceIndex = index;
        ArrayList<ISelectAble> data = new ArrayList<>();
        for (int j = 0; j < areaLists.get(provinceIndex).getChildren().size(); j++) {
            final int finalJ = j;
            data.add(new ISelectAble() {
                @Override
                public String getName() {
                    return areaLists.get(provinceIndex).getChildren().get(finalJ).getValue();
                }

                @Override
                public int getId() {
                    return finalJ;
                }

                @Override
                public Object getArg() {
                    return areaLists.get(provinceIndex).getChildren().get(finalJ);
                }
            });
        }
        return data;
    }

    private ArrayList<ISelectAble> getDistrict(int index) {
        this.cityIndex = index;
        ArrayList<ISelectAble> data = new ArrayList<>();
        for (int j = 0; j < areaLists.get(provinceIndex).getChildren().get(cityIndex).getChildren().size(); j++) {
            final int finalJ = j;
            data.add(new ISelectAble() {
                @Override
                public String getName() {
                    return areaLists.get(provinceIndex).getChildren().get(cityIndex).getChildren().get(finalJ).getValue();
                }

                @Override
                public int getId() {
                    return finalJ;
                }

                @Override
                public Object getArg() {
                    return areaLists.get(provinceIndex).getChildren().get(cityIndex).getChildren().get(finalJ);
                }
            });
        }
        return data;
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
                    urlZhiZhao = "" ;
//                    GlideUtils.loadImage(CompanyAuthActivity.this, ZhiZhaoLists.get(0).path, imgSellCard);
                    GlideUtils.loadImage(CompanyAuthActivity.this, ZhiZhaoLists.get(0).path, imgLicense);
                } else if (requestCode == REQUEST_CODE_SELECT_TWO) {
                    cardFLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = cardFLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64IdCardF = FileUtils.imgToBase64(cardFLists.get(0).path);
                    urlIdCardF = "";
//                    GlideUtils.loadImage(CompanyAuthActivity.this, cardLists.get(0).path, imgIDCard);
                    GlideUtils.loadImage(CompanyAuthActivity.this, cardFLists.get(0).path, imgIdCardFront);
                } else if (requestCode == REQUEST_CODE_SELECT_ALLOW_CARD) {
                    cardBLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = cardBLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64IdCardB = FileUtils.imgToBase64(cardBLists.get(0).path);
                    urlIdCardB = "";
//                    GlideUtils.loadImage(CompanyAuthActivity.this, allowLists.get(0).path, imgAllowCard);
                    GlideUtils.loadImage(CompanyAuthActivity.this, cardBLists.get(0).path, imgIdCardBehind);
                } else if (requestCode == REQUEST_CODE_SELECT_FOOD_LISENCE) {
                    foodLisenceLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    ImageItem imageItem = foodLisenceLists.get(0);
                    int index = imageItem.path.lastIndexOf("/");
                    imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                    base64FoodLicense = FileUtils.imgToBase64(foodLisenceLists.get(0).path);
                    urlFoodLicense = "";
//                    GlideUtils.loadImage(CompanyAuthActivity.this, allowLists.get(0).path, imgAllowCard);
                    GlideUtils.loadImage(CompanyAuthActivity.this, foodLisenceLists.get(0).path, imgFoodCertificate);
                }
            }
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

        }else{
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
        }else{
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
        }else{
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
        }else{
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
