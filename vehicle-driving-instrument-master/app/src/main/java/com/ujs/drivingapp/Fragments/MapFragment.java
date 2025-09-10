package com.ujs.drivingapp.Fragments;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:35 PM.
 * @description 汇总部分 地图 界面
 */

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.MovingPointOverlay;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.ujs.drivingapp.Pojo.Path;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.MapService;
import com.ujs.drivingapp.Room.PathDao;
import com.ujs.drivingapp.Room.PathDataBase;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.RetrofitUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MapFragment extends Fragment {

    private static String TAG = "MapFragment";
    private static MapFragment fragment = null;
    public static final int POSITION = 0;

    private MapView mapView;
    private AMap aMap;
    private View mapLayout;
    private UiSettings mUiSettings;
    private String statisticId;
    MapService mapService;

    private Map<String, LatLng> pointMap;

    TextView tv_mileage_current;

    public static MapFragment newInstance() {
        if (fragment == null) {
            synchronized (MapFragment.class) {
                if (fragment == null) {
                    fragment = new MapFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        if (mapLayout == null) {
            Log.i("sys", "MF onCreateView() null");
            mapLayout = inflater.inflate(R.layout.fragment_map, null);
            mapView = (MapView) mapLayout.findViewById(R.id.map_track);

            mapView.onCreate(savedInstanceState);
            if (aMap == null) {
                aMap = mapView.getMap();
            }

            aMap.setMapType(AMap.MAP_TYPE_NAVI);

            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//  设置地图缩放级数
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对
            mUiSettings.setZoomControlsEnabled(false);

        } else {

            if (mapLayout.getParent() != null) {
                ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
            }
        }
        Log.e(TAG, "onCreateView: CreateView");
        return mapLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: Create");
        mapService = new RetrofitUtil.Builder(requireContext()).setClient().setEnv(RetrofitUtil.Env.ENV_DEV).build(MapService.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        tv_mileage_current=findViewById(R.id.tv_mileage_current);

        Bundle b = getArguments();
        if (null != b) {
            statisticId = b.getString("statisticId");
            Log.e(TAG, "onStart: " + statisticId);
        }

//        mapService.getPath("123").enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    pointMap = new LinkedHashMap<>();
//                    String json = response.body().string();
//                    System.out.println(json);
//                    Map<String, Object> map = JsonUtil.toMap(json);
//                    Map<String, Object> data = JsonUtil.getMap(map, "data");
//                    String path = JsonUtil.getString(data, "path");
//                    System.out.println(path);
//                    String startTime = JsonUtil.getString(data, "startTime");
//                    System.out.println(startTime);
//                    String[] points = path.split(";");
//                    List<LatLng> latLngs = new ArrayList<>();
//                    for (String point : points) {
//                        String[] split = point.split(",");
//                        pointMap.put(split[split.length - 1], new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
//                        latLngs.add(new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
//                    }
//                    PolylineOptions polylineOptions = new PolylineOptions();
//                    polylineOptions.width(10).color(Color.argb(255, 25, 198, 73));
//                    polylineOptions.addAll(latLngs);
//                    aMap.addPolyline(polylineOptions);
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngs.get(0)));
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(latLngs.get(0));
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
//                    Marker marker = aMap.addMarker(markerOptions);
//                    MovingPointOverlay movingPointOverlay = new MovingPointOverlay(aMap, marker);
//
//                    movingPointOverlay.setPoints(latLngs);
//                    movingPointOverlay.setTotalDuration(20);
//                    movingPointOverlay.startSmoothMove();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


        PathDataBase dataBase = PathDataBase.getInstance(getContext());
        PathDao pathDao = dataBase.pathDao();
        Path path = pathDao.selectOne();

        Log.e("onStart", "onStart: "+path.getMileage());
        tv_mileage_current.setText(String.format("%.2f", path.getMileage()/1000));
        pointMap = new LinkedHashMap<>();
        String[] points = path.getPath().split(";");
        List<LatLng> latLngs = new ArrayList<>();
        for (String point : points) {
            String[] split = point.split(",");
            pointMap.put(split[split.length - 1], new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
            latLngs.add(new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(14).color(Color.argb(255, 25, 198, 73));
        polylineOptions.addAll(latLngs);
        aMap.addPolyline(polylineOptions);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 17));
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        smoothMarker.setPoints(latLngs);
        smoothMarker.setTotalDuration(10);
        smoothMarker.startSmoothMove();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: Stop");
    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        super.onResume();
        mapView.onResume();
        Log.e(TAG, "onResume: Resume");
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        super.onPause();
        mapView.onPause();
        Log.e(TAG, "onPause: Pause");
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: SaveInstanceState");
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        super.onDestroy();
        mapView.onDestroy();
        mapLayout = null;
        aMap = null;
        Log.e(TAG, "onDestroy: Destroy");
    }

    public <T extends View> T findViewById(@IdRes int resId){
        return requireView().findViewById(resId);
    }

}