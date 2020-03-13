package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.activity.MainActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AddressListDetailBean;
import com.chunlangjiu.app.user.bean.LocalAreaBean;
import com.chunlangjiu.app.util.AreaUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.choicearea.BottomDialog;
import com.pkqup.commonlibrary.view.choicearea.DataProvider;
import com.pkqup.commonlibrary.view.choicearea.ISelectAble;
import com.pkqup.commonlibrary.view.choicearea.SelectedListener;
import com.pkqup.commonlibrary.view.choicearea.Selector;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe:
 */
public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.rlChoiceArea)
    RelativeLayout rlChoiceArea;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.imgSetDefault)
    ImageView imgSetDefault;
    @BindView(R.id.tvSave)
    TextView tvSave;

    private CompositeDisposable disposable;
    private List<LocalAreaBean.ProvinceData> areaLists;
    private BottomDialog areaDialog;
    private boolean isEditAddress = false;
    private boolean isDefault = false;
    private String addressId;
    private String provinceId;
    private String cityId;
    private String districtId;
    private int provinceIndex;
    private int cityIndex;

    public static void startEditAddressActivity(Activity activity, AddressListDetailBean addressListDetailBean) {
        Intent intent = new Intent(activity, AddAddressActivity.class);
        intent.putExtra("address", addressListDetailBean);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.rlChoiceArea://选择省市区
                    showAreaDialog();
                    break;
                case R.id.imgSetDefault://设置为默认地址
                    setDefault();
                    break;
                case R.id.tvSave://提交
                    commit();
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        titleName.setText("添加地址");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_add_address);
        initView();
        initData();
        isEditAddress();
    }


    private void initView() {
        rlChoiceArea.setOnClickListener(onClickListener);
        imgSetDefault.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
    }

    private void initData() {
        disposable = new CompositeDisposable();
        disposable.add(Observable.create(new ObservableOnSubscribe<List<LocalAreaBean.ProvinceData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalAreaBean.ProvinceData>> e) throws Exception {
                List<LocalAreaBean.ProvinceData> json = AreaUtils.getJson(AddAddressActivity.this);
                e.onNext(json);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    private void isEditAddress() {
        AddressListDetailBean addressListDetailBean = (AddressListDetailBean) getIntent().getSerializableExtra("address");
        if (addressListDetailBean != null) {
            titleName.setText("修改地址");
            isEditAddress = true;
            addressId = addressListDetailBean.getAddr_id();
            isDefault = addressListDetailBean.getDef_addr().equals("1");
            etName.setText(addressListDetailBean.getName());
            etPhone.setText(addressListDetailBean.getMobile());
            String area = addressListDetailBean.getArea().replace("/", " ");
            tvArea.setText(area);
            etAddress.setText(addressListDetailBean.getAddr());
            imgSetDefault.setSelected(addressListDetailBean.getDef_addr().equals("1"));

            String region_id = addressListDetailBean.getRegion_id();
            String[] split = region_id.split(",");

            provinceId = split[0];
            cityId = split[1];
            districtId = split[2];
        }
    }

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
                    areaName = selectAbles.get(1).getName();
                } else {
                    areaName = selectAbles.get(0).getName() + " " + selectAbles.get(1).getName();
                }
                provinceId = ((LocalAreaBean.ProvinceData) (selectAbles.get(0).getArg())).getId();
                cityId = ((LocalAreaBean.ProvinceData.City) (selectAbles.get(1).getArg())).getId();

                if(selectAbles.size()>=3 && null != selectAbles.get(2)){
                    areaName += " " + selectAbles.get(2).getName();
                    districtId = ((LocalAreaBean.ProvinceData.City.District) (selectAbles.get(2).getArg())).getId();
                }
                tvArea.setText(areaName);
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
        try {
            List<LocalAreaBean.ProvinceData.City> araListOne = areaLists.get(provinceIndex).getChildren();
            final List<LocalAreaBean.ProvinceData.City.District> disList = araListOne.get(cityIndex).getChildren();
            if(null != disList){
                for (int j = 0; j < disList.size(); j++) {
                    final int finalJ = j;
                    data.add(new ISelectAble() {
                        @Override
                        public String getName() {
                            return disList.get(finalJ).getValue();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private void setDefault() {
        isDefault = !isDefault;
        imgSetDefault.setSelected(isDefault);
    }

    private void commit() {
        showLoadingDialog();
        if (isEditAddress) {
            disposable.add(ApiUtils.getInstance().editAddress(etName.getText().toString(), etPhone.getText().toString(),
                    provinceId + "," + cityId + "," + districtId, etAddress.getText().toString(), isDefault ? "1" : "0", addressId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("修改地址成功");
                            EventManager.getInstance().notify(null, ConstantMsg.ADD_ADDRESS_SUCCESS);
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));
        } else {
            disposable.add(ApiUtils.getInstance().newAddress(etName.getText().toString(), etPhone.getText().toString(),
                    provinceId + "," + cityId + "," + districtId, etAddress.getText().toString(), isDefault ? "1" : "0")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("添加地址成功");
                            EventManager.getInstance().notify(null, ConstantMsg.ADD_ADDRESS_SUCCESS);
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

}
