package com.ujs.drivingapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Pojo.AdviceTypeItem;
import com.ujs.drivingapp.Pojo.Image;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.CarappService;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.RetrofitUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdviceActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private final String TAG = "AdviceActivity";
    private RecyclerView type;
    private List<AdviceTypeItem> adviceTypeItems = new ArrayList<>();
    private RecyclerView image;
    private List<Image> images = new ArrayList<>();
    private UniversalRecyclerViewAdapter<Image> imageAdapter;
    private RxPermissions rxPermissions;
    private EditText advice;
    private TextView count;
    private Retrofit retrofit;
    private CarappService carappService;
    private String currentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_advice);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        adviceTypeItems.add(new AdviceTypeItem("功能异常", true));
        adviceTypeItems.add(new AdviceTypeItem("意见与建议", false));
        adviceTypeItems.add(new AdviceTypeItem("其他", false));

        images.add(new Image());

        carappService = new RetrofitUtil.Builder(this)
                .setEnv(RetrofitUtil.Env.ENV_PROD)
                .setClient()
                .build(CarappService.class);

        permissionsRequest();
    }


    @Override
    protected void onStart() {
        super.onStart();

        type = findViewById(R.id.rv_advice_type);
        type.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        type.setAdapter(new UniversalRecyclerViewAdapter<AdviceTypeItem>(adviceTypeItems, R.layout.advice_type_item) {

            final UniversalRecyclerViewAdapter<AdviceTypeItem> that = this;

            @Override
            public void bindView(RecyclerViewHolder holder, AdviceTypeItem obj) {
                holder.setText(R.id.tv_advice_type, obj.getType());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentType = obj.getType();
                        CardView cardView = holder.getView(R.id.cv_advice_type);
                        cardView.setCardBackgroundColor(getColor(R.color.theme_orange));
                        List<AdviceTypeItem> adviceTypeItems_new = new ArrayList<>();
                        for (AdviceTypeItem item : adviceTypeItems) {
                            item.setChoose(item.getType().equals(obj.getType()));
                            adviceTypeItems_new.add(item);
                        }
                        adviceTypeItems.clear();
                        adviceTypeItems.addAll(adviceTypeItems_new);
                        that.notifyDataSetChanged();
                    }
                });
                CardView cardView = holder.getView(R.id.cv_advice_type);
                if (!obj.isChoose()) {
                    cardView.setCardBackgroundColor(getColor(R.color.bg_gary));
                } else {
                    cardView.setCardBackgroundColor(getColor(R.color.theme_orange));
                    currentType = obj.getType();
                }

            }
        });

        image = findViewById(R.id.rv_advice_image);
        image.setLayoutManager(new GridLayoutManager(this, 5));
        imageAdapter = new UniversalRecyclerViewAdapter<Image>(images, R.layout.image_item) {

            UniversalRecyclerViewAdapter<Image> that = this;

            @Override
            public void bindView(RecyclerViewHolder holder, Image obj) {
                ImageView imageView = holder.getView(R.id.img_picker);
                if (obj.getPath() != null) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    Glide.with(AdviceActivity.this).load(obj.getPath()).into(imageView);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    holder.setDrawableRes(R.id.img_picker, R.drawable.ic_add);
                }
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (images.size() >= 5) {
                            showMsg("至多选择4张图片", Toast.LENGTH_SHORT);
                            return;
                        }
                        if (obj.getPath() == null) {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }

                    }
                });

                holder.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (obj.getPath() != null) {
                            showDialog("确认删除？", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.e(TAG, "onClick: " + obj);
                                    images.remove(obj);
                                    Log.e(TAG, "onClick: images" + images);
                                    that.notifyDataSetChanged();
                                }
                            });

                        }
                        return true;
                    }
                });
            }
        };
        image.setAdapter(imageAdapter);
        count = findViewById(R.id.tv_advice_count);
        advice = findViewById(R.id.edt_advice);
        advice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                count.setText(charSequence.length() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        TwinklingRefreshLayout twinklingRefreshLayout = findViewById(R.id.refreshLayout); //找到控件
        twinklingRefreshLayout.setPureScrollModeOn();  //设置回弹效果
    }


    /**
     * 动态权限申请
     */
    @SuppressLint("CheckResult")
    private void permissionsRequest() {//使用这个框架使用了Lambda表达式，设置JDK版本为 1.8或者更高
        rxPermissions = new RxPermissions(this);//实例化这个权限请求框架，否则会报错
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        Log.e(TAG, "permissionsRequest: 权限申请成功");
                    } else {//申请失败
                        Log.e(TAG, "permissionsRequest: 权限未开启");
                    }
                });
    }


    /**
     * 消息提示
     *
     * @param msg  消息
     * @param type 类型(时间长短)
     */
    private void showMsg(String msg, int type) {
        Toast.makeText(this, msg, type).show();
    }

    /**
     * 弹窗
     *
     * @param dialogTitle     标题
     * @param onClickListener 按钮的点击事件
     */
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener
            onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.e(TAG, "onActivityResult: " + picturePath);
                images.add(0, new Image(picturePath));
                imageAdapter.notifyDataSetChanged();
            }


        }
    }


    //将bitmap转化为png格式
    public File saveMyBitmap(Bitmap mBitmap) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = null;
        try {
            file = File.createTempFile(
                    "test",  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void onNavBack(View view) {
        onBackPressed();
    }

    public void onSubmit(View view) {

        //1.创建MultipartBody.Builder对象
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型

//        //2.获取图片，创建请求体
//        File file = new File(images.get(0).getPath());
//        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);//表单类型


        //3.调用MultipartBody.Builder的addFormDataPart()方法添加表单数据
        builder.addFormDataPart("type", currentType);//传入服务器需要的key，和相应value值
        builder.addFormDataPart("advice", String.valueOf(advice.getText()));//传入服务器需要的key，和相应value值

//        builder.addFormDataPart("image", file.getName(), body); //添加图片数据，body创建的请求体

        Log.e(TAG, "onSubmit: " + images.size());
        for (int i = 0; i < images.size() - 1; i++) {
            //2.获取图片，创建请求体
            File file = new File(images.get(i).getPath());
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);//表单类型
            builder.addFormDataPart("image", file.getName(), body); //添加图片数据，body创建的请求体
        }

        //4.创建List<MultipartBody.Part> 集合，
        //  调用MultipartBody.Builder的build()方法会返回一个新创建的MultipartBody
        //  再调用MultipartBody的parts()方法返回MultipartBody.Part集合
        List<MultipartBody.Part> parts = builder.build().parts();


        //5.最后进行HTTP请求，传入parts即可
        Call<ResponseBody> call = carappService.postAdvice(parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200) {
                    Log.e(TAG, "onResponse: " + response.message());
                    showMsg("提交失败", Toast.LENGTH_SHORT);
                } else {
                    try {
                        Map<String, Object> map = JsonUtil.toMap(response.body().string());
                        Log.e(TAG, "onResponse: " + map);
                        showMsg("提交成功", Toast.LENGTH_SHORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}