package com.lx.okhttputils.builder;


import com.lx.okhttputils.OkHttpUtils;
import com.lx.okhttputils.request.RequestCall;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luoXiong on 16/08/09.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected int id;
    public OkHttpRequestBuilder(){}
    public OkHttpRequestBuilder(String url) {
        this.url = url;
        addCommonHeaderAndParams();
    }

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T headers(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    /**
     * 添加全局请求头或者请求参数
     */
    protected void addCommonHeaderAndParams() {
        if (OkHttpUtils.sParams != null) {
            if(params==null){
                params=new LinkedHashMap<>();
            }
            params.putAll(OkHttpUtils.sParams);
        }
        if (OkHttpUtils.sHeaders != null) {
            if(headers==null){
                headers=new LinkedHashMap<>();
            }
            headers.putAll(OkHttpUtils.sHeaders);
        }
    }

    public abstract RequestCall build();
}
