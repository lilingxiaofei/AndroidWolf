package com.chunlangjiu.app.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.adapter.OrderEvaluationPicAdapter;
import com.chunlangjiu.app.order.bean.OrderAfterSaleReasonBean;
import com.chunlangjiu.app.order.bean.OrderDetailBean;
import com.chunlangjiu.app.order.bean.OrderEvaluationPicBean;
import com.chunlangjiu.app.order.dialog.OrderAfterSaleReasonDialog;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.user.bean.UploadImageBean;
import com.chunlangjiu.app.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pkqup.commonlibrary.dialog.ChoicePhotoDialog;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.FileUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OrderApplyForAfterSaleActivity extends BaseActivity {

    @BindView(R.id.etNote)
    EditText etNote;
    @BindView(R.id.llProducts)
    LinearLayout llProducts;
    @BindView(R.id.tvCommit)
    TextView tvCommit;
    @BindView(R.id.tvAfterSaleNum)
    TextView tvAfterSaleNum;
    @BindView(R.id.llAfterSaleReason)
    LinearLayout llAfterSaleReason;
    @BindView(R.id.tvAfterSaleReason)
    TextView tvAfterSaleReason;

    public static final int REQUEST_CODE_SELECT_PIC = 1001;
    public static final int REQUEST_CODE_CAMERA_PIC = 1002;
    @BindView(R.id.gvPhoto)
    GridView gvPhoto;
    private int selectLimit = 3;
    private ChoicePhotoDialog photoDialog;
    private List<OrderEvaluationPicBean> orderEvaluationPicBeanList;
    private OrderEvaluationPicAdapter orderEvaluationPicAdapter;
    private int longClickPosition;
    private CommonConfirmDialog commonConfirmDialog;

    private CompositeDisposable disposable;
    private List<String> uploadImageUrls;
    private int orderEvaluationPicBeanListSize;

    private String oid;
    private String tid;
    private OrderAfterSaleReasonDialog orderAfterSaleReasonDialog;
    private String reason;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_applyforaftersale);

        initImagePicker();
        initData();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText("申请售后");
    }

    private void initData() {
        uploadImageUrls = new ArrayList<>();
        disposable = new CompositeDisposable();

        OrderDetailBean.OrdersBean orderBean = (OrderDetailBean.OrdersBean) getIntent().getSerializableExtra(OrderParams.PRODUCTS);
        oid = String.valueOf(orderBean.getOid());
        tid = getIntent().getStringExtra(OrderParams.ORDERID);

        LayoutInflater inflater = LayoutInflater.from(this);
        View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
        ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
        GlideUtils.loadImage(getApplicationContext(), orderBean.getPic_path(), imgProduct);
        TextView tvProductName = inflate.findViewById(R.id.tvProductName);
        tvProductName.setText(orderBean.getTitle());
        TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
        tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderBean.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
        tvProductDesc.setText(orderBean.getSpec_nature_info());
        TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
        tvProductNum.setText(String.format("x%d", orderBean.getNum()));
        llProducts.addView(inflate);

        tvAfterSaleNum.setText(String.valueOf(orderBean.getNum()));

        orderEvaluationPicBeanList = new ArrayList<>();
        orderEvaluationPicAdapter = new OrderEvaluationPicAdapter(this, orderEvaluationPicBeanList);
        gvPhoto.setAdapter(orderEvaluationPicAdapter);
        gvPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longClickPosition = i;
                OrderEvaluationPicBean item = orderEvaluationPicAdapter.getItem(i);
                if (item != null) {
                    if (null == commonConfirmDialog) {
                        commonConfirmDialog = new CommonConfirmDialog(OrderApplyForAfterSaleActivity.this, "是否取消上传该图片？");
                        commonConfirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                            @Override
                            public void onConfirm() {
                                orderEvaluationPicBeanList.remove(longClickPosition);
                                ImagePicker.getInstance().removeSelectedImage(longClickPosition);
                                orderEvaluationPicAdapter.updateData(orderEvaluationPicBeanList);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                    commonConfirmDialog.show();
                }
                return true;
            }
        });
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderEvaluationPicBean item = orderEvaluationPicAdapter.getItem(i);
                if (item == null) {
                    showPhotoDialog();
                }
            }
        });
        tvCommit.setOnClickListener(onClickListener);
        llAfterSaleReason.setOnClickListener(onClickListener);

        getAfterSaleReason();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.llAfterSaleReason:
                    if (null != orderAfterSaleReasonDialog) {
                        orderAfterSaleReasonDialog.show();
                    }
                    break;
                case R.id.tvCommit:
                    commit();
                    break;
            }
        }
    };

    private void getAfterSaleReason() {
        disposable.add(ApiUtils.getInstance().getAfterSaleReason(oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderAfterSaleReasonBean>>() {
                    @Override
                    public void accept(ResultBean<OrderAfterSaleReasonBean> OrderAfterSaleReasonBean) throws Exception {
                        List<String> list = OrderAfterSaleReasonBean.getData().getReason();
                        orderAfterSaleReasonDialog = new OrderAfterSaleReasonDialog(OrderApplyForAfterSaleActivity.this, list);
                        orderAfterSaleReasonDialog.setCancelCallBack(new OrderAfterSaleReasonDialog.CancelCallBack() {
                            @Override
                            public void confirmReason(String reason) {
                                tvAfterSaleReason.setText(reason);
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void commit() {
        reason = tvAfterSaleReason.getText().toString();
        note = etNote.getText().toString();
        if ("其他原因".equals(reason)) {
            if (note.isEmpty()) {
                ToastUtils.showShort("请输入备注");
                return;
            }
        } else if ("请选择".equals(reason)) {
            ToastUtils.showShort("请选择售后原因");
            return;
        }
        uploadImageUrls.clear();

        if (orderEvaluationPicBeanList.size() > 0) {
            showLoadingDialog();
            ArrayList<Observable<ResultBean<UploadImageBean>>> tempList = new ArrayList();
            //可选上传的图片
            for (OrderEvaluationPicBean imageItem : orderEvaluationPicBeanList) {
                String base64Data = imageItem.getBase64Data();
                String name = imageItem.getName();
                Observable<ResultBean<UploadImageBean>> detailsGoods = ApiUtils.getInstance().userUploadImage(base64Data, name, "aftersales");
                tempList.add(detailsGoods);
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
                                uploadImageUrls = strings;
                                commitContent();
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
        } else {
            showLoadingDialog();
            commitContent();
        }
    }

    private void commitContent() {
        StringBuilder ratePic = new StringBuilder();
        for (int i = 0; i <= uploadImageUrls.size() - 1; i++) {
            if (i == uploadImageUrls.size() - 1) {
                ratePic.append(uploadImageUrls.get(i));
            } else {
                ratePic.append(uploadImageUrls.get(i)).append(",");
            }
        }
        disposable.add(ApiUtils.getInstance().applyAfterSaleReason(tid, oid, reason, note, ratePic.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean uploadImageBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == uploadImageBeanResultBean.getErrorcode()) {
                            ToastUtils.showShort("申请售后成功");
                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_DETAIL);
                            finish();
                        } else {
                            if (TextUtils.isEmpty(uploadImageBeanResultBean.getMsg())) {
                                ToastUtils.showShort("申请售后失败");
                            } else {
                                ToastUtils.showShort(uploadImageBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("申请售后失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                        hideLoadingDialog();
                    }
                }));
    }

    private void showPhotoDialog() {
        if (photoDialog == null) {
            photoDialog = new ChoicePhotoDialog(this);
            photoDialog.setCallBackListener(new ChoicePhotoDialog.OnCallBackListener() {
                @Override
                public void takePhoto() {
                    openCamera(REQUEST_CODE_CAMERA_PIC);
                }

                @Override
                public void toPhotoAlbum() {
                    openAlbum(REQUEST_CODE_SELECT_PIC);
                }
            });
        }
        photoDialog.show();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
//        imagePicker.setMultiMode(true);
        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(3);                        //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(500);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(500);                         //保存文件的高度。单位像素
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        intent.putExtra(ImageGridActivity.NEED_CLEAR, false);
        startActivityForResult(intent, requestCode);
    }

    private void openAlbum(int requestCode) {
        Intent intent1 = new Intent(this, ImageGridActivity.class);
        ImagePicker.getInstance().setSelectLimit(getSelectLimit());
        intent1.putExtra(ImageGridActivity.NEED_CLEAR, false);
        startActivityForResult(intent1, requestCode);
    }

    private int getSelectLimit() {
        int cameraSize = 0;
        for (OrderEvaluationPicBean picBean : orderEvaluationPicBeanList) {
            if (picBean.isCamera()) {
                cameraSize++;

            }
        }
        return selectLimit - cameraSize;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == REQUEST_CODE_SELECT_PIC || REQUEST_CODE_CAMERA_PIC == requestCode) {
//                        if(requestCode == REQUEST_CODE_SELECT_PIC ){
//                            orderEvaluationPicBeanList.clear();
//                        }
                        ArrayList<ImageItem> pics = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        for (ImageItem imageItem : pics) {
                            OrderEvaluationPicBean orderEvaluationPicBean = new OrderEvaluationPicBean();
                            orderEvaluationPicBean.setAddButton(false);
                            int index = imageItem.path.lastIndexOf("/");
                            imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                            orderEvaluationPicBean.setBase64Data(FileUtils.imgToBase64(imageItem.path));
                            orderEvaluationPicBean.setPicPath(imageItem.path);
                            orderEvaluationPicBean.setName(imageItem.name);
                            orderEvaluationPicBean.setCamera(REQUEST_CODE_CAMERA_PIC == requestCode);
                            orderEvaluationPicBeanList.add(orderEvaluationPicBean);
                        }
                        orderEvaluationPicAdapter.updateData(orderEvaluationPicBeanList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        ImagePicker.getInstance().clear();
        super.onDestroy();
    }

}
