package com.chunlangjiu.app.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.adapter.OrderEvaluationPicAdapter;
import com.chunlangjiu.app.order.bean.OrderEvaluationPicBean;
import com.chunlangjiu.app.order.bean.OrderListBean;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderEvaluationDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE_SELECT_PIC = 1001;
    @BindView(R.id.imgStore)
    ImageView imgStore;
    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.llProducts)
    LinearLayout llProducts;
    @BindView(R.id.rbEvaluation)
    RatingBar rbEvaluation;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvCommit)
    TextView tvCommit;
    @BindView(R.id.cbAnonymous)
    CheckBox cbAnonymous;

    private int codeType;
    private ChoicePhotoDialog photoDialog;
    private List<OrderEvaluationPicBean> orderEvaluationPicBeanList;
    private OrderEvaluationPicAdapter orderEvaluationPicAdapter;
    @BindView(R.id.gvPhoto)
    GridView gvPhoto;
    private OrderEvaluationPicBean orderEvaluationPicBean;

    private CommonConfirmDialog commonConfirmDialog;
    private int longClickPosition;

    private CompositeDisposable disposable;

    private List<String> uploadImageUrls;
    private int orderEvaluationPicBeanListSize;

    private OrderListBean.ListBean listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_evaluationdetail);

        initImagePicker();
        initData();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText("商品评价");
    }

    private void initData() {
        uploadImageUrls = new ArrayList<>();
        disposable = new CompositeDisposable();
        listBean = (OrderListBean.ListBean) getIntent().getSerializableExtra(OrderParams.PRODUCTS);

        GlideUtils.loadImage(getApplicationContext(), listBean.getShop_logo(), imgStore);
        tvStore.setText(listBean.getShopname());

        List<OrderListBean.ListBean.OrderBean> order = listBean.getOrder();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i <= order.size() - 1; i++) {
            OrderListBean.ListBean.OrderBean orderBean = order.get(i);
            View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
            ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
            GlideUtils.loadImage(getApplicationContext(), orderBean.getPic_path(), imgProduct);
            TextView tvProductName = inflate.findViewById(R.id.tvProductName);
            tvProductName.setText(orderBean.getTitle());
            TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
            tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderBean.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
            tvProductDesc.setText(orderBean.getSpec_nature_info());
            TextView tvAfterSale = inflate.findViewById(R.id.tvAfterSale);
            tvAfterSale.setVisibility(View.GONE);
            TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
            tvProductNum.setText(String.format("x%d", orderBean.getNum()));
            llProducts.addView(inflate);
//            if (llProducts.getChildCount() == order.size()) {
//                View view_line = inflate.findViewById(R.id.view_line);
//                view_line.setVisibility(View.GONE);
//            }
        }

        orderEvaluationPicBeanList = new ArrayList<>();
        orderEvaluationPicBean = new OrderEvaluationPicBean();
        orderEvaluationPicBean.setAddButton(true);
        orderEvaluationPicBeanList.add(orderEvaluationPicBean);
        orderEvaluationPicAdapter = new OrderEvaluationPicAdapter(this, orderEvaluationPicBeanList);
        gvPhoto.setAdapter(orderEvaluationPicAdapter);
        gvPhoto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                longClickPosition = i;
                OrderEvaluationPicBean item = orderEvaluationPicBeanList.get(i);
                if (!item.isAddButton()) {
                    if (null == commonConfirmDialog) {
                        commonConfirmDialog = new CommonConfirmDialog(OrderEvaluationDetailActivity.this, "是否取消上传该图片？");
                        commonConfirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                            @Override
                            public void onConfirm() {
                                orderEvaluationPicBeanList.remove(longClickPosition);
                                ImagePicker.getInstance().removeSelectedImage(longClickPosition);
                                boolean hasAddButton = false;
                                for (OrderEvaluationPicBean orderEvaluationPicBean : orderEvaluationPicBeanList) {
                                    if (orderEvaluationPicBean.isAddButton()) {
                                        hasAddButton = true;
                                    }
                                }
                                if (!hasAddButton) {
                                    orderEvaluationPicBeanList.add(orderEvaluationPicBean);
                                }
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
                if (item.isAddButton()) {
                    showPhotoDialog(REQUEST_CODE_SELECT_PIC);
                }
            }
        });
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

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(true);
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
        intent1.putExtra(ImageGridActivity.NEED_CLEAR, false);
        startActivityForResult(intent1, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                //添加图片返回
                if (data != null) {
                    if (requestCode == REQUEST_CODE_SELECT_PIC) {
                        ArrayList<ImageItem> pics = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        orderEvaluationPicBeanList.clear();
                        for (ImageItem imageItem : pics) {
                            OrderEvaluationPicBean orderEvaluationPicBean = new OrderEvaluationPicBean();
                            orderEvaluationPicBean.setAddButton(false);
                            int index = imageItem.path.lastIndexOf("/");
                            imageItem.name = imageItem.path.substring(index + 1, imageItem.path.length());
                            orderEvaluationPicBean.setBase64Data(FileUtils.imgToBase64(imageItem.path));
                            orderEvaluationPicBean.setPicPath(imageItem.path);
                            orderEvaluationPicBean.setName(imageItem.name);
                            orderEvaluationPicBeanList.add(orderEvaluationPicBean);
                        }
                        if (orderEvaluationPicBeanList.size() < 3) {
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvCommit:
                    commit();
                    break;
            }
        }
    };

    private void commit() {
        String content = etContent.getText().toString();
        if (content.isEmpty()) {
            ToastUtils.showShort("请输入评价内容");
            return;
        }
        uploadImageUrls.clear();
        if (orderEvaluationPicBeanList.size() > 1) {
            showLoadingDialog();
            orderEvaluationPicBeanListSize = 0;
            for (OrderEvaluationPicBean orderEvaluationPicBean : orderEvaluationPicBeanList) {
                if (!orderEvaluationPicBean.isAddButton()) {
                    orderEvaluationPicBeanListSize++;
                    String base64Data = orderEvaluationPicBean.getBase64Data();
                    String name = orderEvaluationPicBean.getName();
                    disposable.add(ApiUtils.getInstance().userUploadImage(base64Data, name, "rate")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ResultBean<UploadImageBean>>() {
                                @Override
                                public void accept(ResultBean<UploadImageBean> uploadImageBeanResultBean) throws Exception {
                                    uploadImageUrls.add(uploadImageBeanResultBean.getData().getUrl());
                                    if (uploadImageUrls.size() == orderEvaluationPicBeanListSize) {
                                        commitContent();
                                    } else {
                                        if (TextUtils.isEmpty(uploadImageBeanResultBean.getMsg())) {
                                            ToastUtils.showShort("上传图片失败");
                                        } else {
                                            ToastUtils.showShort(uploadImageBeanResultBean.getMsg());
                                        }
                                        hideLoadingDialog();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if (TextUtils.isEmpty(throwable.getMessage())) {
                                        ToastUtils.showShort("上传图片失败");
                                    } else {
                                        ToastUtils.showShort(throwable.getMessage());
                                    }
                                    hideLoadingDialog();
                                }
                            }));
                }
            }
        } else {
            showLoadingDialog();
            commitContent();
        }
    }

    private void commitContent() {
        JSONArray jsonArray = new JSONArray();
        try {
            List<OrderListBean.ListBean.OrderBean> order = listBean.getOrder();
            for (OrderListBean.ListBean.OrderBean orderBean : order) {
                JSONObject object = new JSONObject();
                object.put("oid", String.valueOf(orderBean.getOid()));
                StringBuilder ratePic = new StringBuilder();
                for (int i = 0; i <= uploadImageUrls.size() - 1; i++) {
                    if (i == uploadImageUrls.size() - 1) {
                        ratePic.append(uploadImageUrls.get(i));
                    } else {
                        ratePic.append(uploadImageUrls.get(i)).append(",");
                    }
                }
                object.put("rate_pic", ratePic.toString());
                int rating = new BigDecimal(rbEvaluation.getRating()).intValue();
                switch (rating) {
                    case 1:
                    case 2:
                        object.put("result", "bad");
                        break;
                    case 3:
                        object.put("result", "neutral");
                        break;
                    case 4:
                    case 5:
                        object.put("result", "good");
                        break;
                }
                object.put("content", etContent.getText().toString());
                jsonArray.put(object);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        disposable.add(ApiUtils.getInstance().addRate(String.valueOf(listBean.getTid()), jsonArray, cbAnonymous.isChecked(), 5, 5, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean uploadImageBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == uploadImageBeanResultBean.getErrorcode()) {
                            ToastUtils.showShort("商品评价成功");
                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_DETAIL);
                            finish();
                        } else {
                            if (TextUtils.isEmpty(uploadImageBeanResultBean.getMsg())) {
                                ToastUtils.showShort("商品评价失败");
                            } else {
                                ToastUtils.showShort(uploadImageBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("商品评价失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                        hideLoadingDialog();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        ImagePicker.getInstance().clear();
        super.onDestroy();
    }
}
