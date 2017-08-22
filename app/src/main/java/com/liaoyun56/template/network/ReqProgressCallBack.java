package com.liaoyun56.template.network;

public interface ReqProgressCallBack  extends ReqCallBack{
    /**
     * 响应进度更新
     */
    void onProgress(long total, long current);
}