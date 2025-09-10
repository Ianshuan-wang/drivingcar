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
import com.ujs.drivingapp.Pojo.ArchivesItem;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArchivesActivity extends AppCompatActivity {

    private static final String TAG = "ArchivesActivity";
    private RecyclerView rv_archives_1;
    private RecyclerView rv_archives_2;
    private RecyclerView rv_archives_3;
    private List<ArchivesItem> archivesItems_1 = new ArrayList<>();
    private List<ArchivesItem> archivesItems_2 = new ArrayList<>();
    private List<ArchivesItem> archivesItems_3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面

        rv_archives_1 = findViewById(R.id.rv_archives_1);
        rv_archives_2 = findViewById(R.id.rv_archives_2);
        rv_archives_3 = findViewById(R.id.rv_archives_3);

        archivesItems_1.add(new ArchivesItem("绑定手机", "157******69"));
        archivesItems_1.add(new ArchivesItem("绑定邮箱", "去绑定"));
        archivesItems_1.add(new ArchivesItem("修改密码", "已设置"));

        archivesItems_2.add(new ArchivesItem("账号保护", "去开启"));
        archivesItems_2.add(new ArchivesItem("绑定微信", "去绑定"));
        archivesItems_2.add(new ArchivesItem("绑定QQ", "去绑定"));


    }

    @Override
    protected void onStart() {
        super.onStart();


        ImageView imageView = findViewById(R.id.img_divide_line);
        imageView.setSelected(true);

        rv_archives_1.setLayoutManager(new LinearLayoutManager(this));
        rv_archives_1.setAdapter(new UniversalRecyclerViewAdapter<ArchivesItem>(archivesItems_1, R.layout.archives_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, ArchivesItem obj) {
                holder.setText(R.id.tv_name, obj.getName());
                holder.setText(R.id.tv_tip, obj.getTip());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
            }
        });


        rv_archives_2.setLayoutManager(new LinearLayoutManager(this));
        rv_archives_2.setAdapter(new UniversalRecyclerViewAdapter<ArchivesItem>(archivesItems_2, R.layout.archives_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, ArchivesItem obj) {
                holder.setText(R.id.tv_name, obj.getName());
                holder.setText(R.id.tv_tip, obj.getTip());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
            }
        });

    }

    public void onNavBack(View view) {
        onBackPressed();
    }
}