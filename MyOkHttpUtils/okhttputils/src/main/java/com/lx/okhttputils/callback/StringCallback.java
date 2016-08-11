package com.lx.okhttputils.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by luoXiong on 16/07/28.
 */
public abstract class StringCallback extends Callback
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }
}
