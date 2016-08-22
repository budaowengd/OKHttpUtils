package com.lx.okhttputils.builder;

import java.util.Map;

/**
 * Created by luoXiong on 16/08/08.
 */
public interface HasParamsable
{
    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParams(String key, String val);
}
