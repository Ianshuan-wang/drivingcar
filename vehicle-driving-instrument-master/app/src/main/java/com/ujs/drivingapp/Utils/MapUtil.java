package com.ujs.drivingapp.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

/**
 * @author huanqiu
 * @email 2364521714@qq.com
 * @create time 13/02/2022 下午 9:02
 * @description
 */
public class MapUtil {

    /**
     * 计算距离
     *
     * @param poiItem PoiItem对象
     * @param latLng  LatLng对象
     * @return 距离
     */
    public static float calculateDistance(@NonNull PoiItem poiItem, @NonNull LatLng latLng) {
        return AMapUtils.calculateLineDistance(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()), latLng);
    }

    /**
     * 计算距离
     *
     * @param p1 PoiItem对象
     * @param p2 PoiItem对象
     * @return 距离
     */
    public static float calculateDistance(@NonNull PoiItem p1, @NonNull PoiItem p2) {
        return AMapUtils.calculateLineDistance(new LatLng(p1.getLatLonPoint().getLatitude(), p1.getLatLonPoint().getLongitude()), new LatLng(p2.getLatLonPoint().getLatitude(), p2.getLatLonPoint().getLongitude()));
    }

    /**
     * 计算距离
     *
     * @param latLonPoint LatLonPoint对象
     * @param poiItem     PoiItem对象
     * @return 距离
     */
    public static float calculateDistance(@NonNull LatLonPoint latLonPoint, @NonNull PoiItem poiItem) {
        return AMapUtils.calculateLineDistance(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
    }

    /**
     * 计算距离
     *
     * @param latLonPoint LatLonPoint对象
     * @param latLng      LatLng
     * @return 距离
     */
    public static float calculateDistance(@NonNull LatLonPoint latLonPoint, @NonNull LatLng latLng) {
        return AMapUtils.calculateLineDistance(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), latLng);
    }

    /**
     * 开始导航
     *
     * @param context 上下文对象
     * @param end     Poi对象 终点
     */
    public static void startNavi(Context context, Poi end) {
        AmapNaviParams params = new AmapNaviParams(null, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
        AmapNaviPage.getInstance().showRouteActivity(context, params, null);
    }

    /**
     * 退出导航
     */
    public static void exitNavi() {
        AmapNaviPage.getInstance().exitRouteActivity();
    }

    /**
     * 移动地图视角至指定地点
     *
     * @param aMap      地图对象
     * @param latLng    点的经纬度
     * @param addMarker 是否添加标记点
     * @return 若添加标记点则返回标记点，否则返回null
     */
    public static Marker moveToPoi(AMap aMap, LatLng latLng, boolean addMarker) {
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (addMarker) {
            return aMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            return null;
        }
    }

    /**
     * 移动地图视角至指定地点
     *
     * @param aMap      地图对象
     * @param latLng    点的经纬度
     * @param options   标记点配置信息
     * @param addMarker 是否添加标记点
     * @return 若添加标记点则返回标记点，否则返回null
     */
    public static Marker moveToPoi(AMap aMap, LatLng latLng, MarkerOptions options, boolean addMarker) {
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (addMarker) {
            return aMap.addMarker(options.position(latLng));
        } else {
            return null;
        }
    }

    /**
     * 移动地图视角至指定地点
     *
     * @param aMap      地图对象
     * @param poiItem   Poi点
     * @param addMarker 是否添加标记点
     * @return 若添加标记点则返回标记点，否则返回null
     */
    public static Marker moveToPoi(AMap aMap, PoiItem poiItem, boolean addMarker) {
        LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (addMarker) {
            return aMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            return null;
        }
    }

    /**
     * 移动地图视角至指定地点
     *
     * @param aMap      地图对象
     * @param poiItem   Poi点
     * @param options 标记点配置信息
     * @param addMarker 是否添加标记点
     * @return 若添加标记点则返回标记点，否则返回null
     */
    public static Marker moveToPoi(AMap aMap, PoiItem poiItem,MarkerOptions options , boolean addMarker) {
        LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (addMarker) {
            return aMap.addMarker(options.position(latLng));
        } else {
            return null;
        }
    }

    /**
     * 添加地图标记
     *
     * @param aMap    地图对象
     * @param poiItem Poi点
     * @return 标记对象
     */
    public static Marker addMarker(AMap aMap, PoiItem poiItem) {
        LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
        return aMap.addMarker(new MarkerOptions().position(latLng));
    }

    /**
     * 添加地图标记
     *
     * @param aMap    地图对象
     * @param poiItem Poi点
     * @param options 点配置信息
     * @return 标记对象
     */
    public static Marker addMarker(AMap aMap, PoiItem poiItem, MarkerOptions options) {
        LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
        return aMap.addMarker(options.position(latLng));
    }


}
