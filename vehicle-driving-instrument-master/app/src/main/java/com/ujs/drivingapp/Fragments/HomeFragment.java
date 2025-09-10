package com.ujs.drivingapp.Fragments;

/**
 * @author HWH.
 * @email huangwenhuan2019@outlook.com
 * @create time 1/27/2022 8:36 PM.
 * @description 首页
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.ujs.drivingapp.Adapters.UniversalBaseBannerAdapter;
import com.ujs.drivingapp.Adapters.UniversalRecyclerViewAdapter;
import com.ujs.drivingapp.Commen.Constants;
import com.ujs.drivingapp.Pojo.Option;
import com.ujs.drivingapp.Pojo.Tip;
import com.ujs.drivingapp.R;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    List<Option> options = new ArrayList<>();
    private List<Tip> tipList = new ArrayList<>();
    private BannerViewPager<Tip, UniversalBaseBannerAdapter.BaseBannerViewHolder<Tip>> mViewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        tipList.add(new Tip("Tip1"));
        tipList.add(new Tip("Tip2"));
        tipList.add(new Tip("Tip3"));

        options = new ArrayList<>();
        options.add(new Option("餐饮", R.drawable.ic_repast));
        options.add(new Option("景点", R.drawable.ic_attraction));
        options.add(new Option("酒店", R.drawable.ic_hotel));
        options.add(new Option("娱乐", R.drawable.ic_leisure));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
//        ViewPager2 viewPager2 = getView().findViewById(R.id.pager_tips);

//        UniversalBaseBannerAdapter adapter = new UniversalBaseBannerAdapter(tipList);
//        viewPager2.setAdapter(adapter);

        mViewPager = requireView().findViewById(R.id.banner_view);
        mViewPager.setLifecycleRegistry(getLifecycle())
                .setAdapter(new UniversalBaseBannerAdapter<Tip>(R.layout.tip_item) {
                    @Override
                    protected void onBind(BaseBannerViewHolder<Tip> holder, Tip data, int position, int pageSize) {

                    }
                })
                .setPageMargin(24)
                .create(tipList);


        RecyclerView recyclerView = requireView().findViewById(R.id.rv_options);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new UniversalRecyclerViewAdapter<Option>(options, R.layout.option_item) {
            @Override
            public void bindView(RecyclerViewHolder holder, Option obj) {
                Log.e(TAG, "bindView: ");
                holder.setDrawableRes(R.id.img_option_icon, obj.getIcon());
                holder.setText(R.id.tv_option_name, obj.getName());
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent("com.ujs.driving.app.POI");
                        String type="";
                        switch (obj.getName()){
                            case "餐饮":
                                type=Constants.Poi.TYPE_REPAST;
                                break;
                            case "景点":
                                type=Constants.Poi.TYPE_ATTRACTION;
                                break;
                            case "酒店":
                                type=Constants.Poi.TYPE_HOTEL;
                                break;
                            case "娱乐":
                                type=Constants.Poi.TYPE_LEISURE;
                                break;
                            default:
                                type=Constants.Poi.TYPE_ALL;
                                break;
                        }
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                });
            }
        });
        RelativeLayout relativeLayout = requireView().findViewById(R.id.rlayout_drive);

        TextView tv_type = requireView().findViewById(R.id.tv_type);
        relativeLayout.setClickable(true);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setClickable(false);
                Log.e("onClick", "onClick: tv_getText"+tv_type.getText());
                if ( "智能巡航".contentEquals(tv_type.getText())) {
                    Intent intent = new Intent("com.ujs.driving.app.DRIVE");
                    requireContext().startActivity(intent);
                } else {
                    //构建导航组件配置类，没有传入起点，所以起点默认为 “我的位置”
                    AmapNaviParams params = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                    //启动导航组件
                    AmapNaviPage.getInstance().showRouteActivity(getContext(), params, null);
                }

            }
        });


//        ImageView imageView = getView().findViewById(R.id.image_map);
//        Glide.with(this)
//                .load(R.drawable.bg_map)
//                .transform(new RoundedCorners(50))
//                .into(imageView);

    }

    public void onDrive(View view) {
        Log.e(TAG, "onDrive: Clicked");
    }
}