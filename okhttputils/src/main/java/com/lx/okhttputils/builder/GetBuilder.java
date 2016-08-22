package com.lx.okhttputils.builder;

import android.net.Uri;

import com.lx.okhttputils.OkHttpUtils;
import com.lx.okhttputils.request.GetRequest;
import com.lx.okhttputils.request.RequestCall;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by luoXiong on 16/08/08.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> {

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, tag, params, headers, id).build();
    }

    public GetBuilder() {
    }

    public GetBuilder(String url) {
        super(url);
    }

    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }


    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public GetBuilder params(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }
}
