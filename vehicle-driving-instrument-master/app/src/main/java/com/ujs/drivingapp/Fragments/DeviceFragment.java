package com.ujs.drivingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ujs.drivingapp.Adapters.UniversalBaseBannerAdapter;
import com.ujs.drivingapp.Pojo.Device;
import com.ujs.drivingapp.Pojo.Tip;
import com.ujs.drivingapp.R;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Device> deviceList = new ArrayList<>();
    private BannerViewPager<Device, UniversalBaseBannerAdapter.BaseBannerViewHolder<Device>> mViewPager;

    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
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

        deviceList.add(new Device(1, "智能手环"));
        deviceList.add(new Device(2, "守心盒子"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mViewPager = requireView().findViewById(R.id.banner_device);
//        mViewPager.setLifecycleRegistry(getLifecycle())
//                .setAdapter(new UniversalBaseBannerAdapter<Device>(R.layout.device_item) {
//                    @Override
//                    protected void onBind(BaseBannerViewHolder<Device> holder, Device data, int position, int pageSize) {
//                        TextView textView = holder.itemView.findViewById(R.id.tv_device_name);
//                        textView.setText(data.getName());
//                    }
//                })
//                .setPageMargin(24)
//                .create(deviceList);


    }
}