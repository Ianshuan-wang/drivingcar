package com.ujs.drivingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ujs.drivingapp.Component.NoScrollViewPager;
import com.ujs.drivingapp.Fragments.RegisterFragment;
import com.ujs.drivingapp.Fragments.SigninFragment;
import com.ujs.drivingapp.Fragments.StartFragment;
import com.ujs.drivingapp.Pojo.BaseResponse;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.CarappService;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;
import com.ujs.drivingapp.Utils.TimerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";
    ArrayList<Fragment> fragments = new ArrayList<>();
    NoScrollViewPager noScrollViewPager;
    private Retrofit retrofit;
    private CarappService carappService;
    private String token = "";
    private int time = 60;
    private TimerUtil timerUtil;
    private TextView textView;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        fragments.add(RegisterFragment.newInstance("RegisterPage", "TODO"));
        fragments.add(StartFragment.newInstance("StartPage", "TODO"));
        fragments.add(SigninFragment.newInstance("SigninPage", "TODO"));
        retrofit = new Retrofit.Builder().baseUrl("http://47.96.138.120:6583/").build();
        carappService = retrofit.create(CarappService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noScrollViewPager = findViewById(R.id.pager_start);
        noScrollViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        noScrollViewPager.setCurrentItem(1);
        SharedPreferences tokenSet = getSharedPreferences("TokenSet", Context.MODE_PRIVATE);
        String token = tokenSet.getString("token", "");
        Log.e(TAG, "onStart: " + token);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Call<ResponseBody> verifyToken = carappService.postVerifyToken(json);
        verifyToken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Double code = JsonUtil.getDouble(map, "code");
                        if (code.intValue() == 200) {
                            finish();
                            Intent intent = new Intent("com.ujs.driving.app.MAIN");
                            startActivity(intent);
                        } else {
                            String msg = JsonUtil.getString(map, "msg");
                            Log.e(TAG, "onResponse: " + msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (noScrollViewPager.getCurrentItem() == 0) {
                    textView = fragments.get(0).requireView().findViewById(R.id.tv_send_code);
                    switch (msg.what) {
                        case 1:
                            textView.setText(time + "后重新发送");
                            break;
                        case 2:
                            textView.setText("发送验证码");
                            time = 60;
                            break;
                    }
                }
                time--;
            }
        };

    }

    public void onNavRegister(View view) {
        noScrollViewPager.setCurrentItem(0, true);
    }

    public void onNavSignin(View view) {
        noScrollViewPager.setCurrentItem(2, true);
    }

    public void onNavStart(View view) {
        noScrollViewPager.setCurrentItem(1, true);
    }

    public void onSignin(View view) {
        EditText account = view.getRootView().findViewById(R.id.edt_account_login);
        EditText password = view.getRootView().findViewById(R.id.edt_password_login);
        if (account.getText().toString().equals("") || password.getText().toString().equals("")) {
            Log.e(TAG, "onSignin: 用户名密码为空");
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("account", account.getText().toString());
        data.put("password", password.getText().toString());

        Gson gson = new Gson();
        String json = gson.toJson(data);
        Call<ResponseBody> call = carappService.postLogin(json);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: " + response.message());
                    } else {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Log.e(TAG, "onResponse: " + map);
                        Double code = JsonUtil.getDouble(map, "code");
                        Map<String, Object> map1 = JsonUtil.getMap(map, "data");
                        if (code.intValue() == 200) {
                            String str = JsonUtil.getString(map1, "token");
                            Log.e(TAG, "onResponse: " + str);
                            SharedPreferences tokenSet = getSharedPreferences("TokenSet", Context.MODE_PRIVATE);
                            tokenSet.edit().putString("token", str).apply();
                            if (!str.equals("")) {
                                finish();
                                Intent intent = new Intent("com.ujs.driving.app.MAIN");
                                startActivity(intent);
                            }
                        } else {
                            String msg = JsonUtil.getString(map, "msg");
                            Log.e(TAG, "onResponse: " + msg);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "t: " + t.getMessage());
            }
        });
    }

    public void onVerifyCode(View view) {
        EditText account = view.getRootView().findViewById(R.id.edt_account_register);
        EditText password = view.getRootView().findViewById(R.id.edt_password_register);
        if (account.getText().toString().equals("") || password.getText().toString().equals("")) {
            Log.e(TAG, "onSignin: 用户名密码为空");
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("account", account.getText().toString());
        data.put("password", password.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Call<ResponseBody> call = carappService.postVerifyCode(json);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Log.e(TAG, "onResponse: " + map);
                        Map<String, Object> data = JsonUtil.getMap(map, "data");
                        token = JsonUtil.getString(data, "token");
                        Log.e(TAG, "onResponse: " + token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "t: " + t.getMessage());
            }

        });

        timerUtil = new TimerUtil(new TimerUtil.Run() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + time);

                if (time == 0) {
                    Message obtain = Message.obtain(handler, 2);
                    handler.sendMessage(obtain);
                    timerUtil.destroy();
                } else {
                    Message obtain = Message.obtain(handler, 1);
                    handler.sendMessage(obtain);
                }
            }
        }, 0, 1000);

        timerUtil.start();

    }

    public void onRegister(View view) {
        EditText account = view.getRootView().findViewById(R.id.edt_account_register);
        EditText password = view.getRootView().findViewById(R.id.edt_password_register);
        EditText verifycode = view.getRootView().findViewById(R.id.edt_verifycode);
        if (account.getText().toString().equals("") || password.getText().toString().equals("") || verifycode.getText().toString().equals("")) {
            Log.e(TAG, "onSignin: 用户名、密码或验证码为空");
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("account", account.getText().toString());
        data.put("password", password.getText().toString());
        data.put("verifycode", verifycode.getText().toString());
        data.put("token", token);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Call<ResponseBody> call = carappService.postRegister(json);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: " + response.message());
                    } else {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Map<String, Object> map1 = JsonUtil.getMap(map, "data");
                        Double code = JsonUtil.getDouble(map, "code");
                        if (code.intValue() == 200) {
                            String str = JsonUtil.getString(map1, "token");
                            Log.e(TAG, "onResponse: " + str);
                            SharedPreferences tokenSet = getSharedPreferences("TokenSet", Context.MODE_PRIVATE);
                            tokenSet.edit().putString("token", str).apply();
                            if (!str.equals("")) {
                                timerUtil.destroy();
                                finish();
                                Intent intent = new Intent("com.ujs.driving.app.MAIN");
                                startActivity(intent);
                            }
                        } else {
                            String msg = JsonUtil.getString(map, "msg");
                            Log.e(TAG, "onResponse: " + msg);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "t: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}