package com.sunli.sunli0112.shell_frame.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.sunli.sunli0112.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManager<T> {
    private final String BASE_URL = "http://www.zhaoapi.cn/product/";

    private static RetrofitManager retrofitManager;
    private BaseApis baseApis;

    public static synchronized RetrofitManager getInstance(){
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }

    private RetrofitManager() {
        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); */

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                SharedPreferences sharedPreferences =
                        App.getApplication().getSharedPreferences("Header", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId", "");
                String sessionId = sharedPreferences.getString("sessionId", "");

                Request.Builder newBuilder = original.newBuilder();
                newBuilder.method(original.method(), original.body());

                if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(sessionId)) {
                    newBuilder.addHeader("userId", userId);
                    newBuilder.addHeader("sessionId", sessionId);
                }
                Request request = newBuilder.build();
                return chain.proceed(request);
            }
        });
        builder.retryOnConnectionFailure(true);
        OkHttpClient okClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okClient)
                .build();
        baseApis = retrofit.create(BaseApis.class);
    }

    public Map<String, RequestBody> generateRequestBody(Map<String, String> requestDataMap) {
        HashMap<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key) == null ? "" : requestDataMap.get(key));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }

    private Observer getObserver(final HttpListener listener) {
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("TAG",e+"");
                if (listener != null) {listener.onFail(e.getMessage());}
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if (listener != null) { listener.onSuccess(data); }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) { listener.onFail(e.getMessage()); }
                }
            }
        };
        return observer;
    }

    /**
     * get请求
     */
    public RetrofitManager get(String url, HttpListener listener) {
        baseApis.get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
        return retrofitManager;
    }

    /**
     * 表单post请求
     * @param url
     * @param map
     * @return
     */
    public RetrofitManager postFormBody(String url, Map<String, RequestBody> map,HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }

        baseApis.postFormBody(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
        return retrofitManager;
    }

    /**
     * put 请求
     * @param url
     * @param map
     * @param listener
     * @return
     */
    public RetrofitManager putFormBody(String url, Map<String, String> map,HttpListener listener) {

        baseApis.putFormBody(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));

        return retrofitManager;
    }

    public interface HttpListener {
        void onSuccess(String data);

        void onFail(String error);
    }
}
