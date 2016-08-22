package com.lx.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lx.okhttputils.OkHttpUtils;
import com.lx.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    public static String getUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13714913940";
    public static String postUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";//key:tel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * get请求
     */
    public void getCall(View view) {
        OkHttpUtils
                .get(getUrl)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    //response 服务器返回
                    public void onResponse(Object response, int id) {
                        String json= (String) response;
                        Toast.makeText(MainActivity.this,json,Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * post请求  json形式提交参数
     */
    public void postStringCall(View view) {
//        OkHttpUtils.postString("www.github.com")
//                .headers("key", "value")
//                .mediaType(null)
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(Object response, int id) {
//
//            }
//        });
    }

    /**
     * post请求  表单形式提交参数
     */
    public void postFormCall(View view) {
        OkHttpUtils.post(postUrl)
                .params("tel", "13714913940")//表单
               // .params(null)//表单，可以传递Map
               // .headers("key", "value") //请求头，传递键值对
                //.headers(null)//请求头，可以传递Map
              //  .files("key", null)//多个或者单个文件
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {
                String json= (String) response;
                Toast.makeText(MainActivity.this,json,Toast.LENGTH_LONG).show();
            }
        });
    }
}
