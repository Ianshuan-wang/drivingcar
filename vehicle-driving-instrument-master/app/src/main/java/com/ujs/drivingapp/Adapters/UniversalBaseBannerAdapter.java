package com.ujs.drivingapp.Adapters;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:42 PM.
 * @description 行车贴士适配器
 */

import android.view.View;

import androidx.annotation.NonNull;

import com.ujs.drivingapp.R;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class UniversalBaseBannerAdapter<T> extends BaseBannerAdapter<T, UniversalBaseBannerAdapter.BaseBannerViewHolder<T>> {
    private int mLayout;

    public UniversalBaseBannerAdapter(int mLayout) {
        this.mLayout = mLayout;
    }

    @Override
    public UniversalBaseBannerAdapter.BaseBannerViewHolder createViewHolder(View itemView, int viewType) {
        return new BaseBannerViewHolder<T>(itemView);
    }

    @Override
    public int getLayoutId(int viewType) {
        return mLayout;
    }


    public static class BaseBannerViewHolder<T> extends BaseViewHolder<T> {

        public BaseBannerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(T data, int position, int pageSize) {

        }

    }


//    List<Tip> tipList = new ArrayList<>();
//
//    public UniversalBaseBannerAdapter(List<Tip> tipList) {
//        this.tipList = tipList;
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public TipsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        return new TipsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_item,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull UniversalBaseBannerAdapter.TipsViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return tipList.isEmpty()?0: tipList.size();
//    }
//
//    class TipsViewHolder extends RecyclerView.ViewHolder{
//
//        public TipsViewHolder(@NonNull @NotNull View itemView) {
//            super(itemView);
//        }
//    }
}
