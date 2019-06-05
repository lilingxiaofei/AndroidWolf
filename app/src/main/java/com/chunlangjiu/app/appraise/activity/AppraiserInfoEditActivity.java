package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 新手必看
 */
public class AppraiserInfoEditActivity extends BaseActivity {
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAppraiseScope)
    EditText etAppraiseScope;
    @BindView(R.id.etAppraiseRequire)
    EditText etAppraiseRequire;
    @BindView(R.id.etAppraiseAttention)
    EditText etAppraiseAttention;


    AppraiseBean appraiseBean;
    private String headUrl;
    private String headName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_edit);
        initView();
    }

    public static void startAppraiserInfoEditActivity(Activity activity, AppraiseBean appraiseBean, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, AppraiserInfoEditActivity.class);
            intent.putExtra("appraiseBean", appraiseBean);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    private void initView() {
        appraiseBean = (AppraiseBean) getIntent().getSerializableExtra("appraiseBean");
        updateView();
        imgHead.setOnClickListener(onClickListener);
    }


    @Override
    public void setTitleView() {
        titleName.setText(R.string.edit_info);
        titleImgLeft.setOnClickListener(onClickListener);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.done);
        tvRight.setOnClickListener(onClickListener);
    }

    private void updateView() {
        if (appraiseBean != null) {
            headUrl = appraiseBean.getAuthenticate_img();
            GlideUtils.loadImageHead(AppraiserInfoEditActivity.this, headUrl, imgHead);
            etName.setText(appraiseBean.getAuthenticate_name());
            etAppraiseScope.setText(appraiseBean.getAuthenticate_scope());
            etAppraiseRequire.setText(appraiseBean.getAuthenticate_require());
            etAppraiseAttention.setText(appraiseBean.getAuthenticate_content());
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.img_title_left) {
                finish();
            } else if (view.getId() == R.id.imgHead) {
                setHeadIcon();
            } else if (view.getId() == R.id.tv_right) {
                if (TextUtils.isEmpty(headUrl) && CommonUtils.isNetworkPic(headUrl)) {
                    editAppraiserInfo();
                } else {
                    uploadHeadIcon();
                }
            }
        }
    };

    private ChoicePhotoDialog photoDialog;

    private void setHeadIcon() {
        if (photoDialog == null) {
            photoDialog = new ChoicePhotoDialog(this);
            photoDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    initImagePicker();
                    openCamera(ConstantMsg.REQUEST_CODE_CHOICE_HEAD);
                }

                @Override
                public void toPhotoAlbum() {
                    initImagePicker();
                    openAlbum(ConstantMsg.REQUEST_CODE_CHOICE_HEAD);
                }
            });
        }
        photoDialog.show();
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

    private void uploadHeadIcon() {
        showLoadingDialog();
        String base64Head = FileUtils.imgToBase64(headUrl);
        disposable.add(ApiUtils.getInstance().userUploadImage(base64Head, headName, "rate")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UploadImageBean>>() {
                    @Override
                    public void accept(ResultBean<UploadImageBean> uploadImageBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        headUrl = uploadImageBeanResultBean.getData().getUrl();
                        editAppraiserInfo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("上传头像失败");
                    }
                }));
    }


    private void editAppraiserInfo() {
        String name = etName.getText().toString();
        String scope = etAppraiseScope.getText().toString();
        String require = etAppraiseRequire.getText().toString();
        String attention = etAppraiseAttention.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入鉴定师名称");
            return;
        } else if (TextUtils.isEmpty(scope)) {
            ToastUtils.showShort("请输入鉴定范围");
            return;
        } else if (TextUtils.isEmpty(require)) {
            ToastUtils.showShort("请输入鉴定要求");
            return;
        } else if (TextUtils.isEmpty(attention)) {
            ToastUtils.showShort("请输入注意事项");
            return;
        }


        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().updateAppraiserInfo(name, scope, require, attention, headUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (resultBean.getErrorcode() == 0) {
                            ToastUtils.showShort("修改成功");
                            setResult(Activity.RESULT_OK,getIntent());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == ConstantMsg.REQUEST_CODE_CHOICE_HEAD) {
                        ArrayList<ImageItem> mainPicLists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        ImageItem imageItem = mainPicLists.get(0);
                        int index = imageItem.path.lastIndexOf("/");
                        imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                        headUrl = mainPicLists.get(0).path;
                        headName = imageItem.name;
                        GlideUtils.loadImageHead(AppraiserInfoEditActivity.this, headUrl, imgHead);
//                        String base64Head = FileUtils.imgToBase64(mainPicLists.get(0).path);
//                        uploadHeadIcon(mainPicLists, base64Head);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
