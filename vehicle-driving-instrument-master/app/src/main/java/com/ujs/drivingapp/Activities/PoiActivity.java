package com.ujs.drivingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.ujs.drivingapp.Adapters.InfoWindowAdapter;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Commen.Constants;
import com.ujs.drivingapp.Pojo.PoiSearchHistory;
import com.ujs.drivingapp.R;
import com.ujs.drivingapp.Retrofit.MapService;
import com.ujs.drivingapp.Room.PoiSearchHistoryDao;
import com.ujs.drivingapp.Room.PoiSearchHistoryDataBase;
import com.ujs.drivingapp.Utils.JsonUtil;
import com.ujs.drivingapp.Utils.MapUtil;
import com.ujs.drivingapp.Utils.MyUtil;
import com.ujs.drivingapp.Utils.RetrofitUtil;
import com.ujs.drivingapp.Utils.StatusBarStyleUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PoiActivity extends AppCompatActivity {
    private static final String TAG = "PoiActivity";
    AMap aMap;
    AMapLocationClient aMapLocationClient;
    PoiSearch poiSearch;
    PoiSearch poiSearchId;
    Map<Marker, PoiItem> poiMarkers;
    InfoWindowAdapter infoWindowAdapter;
    Marker oldMarker = null;
    boolean infoWindowShown = true;
    Inputtips inputtips;
    LatLng currentLatLng;
    String currentPlace;

    int allTipNum;
    int readyTipNum = 0;
    Marker currentPoiMarker = null;//当前poi搜索时添加的marker点

    MapView mapView;
    String poiType = "";

    TextView tv_tip;
    EditText edt_search;
    RecyclerView rec_type;
    RecyclerView rec_content;
    RecyclerView rec_history;
    List<PoiItem> resultPoi;
    List<String> currentPoiId;
    UniversalRecyclerViewAdapter<PoiItem> poiItemAdapter;
    UniversalRecyclerViewAdapter<PoiSearchHistory> poiSearchHistoryAdapter;
    View mask;
    CardView search_box;

    PoiSearchHistoryDao poiSearchHistoryDao;
    List<PoiSearchHistory> poiSearchHistories = new ArrayList<>();

    MapService mapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        Window window = getWindow();
        StatusBarStyleUtil.changeStatusBarTextColor(window, true);
        Objects.requireNonNull(getSupportActionBar()).hide();//这行代码必须写在setContentView()方法的后面
        ServiceSettings.updatePrivacyShow(getApplicationContext(), true, true);
        ServiceSettings.updatePrivacyAgree(getApplicationContext(), true);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        poiSearchHistoryDao = PoiSearchHistoryDataBase.getInstance(getApplicationContext()).poiSearchHistoryDao();
        Intent intent = getIntent();
        poiType = intent.getStringExtra("type");
        initView();
        initMap();
        mapView.onCreate(savedInstanceState);

        mapService = new RetrofitUtil.Builder(PoiActivity.this).setEnv(RetrofitUtil.Env.ENV_PROD).setClient().build(MapService.class);
    }

    private void initView() {
        tv_tip = findViewById(R.id.tv_tip);
        edt_search = findViewById(R.id.edt_search);
        rec_content = findViewById(R.id.rec_content);
        rec_type = findViewById(R.id.rec_types);
        rec_history = findViewById(R.id.rec_history);
        search_box = findViewById(R.id.search_box);
        mask = findViewById(R.id.mask);
        edt_search.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == 0 || actionId == 3) && event != null) {
                if (poiSearchHistoryDao.selectByContent(edt_search.getText().toString().trim()).size() == 0) {
                    poiSearchHistoryDao.insert(new PoiSearchHistory(null, edt_search.getText().toString().trim()));
                }
                search("");
                MyUtil.hideSoftInputFromWindow(getApplicationContext(), edt_search);
            }
            return false;
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search("");
                if (s.toString().trim().equals("")) {
                    currentPoiId.clear();
                    resultPoi.clear();
                    poiItemAdapter.notifyDataSetChanged();
                    showHistory();
                }

            }
        });

        resultPoi = new ArrayList<>();
        currentPoiId = new ArrayList<>();
        rec_content.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        poiItemAdapter = new UniversalRecyclerViewAdapter<PoiItem>(resultPoi, R.layout.poi_item) {
            @Override
            public void bindView(RecyclerViewHolder holder, PoiItem obj) {
                float distance = MapUtil.calculateDistance(obj, currentLatLng);
                if (distance <= 800) {
                    holder.setText(R.id.tv_poi_item_distance, (int) distance + "米");
                } else if (distance < 1000) {
                    holder.setText(R.id.tv_poi_item_distance, String.format("%.2f公里", distance / 1000));
                } else if (distance < 20000) {
                    holder.setText(R.id.tv_poi_item_distance, String.format("%.1f公里", distance / 1000));
                } else {
                    holder.setText(R.id.tv_poi_item_distance, "路线");
                }
                LatLng latLng = new LatLng(obj.getLatLonPoint().getLatitude(), obj.getLatLonPoint().getLongitude());
                holder.setText(R.id.tv_poi_item_name, obj.getTitle());
                holder.setText(R.id.tv_poi_item_snippet, obj.getSnippet());
                holder.setOnClickListener(v -> {
                    MapUtil.startNavi(getApplicationContext(), new Poi(obj.getTitle(), latLng, obj.getPoiId()));
                    if (poiSearchHistoryDao.selectByContent(obj.getTitle()).size() == 0) {
                        poiSearchHistoryDao.insert(new PoiSearchHistory(null, obj.getTitle(), Constants.PoiSearchHistory.TYPE_POI, obj.getPoiId()));
                    }
                });

                holder.setOnClickListener(R.id.llayout_poi_item_car, v -> {
                    MapUtil.startNavi(getApplicationContext(), new Poi(obj.getTitle(), latLng, obj.getPoiId()));
                    if (poiSearchHistoryDao.selectByContent(obj.getTitle()).size() == 0) {
                        poiSearchHistoryDao.insert(new PoiSearchHistory(null, obj.getTitle(), Constants.PoiSearchHistory.TYPE_POI, obj.getPoiId()));
                    }
                });

                holder.setOnClickListener(R.id.llayout_poi_item_nav, v -> {
//                    MapUtil.startNavi(getApplicationContext(), new Poi(obj.getTitle(), latLng, obj.getPoiId()));
                    if (poiSearchHistoryDao.selectByContent(obj.getTitle()).size() == 0) {
                        poiSearchHistoryDao.insert(new PoiSearchHistory(null, obj.getTitle(), Constants.PoiSearchHistory.TYPE_POI, obj.getPoiId()));
                    }
                });

                holder.setOnClickListener(R.id.llayout_poi_item_content, v -> {
                    if (poiSearchHistoryDao.selectByContent(obj.getTitle()).size() == 0) {
                        poiSearchHistoryDao.insert(new PoiSearchHistory(null, obj.getTitle(), Constants.PoiSearchHistory.TYPE_POI, obj.getPoiId()));
                    }
                    onQuitSearch(findViewById(R.id.img_quit));
                    if (currentPoiMarker != null) {
                        infoWindowAdapter.removePoiMarker(currentPoiMarker);
                        currentPoiMarker.remove();
                    }
                    currentPoiMarker = MapUtil.moveToPoi(aMap, latLng, new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_default)), true);
                    infoWindowAdapter.addPoiMarker(currentPoiMarker, obj);
                    currentPoiMarker.showInfoWindow();
                    oldMarker = currentPoiMarker;
                    infoWindowShown = false;
                });
            }
        };
        rec_content.setAdapter(poiItemAdapter);

        //历史记录代码段
        rec_history.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        poiSearchHistoryAdapter = new UniversalRecyclerViewAdapter<PoiSearchHistory>(poiSearchHistories, R.layout.poi_search_history_item) {
            @Override
            public void bindView(RecyclerViewHolder holder, PoiSearchHistory obj) {
                Log.e("bindView", "bindView: obj:" + obj.getContent());
                holder.setText(R.id.tv_content, obj.getContent());
                if (obj.getType().equals(Constants.PoiSearchHistory.TYPE_POI)) {
                    holder.getView(R.id.img_icon).setBackgroundResource(R.drawable.ic_poi_history_location);
                    holder.setOnClickListener(v -> {
                        try {
                            PoiSearch ps = new PoiSearch(PoiActivity.this, null);
                            ps.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                                @Override
                                public void onPoiSearched(PoiResult poiResult, int i) {

                                }

                                @Override
                                public void onPoiItemSearched(PoiItem poiItem, int i) {
                                    if (currentPoiMarker != null) {
                                        infoWindowAdapter.removePoiMarker(currentPoiMarker);
                                        currentPoiMarker.remove();
                                    }
                                    currentPoiMarker = MapUtil.moveToPoi(aMap, poiItem,new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_default)), true);
                                    infoWindowAdapter.addPoiMarker(currentPoiMarker, poiItem);
                                    onQuitSearch(findViewById(R.id.img_quit));
                                    currentPoiMarker.showInfoWindow();
                                    oldMarker = currentPoiMarker;
                                    infoWindowShown = false;
                                }
                            });
                            ps.searchPOIIdAsyn(obj.getPoiId());
                        } catch (AMapException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    holder.getView(R.id.img_icon).setBackgroundResource(R.drawable.ic_poi_history_search);
                    holder.getView(R.id.llayout_pos).setVisibility(View.INVISIBLE);
                    holder.setOnClickListener(v -> {
                        edt_search.setText(obj.getContent());
                        edt_search.clearFocus();
                        MyUtil.hideSoftInputFromWindow(getApplicationContext(), edt_search);
                    });
                }
            }
        };
        rec_history.setAdapter(poiSearchHistoryAdapter);
        //历史记录代码段

    }


    private void initMap() {
        try {
            mapView = findViewById(R.id.poi_map);
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
            //初始化定位蓝点
            MyLocationStyle locationStyle = new MyLocationStyle();
            locationStyle.interval(1000);
            locationStyle.showMyLocation(true);
            locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
            aMap.setMyLocationStyle(locationStyle);
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setMyLocationButtonEnabled(false);
            uiSettings.setZoomControlsEnabled(false);
            aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));//  设置地图缩放级数
            aMapLocationClient = new AMapLocationClient(getApplicationContext());
            //初始化定位监听器 只定位一次 作用：刚进入界面时自身在地图中心点
            aMapLocationClient.setLocationOption(new AMapLocationClientOption().setOnceLocationLatest(true));
            aMapLocationClient.setLocationListener(aMapLocation -> {
                if (aMapLocation.getErrorCode() == 0) {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    aMapLocationClient.stopLocation();
                }
            });
            aMapLocationClient.startLocation();
            aMapLocationClient.setLocationOption(new AMapLocationClientOption().setOnceLocationLatest(false));
            aMapLocationClient.setLocationListener(aMapLocation -> {
                if (aMapLocation.getErrorCode() == 0) {
                    double latitude = aMapLocation.getLatitude();
                    double longitude = aMapLocation.getLongitude();
                    currentLatLng = new LatLng(latitude, longitude);
                    currentPlace = aMapLocation.getCityCode();
                    addPoiMarker(currentLatLng, 20000);
                }
            });

            //设置Poi查询监听器
            PoiSearch.Query query = new PoiSearch.Query("", poiType);
            query.setPageSize(100);
            poiSearch = new PoiSearch(PoiActivity.this, query);
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    ArrayList<PoiItem> pois = poiResult.getPois();
                    poiMarkers.clear();
                    if (pois.size() > 0) {
                        for (PoiItem poiItem : pois) {
                            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                            MarkerOptions markerOptions = new MarkerOptions();
                            switch (poiType) {
                                case Constants.Poi.TYPE_HOTEL:
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_hotel));
                                    break;
                                case Constants.Poi.TYPE_REPAST:
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_repast));
                                    break;
                                case Constants.Poi.TYPE_ATTRACTION:
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_attraction));
                                    break;
                                case Constants.Poi.TYPE_LEISURE:
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_leisure));
                                    break;
                                default:
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_poi_marker_default));
                                    break;
                            }
                            markerOptions.position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                            Marker marker = aMap.addMarker(markerOptions);
                            poiMarkers.put(marker, poiItem);
                        }
                        infoWindowAdapter.setPoiMarkers(poiMarkers);
                        aMap.setInfoWindowAdapter(infoWindowAdapter);
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });

            //点击Marker标记弹出
            aMap.addOnMarkerClickListener(marker -> {
                Set<Marker> markers = poiMarkers.keySet();
                //如果marker不是定位小蓝点
                if (markers.contains(marker) || marker.equals(currentPoiMarker)) {
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

            poiSearchId = new PoiSearch(PoiActivity.this, null);
            poiSearchId.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {
                    if (currentPoiId.contains(poiItem.getPoiId()) && !resultPoi.contains(poiItem)) {
                        resultPoi.add(poiItem);
                        readyTipNum++;
                    }
                    if (readyTipNum == allTipNum && readyTipNum != 0) {
                        resultPoi.sort((o1, o2) -> {
                            if (MapUtil.calculateDistance(o1, currentLatLng) < MapUtil.calculateDistance(o2, currentLatLng)) {
                                return -1;
                            } else if (MapUtil.calculateDistance(o1, currentLatLng) > MapUtil.calculateDistance(o2, currentLatLng)) {
                                return 1;
                            }
                            return 0;
                        });
                        poiItemAdapter.notifyDataSetChanged();
                        findViewById(R.id.tv_tip2).setVisibility(View.INVISIBLE);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以latLng为圆心以range为半径的圆内所有的poi点画上标记
     *
     * @param latLng 经纬度坐标
     * @param range  半径
     */
    private void addPoiMarker(LatLng latLng, int range) {
        if (poiMarkers.size() > 0) {
            for (Marker poiMarker : poiMarkers.keySet()) {
                poiMarker.remove();
            }
        }
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
                latLng.longitude), range));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
    }


    public void search(String type) {
        rec_history.setVisibility(View.INVISIBLE);
        readyTipNum = 0;
        allTipNum = 0;
        resultPoi.clear();
        currentPoiId.clear();
        String text = edt_search.getText().toString();
        Log.e("search", "search: Text" + text);
        if (text.trim().equals("")) return;
        InputtipsQuery query = new InputtipsQuery(text, currentPlace);
        if (!type.equals("")) {
            query.setType(type);
        }
        query.setCityLimit(true);
        query.setLocation(new LatLonPoint(currentLatLng.latitude, currentLatLng.longitude));
        inputtips = new Inputtips(PoiActivity.this, query);
        inputtips.setInputtipsListener((list, i) -> {
            if (!list.isEmpty()) {
                for (Tip tip : list) {
                    if (tip.getPoiID() != null && tip.getPoint() != null) {
                        allTipNum++;
                        currentPoiId.add(tip.getPoiID());
                    }
                }
                for (Tip tip : list) {
                    if (tip.getPoiID() == null && tip.getPoint() == null) {
                        //TODO 根据关键词查询
                        String keyword = tip.getName();
                    } else if (tip.getPoiID() != null && tip.getPoint() == null) {
                        //TODO 公交线路 根据ID查询公交线路
                    } else if (tip.getPoiID() != null && tip.getPoint() != null) {
                        poiSearchId.searchPOIIdAsyn(tip.getPoiID());
                    }
                }
            } else {
                //TODO 显示空状态
            }
        });
        inputtips.requestInputtipsAsyn();
    }

    //展示搜索界面
    public void onStartSearch(View view) {
        findViewById(R.id.img_quit).setClickable(true);
        search_box.setVisibility(View.INVISIBLE);
        mask.setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_tip2).setVisibility(View.VISIBLE);
        long time = 300;

        showHistory();

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(time);
        sa.setFillAfter(true);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                search_box.setVisibility(View.VISIBLE);
                MyUtil.showSoftInput(getApplicationContext(), edt_search);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(time);
        aa.setFillAfter(true);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                search_box.clearAnimation();
                mask.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(sa);


        ScaleAnimation sa1 = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(time);
        sa1.setFillAfter(true);
        sa1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AnimationSet as1 = new AnimationSet(true);
        as1.addAnimation(aa);
        as1.addAnimation(sa1);

        search_box.startAnimation(as);
        mask.startAnimation(as1);
    }

    //隐藏搜索界面
    public void onQuitSearch(View view) {
        view.setClickable(false);
        MyUtil.hideSoftInputFromWindow(getApplicationContext(), edt_search);
        long time = 300;
        ScaleAnimation sa = new ScaleAnimation(1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(time);
        sa.setFillAfter(true);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("onAnimationEnd", "onAnimationEnd: search 结束了");
                search_box.clearAnimation();
                edt_search.setText("");
                resultPoi.clear();
                poiItemAdapter.notifyDataSetChanged();
                search_box.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(time);
        aa.setFillAfter(true);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(aa);
        as.addAnimation(sa);

        ScaleAnimation sa1 = new ScaleAnimation(1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(time);
        sa1.setFillAfter(true);
        sa1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("onAnimationEnd", "onAnimationEnd: mask 结束了");
                mask.clearAnimation();
                mask.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AnimationSet as1 = new AnimationSet(true);
        as1.addAnimation(aa);
        as1.addAnimation(sa1);

        search_box.startAnimation(as);
        mask.startAnimation(as1);
    }

    //点击搜索按钮
    public void onSearch(View view) {
        search("");
        MyUtil.hideSoftInputFromWindow(getApplicationContext(), edt_search);
    }

    public void showHistory() {
        List<PoiSearchHistory> poiSearchHistories = poiSearchHistoryDao.select();
        for (PoiSearchHistory poiSearchHistory : poiSearchHistories) {
            Log.e("showHistory", "showHistory: " + poiSearchHistory.getContent());
        }
        if (poiSearchHistories.size() == 0) {
            rec_history.setVisibility(View.INVISIBLE);
        } else {
            rec_history.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_tip2).setVisibility(View.INVISIBLE);
            this.poiSearchHistories.clear();
            this.poiSearchHistories.addAll(poiSearchHistories);
            poiSearchHistoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mask.getVisibility() == View.VISIBLE) {
                //拦截事件
                onQuitSearch(findViewById(R.id.img_quit));
                return false;
            }
        }


        return super.onKeyDown(keyCode, event);
    }
}