package com.ujs.drivingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ujs.drivingapp.Commen.Constants;
import com.ujs.drivingapp.Retrofit.CarappService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/22/2022 9:22 PM.
 * @description Retrofit工具类$
 */
public class RetrofitUtil {

    /**
     * 环境配置类
     */
    public class Env {
        public static final String BASEURL_PROD = "http://47.96.138.120:6583/";
        public static final String BASEURL_DEV = "http://47.96.138.120:8082/api/";
        public static final int ENV_CURRENT = 0;
        public static final int ENV_DEV = 0; //dev - 开发环境
        public static final int ENV_PROD = 1; //prod - 生产环境

    }

    public static class Builder {
        private final Retrofit.Builder builder;
        private Context context;
        private String baseUrl;

        /**
         * Builder 构造函数
         *
         * @param context 上下文对象
         */
        public Builder(Context context) {
            if (Env.ENV_CURRENT == Env.ENV_DEV) {
                baseUrl = Env.BASEURL_DEV;
            } else if (Env.ENV_CURRENT == Env.ENV_PROD) {
                baseUrl = Env.BASEURL_PROD;
            }
            builder = new Retrofit.Builder();
            this.context = context;
        }


        /**
         * 设置环境
         *
         * @param env 环境
         * @return Builder
         */
        public Builder setEnv(int env) {
            if (env == Env.ENV_DEV) {
                baseUrl = Env.BASEURL_DEV;
            } else if (env == Env.ENV_PROD) {
                baseUrl = Env.BASEURL_PROD;
            }
            return this;
        }

        /**
         * 设置拦截器，默认向请求头中添加token
         *
         * @return Builder
         */
        public Builder setClient() {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
            httpClientBuilder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    SharedPreferences tokenSet = context.getSharedPreferences("TokenSet", Context.MODE_PRIVATE);
                    String token = tokenSet.getString("token", "");
                    if (token.isEmpty()) {
                        Request originalRequest = chain.request();
                        return chain.proceed(originalRequest);
                    } else {
                        Request originalRequest = chain.request();
                        Request updateRequest = originalRequest.newBuilder().header("token", token).build();
                        return chain.proceed(updateRequest);
                    }
                }
            });
            builder.client(httpClientBuilder.build());
            return this;
        }


        /**
         * 设置拦截器
         *
         * @param interceptor 拦截器
         * @return Builder
         */
        public Builder setClient(Interceptor interceptor) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
            httpClientBuilder.addNetworkInterceptor(interceptor);
            builder.client(httpClientBuilder.build());
            return this;
        }

        /**
         * 构建 service 对象
         *
         * @param service Retrofit Service 接口的类
         * @param <T>     泛型
         * @return Retrofit Service 接口的类对象
         */
        public <T> T build(Class<T> service) {
            Retrofit retrofit = builder.baseUrl(baseUrl).build();
            return retrofit.create(service);
        }

    }


}
