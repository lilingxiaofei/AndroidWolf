package com.chunlangjiu.app.user.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.BankCardInfoBean;
import com.chunlangjiu.app.user.bean.LocalAreaBean;
import com.chunlangjiu.app.util.AreaUtils;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.ClearEditText;
import com.pkqup.commonlibrary.view.choicearea.BottomDialog;
import com.pkqup.commonlibrary.view.choicearea.DataProvider;
import com.pkqup.commonlibrary.view.choicearea.ISelectAble;
import com.pkqup.commonlibrary.view.choicearea.SelectedListener;
import com.pkqup.commonlibrary.view.choicearea.Selector;

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
import io.reactivex.schedulers.Schedulers;

import static com.chunlangjiu.app.util.ConstantMsg.BANKCARD_CHANGE;

public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.edtName)
    ClearEditText edtName;
    @BindView(R.id.edtIdCard)
    ClearEditText edtIdCard;
    @BindView(R.id.edtBankCard)
    ClearEditText edtBankCard;
    @BindView(R.id.edtBranch)
    ClearEditText edtBranch;
    @BindView(R.id.edtPhone)
    ClearEditText edtPhone;
    @BindView(R.id.edtCode)
    ClearEditText edtCode;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.edtBankName)
    EditText edtBankName;
    @BindView(R.id.tvProvince)
    TextView tvProvince;
    @BindView(R.id.tvCity)
    TextView tvCity;

    private CompositeDisposable disposable;
    private List<LocalAreaBean.ProvinceData> areaLists;
    private int provinceIndex;
    private int cityIndex;
    private String provinceId;
    private String cityId;
    private String districtId;
    private BottomDialog areaDialog;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        initData();
        initEvent();

//        edtName.setText("吴申飞");
//        edtIdCard.setText("430281199101039116");
//        edtBankCard.setText("6217921175562548");
//        edtPhone.setText("18665398952");
//        edtBranch.setText("深圳科苑支行");
    }

    private void initEvent() {
        edtBankCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>= 8){
                    Log.i("bankCardId",s.toString());
                    getBankCardInfo(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setTitleView() {
        titleName.setText("添加银行卡");
    }

    @OnClick({R.id.img_title_left, R.id.btnBindBankCard, R.id.tvCity, R.id.tvProvince,R.id.btnGetCode})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.btnBindBankCard:
                addBankCardList();
                break;
            case R.id.tvCity:
                showAreaDialog();
                break;
            case R.id.tvProvince:
                showAreaDialog();
                break;
            case R.id.btnGetCode:
                String mobile = edtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)){
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                countDownTime();
                sendCode();
                break;
        }
    }

    private void initData() {
        disposable = new CompositeDisposable();
        disposable.add(Observable.create(new ObservableOnSubscribe<List<LocalAreaBean.ProvinceData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocalAreaBean.ProvinceData>> e) throws Exception {
                List<LocalAreaBean.ProvinceData> json = AreaUtils.getJson(AddBankCardActivity.this);
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
    private void sendCode(){
        //sendSmsCode
        disposable.add(ApiUtils.getInstance().sendSms((String) SPUtils.get("token",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }
    private void countDownTime() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millsTime) {
                if (millsTime / 1000 == 60) {
                    btnGetCode.setText("59s");
                } else {
                    btnGetCode.setText(millsTime / 1000 + "s");
                }
            }

            @Override
            public void onFinish() {
                btnGetCode.setText("获取验证码");
                btnGetCode.setClickable(true);
                btnGetCode.setBackgroundResource(R.drawable.bg_round_red);
            }
        };
        countDownTimer.start();
        btnGetCode.setClickable(false);
        btnGetCode.setBackgroundResource(R.drawable.bg_round_gray);
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
    private void getBankCardInfo(String cardId){
        disposable.add(ApiUtils.getInstance().getBankCardInfo((String) SPUtils.get("token", ""), cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<BankCardInfoBean>>() {
                    @Override
                    public void accept(ResultBean<BankCardInfoBean> resultBean) throws Exception {
                        if (null!=resultBean&&null!=resultBean.getData()){
                            BankCardInfoBean bankCardInfoBean = resultBean.getData();
                            edtBankName.setText(bankCardInfoBean.getBankname());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    private void addBankCardList() {
        String name = edtName.getText().toString().trim();
        String bank =edtBankName.getText().toString().trim();
        String card = edtBankCard.getText().toString().trim();
        String bank_branch =edtBranch.getText().toString().trim();
        String idcard = edtIdCard.getText().toString().trim();
        String mobile = edtPhone.getText().toString().trim();
        String verifycode = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            ToastUtils.showShort("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(idcard)){
            ToastUtils.showShort("请输入身份证");
            return;
        }
        if (TextUtils.isEmpty(card)){
            ToastUtils.showShort("请输入银行卡号");
            return;
        }
        if (TextUtils.isEmpty(bank)){
            ToastUtils.showShort("请输入银行卡开户行");
            return;
        }

        if (TextUtils.isEmpty(bank_branch)){
            ToastUtils.showShort("请输入支行");
            return;
        }

        if (TextUtils.isEmpty(mobile)){
            ToastUtils.showShort("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(verifycode)){
            ToastUtils.showShort("请输入验证码");
            return;
        }
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().addBankCardList((String) SPUtils.get("token", ""), name, bank, card
                , bank_branch, idcard, mobile, verifycode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("添加成功");
                        EventManager.getInstance().notify(null,BANKCARD_CHANGE);
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
                tvProvince.setText(((LocalAreaBean.ProvinceData) (selectAbles.get(0).getArg())).getValue());
                tvCity.setText(((LocalAreaBean.ProvinceData.City) (selectAbles.get(1).getArg())).getValue());
                provinceId = ((LocalAreaBean.ProvinceData) (selectAbles.get(0).getArg())).getId();
                cityId = ((LocalAreaBean.ProvinceData.City) (selectAbles.get(1).getArg())).getId();
                districtId = ((LocalAreaBean.ProvinceData.City.District) (selectAbles.get(2).getArg())).getId();
//                tvArea.setText(areaName);
                areaDialog.dismiss();
            }
        });

        if (areaDialog == null) {
            areaDialog = new BottomDialog(this);
            areaDialog.init(this, selector);
        }
        areaDialog.show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
