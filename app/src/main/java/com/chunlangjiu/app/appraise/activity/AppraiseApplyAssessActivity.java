package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.activity.ValuationSuccessActivity;
import com.chunlangjiu.app.goodsmanage.adapter.GoodsPicAdapter;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/16
 * @Describe: 名酒估价页面
 */
public class AppraiseApplyAssessActivity extends BaseActivity {

    public static final int REQUEST_CODE_SELECT_MAIN_PIC = 1001;
    public static final int REQUEST_CODE_SELECT_DETAIL_ONE_PIC = 10021;
    public static final int REQUEST_CODE_SELECT_DETAIL_TWO_PIC = 10022;
    public static final int REQUEST_CODE_SELECT_DETAIL_THREE_PIC = 10023;
    public static final int REQUEST_CODE_SELECT_DETAIL_FOUR_PIC = 10024;
    public static final int REQUEST_CODE_SELECT_GOODS_PIC = 1003;
    private int codeType;
    private String authenticateId ;

    @BindView(R.id.etTitle) EditText etTitle;//商品标题
    @BindView(R.id.etSeries) TextView etSeries;//所属系列
    @BindView(R.id.etYear) EditText etYear;//商品年份
    @BindView(R.id.etOther) EditText etOther;//其他说明

    @BindView(R.id.imgMainEx) ImageView imgMainEx;
    @BindView(R.id.imgMainPic) ImageView imgMainPic;
    @BindView(R.id.llMainPicAdd) LinearLayout llMainPicAdd;
    @BindView(R.id.imgDeleteMainPic) ImageView imgDeleteMainPic;

    @BindView(R.id.imgMainExTwo) ImageView imgMainExTwo;
    @BindView(R.id.imgMainPicTwo) ImageView imgMainPicTwo;
    @BindView(R.id.llMainPicAddTwo) LinearLayout llMainPicAddTwo;
    @BindView(R.id.imgDeleteMainPicTwo) ImageView imgDeleteMainPicTwo;


