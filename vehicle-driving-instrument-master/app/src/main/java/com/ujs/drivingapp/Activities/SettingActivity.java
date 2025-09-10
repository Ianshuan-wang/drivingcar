package com.ujs.drivingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Pojo.SettingItem;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";
    private RecyclerView rv_setting_1;
    private RecyclerView rv_setting_2;
    private RecyclerView rv_setting_3;
    private List<SettingItem> settingItems_1 = new ArrayList<>();
    private List<SettingItem> settingItems_2 = new ArrayList<>();
    private List<SettingItem> settingItems_3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_setting);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        rv_setting_1 = findViewById(R.id.rv_setting_1);
        rv_setting_2 = findViewById(R.id.rv_setting_2);
        rv_setting_3 = findViewById(R.id.rv_setting_3);
        settingItems_1.add(new SettingItem("通用设置"));
        settingItems_1.add(new SettingItem("导航设置"));
        settingItems_1.add(new SettingItem("语音设置"));
        settingItems_1.add(new SettingItem("消息通知"));

        settingItems_2.add(new SettingItem("隐私设置"));
        settingItems_2.add(new SettingItem("隐私策略摘要"));
        settingItems_2.add(new SettingItem("隐私策略"));
        settingItems_2.add(new SettingItem("已收集个人信息清单"));

        settingItems_3.add(new SettingItem("关于我们"));
        settingItems_3.add(new SettingItem("联系我们"));
        settingItems_3.add(new SettingItem("新功能介绍"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        rv_setting_1.setLayoutManager(new LinearLayoutManager(this));
        rv_setting_1.setAdapter(new UniversalRecyclerViewAdapter<SettingItem>(settingItems_1, R.layout.setting_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, SettingItem obj) {
                holder.setText(R.id.tv_setting_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
                ImageView imageView = holder.getView(R.id.img_divide_line);
                if (!obj.getName().equals(settingItems_1.get(0).getName())) {
                    imageView.setSelected(true);
                }
            }
        });

        rv_setting_2.setLayoutManager(new LinearLayoutManager(this));
        rv_setting_2.setAdapter(new UniversalRecyclerViewAdapter<SettingItem>(settingItems_2, R.layout.setting_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, SettingItem obj) {
                holder.setText(R.id.tv_setting_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
                ImageView imageView = holder.getView(R.id.img_divide_line);
                if (!obj.getName().equals(settingItems_2.get(0).getName())) {
                    imageView.setSelected(true);
                }
            }
        });

        rv_setting_3.setLayoutManager(new LinearLayoutManager(this));
        rv_setting_3.setAdapter(new UniversalRecyclerViewAdapter<SettingItem>(settingItems_3, R.layout.setting_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, SettingItem obj) {
                holder.setText(R.id.tv_setting_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
                ImageView imageView = holder.getView(R.id.img_divide_line);
                if (!obj.getName().equals(settingItems_3.get(0).getName())) {
                    imageView.setSelected(true);
                }
            }
        });
    }

    public void onNavBack(View view) {
        onBackPressed();
    }
}