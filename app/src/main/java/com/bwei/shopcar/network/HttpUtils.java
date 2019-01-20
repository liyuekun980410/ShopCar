package com.bwei.shopcar.network;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private static volatile HttpUtils instance;
    private final OkHttpClient client;
    public Handler handler = new Handler();

    //添加拦截器
    private Interceptor getAppInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.e("++++++++++", "拦截前");
                Response proceed = chain.proceed(request);
                Log.e("++++++++++", "拦截后");
                return proceed;
            }
        };
        return interceptor;
    }

    public HttpUtils() {
        File file = new File(Environment.getExternalStorageDirectory(), "call");
        //设置读取超时时间
//设置连接的超时时间
//Application拦截器
        client = new OkHttpClient().newBuilder()
                //设置读取超时时间
                .readTimeout(300, TimeUnit.SECONDS)
                //设置连接的超时时间
                .connectTimeout(300, TimeUnit.SECONDS)
                //Application拦截器
                .addInterceptor(getAppInterceptor())
                .cache(new Cache(file, 10 * 1024))
                .build();
    }

    //单例okhttp
    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (null == instance) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    //get请求
    public void doget(String url, final Class clazz, final NetCallBack netCallBack) {
        Request build = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final Object o = gson.fromJson(result, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onSuccess(o);
                    }
                });
            }
        });
    }

    public interface NetCallBack {
        void onSuccess(Object o);

        void onFailure(Exception e);
    }
}