    @BindView(R.id.gvGoodsList)
    GridView recyclerViewGoods;
    GoodsPicAdapter picAdapter ;
    private ArrayList<ImageItem> mainPicLists;
    private ArrayList<ImageItem> mainPicListsTwo;
    private ArrayList<ImageItem> goodsPicLists = new ArrayList<>();
    private String mainPicOne;
    private String mainPicTwo;

    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private CompositeDisposable disposable;
    private ChoicePhotoDialog photoDialog;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.llMainPicAdd:
                    showPhotoDialog(REQUEST_CODE_SELECT_MAIN_PIC);
                    break;
                case R.id.llMainPicAddTwo:
                    showPhotoDialog(REQUEST_CODE_SELECT_DETAIL_ONE_PIC);
                    break;
                case R.id.rlGoodsPic:
                    showPhotoDialog(REQUEST_CODE_SELECT_GOODS_PIC);
                    break;
                case R.id.imgDeleteMainPic:
                    deleteMainPic();
                    break;
                case R.id.imgDeleteMainPicTwo:
                    deleteDescOnePic();
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
        setContentView(R.layout.appraiser_activity_fine_wine);
        initImagePicker();
        initView();
    }

    public static void startApplyAssessActivity(Activity activity,String authenticateId){
        if(activity!=null){
            Intent intent = new Intent(activity,AppraiseApplyAssessActivity.class);
            intent.putExtra("authenticateId",authenticateId);
            activity.startActivity(intent);
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
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(500);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(500);                         //保存文件的高度。单位像素
    }


    private void initView() {
        authenticateId = getIntent().getStringExtra("authenticateId");
        disposable = new CompositeDisposable();
        setMainPicSize();
        picAdapter = new GoodsPicAdapter(this, goodsPicLists);
        picAdapter.setOnItemChildClickListener(new GoodsPicAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                if(view.getId() == R.id.llAdd){
                    showPhotoDialog(REQUEST_CODE_SELECT_GOODS_PIC);
                }else if(view.getId() == R.id.ivDeleteGoodsPic){
                    goodsPicLists.remove(position);
                    picAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerViewGoods.setAdapter(picAdapter);

        imgMainPic.setOnClickListener(onClickListener);
        llMainPicAdd.setOnClickListener(onClickListener);
        imgDeleteMainPic.setOnClickListener(onClickListener);

        imgMainPicTwo.setOnClickListener(onClickListener);
        llMainPicAddTwo.setOnClickListener(onClickListener);
        imgDeleteMainPicTwo.setOnClickListener(onClickListener);
        tvCommit.setOnClickListener(onClickListener);
    }

    private void setMainPicSize(){
        int picSize = (SizeUtils.getScreenWidth() - 35) / 2;
        List<View> list = new ArrayList<>();
        list.add(imgMainEx);
        list.add(imgMainPic);
        list.add(imgMainExTwo);
        list.add(imgMainPicTwo);
        for (int i = 0; i < list.size(); i++) {
            ViewGroup.LayoutParams layoutParams = list.get(i).getLayoutParams();
            layoutParams.height = picSize;
            layoutParams.height = picSize ;
            list.get(i).setLayoutParams(layoutParams);
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
                        mainPicOne =mainPicLists.get(0).path;
                        llMainPicAdd.setVisibility(View.GONE);
                        imgDeleteMainPic.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(AppraiseApplyAssessActivity.this, mainPicLists.get(0).path, imgMainPic);
                    } else if (requestCode == REQUEST_CODE_SELECT_DETAIL_ONE_PIC) {
                        mainPicListsTwo = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = mainPicListsTwo.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        mainPicTwo = mainPicListsTwo.get(0).path;
                        imgDeleteMainPicTwo.setVisibility(View.VISIBLE);
                        llMainPicAddTwo.setVisibility(View.GONE);
                        GlideUtils.loadImage(AppraiseApplyAssessActivity.this, mainPicListsTwo.get(0).path, imgMainPicTwo);
                    }  else if (requestCode == REQUEST_CODE_SELECT_GOODS_PIC) {
                        ArrayList<ImageItem> tempItem = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = tempItem.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        goodsPicLists.add(imageItem);
                        picAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMainPic() {
        mainPicLists = null;
        mainPicOne = null;
        llMainPicAdd.setVisibility(View.VISIBLE);
        imgDeleteMainPic.setVisibility(View.GONE);
    }

    private void deleteDescOnePic() {
        mainPicListsTwo = null;
        mainPicTwo = null;
        llMainPicAddTwo.setVisibility(View.VISIBLE);
        imgDeleteMainPicTwo.setVisibility(View.GONE);
    }


    private void checkData() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            ToastUtils.showShort("请填写商品标题");
        } else if (TextUtils.isEmpty(etSeries.getText().toString().trim())) {
            ToastUtils.showShort("请填写所属系列");
        } else if (TextUtils.isEmpty(etYear.getText().toString().trim())) {
            ToastUtils.showShort("请填写所属年份");
        } else if (mainPicOne == null || mainPicTwo == null) {
            ToastUtils.showShort("请上传商品主图");
        } else if (goodsPicLists!= null && goodsPicLists.size()<3) {
            ToastUtils.showShort("最少上传五张图");
        }else {
            uploadImageNew();
        }
    }

    private void uploadImageNew() {
        showLoadingDialog();
        final List<String> base64Lists = new ArrayList<>();
        List<String> nameLists = new ArrayList<>();
        final List<String> imageLists = new ArrayList<>();
        if (mainPicOne != null) {
            base64Lists.add(mainPicOne);
            nameLists.add(mainPicLists.get(0).name);
        }
        if (mainPicTwo != null) {
            base64Lists.add(mainPicTwo);
            nameLists.add(mainPicListsTwo.get(0).name);
        }
        //一定要上传的五张主图
        Observable<ResultBean<UploadImageBean>> main = ApiUtils.getInstance().shopUploadImage(FileUtils.imgToBase64(mainPicLists.get(0).path), mainPicLists.get(0).name);
        Observable<ResultBean<UploadImageBean>> detailOne = ApiUtils.getInstance().shopUploadImage(FileUtils.imgToBase64(mainPicListsTwo.get(0).path), mainPicListsTwo.get(0).name);
        ArrayList<Observable<ResultBean<UploadImageBean>>> tempList = new ArrayList();
        tempList.add(main);
        tempList.add(detailOne);
        //可选上传的图片
        if(goodsPicLists!= null && goodsPicLists.size()>0){
            for (ImageItem imageItem:goodsPicLists) {
                String base64DetailFour = FileUtils.imgToBase64(imageItem.path);
                Observable<ResultBean<UploadImageBean>> detailsGoods = ApiUtils.getInstance().shopUploadImage(base64DetailFour, imageItem.name);
                tempList.add(detailsGoods);
            }
        }


        disposable.add(Observable.zip(tempList, new Function<Object[], String>() {
            @Override
            public String apply(Object[] result) throws Exception {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    ResultBean<UploadImageBean> resultBeans = (ResultBean<UploadImageBean>)result[i];
                    stringBuffer.append(resultBeans.getData().getUrl());
                    if (i != result.length - 1) {
                        stringBuffer.append(",");
                    }
                }
                return stringBuffer.toString();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String strings) throws Exception {
                        if(!TextUtils.isEmpty(strings)){
                            valuationGoods(strings);
                        }else{
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

    private void valuationGoods(String imgUrls) {
        String title = etTitle.getText().toString();
        String series = etSeries.getText().toString();
        String year = etYear.getText().toString();
        String other = etOther.getText().toString();
        disposable.add(ApiUtils.getInstance().assessGoods(authenticateId,title,series,year,other,imgUrls)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        startActivity(new Intent(AppraiseApplyAssessActivity.this, ValuationSuccessActivity.class));
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
