package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.bean.FirstClassBean;
import com.chunlangjiu.app.amain.bean.SecondClassBean;
import com.chunlangjiu.app.amain.bean.ThirdClassBean;
import com.chunlangjiu.app.goods.bean.AlcListBean;
import com.chunlangjiu.app.goods.bean.AreaListBean;
import com.chunlangjiu.app.goods.bean.BrandsListBean;
import com.chunlangjiu.app.goods.bean.OrdoListBean;
import com.chunlangjiu.app.goodsmanage.adapter.GoodsPicAdapter;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AddGoodsValueBean;
import com.chunlangjiu.app.user.bean.EditGoodsDetailBean;
import com.chunlangjiu.app.user.bean.ShopClassList;
import com.chunlangjiu.app.user.bean.SkuBean;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.user.dialog.ChoiceAlcPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceAreaPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceBrandPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceOrdoPopWindow;
import com.chunlangjiu.app.user.dialog.ShopClassPopWindow;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.noober.api.NeedSave;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.glide.GlideApp;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/30.
 * @Describe:
 */
public class EditGoodsActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_MAIN_PIC = 1001;
    public static final int REQUEST_CODE_SELECT_DETAIL_ONE_PIC = 10021;
    public static final int REQUEST_CODE_SELECT_DETAIL_TWO_PIC = 10022;
    public static final int REQUEST_CODE_SELECT_DETAIL_THREE_PIC = 10023;
    public static final int REQUEST_CODE_SELECT_DETAIL_FOUR_PIC = 10024;
    public static final int REQUEST_CODE_SELECT_GOODS_PIC = 1003;

    @BindView(R.id.rlChoiceClass)
    RelativeLayout rlChoiceClass;
    @BindView(R.id.tvClass)
    TextView tvClass;

    @BindView(R.id.rlChoicePlateClass)
    RelativeLayout rlChoicePlateClass;
    @BindView(R.id.tvPlateClass)
    TextView tvPlateClass;
    @BindView(R.id.rlChoiceBrand)
    RelativeLayout rlChoiceBrand;
    @BindView(R.id.tvBrand)
    TextView tvBrand;

    @BindView(R.id.rlChoiceArea)
    RelativeLayout rlChoiceArea;
    @BindView(R.id.tvChoiceArea)
    TextView tvChoiceArea;
    @BindView(R.id.rlChoiceIncense)
    RelativeLayout rlChoiceIncense;
    @BindView(R.id.tvIncense)
    TextView tvIncense;
    @BindView(R.id.rlChoiceDegree)
    RelativeLayout rlChoiceDegree;
    @BindView(R.id.tvDegree)
    TextView tvDegree;

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etSecondName)
    EditText etSecondName;
    @BindView(R.id.etTag)
    EditText etTag;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etCount)
    EditText etCount;


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
    @BindView(R.id.gvGoods)
    GridView recyclerViewGoods;

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

    @BindView(R.id.etGoodsDesc)
    EditText etGoodsDesc;

    @BindView(R.id.etType)
    EditText etType;
    @BindView(R.id.etSize)
    EditText etSize;
    @BindView(R.id.etChateau)
    EditText etChateau;
    @BindView(R.id.etSeries)
    EditText etSeries;
    @BindView(R.id.etPackage)
    EditText etPackage;
    @BindView(R.id.etAlco)
    EditText etAlco;
    @BindView(R.id.etArea)
    EditText etArea;

    @BindView(R.id.etYear)
    EditText etYear;
    @BindView(R.id.etYuanLiao)
    EditText etYuanLiao;
    @BindView(R.id.etSave)
    EditText etSave;
    @BindView(R.id.etOther)
    EditText etOther;
    @BindView(R.id.etFrom)
    EditText etFrom;

    @BindView(R.id.tvCommit)
    TextView tvCommit;


    @NeedSave
    int codeType;
    //分类列表
    ShopClassPopWindow shopPopWindow;
    @NeedSave
    ArrayList<ThirdClassBean> classLists;
    @NeedSave
    String classId;
    @NeedSave
    String className;

    //品牌列表
    ChoiceBrandPopWindow choiceBrandPopWindow;
    @NeedSave
    ArrayList<BrandsListBean.BrandBean> brandLists = new ArrayList<>();
    @NeedSave
    String brandId;
    @NeedSave
    String brandName;

    //产地列表
    ChoiceAreaPopWindow choiceAreaPopWindow;
    @NeedSave
    ArrayList<AreaListBean.AreaBean> areaLists = new ArrayList<>();
    @NeedSave
    String areaId = "";
    @NeedSave
    String areaName = "";

    //香型列表
    ChoiceOrdoPopWindow choiceOrdoPopWindow;
    @NeedSave
    ArrayList<OrdoListBean.OrdoBean> ordoLists = new ArrayList<>();
    @NeedSave
    String ordoId = "";
    @NeedSave
    String ordoName = "";

    //酒精度列表
    ChoiceAlcPopWindow choiceAlcPopWindow;
    @NeedSave
    ArrayList<AlcListBean.AlcBean> alcLists = new ArrayList<>();
    @NeedSave
    String alcId = "";
    @NeedSave
    String alcName = "";

    CompositeDisposable disposable;
    ChoicePhotoDialog photoDialog;
    @NeedSave
    ArrayList<ImageItem> mainPicLists;
    @NeedSave
    ArrayList<ImageItem> detailOnePicLists;
    @NeedSave
    ArrayList<ImageItem> detailTwoPicLists;
    @NeedSave
    ArrayList<ImageItem> detailThreePicLists;
    @NeedSave
    ArrayList<ImageItem> detailFourPicLists;


    GoodsPicAdapter goodsAdapter;
    @NeedSave
    ArrayList<ImageItem> goodsPicLists = new ArrayList<>();
    @NeedSave
    String base64Main;
    @NeedSave
    String base64DetailOne;
    @NeedSave
    String base64DetailTwo;
    @NeedSave
    String base64DetailThree;
    @NeedSave
    String base64DetailFour;
    @NeedSave
    String base64Goods;


    String itemId;

    public static void startEditGoodsDetailsActivity(Activity activity, String item_id) {
        Intent intent = new Intent(activity, EditGoodsActivity.class);
        intent.putExtra("item_id", item_id);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.rlChoiceClass:
//                    showShopClassPopWindow();
                    break;
                case R.id.rlChoicePlateClass:
                    showShopClassPopWindow();
                    break;
                case R.id.rlChoiceBrand:
                    showBrandPopWindow();
                    break;
                case R.id.rlChoiceArea:
                    showAreaPopWindow();
                    break;
                case R.id.rlChoiceIncense:
                    showIncensePopWindow();
                    break;
                case R.id.rlChoiceDegree:
                    showDegreePopWindow();
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
        titleName.setText("商品编辑");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_add_goods);
        initView();
        initImagePicker();
        if(savedInstanceState==null){
            initData();
        }else{
            setImgPic();
            setChooserName();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setChooserName() {
        tvPlateClass.setText(className);
        tvBrand.setText(brandName);
        tvChoiceArea.setText(areaName);
        tvIncense.setText(ordoName);
        tvDegree.setText(alcName);
    }

    private void setImgPic() {
        if (!TextUtils.isEmpty(base64Main)) {
            GlideUtils.loadImage(EditGoodsActivity.this, base64Main, imgMainPic);
            imgMainPic.setVisibility(View.VISIBLE);
            imgDeleteMainPic.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(base64DetailOne)) {
            GlideUtils.loadImage(EditGoodsActivity.this, base64DetailOne, imgDescOnePic);
            imgDescOnePic.setVisibility(View.VISIBLE);
            imgDeleteDescOnePic.setVisibility(View.VISIBLE);
            rlDescTwo.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(base64DetailTwo)) {
            GlideUtils.loadImage(EditGoodsActivity.this, base64DetailTwo, imgDescTwoPic);
            imgDescTwoPic.setVisibility(View.VISIBLE);
            imgDeleteDescTwoPic.setVisibility(View.VISIBLE);
            rlDescThree.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(base64DetailThree)) {
            GlideUtils.loadImage(EditGoodsActivity.this, base64DetailThree, imgDescFourPic);
            imgDescThreePic.setVisibility(View.VISIBLE);
            imgDeleteDescThreePic.setVisibility(View.VISIBLE);
            rlDescFour.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(base64DetailFour)) {
            GlideUtils.loadImage(EditGoodsActivity.this, base64DetailFour, imgDescThreePic);
            imgDescFourPic.setVisibility(View.VISIBLE);
            imgDeleteDescFourPic.setVisibility(View.VISIBLE);
            recyclerViewGoods.setVisibility(View.VISIBLE);
        }
        if (goodsPicLists != null) {
            goodsAdapter.notifyDataSetChanged();
        }
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

        rlChoiceClass.setOnClickListener(onClickListener);
        rlChoicePlateClass.setOnClickListener(onClickListener);
        rlChoiceBrand.setOnClickListener(onClickListener);
        rlChoiceArea.setOnClickListener(onClickListener);
        rlChoiceIncense.setOnClickListener(onClickListener);
        rlChoiceDegree.setOnClickListener(onClickListener);

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

        rlDescTwo.setVisibility(View.VISIBLE);
        rlDescThree.setVisibility(View.VISIBLE);
        rlDescFour.setVisibility(View.VISIBLE);
        rlGoods.setVisibility(View.GONE);


        classLists = new ArrayList<>();
        itemId = getIntent().getStringExtra("item_id");
        goodsAdapter = new GoodsPicAdapter(this, goodsPicLists);
        goodsAdapter.setOnItemChildClickListener(new GoodsPicAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                if (view.getId() == R.id.llAdd) {
                    showPhotoDialog(REQUEST_CODE_SELECT_GOODS_PIC);
                } else if (view.getId() == R.id.ivDeleteGoodsPic) {
                    goodsPicLists.remove(position);
                    goodsAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerViewGoods.setAdapter(goodsAdapter);
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


    private void initData() {
        getDetail();
    }

    private void getClassList() {
        //获取平台分类
        disposable.add(ApiUtils.getInstance().getShopClassList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ShopClassList>>() {
                    @Override
                    public void accept(ResultBean<ShopClassList> mainClassBeanResultBean) throws Exception {
                        List<FirstClassBean> categorys = mainClassBeanResultBean.getData().getCategory();
                        for (int i = 0; i < categorys.size(); i++) {
                            List<SecondClassBean> lv2 = categorys.get(i).getLv2();
                            for (int j = 0; j < lv2.size(); j++) {
                                List<ThirdClassBean> lv3 = lv2.get(j).getLv3();
                                classLists.addAll(lv3);
                            }
                        }

                        for (int i = 0; i < classLists.size(); i++) {
                            if (classLists.get(i).getCat_id().equals(classId)) {
                                EditGoodsActivity.this.className = classLists.get(i).getCat_name();
                                tvPlateClass.setText(classLists.get(i).getCat_name());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void getDetail() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().editGoodsDetail(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<EditGoodsDetailBean>>() {
                    @Override
                    public void accept(ResultBean<EditGoodsDetailBean> editGoodsDetailBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        getDetailSuccess(editGoodsDetailBeanResultBean.getData());
                        getClassList();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }


    private void getDetailSuccess(EditGoodsDetailBean data) {
        EditGoodsDetailBean.Item item = data.getItem();

        classId = item.getCat_id();
        brandId = item.getBrand_id();
        areaId = item.getArea_id();
        ordoId = item.getOdor_id();
        alcId = item.getAlcohol_id();

        etTitle.setText(item.getTitle());
        etSecondName.setText(item.getSub_title());
        etTag.setText(item.getLabel());
        etPrice.setText(item.getPrice());
        etCount.setText(item.getStore());
        etGoodsDesc.setText(item.getExplain());
        setChooserName();
        try {
            String parameter = item.getParameter();
            List<AddGoodsValueBean> ps = new Gson().fromJson(parameter, new TypeToken<List<AddGoodsValueBean>>() {
            }.getType());
            for (int i = 0; i < ps.size(); i++) {
                if (ps.get(i).getTitle().equals("容量")) {
                    etSize.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("酒庄")) {
                    etChateau.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("系列")) {
                    etSeries.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("包装")) {
                    etPackage.setText(ps.get(i).getValue());
                }

                if (ps.get(i).getTitle().equals("年份")) {
                    etYear.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("原料")) {
                    etYuanLiao.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("储存条件")) {
                    etSave.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("附件")) {
                    etOther.setText(ps.get(i).getValue());
                }
                if (ps.get(i).getTitle().equals("商品来源")) {
                    etFrom.setText(ps.get(i).getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<String> images = item.getImages();
            GlideApp.with(this).load(base64Main = images.get(0)).into(imgMainPic);
            GlideApp.with(this).load(base64DetailOne = images.get(1)).into(imgDescOnePic);
            GlideApp.with(this).load(base64DetailTwo = images.get(2)).into(imgDescTwoPic);
            GlideApp.with(this).load(base64DetailThree = images.get(3)).into(imgDescThreePic);
            GlideApp.with(this).load(base64DetailFour = images.get(4)).into(imgDescFourPic);

//            GlideApp.with(this).asBitmap().load(images.get(0)).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    imgMainPic.setImageBitmap(resource);
//                    base64Main = images.get(0);
//                }
//            });
//            GlideApp.with(this).asBitmap().load(images.get(1)).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    imgDescOnePic.setImageBitmap(resource);
//                    base64DetailOne = FileUtils.bitMapToBase64(resource);
//                }
//            });
//            GlideApp.with(this).asBitmap().load(images.get(2)).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    imgDescTwoPic.setImageBitmap(resource);
//                    base64DetailTwo = FileUtils.bitMapToBase64(resource);
//                }
//            });
//            GlideApp.with(this).asBitmap().load(images.get(3)).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    imgDescThreePic.setImageBitmap(resource);
//                    base64DetailThree = FileUtils.bitMapToBase64(resource);
//                }
//            });
//            GlideApp.with(this).asBitmap().load(images.get(4)).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                    imgDescFourPic.setImageBitmap(resource);
//                    base64DetailFour = FileUtils.bitMapToBase64(resource);
//                }
//            });
            if (images.size() >= 5) {
                for (int i = 5; i < images.size(); i++) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.path = images.get(i);
                    goodsPicLists.add(imageItem);
                }
                goodsAdapter.notifyDataSetChanged();
                recyclerViewGoods.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getBrandLists();
        getAreaLists();
        getInsenceLists();
        getAlcLists();
    }


    private void showShopClassPopWindow() {
        if (classLists == null || classLists.size() == 0) {
            ToastUtils.showShort("暂无分类");
        } else {
            if (shopPopWindow == null) {
                shopPopWindow = new ShopClassPopWindow(this, classLists, classId);
                shopPopWindow.setCallBack(new ShopClassPopWindow.CallBack() {
                    @Override
                    public void choiceClassId(String className, String classIdChoice) {
                        classId = classIdChoice;
                        EditGoodsActivity.this.className = className;
                        brandId = "";
                        areaId = "";
                        ordoId = "";
                        alcId = "";
                        getBrandLists();
                        getAreaLists();
                        getInsenceLists();
                        getAlcLists();
                        setChooserName();
                    }
                });
            }
            shopPopWindow.showAsDropDown(rlChoicePlateClass, 0, 1);
        }
    }


    private void getBrandLists() {
        disposable.add(ApiUtils.getInstance().getAddShopBrandList(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<BrandsListBean>>() {
                    @Override
                    public void accept(ResultBean<BrandsListBean> brandsListBeanResultBean) throws Exception {
                        brandLists = brandsListBeanResultBean.getData().getBrands();
                        for (int i = 0; i < brandLists.size(); i++) {
                            if (brandLists.get(i).getBrand_id().equals(brandId)) {
                                tvBrand.setText(brandLists.get(i).getBrand_name());
                                EditGoodsActivity.this.brandName = brandLists.get(i).getBrand_name();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getAreaLists() {
        disposable.add(ApiUtils.getInstance().getShopAreaList(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AreaListBean>>() {
                    @Override
                    public void accept(ResultBean<AreaListBean> areaListBeanResultBean) throws Exception {
                        areaLists = areaListBeanResultBean.getData().getList();
                        for (int i = 0; i < areaLists.size(); i++) {
                            if (areaLists.get(i).getArea_id().equals(areaId)) {
                                tvChoiceArea.setText(areaLists.get(i).getArea_name());
                                EditGoodsActivity.this.areaName = areaLists.get(i).getArea_name();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getInsenceLists() {
        disposable.add(ApiUtils.getInstance().getShopOrdoList(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrdoListBean>>() {
                    @Override
                    public void accept(ResultBean<OrdoListBean> ordoListBeanResultBean) throws Exception {
                        ordoLists = ordoListBeanResultBean.getData().getList();
                        for (int i = 0; i < ordoLists.size(); i++) {
                            if (ordoLists.get(i).getOdor_id().equals(ordoId)) {
                                tvIncense.setText(ordoLists.get(i).getOdor_name());
                                EditGoodsActivity.this.ordoName = ordoLists.get(i).getOdor_name();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getAlcLists() {
        disposable.add(ApiUtils.getInstance().getShopAlcList(classId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AlcListBean>>() {
                    @Override
                    public void accept(ResultBean<AlcListBean> alcListBeanResultBean) throws Exception {
                        alcLists = alcListBeanResultBean.getData().getList();
                        for (int i = 0; i < alcLists.size(); i++) {
                            if (alcLists.get(i).getAlcohol_id().equals(alcId)) {
                                tvDegree.setText(alcLists.get(i).getAlcohol_name());
                                EditGoodsActivity.this.alcName = alcLists.get(i).getAlcohol_name();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void showBrandPopWindow() {
        if (TextUtils.isEmpty(classId)) {
            ToastUtils.showShort("请先选择分类");
        } else {
            if (brandLists == null || brandLists.size() == 0) {
                ToastUtils.showShort("暂无品牌");
            } else {
                if (choiceBrandPopWindow == null) {
                    choiceBrandPopWindow = new ChoiceBrandPopWindow(this, brandLists, brandId);
                    choiceBrandPopWindow.setCallBack(new ChoiceBrandPopWindow.CallBack() {
                        @Override
                        public void choiceBrand(String brandName, String brandIdC) {
                            EditGoodsActivity.this.className = brandName;
                            tvBrand.setText(brandName);
                            brandId = brandIdC;
                        }
                    });
                }
                choiceBrandPopWindow.setBrandList(brandLists, brandId);
                choiceBrandPopWindow.showAsDropDown(rlChoiceBrand, 0, 1);
            }
        }
    }


    private void showAreaPopWindow() {
        if (TextUtils.isEmpty(classId)) {
            ToastUtils.showShort("请先选择分类");
        } else {
            if (areaLists == null || areaLists.size() == 0) {
                ToastUtils.showShort("暂无产地");
            } else {
                if (choiceAreaPopWindow == null) {
                    choiceAreaPopWindow = new ChoiceAreaPopWindow(this, areaLists, areaId);
                    choiceAreaPopWindow.setCallBack(new ChoiceAreaPopWindow.CallBack() {
                        @Override
                        public void choiceBrand(String brandName, String brandId) {
                            tvChoiceArea.setText(brandName);
                            EditGoodsActivity.this.areaName = areaName;
                            areaId = brandId;
                        }
                    });
                }
                choiceAreaPopWindow.setBrandList(areaLists, areaId);
                choiceAreaPopWindow.showAsDropDown(rlChoiceArea, 0, 1);
            }
        }
    }

    private void showIncensePopWindow() {
        if (TextUtils.isEmpty(classId)) {
            ToastUtils.showShort("请先选择分类");
        } else {
            if (ordoLists == null || ordoLists.size() == 0) {
                ToastUtils.showShort("暂无香型");
            } else {
                if (choiceOrdoPopWindow == null) {
                    choiceOrdoPopWindow = new ChoiceOrdoPopWindow(this, ordoLists, ordoId);
                    choiceOrdoPopWindow.setCallBack(new ChoiceOrdoPopWindow.CallBack() {
                        @Override
                        public void choiceBrand(String brandName, String brandId) {
                            EditGoodsActivity.this.ordoName = brandName;
                            tvIncense.setText(brandName);
                            ordoId = brandId;
                        }
                    });
                }
                choiceOrdoPopWindow.setBrandList(ordoLists, ordoId);
                choiceOrdoPopWindow.showAsDropDown(rlChoiceIncense, 0, 1);
            }
        }

    }

    private void showDegreePopWindow() {
        if (TextUtils.isEmpty(classId)) {
            ToastUtils.showShort("请先选择分类");
        } else {
            if (alcLists == null || alcLists.size() == 0) {
                ToastUtils.showShort("暂无酒精度");
            } else {
                if (choiceAlcPopWindow == null) {
                    choiceAlcPopWindow = new ChoiceAlcPopWindow(this, alcLists, alcId);
                    choiceAlcPopWindow.setCallBack(new ChoiceAlcPopWindow.CallBack() {
                        @Override
                        public void choiceBrand(String brandName, String brandId) {
                            EditGoodsActivity.this.alcName = brandName;
                            tvDegree.setText(brandName);
                            alcId = brandId;
                        }
                    });
                }
                choiceAlcPopWindow.setBrandList(alcLists, alcId);
                choiceAlcPopWindow.showAsDropDown(rlChoiceDegree, 0, 1);
            }
        }
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


    private void checkData() {
        if (TextUtils.isEmpty(classId)) {
            ToastUtils.showShort("请选择平台分类");
        } else if (TextUtils.isEmpty(brandId)) {
            ToastUtils.showShort("请选择品牌");
        } else if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            ToastUtils.showShort("请填写标题");
        } else if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            ToastUtils.showShort("请填写价格");
        } else if (TextUtils.isEmpty(etCount.getText().toString().trim())) {
            ToastUtils.showShort("请填写库存");
        } else if (TextUtils.isEmpty(etSize.getText().toString().trim())) {
            ToastUtils.showShort("请填写容量");
        } else if (TextUtils.isEmpty(etFrom.getText().toString().trim())) {
            ToastUtils.showShort("请填写商品来源");
        } else if (base64Main == null || base64DetailOne == null || base64DetailTwo == null || base64DetailThree == null || base64DetailFour == null) {
            ToastUtils.showShort("请添加图片");
        } else {
            uploadImageNew();
        }
    }

    private void uploadImageNew() {
        showLoadingDialog();

        //一定要上传的五张主图
//        Observable<ResultBean<UploadImageBean>> main = ApiUtils.getInstance().shopUploadImage(base64Main, (mainPicLists == null || mainPicLists.size() == 0) ? "main.jpg" : mainPicLists.get(0).name);
//        Observable<ResultBean<UploadImageBean>> detailOne = ApiUtils.getInstance().shopUploadImage(base64DetailOne, (detailOnePicLists == null || detailOnePicLists.size() == 0) ? "one.jpg" : detailOnePicLists.get(0).name);
//        Observable<ResultBean<UploadImageBean>> detailTwo = ApiUtils.getInstance().shopUploadImage(base64DetailTwo, (detailTwoPicLists == null || detailTwoPicLists.size() == 0) ? "two.jpg" : detailTwoPicLists.get(0).name);
//        Observable<ResultBean<UploadImageBean>> detailThree = ApiUtils.getInstance().shopUploadImage(base64DetailThree, (detailThreePicLists == null || detailThreePicLists.size() == 0) ? "three.jpg" : detailThreePicLists.get(0).name);
//        Observable<ResultBean<UploadImageBean>> detailFour = ApiUtils.getInstance().shopUploadImage(base64DetailFour, (detailFourPicLists == null || detailFourPicLists.size() == 0) ? "four.jpg" : detailFourPicLists.get(0).name);
//
//        ArrayList<Observable<ResultBean<UploadImageBean>>> tempList = new ArrayList();
//        tempList.add(main);
//        tempList.add(detailOne);
//        tempList.add(detailTwo);
//        tempList.add(detailThree);
//        tempList.add(detailFour);
        ArrayList<Observable<ResultBean<UploadImageBean>>> tempList = new ArrayList();
        tempList.add(CommonUtils.getUploadPic(base64Main, (mainPicLists == null || mainPicLists.size() == 0) ? "main.jpg" : mainPicLists.get(0).name));
        tempList.add(CommonUtils.getUploadPic(base64DetailOne, (detailOnePicLists == null || detailOnePicLists.size() == 0) ? "main.jpg" : detailOnePicLists.get(0).name));
        tempList.add(CommonUtils.getUploadPic(base64DetailTwo, (detailTwoPicLists == null || detailTwoPicLists.size() == 0) ? "main.jpg" : detailTwoPicLists.get(0).name));
        tempList.add(CommonUtils.getUploadPic(base64DetailThree, (detailThreePicLists == null || detailThreePicLists.size() == 0) ? "main.jpg" : detailThreePicLists.get(0).name));
        tempList.add(CommonUtils.getUploadPic(base64DetailFour, (detailFourPicLists == null || detailFourPicLists.size() == 0) ? "main.jpg" : detailFourPicLists.get(0).name));
        //可选上传的图片
        if (goodsPicLists != null && goodsPicLists.size() > 0) {
            for (final ImageItem imageItem : goodsPicLists) {
                tempList.add(CommonUtils.getUploadPic(imageItem.path, imageItem.name));
            }
        }


        disposable.add(Observable.zip(tempList, new Function<Object[], List<String>>() {
            @Override
            public List<String> apply(Object[] objects) throws Exception {
                List<String> imageLists = new ArrayList<>();
                for (Object obj : objects) {
                    ResultBean<UploadImageBean> resultBeans = (ResultBean<UploadImageBean>) obj;
                    imageLists.add(resultBeans.getData().getUrl());
                }
                return imageLists;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        if (strings != null && strings.size() > 0) {
                            uploadSuccess(strings);
                        } else {
                            hideLoadingDialog();
                            ToastUtils.showShort("上传图片失败");
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

    private void uploadSuccess(List<String> imageLists) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < imageLists.size(); i++) {
            if (i == imageLists.size() - 1) {
                stringBuffer.append(imageLists.get(i));
            } else {
                stringBuffer.append(imageLists.get(i)).append(",");
            }
        }
        commitGoods(stringBuffer.toString());
    }

    private void commitGoods(String images) {
        SkuBean skuBean = new SkuBean();
        skuBean.setPrice(etPrice.getText().toString().trim());
        skuBean.setStore(etCount.getText().toString().trim());
        List<SkuBean> list = new ArrayList<>();
        list.add(skuBean);
        String skuArray = new Gson().toJson(list);

        List<AddGoodsValueBean> valueBeanList = new ArrayList<>();
//        valueBeanList.add(new AddGoodsValueBean("类型", etType.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("容量", etSize.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("酒庄", etChateau.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("系列", etSeries.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("包装", etPackage.getText().toString().trim()));
//        valueBeanList.add(new AddGoodsValueBean("酒精度", etAlco.getText().toString().trim()));
//        valueBeanList.add(new AddGoodsValueBean("产地", etArea.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("年份", etYear.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("原料", etYuanLiao.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("储存条件", etSave.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("附件", etOther.getText().toString().trim()));
        valueBeanList.add(new AddGoodsValueBean("商品来源", etFrom.getText().toString().trim()));
        String parameter = new Gson().toJson(valueBeanList);
        KLog.e(parameter);

        disposable.add(ApiUtils.getInstance().commitEditGoodsDetail(classId, brandId, "", etTitle.getText().toString().trim(),
                etSecondName.getText().toString().trim(), etSize.getText().toString().trim(), images,
                etPrice.getText().toString().trim(), "15", skuArray, etTag.getText().toString().trim(),
                etGoodsDesc.getText().toString().trim(), parameter, areaId, ordoId, alcId, etCount.getText().toString().trim(), itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        startActivity(new Intent(EditGoodsActivity.this, AddGoodsSuccessActivity.class));
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                        hideLoadingDialog();
                    }
                }));
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
                        base64Main = mainPicLists.get(0).path;
                        imgMainPic.setVisibility(View.VISIBLE);
                        imgDeleteMainPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(EditGoodsActivity.this, mainPicLists.get(0).path, imgMainPic);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_ONE_PIC) {
                        detailOnePicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailOnePicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailOne = detailOnePicLists.get(0).path;
                        imgDescOnePic.setVisibility(View.VISIBLE);
                        imgDeleteDescOnePic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(EditGoodsActivity.this, detailOnePicLists.get(0).path, imgDescOnePic);
                        rlDescTwo.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_TWO_PIC) {
                        detailTwoPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailTwoPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailTwo = detailTwoPicLists.get(0).path;
                        imgDescTwoPic.setVisibility(View.VISIBLE);
                        imgDeleteDescTwoPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(EditGoodsActivity.this, detailTwoPicLists.get(0).path, imgDescTwoPic);
                        rlDescThree.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_THREE_PIC) {
                        detailThreePicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailThreePicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailThree = detailThreePicLists.get(0).path;
                        imgDescThreePic.setVisibility(View.VISIBLE);
                        imgDeleteDescThreePic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(EditGoodsActivity.this, detailThreePicLists.get(0).path, imgDescThreePic);
                        rlDescFour.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_FOUR_PIC) {
                        detailFourPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = detailFourPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        base64DetailFour = detailFourPicLists.get(0).path;
                        imgDescFourPic.setVisibility(View.VISIBLE);
                        imgDeleteDescFourPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(EditGoodsActivity.this, detailFourPicLists.get(0).path, imgDescFourPic);
                        recyclerViewGoods.setVisibility(View.VISIBLE);
                    } else if (requestCode == REQUEST_CODE_SELECT_GOODS_PIC) {
                        ArrayList<ImageItem> tempItem = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = tempItem.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        goodsPicLists.add(imageItem);
                        goodsAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
