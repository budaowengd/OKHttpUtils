# OKHttpUtils


### 封装了okhttp的网络框架，

该项目参考了以下项目：

 * [https://github.com/wyouflf/xUtils](https://github.com/wyouflf/xUtils) 
 * [https://github.com/hongyangAndroid/okhttp-utils](https://github.com/hongyangAndroid/okhttp-utils)  

在此特别感谢上述作者，同时欢迎大家下载体验本项目，如果使用过程中遇到什么问题，欢迎反馈。
## 联系方式
 * 邮箱地址： 382060748@qq.com
 * QQ群： 238972269 （建议使用QQ群，邮箱使用较少，可能看的不及时）
 * 本群旨在为使用我的github项目的人提供方便，如果遇到问题欢迎在群里提问。个人能力也有限，希望一起学习一起进步。

## 目前对以下需求进行了封装
* 一般的get请求
* 一般的post请求
		* 多文件和多参数统一的表单上传
		* 多参数表单上传
		* String格式上传
* 文件下载/加载图片
* 上传下载的进度回调
* 支持取消某个请求
* 支持自定义Callback
* 支持HEAD、DELETE、PATCH、PUT
* 支持session的保持
* 支持自签名网站https的访问，提供方法设置下证书就
## 使用方法
1、通过Android studio导入
	```
	compile 'com.zhy:okhttputils:2.6.2'
	```
2、配置Application
```
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
        //header不支持中文
        headers.put("commonHeaderKey1", "commonHeaderValue1");
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        Map<String, String> params = new HashMap<>();
        params.put("commonParamsKey1", "commonParamsValue1");
        params.put("commonParamsKey2", "这里支持中文参数");

        OkHttpUtils.addCommonParams(params);    //添加公共参数
        OkHttpUtils.addCommonHeaders(headers);  //添加公共头
        //---------------------------------------------------------//
    }
```
###1、get请求
```java
   OkHttpUtils
         .get("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13714913940")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    //response 服务器返回
                    public void onResponse(Object response, int id) {

                    }
                });
```
###2、post表单请求
```
     OkHttpUtils.post("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm")
                .params("tel","13714913940")//表单
                .params(null)//表单，可以传递Map
                .headers("key","value") //请求头，传递键值对
                .headers(null)//请求头，可以传递Map
                .files("key",null)//多个或者单个文件
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
```
###3、post String请求
```
    OkHttpUtils.postString("www.github.com")
                .headers("key", "value") //请求头
                .mediaType(null)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
```
###4、下载文件

```java
 OkHttpUtils//
	.get()//
	.url(url)//
	.build()//
	.execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "gson-2.2.1.jar")//
	{
	    @Override
	    public void inProgress(float progress)
	    {
	        mProgressBar.setProgress((int) (100 * progress));
	    }

	    @Override
	    public void onError(Request request, Exception e)
	    {
	        Log.e(TAG, "onError :" + e.getMessage());
	    }

	    @Override
	    public void onResponse(File file)
	    {
	        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
	    }
	});
```

注意下载文件可以使用`FileCallback`，需要传入文件需要保存的文件夹以及文件名。
###5、上传下载的进度显示

```
...
new Callback<T>()
{
    //...
    @Override
    public void inProgress(float progress)
    {
       //use progress: 0 ~ 1
    }
}
```
### 6、取消单个请求

```java
 RequestCall call = OkHttpUtils.get().url(url).build();
 call.cancel();

```

### 7、根据tag取消请求

目前对于支持的方法都添加了最后一个参数`Object tag`，取消则通过` OkHttpUtils.cancelTag(tag)`执行。

例如：在Activity中，当Activity销毁取消请求：

callback回调中有`inProgress `方法，直接复写即可。
```
OkHttpUtils
    .get()//
    .url(url)//
    .tag(this)//
    .build()//

@Override
protected void onDestroy()
{
    super.onDestroy();
    //可以取消同一个tag的
    OkHttpUtils.cancelTag(this);//取消以Activity.this作为tag的请求
}
```
比如，当前Activity页面所有的请求以Activity对象作为tag，可以在onDestory里面统一取消。
## 混淆

```
#okhttputils
-dontwarn com.lx.http.**
-keep class com.lx.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}


```