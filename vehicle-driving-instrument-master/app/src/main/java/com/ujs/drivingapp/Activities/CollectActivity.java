package com.ujs.drivingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.ujs.drivingapp.Adapters.InfoWindowAdapter;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.MapService;
import com.ujs.drivingapp.Utils.FullScreenUtil;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.MapUtil;
import com.ujs.drivingapp.Utils.RetrofitUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectActivity extends AppCompatActivity {

    MapView mapView;
    AMap aMap;
    MapService mapService;
    PoiSearch poiSearchId;

    Map<Marker, PoiItem> poiMarkers;
    InfoWindowAdapter infoWindowAdapter;
    Marker oldMarker = null;
    boolean infoWindowShown = true;

    int allNum=0;
    int currentNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        StatusBarStyleUtil.changeStatusBarTextColor(getWindow(), true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        ServiceSettings.updatePrivacyShow(getApplicationContext(), true, true);
        ServiceSettings.updatePrivacyAgree(getApplicationContext(), true);
        FullScreenUtil.setFullScreen(CollectActivity.this);

        mapService = new RetrofitUtil.Builder(CollectActivity.this).setClient().build(MapService.class);
        initMap();
        mapView.onCreate(savedInstanceState);


    }


    private void initMap() {

        try {
            mapView = findViewById(R.id.map);
            aMap = mapView.getMap();

            poiMarkers = new HashMap<>();
            infoWindowAdapter = new InfoWindowAdapter(getApplicationContext());
            infoWindowAdapter.setOnInfoWindowListener(new InfoWindowAdapter.OnInfoWindowListener() {
                @Override
                public void initData(InfoWindowAdapter infoWindowAdapter) {
                    PoiItem poiItem = infoWindowAdapter.getPoiItem();
                    Call<ResponseBody> call = mapService.getIsCollect(poiItem.getPoiId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            infoWindowAdapter.setCollect(response.isSuccessful());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onClickCollect(InfoWindowAdapter infoWindowAdapter) {
                    PoiItem poiItem = infoWindowAdapter.getPoiItem();
                    Gson gson = new Gson();
                    Map<String, String> map = new HashMap<>();
                    map.put("poiId", poiItem.getPoiId());
                    if (infoWindowAdapter.getCollect()) {
                        mapService.uncollect(gson.toJson(map)).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    infoWindowAdapter.setCollect(!infoWindowAdapter.getCollect());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    } else {
                        mapService.collect(gson.toJson(map)).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    infoWindowAdapter.setCollect(!infoWindowAdapter.getCollect());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                }

                @Override
                public void onClickNavi(InfoWindowAdapter infoWindowAdapter) {
                    PoiItem poiItem = infoWindowAdapter.getPoiItem();

                    MapUtil.startNavi(getApplicationContext(), new Poi(poiItem.getTitle(), new LatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude()), poiItem.getPoiId()));
                }
            });


            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);

            MyLocationStyle locationStyle = new MyLocationStyle();
            locationStyle.interval(1000);
            locationStyle.showMyLocation(true);
            locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
            aMap.setMyLocationStyle(locationStyle);
            aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            AMapLocationClient aMapLocationClient = new AMapLocationClient(getApplicationContext());

            //初始化定位监听器 只定位一次 作用：刚进入界面时自身在地图中心点
            aMapLocationClient.setLocationOption(new AMapLocationClientOption().setOnceLocationLatest(true));
            AMapLocationListener aMapLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation.getErrorCode() == 0) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                        aMapLocationClient.stopLocation();
                        aMapLocationClient.unRegisterLocationListener(this);
                    }
                }
            };
            aMapLocationClient.setLocationListener(aMapLocationListener);
            aMapLocationClient.startLocation();


            poiSearchId = new PoiSearch(CollectActivity.this, null);
            poiSearchId.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {

                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {
                    MarkerOptions options = new MarkerOptions();
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_collect));
                    Marker marker = MapUtil.addMarker(aMap, poiItem, options);
                    poiMarkers.put(marker,poiItem);
                    currentNum++;
                    if (currentNum==allNum){
                        infoWindowAdapter.setPoiMarkers(poiMarkers);
                        aMap.setInfoWindowAdapter(infoWindowAdapter);
                    }
                }
            });


            Call<ResponseBody> call = mapService.getAllCollect();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json = response.body().string();
                        Map<String, Object> map = JsonUtil.toMap(json);
                        Map<String, Object> data = JsonUtil.getMap(map, "data");
                        List<String> poiIdArr = (List<String>) data.get("poiId");

                        allNum=poiIdArr.size();
                        currentNum=0;
                        for (String poiId : poiIdArr) {
                            poiSearchId.searchPOIIdAsyn(poiId);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


            //点击Marker标记弹出
            aMap.addOnMarkerClickListener(marker -> {
                Set<Marker> markers = poiMarkers.keySet();
                //如果marker不是定位小蓝点
                if (markers.contains(marker)) {
                    if (oldMarker != null) {
                        oldMarker.hideInfoWindow();
                        infoWindowShown = true;
                    }
                    oldMarker = marker;
                    oldMarker.showInfoWindow();
                }
                return false;
            });

            aMap.addOnMapClickListener(latLng -> {
                if (oldMarker != null) {
                    if (oldMarker.isInfoWindowShown() && infoWindowShown) {
                        infoWindowShown = false;
                    } else if (oldMarker.isInfoWindowShown() && !infoWindowShown) {
                        oldMarker.hideInfoWindow();
                    }
                }
            });

            aMap.addOnInfoWindowClickListener(marker -> {
                if (oldMarker != null) {
                    infoWindowShown = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}