package com.liaoyun56.template.utils;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.liaoyun56.template.BuildConfig;
import com.liaoyun56.template.network.ReqCallBack;
import com.liaoyun56.template.network.ReqProgressCallBack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by liaoyun on 2017/8/22.
 */

public class NetworkUtil {
    /**
     * okhttp客户端
     */
    private static OkHttpClient client = new OkHttpClient();
    /**
     * 系统cookie保持
     */
    private static List<Cookie> cookiesList = new ArrayList<>();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");
    /**
     * 同步 GET 请求
     * @param url
     * @return
     */
    public static String doGetSyn(String url, Map<String, Object> params) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url + buildParam(params);
        LogUtil.debug(requestUrl);
        Request request = new Request.Builder().url(requestUrl).build();
        Response response = getClient().newCall(request).execute();
        String res = response.body().string();
        LogUtil.debug(res);
        return res;
    }

    /**
     * 同步 POST 请求
     * @param url
     * @param params
     * @param object
     * @return
     */
    public static String doPostSyn(String url, Map<String, Object> params, Object object) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url + buildParam(params);
        LogUtil.debug(requestUrl);
        String json = null;
        if(object != null){
            json = objectToJson(object);
            LogUtil.debug(json);
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(requestUrl).post(body).build();
        OkHttpClient client = getClient();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        LogUtil.debug(res);
        return res;
    }

    /**
     * 异步 GET 请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void doGetAsyn(String url, Map<String, Object> params, final ReqCallBack callBack) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url + buildParam(params);
        LogUtil.debug(requestUrl);
        Request request = new Request.Builder().url(requestUrl).build();
        Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
                callBack.onReqFailed(call,e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.debug(res);
                callBack.onReqSuccess(call,response);
            }
        });
        call.execute();
    }

    /**
     * 异步 POST 请求
     * @param url
     * @param params
     * @param object
     * @param callBack
     * @throws Exception
     */
    public static void doPostAsyn(String url, Map<String, Object> params, Object object, final ReqCallBack callBack) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url + buildParam(params);
        LogUtil.debug(requestUrl);
        String json = null;
        if(object != null){
            json = objectToJson(object);
            LogUtil.debug(json);
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(requestUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
                callBack.onReqFailed(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.debug(res);
                callBack.onReqSuccess(call, response);
            }
        });
        call.execute();
    }

    /**
     * 本地文件上传
     * @param url
     * @param filePath      本地文件地址
     * @param reqCallBack   回调接口
     */
    public static void uploadFile(String url, String filePath, final ReqCallBack reqCallBack) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url;
        File file = new File(filePath);                                             //本地文件
        RequestBody body = RequestBody.create(MEDIA_OBJECT_STREAM, file);
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
                reqCallBack.onReqFailed(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.debug(res);
                reqCallBack.onReqSuccess(call, response);
            }
        });
        call.execute();
    }

    /**
     * 带参数上传文件
     * @param url
     * @param params
     * @param reqCallBack            结果处理回调接口
     */
    public void uploadFile(String url, Map<String, Object> params, final ReqCallBack reqCallBack) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url + buildParam(params);
        MultipartBody.Builder builder   = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for(String key : params.keySet()){
            Object object = params.get(key);
            if(object==null){
                continue;
            }
            if(!(object instanceof File)){
                builder.addFormDataPart(key, object.toString());
            }else{
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
            }
        }
        RequestBody body = builder.build();
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        final Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
                reqCallBack.onReqFailed(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.debug(res);
                reqCallBack.onReqSuccess(call, response);
            }
        });
        call.execute();
    }

    /**
     * 带参数带进度上传文件
     * @param url
     * @param params
     * @param reqProgressCallBack        进度处理回调接口
     */
    public static void uploadFile(String url, Map<String, Object> params, final ReqProgressCallBack reqProgressCallBack) throws Exception{
        String requestUrl = BuildConfig.BASE_URL + url;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for(String key : params.keySet()){
            Object object = params.get(key);
            if(object==null){
                continue;
            }
            if(!(object instanceof File)){
                builder.addFormDataPart(key, object.toString());
            }else{
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, reqProgressCallBack));
            }
        }
        RequestBody body = builder.build();
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        final Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.debug(res);
            }
        });
        call.execute();
    }

    /**
     * 文件下载
     * @param url
     * @param destFileDir
     * @param name             保存的文件名
     * @param reqCallBack      处理结果 的回调
     */
    public static void downloadFile(String url, final String destFileDir, String name, final ReqCallBack reqCallBack) throws Exception{
        final File file = new File(destFileDir, name);
        if (file.exists()) {
            //successCallBack((T) file, callBack);
            //return;
        }
        final Request request = new Request.Builder().url(url).build();
        final Call call = getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.error(e.getMessage());
                reqCallBack.onReqFailed(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {                      //处理进度
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    LogUtil.debug("total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        LogUtil.debug("current------>" + current);
                    }
                    fos.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.error(e.getMessage());
                }
            }
        });
        call.execute();
    }


    /**
     * 创建带进度的RequestBody
     * @param contentType MediaType
     * @param file  准备上传的文件
     * @param callBack 回调
     * @return
     */
    public static RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }
            @Override
            public long contentLength() {
                return file.length();
            }
            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        LogUtil.error("current------>" + current);
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 统一处理进度信息
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     */
    private static void progressCallBack(final long total, final long current, final ReqProgressCallBack callBack) {
        Handler okHttpHandler = new Handler(Looper.getMainLooper());
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }

    /**
     * 组装 url 后面的请求的参数
     * @param params
     * @return
     */
    private static String buildParam(Map<String, Object> params){
        StringBuffer sbf = new StringBuffer("");
        if(params!=null){
            int pos = 0;
            for(String key : params.keySet()){
                if(params.get(key) != null){
                    if(pos==0){
                        sbf.append("?");
                    }
                    if(pos>0){
                        sbf.append("&");
                    }
                    sbf.append(String.format("%s=%s", key, params.get(key).toString()));
                    pos++;
                }
            }
        }
        return sbf.toString();
    }


    /**
     * 获取 okhttp 客户端
     * @return
     */
    private static OkHttpClient getClient(){
        OkHttpClient res = client.newBuilder().readTimeout(30, TimeUnit.SECONDS).cookieJar(new CookiesManager()).build();
        return res;
    }

    private static class CookiesManager implements CookieJar{
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookiesList = cookies;
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return cookiesList;
        }
    }

    public static String objectToJson(Object object){
        Gson gson = new Gson();
        String str = gson.toJson(object);
        return str;
    }

    /**
     * json字符串转化为 java 对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> clazz){
        try{
            JsonElement je = new JsonParser().parse(jsonStr);
            T t =  new Gson().fromJson(je, clazz);
            return t;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * list 序列化后的字符串转为 list 对象集合
     * @param type
     * @param listString
     * @param <T>
     * @return
     */
    public static <T> List<T> listStringToOjectList(Class<T> type,String listString){
        List<T>list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(listString).getAsJsonArray();
        for(JsonElement e : array) {
            list.add(new Gson().fromJson(e, type));
        }
        return list;
    }

}
