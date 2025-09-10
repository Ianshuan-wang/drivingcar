package com.ujs.drivingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Pojo.PunchItem;
import com.ujs.drivingapp.Pojo.TaskItem;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PunchActivity extends AppCompatActivity {
    private static final String TAG = "PunchActivity";
    private RecyclerView punch;
    private List<PunchItem> punchItems = new ArrayList<>();
    private RecyclerView task;

    private List<TaskItem> taskItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.bg_orange));
        setContentView(R.layout.activity_punch);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面


        punchItems.add(new PunchItem("昨天", "未签", false));
        punchItems.add(new PunchItem("今天", "", true));
        punchItems.add(new PunchItem("明天", "+3", false));
        punchItems.add(new PunchItem("2/10", "+6", false));
        punchItems.add(new PunchItem("2/11", "+2", false));
        punchItems.add(new PunchItem("2/12", "+5", false));
        punchItems.add(new PunchItem("2/13", "+4", false));

        taskItems.add(new TaskItem("完善个人资料", "首次完善个人资料即可完成任务，该任务仅限领取一次", "+10", false));
        taskItems.add(new TaskItem("首次添加任意设备", "连接任意一台设备即可完成任务，该任务仅限领取一次", "+10", false));
        taskItems.add(new TaskItem("首次开通会员", "首次购买会员即可完成任务，该任务仅限领取一次", "+200", false));

    }

    @Override
    protected void onStart() {
        super.onStart();
        punch = findViewById(R.id.rv_punch);
        punch.setLayoutManager(new GridLayoutManager(this, 7));
        punch.setAdapter(new UniversalRecyclerViewAdapter<PunchItem>(punchItems, R.layout.punch_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, PunchItem obj) {
                if (obj.isStatus()) {
                    ImageView imageView = holder.getView(R.id.img_experience);
                    imageView.setImageResource(R.drawable.ic_hook);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(70, 70));
                    TextView textView = holder.getView(R.id.tv_punch_info);
                    textView.setVisibility(View.GONE);
                    LinearLayout linearLayout = holder.getView(R.id.llayout_punch);
                    linearLayout.setBackground(getDrawable(R.drawable.bg_punch_selected));
                }
                holder.setText(R.id.tv_punch_info, obj.getInfo());
                holder.setText(R.id.tv_punch_date, obj.getDate());
            }
        });

        task = findViewById(R.id.llayout_task);
        task.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        task.setAdapter(new UniversalRecyclerViewAdapter<TaskItem>(taskItems, R.layout.task_item) {

            @Override
            public void bindView(RecyclerViewHolder holder, TaskItem obj) {
                holder.setText(R.id.tv_task_name, obj.getName());
                holder.setText(R.id.tv_task_exp, obj.getExp());
                holder.setOnClickListener(R.id.img_task_detail, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });

                holder.setOnClickListener(R.id.tv_make_task, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: " + obj.getName());
                    }
                });
                if (!obj.getName().equals(taskItems.get(0).getName())) {
                    ImageView imageView = holder.getView(R.id.img_divide_line);
                    imageView.setSelected(true);
                }
            }
        });
    }

    public void onNavBack(View view) {
        onBackPressed();
    }
}