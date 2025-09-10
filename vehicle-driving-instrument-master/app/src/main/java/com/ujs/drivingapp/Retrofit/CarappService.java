package com.ujs.drivingapp.Retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 2/5/2022 7:35 PM.
 * @description 请求定义接口$
 */
public interface CarappService {
    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> postLogin(@Field("json") String json);

    @POST("verifycode")
    @FormUrlEncoded
    Call<ResponseBody> postVerifyCode(@Field("json") String json);

    @POST("register")
    @FormUrlEncoded
    Call<ResponseBody> postRegister(@Field("json") String json);

    @POST("verifytoken")
    @FormUrlEncoded
    Call<ResponseBody> postVerifyToken(@Field("json") String json);

    @HTTP(method = "GET", path = "https://autumnfish.cn/api/joke/list?num=100")
    Call<ResponseBody> getTest();

    @Multipart
    @POST("advice")
        //请求方法为POST，里面为你要上传的url
    Call<ResponseBody> postAdvice(@Part List<MultipartBody.Part> partLis);
    //注解用@Part，参数类型为List<MultipartBody.Part> 方便上传其它需要的参数或多张图片

    @GET("statistics")
    Call<ResponseBody> getStatistics(@Query("category") String category);

    @GET("drivestatus")
    Call<ResponseBody> getDriveStatus(@Query("id") String id);

    @GET("fatigue")
    Call<ResponseBody> getFatigue(@Query("id") String id);

    @GET("distraction")
    Call<ResponseBody> getDistraction(@Query("id") String id);

    @GET("sentiment")
    Call<ResponseBody> getSentiment(@Query("id") String id);
}
