package com.liaoyun56.template.network;

import okhttp3.Call;
import okhttp3.Response;

public interface ReqCallBack {
    /**
     * 响应成功
     */
     void onReqSuccess(Call call,Response response);
    /**
     * 响应失败
     */
     void onReqFailed(Call call, Exception e);
}