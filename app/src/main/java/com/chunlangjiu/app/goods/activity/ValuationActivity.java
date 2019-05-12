package com.chunlangjiu.app.goods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/16
 * @Describe: 名酒估价页面
 */
public class ValuationActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_MAIN_PIC = 1001;
    public static final int REQUEST_CODE_SELECT_DETAIL_ONE_PIC = 10021;
    public static final int REQUEST_CODE_SELECT_DETAIL_TWO_PIC = 10022;
    public static final int REQUEST_CODE_SELECT_DETAIL_THREE_PIC = 10023;
    public static final int REQUEST_CODE_SELECT_DETAIL_FOUR_PIC = 10024;
    public static final int REQUEST_CODE_SELECT_GOODS_PIC = 1003;
    private int codeType;

    @BindView(R.id.rlChoiceClass)
    RelativeLayout rlChoiceClass;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.etSeries)
    EditText etSeries;

    @BindView(R.id.llMainPic)
    LinearLayout llMainPic;
    @BindView(R.id.llDescOnePic)
    LinearLayout llDescOnePic;
    @BindView(R.id.llDescTwoPic)
    LinearLayout llDescTwoPic;
    @BindView(R.id.llDescThreePic)
    LinearLayout llDescThreePic;
    @BindView(R.id.llDescFourPic)
    LinearLayout llDescFourPic;
    @BindView(R.id.llGoodsPic)
    LinearLayout llGoodsPic;

    @BindView(R.id.rlMainPic)
    RelativeLayout rlMainPic;
    @BindView(R.id.rlDescOnePic)
    RelativeLayout rlDescOnePic;
    @BindView(R.id.rlDescTwoPic)
    RelativeLayout rlDescTwoPic;
    @BindView(R.id.rlDescThreePic)
    RelativeLayout rlDescThreePic;
    @BindView(R.id.rlDescFourPic)
    RelativeLayout rlDescFourPic;
    @BindView(R.id.rlGoodsPic)
    RelativeLayout rlGoodsPic;

    @BindView(R.id.imgMainPic)
    ImageView imgMainPic;
    @BindView(R.id.imgDescOnePic)
    ImageView imgDescOnePic;
    @BindView(R.id.imgDescTwoPic)
    ImageView imgDescTwoPic;
    @BindView(R.id.imgDescThreePic)
    ImageView imgDescThreePic;
    @BindView(R.id.imgDescFourPic)
    ImageView imgDescFourPic;
    @BindView(R.id.imgGoodsPic)
    ImageView imgGoodsPic;

    @BindView(R.id.imgDeleteMainPic)
    ImageView imgDeleteMainPic;
    @BindView(R.id.imgDeleteDescOnePic)
    ImageView imgDeleteDescOnePic;
    @BindView(R.id.imgDeleteDescTwoPic)
    ImageView imgDeleteDescTwoPic;
    @BindView(R.id.imgDeleteDescThreePic)
    ImageView imgDeleteDescThreePic;
    @BindView(R.id.imgDeleteDescFourPic)
    ImageView imgDeleteDescFourPic;
    @BindView(R.id.imgDeleteGoodsPic)
    ImageView imgDeleteGoodsPic;

    @BindView(R.id.rlDescTwo)
    RelativeLayout rlDescTwo;
    @BindView(R.id.rlDescThree)
    RelativeLayout rlDescThree;
    @BindView(R.id.rlDescFour)
    RelativeLayout rlDescFour;
    @BindView(R.id.rlGoods)
    RelativeLayout rlGoods;

    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private CompositeDisposable disposable;
    private ChoicePhotoDialog photoDialog;
    private ArrayList<ImageItem> mainPicLists;
    private ArrayList<ImageItem> detailOnePicLists;
    private ArrayList<ImageItem> detailTwoPicLists;
    private ArrayList<ImageItem> detailThreePicLists;
    private ArrayList<ImageItem> detailFourPicLists;
    private ArrayList<ImageItem> goodsPicLists;
    private String base64Main;
    private String base64DetailOne;
    private String base64DetailTwo;
    private String base64DetailThree;
    private String base64DetailFour;
    private String base64Goods;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.rlMainPic:
                    showPhotoDialog(REQUEST_CODE_SELECT_MAIN_PIC);
                    break;
                case R.id.rlDescOnePic:
                    showPhotoDialog(REQUEST_CODE_SELECT_DETAIL_ONE_PIC);
                    break;
                case R.id.rlDescTwoPic:
                    showPhotoDialog(REQUEST_CODE_SELECT_DETAIL_TWO_PIC);
                    break;
                case R.id.rlDescThreePic:
                    showPhotoDialog(REQUEST_CODE_SELECT_DETAIL_THREE_PIC);
                    break;
                case R.id.rlDescFourPic:
                    showPhotoDialog(REQUEST_CODE_SELECT_DETAIL_FOUR_PIC);
                    break;
                case R.id.rlGoodsPic:
                    showPhotoDialog(REQUEST_CODE_SELECT_GOODS_PIC);
                    break;
                case R.id.imgDeleteMainPic:
                    deleteMainPic();
                    break;
                case R.id.imgDeleteDescOnePic:
                    deleteDescOnePic();
                    break;
                case R.id.imgDeleteDescTwoPic:
                    deleteDescTwoPic();
                    break;
                case R.id.imgDeleteDescThreePic:
                    deleteDescFourPic();
                    break;
                case R.id.imgDeleteDescFourPic:
                    deleteDescFivePic();
                    break;
                case R.id.imgDeleteGoodsPic:
                    deleteGoodsPic();
                    break;
                case R.id.tvCommit:
                    checkData();
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        titleName.setText("名酒鉴定");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_valuation);
        initImagePicker();
        initView();
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
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(500);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(500);                         //保存文件的高度。单位像素
    }


    private void initView() {
        disposable = new CompositeDisposable();
        int picSize = (SizeUtils.getScreenWidth() - 30) / 2;
        ViewGroup.LayoutParams layoutParams = llMainPic.getLayoutParams();
        layoutParams.height = picSize;
        llMainPic.setLayoutParams(layoutParams);
        llDescOnePic.setLayoutParams(layoutParams);
        llDescTwoPic.setLayoutParams(layoutParams);
        llDescThreePic.setLayoutParams(layoutParams);
        llDescFourPic.setLayoutParams(layoutParams);
        llGoodsPic.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams imgDeleteGoodsPicLayoutParams = (RelativeLayout.LayoutParams) imgDeleteGoodsPic.getLayoutParams();
        imgDeleteGoodsPicLayoutParams.leftMargin = picSize - SizeUtils.dp2px(15);
        imgDeleteGoodsPic.setLayoutParams(imgDeleteGoodsPicLayoutParams);

        rlMainPic.setOnClickListener(onClickListener);
        rlDescOnePic.setOnClickListener(onClickListener);
        rlDescTwoPic.setOnClickListener(onClickListener);
        rlDescThreePic.setOnClickListener(onClickListener);
        rlDescFourPic.setOnClickListener(onClickListener);
        rlGoodsPic.setOnClickListener(onClickListener);

        imgDeleteMainPic.setOnClickListener(onClickListener);
        imgDeleteDescOnePic.setOnClickListener(onClickListener);
        imgDeleteDescTwoPic.setOnClickListener(onClickListener);
        imgDeleteDescThreePic.setOnClickListener(onClickListener);
        imgDeleteDescFourPic.setOnClickListener(onClickListener);
        imgDeleteGoodsPic.setOnClickListener(onClickListener);

        tvCommit.setOnClickListener(onClickListener);
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
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == REQUEST_CODE_SELECT_MAIN_PIC) {
                        mainPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = mainPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64Main = FileUtils.imgToBase64(mainPicLists.get(0).path);
                        imgMainPic.setVisibility(View.VISIBLE);
                        imgDeleteMainPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, mainPicLists.get(0).path, imgMainPic);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_ONE_PIC) {
                        detailOnePicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailOnePicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailOne = FileUtils.imgToBase64(detailOnePicLists.get(0).path);
                        imgDescOnePic.setVisibility(View.VISIBLE);
                        imgDeleteDescOnePic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, detailOnePicLists.get(0).path, imgDescOnePic);
                        rlDescTwo.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_TWO_PIC) {
                        detailTwoPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailTwoPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailTwo = FileUtils.imgToBase64(detailTwoPicLists.get(0).path);
                        imgDescTwoPic.setVisibility(View.VISIBLE);
                        imgDeleteDescTwoPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, detailTwoPicLists.get(0).path, imgDescTwoPic);
                        rlDescThree.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_THREE_PIC) {
                        detailThreePicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailThreePicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailThree = FileUtils.imgToBase64(detailThreePicLists.get(0).path);
                        imgDescThreePic.setVisibility(View.VISIBLE);
                        imgDeleteDescThreePic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, detailThreePicLists.get(0).path, imgDescThreePic);
                        rlDescFour.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_FOUR_PIC) {
                        detailFourPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailFourPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailFour = FileUtils.imgToBase64(detailFourPicLists.get(0).path);
                        imgDescFourPic.setVisibility(View.VISIBLE);
                        imgDeleteDescFourPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, detailFourPicLists.get(0).path, imgDescFourPic);
                        rlGoods.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_GOODS_PIC) {
                        goodsPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = goodsPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64Goods = FileUtils.imgToBase64(goodsPicLists.get(0).path);
                        imgGoodsPic.setVisibility(View.VISIBLE);
                        imgDeleteGoodsPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(ValuationActivity.this, goodsPicLists.get(0).path, imgGoodsPic);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMainPic() {
        mainPicLists = null;
        base64Main = null;
        imgMainPic.setVisibility(View.GONE);
        imgDeleteMainPic.setVisibility(View.GONE);
    }

    private void deleteDescOnePic() {
        detailOnePicLists = null;
        base64DetailOne = null;
        imgDescOnePic.setVisibility(View.GONE);
        imgDeleteDescOnePic.setVisibility(View.GONE);
    }


    private void deleteDescTwoPic() {
        detailTwoPicLists = null;
        base64DetailTwo = null;
        imgDescTwoPic.setVisibility(View.GONE);
        imgDeleteDescTwoPic.setVisibility(View.GONE);
    }

    private void deleteDescFourPic() {
        detailThreePicLists = null;
        base64DetailThree = null;
        imgDescThreePic.setVisibility(View.GONE);
        imgDeleteDescThreePic.setVisibility(View.GONE);
    }

    private void deleteDescFivePic() {
        detailFourPicLists = null;
        base64DetailFour = null;
        imgDescFourPic.setVisibility(View.GONE);
        imgDeleteDescFourPic.setVisibility(View.GONE);
    }

    private void deleteGoodsPic() {
        goodsPicLists = null;
        base64Goods = null;
        imgGoodsPic.setVisibility(View.GONE);
        imgDeleteGoodsPic.setVisibility(View.GONE);
    }

    private void checkData() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            ToastUtils.showShort("请填写商品标题");
        } else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
            ToastUtils.showShort("请填写品牌产地");
        } else if (TextUtils.isEmpty(etSeries.getText().toString().trim())) {
            ToastUtils.showShort("请填写所属系列");
        } else if (base64Main == null || base64DetailOne == null || base64DetailTwo == null || base64DetailThree == null || base64DetailFour == null) {
            ToastUtils.showShort("请上传图片");
        } else {
            uploadImageNew();
        }
    }

    private void uploadImageNew() {
        showLoadingDialog();
        final List<String> base64Lists = new ArrayList<>();
        List<String> nameLists = new ArrayList<>();
        final List<String> imageLists = new ArrayList<>();
        if (base64Main != null) {
            base64Lists.add(base64Main);
            nameLists.add(mainPicLists.get(0).name);
        }
        if (base64DetailOne != null) {
            base64Lists.add(base64DetailOne);
            nameLists.add(detailOnePicLists.get(0).name);
        }
        if (base64DetailTwo != null) {
            base64Lists.add(base64DetailTwo);
            nameLists.add(detailTwoPicLists.get(0).name);
        }
        if (base64DetailThree != null) {
            base64Lists.add(base64DetailThree);
            nameLists.add(detailThreePicLists.get(0).name);
        }
        if (base64DetailFour != null) {
            base64Lists.add(base64DetailFour);
            nameLists.add(detailFourPicLists.get(0).name);
        }
        if (base64Goods != null) {
            base64Lists.add(base64Goods);
            nameLists.add(goodsPicLists.get(0).name);
        }
        for (int i = 0; i < base64Lists.size(); i++) {
            disposable.add(ApiUtils.getInstance().userUploadImage(base64Lists.get(i), nameLists.get(i), "rate")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<UploadImageBean>>() {
                        @Override
                        public void accept(ResultBean<UploadImageBean> uploadImageBeanResultBean) throws Exception {
                            imageLists.add(uploadImageBeanResultBean.getData().getUrl());
                            if (imageLists.size() == base64Lists.size()) {
                                StringBuffer stringBuffer = new StringBuffer();
                                for (int i = 0; i < imageLists.size(); i++) {
                                    if (i == imageLists.size() - 1) {
                                        stringBuffer.append(imageLists.get(i));
                                    } else {
                                        stringBuffer.append(imageLists.get(i)).append(",");
                                    }
                                }
                                valuationGoods(stringBuffer.toString());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("上传图片失败");
                        }
                    }));
        }
    }

    private void valuationGoods(String imgUrls) {
        disposable.add(ApiUtils.getInstance().valuationGoods(etTitle.getText().toString().trim(), etLocation.getText().toString().trim(),
                imgUrls, etSeries.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        startActivity(new Intent(ValuationActivity.this, ValuationSuccessActivity.class));
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("提交失败");
                        hideLoadingDialog();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
