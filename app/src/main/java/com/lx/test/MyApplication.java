package com.lx.test;

import android.app.Application;

import com.lx.okhttputils.OkHttpUtils;
import com.lx.okhttputils.log.LoggerInterceptor;

import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;

/**
 * Created by luoxiong on 2016/8/22.
 * 描述：
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化OkHttpClient构造器
        OkHttpClient.Builder cb = new OkHttpClient.Builder();
        //以下配置都是全局的
        cb.connectTimeout(10000L, TimeUnit.SECONDS)  //连接超时
                .readTimeout(10000L, TimeUnit.SECONDS)     //读取超时
                .writeTimeout(10000L, TimeUnit.SECONDS)    //写入超时

                //根据需要是否添加全局拦截器
                .addInterceptor(new LoggerInterceptor("TAG"))//Log拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request());
                    }
                });
        OkHttpUtils.initClient(cb.build());

        //---------根据实际情况选择，如果不需要设置，就不用设置-------------//
        Map<String, String> headers = new HashMap<>();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        Map<String, String> params = new HashMap<>();
        params.put("commonParamsKey1", "commonParamsValue1");
        params.put("commonParamsKey2", "这里支持中文参数");

        OkHttpUtils.addCommonParams(params);    //添加公共参数
        OkHttpUtils.addCommonHeaders(headers);  //添加公共头
        //-----------------------------------------------------------------------------------//
    }
}
