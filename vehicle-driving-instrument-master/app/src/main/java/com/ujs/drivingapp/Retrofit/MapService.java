package com.ujs.drivingapp.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 17/02/2022 下午 8:15
 * @description
 */
public interface MapService {

    @POST("collect")
    @FormUrlEncoded
    Call<ResponseBody> collect(@Field("json") String json);

    @POST("uncollect")
    @FormUrlEncoded
    Call<ResponseBody> uncollect(@Field("json") String json);

    @GET("iscollect")
    Call<ResponseBody> getIsCollect(@Query("poiId") String poiId);

    @GET("getallcollect")
    Call<ResponseBody> getAllCollect();

    @GET("getallfooter")
    Call<ResponseBody> getAllFooter();

    @GET("getpath")
    Call<ResponseBody> getPath(@Query("id") String id);




}
