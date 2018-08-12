package com.chunlangjiu.app.net;

import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.goods.bean.GoodsListBean;
import com.chunlangjiu.app.user.bean.AddressListBean;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe: 请求接口
 */
public interface ApiService {

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<LoginBean>> login(@Field("method") String method, @Field("v") String v, @Field("account") String mobile, @Field("verifycode") String verifycode);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<MainClassBean>> getClass(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<GoodsListBean>> getGoodsList(@Field("method") String method, @Field("v") String v, @Field("cat_id") String cat_id,
                                                     @Field("page_no") int page_no, @Field("page_size") int page_size,
                                                     @Field("orderBy") String orderBy);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> getAuthSms(@Field("method") String method, @Field("v") String v, @Field("mobile") String mobile);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean<AddressListBean>> getAddressList(@Field("method") String method, @Field("v") String v);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> newAddress(@Field("method") String method, @Field("v") String v,
                                    @Field("name") String name, @Field("mobile") String mobile,
                                    @Field("area") String area, @Field("addr") String addr, @Field("def_addr") String def_addr);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> editAddress(@Field("method") String method, @Field("v") String v,
                                     @Field("name") String name, @Field("mobile") String mobile,
                                     @Field("area") String area, @Field("addr") String addr,
                                     @Field("def_addr") String def_addr, @Field("addr_id") String addr_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> deleteAddress(@Field("method") String method, @Field("v") String v, @Field("addr_id") String addr_id);

    @POST("index.php/topapi")
    @FormUrlEncoded
    Flowable<ResultBean> setDefault(@Field("method") String method, @Field("v") String v, @Field("addr_id") String addr_id);


}
