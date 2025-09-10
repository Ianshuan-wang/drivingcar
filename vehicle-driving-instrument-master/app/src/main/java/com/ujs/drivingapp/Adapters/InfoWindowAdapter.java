package com.ujs.drivingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.PoiItem;
import com.ujs.drivingapp.R;

import java.util.Map;


/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 12/02/2022 下午 9:08
 * @description
 */
public class InfoWindowAdapter implements AMap.InfoWindowAdapter {
    private Context mContext;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_collect;
    private Map<Marker, PoiItem> poiMarkers;
    private PoiItem poiItem;
    private ImageView img_collect;
    private OnInfoWindowListener onInfoWindowListener;
    private boolean isCollect;

    //点击事件
    private LinearLayout llinear_collect;
    private LinearLayout llinear_navi;

    public interface OnInfoWindowListener {
        /**
         * 初始化的时候调用
         *
         * @param infoWindowAdapter InfoWindowAdapter对象
         */
        void initData(InfoWindowAdapter infoWindowAdapter);

        /**
         * 点击收藏按钮的时候调用
         *
         * @param infoWindowAdapter InfoWindowAdapter对象
         */
        void onClickCollect(InfoWindowAdapter infoWindowAdapter);

        /**
         * 点击导航按钮的时候调用
         *
         * @param infoWindowAdapter InfoWindowAdapter对象
         */
        void onClickNavi(InfoWindowAdapter infoWindowAdapter);
    }

    public InfoWindowAdapter(Context context) {
        mContext = context;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
        if (collect) {
            img_collect.setImageResource(R.drawable.ic_driver_collect);
            tv_collect.setText("已收藏");
        } else {
            img_collect.setImageResource(R.drawable.ic_driver_uncollect);
            tv_collect.setText("收藏");
        }
    }

    public void setPoiMarkers(Map<Marker, PoiItem> poiMarkers) {
        this.poiMarkers = poiMarkers;
    }

    public void addPoiMarker(Marker marker,PoiItem poiItem){
        this.poiMarkers.put(marker,poiItem);
    }

    public void removePoiMarker(Marker marker){
        this.poiMarkers.remove(marker);
    }

    public void setOnInfoWindowListener(OnInfoWindowListener onInfoWindowListener) {
        this.onInfoWindowListener = onInfoWindowListener;
    }

    public boolean getCollect() {
        return isCollect;
    }

    public PoiItem getPoiItem(){
        return this.poiItem;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        poiItem = poiMarkers.get(marker);
        View view=initView();
        onInfoWindowListener.initData(this);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.driver_info_window, null, false);
        llinear_navi = view.findViewById(R.id.navigation_LL);
        llinear_collect = view.findViewById(R.id.llinear_collect);
        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        img_collect = view.findViewById(R.id.img_collect);
        tv_collect = view.findViewById(R.id.tv_collect);
        tv_name.setText(poiItem.getTitle());
        tv_address.setText("地址：" + poiItem.getSnippet());
        llinear_collect.setOnClickListener(v -> {
            onInfoWindowListener.onClickCollect(this);
        });
        llinear_navi.setOnClickListener(v->{
            onInfoWindowListener.onClickNavi(this);
        });
        return view;
    }

}