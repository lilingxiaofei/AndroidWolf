package com.chunlangjiu.app.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.net.ApiService;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.AppUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.socks.library.KLog;
import com.socks.library.KLogUtil;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/24
 * @Describe: 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId  接收cid <br>
 * onReceiveOnlineState  cid离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GeTuiIntentService extends GTIntentService {

    private Gson gson;
    private String clientId;

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if(intent.getBooleanExtra("action",false)){
            uploadClientId();
        }
    }

    /**
     * 接收cid
     */
    @Override
    public void onReceiveClientId(Context context, String cid) {
        KLog.e("--clientId---", cid);
        SPUtils.put("clientId",cid);
        clientId = cid ;
        uploadClientId();
    }

    private void uploadClientId(){
        clientId = SPUtils.get("clientId","").toString();
        if (!TextUtils.isEmpty(clientId) && BaseApplication.isLogin()) {
            ApiUtils.getInstance().geTuiRegister(clientId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ToastUtils.showErrorMsg(throwable);
                        }
                    });
        }
    }

    /**
     * 处理透传消息
     *
     * @param context
     * @param msg
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result =
                PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        KLog.e(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        KLog.e(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid
                + "\nmessageid = " + messageid + "\npkg = " + pkg + "\ncid = " + cid);
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            KLog.e(TAG, "receiver payload = " + data);
            disposedTranslateMessage(context, data);
        }

        KLog.e(TAG,
                "----------------------------------------------------------------------------------------------");
    }

    /**
     * @methordName.disposedTranslateMessage :【处理传递过来的透传数据】. @param. @return.
     */
    private void disposedTranslateMessage(Context context, String jsonStr) {
//        ToastUtils.showShort(jsonStr);
        if (null == gson) {
            gson = new Gson();
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        KLog.e(TAG, "onReceiveCommandResult -> " + gtCmdMessage);
        int action = gtCmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) gtCmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) gtCmdMessage);
        }
    }


    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        KLog.e(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        KLog.e(TAG,
                "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid
                        + "\nactionid = " + actionid + "\nresult = " + result + "\ncid = " + cid
                        + "\ntimestamp = " + timestamp);
    }


    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }

}
