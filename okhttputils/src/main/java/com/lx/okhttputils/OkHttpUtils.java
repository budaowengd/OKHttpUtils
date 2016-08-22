package com.lx.okhttputils;


import com.lx.okhttputils.builder.GetBuilder;
import com.lx.okhttputils.builder.HeadBuilder;
import com.lx.okhttputils.builder.OtherRequestBuilder;
import com.lx.okhttputils.builder.PostFileBuilder;
import com.lx.okhttputils.builder.PostFormBuilder;
import com.lx.okhttputils.builder.PostStringBuilder;
import com.lx.okhttputils.callback.Callback;
import com.lx.okhttputils.request.RequestCall;
import com.lx.okhttputils.utils.Platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by luoXiong on 16/08/07.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加全局请求头
     */
    public static Map<String, String> sHeaders;

    public static boolean addCommonHeaders(Map<String, String> headers) {
        if (sHeaders == null) {
            sHeaders = new HashMap<>();
        }
        if (headers != null && !headers.isEmpty()) {
            sHeaders.putAll(headers);
        }
        return true;
    }

    /**
     * 添加全局请求参数
     */
    public static Map<String, String> sParams;
    public static boolean addCommonParams(Map<String, String> params) {
        if (sParams == null) {
            sParams = new HashMap<>();
        }
        if (params != null && !params.isEmpty()) {
            sParams.putAll(params);
        }
        return true;
    }

    /**
     * 清除全局请求参数
     */
    public static void clearCommonParams() {
        if (sParams != null){
            sParams.clear();
            sParams = null;
        }
    }

    /**
     * 清除全局请求头
     */
    public static void clearCommonHeaders() {
        if (sHeaders != null){
            sHeaders.clear();
            sHeaders = null;
        }
    }


    public static OkHttpUtils getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * get请求
     */
    public static GetBuilder get(String url) {
        return new GetBuilder(url);
    }

    /**
     * post   String
     */
    public static PostStringBuilder postString(String url) {
        return new PostStringBuilder(url);
    }

    /**
     * post   File
     */
    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    /**
     * post   key-value  File mapFile
     */
    public static PostFormBuilder post(String url) {
        return new PostFormBuilder(url);
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    /**
     * 执行一个任务
     */
    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 请求失败的回调
     */
    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 请求成功的回调
     */
    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 取消所有的tag
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) { //排队的请求
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) { //正在运行的请求
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

