package com.lx.okhttputils.builder;


import com.lx.okhttputils.request.PostStringRequest;
import com.lx.okhttputils.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by luoXiong on 16/08/08.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType, id).build();
    }
}
