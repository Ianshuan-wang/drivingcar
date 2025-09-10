package com.ujs.drivingapp;

import android.util.Log;

import com.google.gson.Gson;
import com.ujs.drivingapp.Pojo.BaseResponse;
import com.ujs.drivingapp.Retrofit.CarappService;
import com.ujs.drivingapp.Utils.JsonUtil;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Retrofit retrofit;
    private CarappService carappService;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void retrofitTest() throws IOException {
        retrofit = new Retrofit.Builder().baseUrl("http://47.96.138.120:8082/api/").build();
        carappService = retrofit.create(CarappService.class);
        Map<String, String> data = new HashMap<>();
        data.put("account", "12313");
        data.put("password", "123123");
        data.put("verifycode", "eerw");
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Call<ResponseBody> call = carappService.postRegister(json);
        Response<ResponseBody> execute = call.execute();
        Map<String, Object> stringObjectMap = JsonUtil.toMap(execute.body().string());
        System.out.println(stringObjectMap.toString());
    }

//    @Test
//    public void getNameTest() {
//        List<List<BarEntry>> barEntries = new ArrayList<>(4);   // 柱状图数据集
//        System.out.println(barEntries.);
//    }

}