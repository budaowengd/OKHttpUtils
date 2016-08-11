package com.lx.okhttputils.builder;


import com.lx.okhttputils.OkHttpUtils;
import com.lx.okhttputils.request.OtherRequest;
import com.lx.okhttputils.request.RequestCall;

/**
 * Created by luoXiong on 16/08/08.
 */
public class HeadBuilder extends GetBuilder
{


    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
