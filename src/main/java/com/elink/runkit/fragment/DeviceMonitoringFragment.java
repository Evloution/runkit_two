package com.elink.runkit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.elink.runkit.R;
import com.elink.runkit.activity.DeviceMonitoringDetailsActivity;
import com.elink.runkit.activity.MonitoringSearchActivity;
import com.elink.runkit.adapter.MapClickEventsAdapter;
import com.elink.runkit.adapter.MonitoringPointAdapter;
import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.MonitoringPointBean;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.MonitoringPointPresenter;
import com.elink.runkit.util.ToastUtil;
import com.elink.runkit.view.DataView;
import com.elink.runkit.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @author Evloution
 * @date 2020-01-08
 * @email 15227318030@163.com
 * @description
 */
public class DeviceMonitoringFragment extends Fragment {

    @BindView(R.id.fragment_devicemonitoring_reload_btn)
    Button fragmentDevicemonitoringReloadBtn; // 重新加载按钮
    @BindView(R.id.fragment_devicemonitoring_reload_linearlayout)
    LinearLayout fragmentDevicemonitoringReloadLinearlayout; // 重新加载按钮的LinearLayout
    @BindView(R.id.fragment_devicemonitoring_linearlayout)
    LinearLayout fragmentDevicemonitoringLinearlayout; // 主体的LinearLayout
    @BindView(R.id.fragment_devicemonitoring_mapview)
    MapView fragmentDevicemonitoringMapview; // 地图的显示
    Unbinder unbinder;
    @BindView(R.id.search_img)
    ImageView searchImg; // 搜索按钮
    @BindView(R.id.devices_total_txt)
    TextView devicesTotalTxt; // 设备列表总数
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout; // 下拉刷新
    @BindView(R.id.devicelist_listview)
    LoadListView devicelistListview; // 设备列表

    private AMap aMap = null;
    private Marker marker = null;
    public Map<String, Marker> markerMap = new ConcurrentHashMap<>();
    public static float ORGZOON = 30; // 地图初始化比例尺,地图比例尺

    private MonitoringPointPresenter monitoringPointPresenter = null;
    private MonitoringPointAdapter monitoringPointAdapter = null;
    private List<MonitoringPointBean> monitoringPointList = null;
    private int page = 1;
    private int searchcode = 0; // 是否要显示重新加载按钮
    private String pointName = "";
    private String pointIP = "";

    private final int REQUESTCODES = 1001; // 跳转到搜索页面的请求码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devicemonitoring, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentDevicemonitoringMapview.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        page = 1;
        pointName = "";
        pointIP = "";
        initData(pointName, pointIP, 0, Integer.toString(page));
    }

    private void initView() {
        monitoringPointList = new ArrayList<>();
        monitoringPointPresenter = new MonitoringPointPresenter(getContext());
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = fragmentDevicemonitoringMapview.getMap();
        }
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示，非必需设置。
        aMap.showIndoorMap(true); // 是否显示室内地图。 true：显示室内地图；false：不显示；
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是 false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10)); // 设置地图缩放比例

        // 下拉刷新
        swiperefreshlayout.setColorSchemeResources(R.color.colorPrimary, R.color.blueness_two, R.color.blueness_three);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新", "下拉刷新");
                page = 1;
                pointName = "";
                pointIP = "";
                initData(pointName, pointIP, 1, Integer.toString(page));
            }
        });

        // 上拉加载
        devicelistListview.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                L.e("上拉加载");
                page++;
                initData(pointName, pointIP, 2, Integer.toString(page));
            }
        });

        // 列表的点击事件
        devicelistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.e("点击的是：" + monitoringPointList.get(position).ID);
                loadMarker(monitoringPointList, 1, position);
            }
        });
    }

    private void initData(String pointName, String pointIP, final int code, final String page) {
        L.e("initData中的code：+ page：" + page);
        monitoringPointPresenter.onCreate();
        monitoringPointPresenter.getPointListPresenter(pointName, pointIP, "10", page);
        monitoringPointPresenter.attachView(new DataView<BaseDataListBean<MonitoringPointBean>>() {
            @Override
            public void onSuccess(BaseDataListBean<MonitoringPointBean> TBean) {
                searchcode = 1; // 走这里说明第一次进入页面已经成功，不再需要显示重新加载按钮了
                L.e("onSuccess：" + TBean.getData().size());
                fragmentDevicemonitoringReloadLinearlayout.setVisibility(View.GONE);
                swiperefreshlayout.setVisibility(View.VISIBLE);
                // 给页面赋值
                devicesTotalTxt.setText("共" + TBean.count + "家");
                // 如果时下拉刷新和第一次进入时，要重新初始化 monitoringPointList，否则下拉刷新时也会往list中add数据
                if (code == 1 || code == 0) {
                    monitoringPointList = new ArrayList<>();
                }
                // 将数据放入list中，再传到adapter
                for (int i = 0; i < TBean.getData().size(); i++) {
                    monitoringPointList.add(TBean.getData().get(i));
                }
                // 第一次加载数据和下拉刷新时走这里
                if ("1".equals(page) || page == "1") {
                    // 这个判断是为了只创建一次 MonitoringPointAdapter对象，如果上拉加载时再次走这里的话会直接弹回顶部。
                    monitoringPointAdapter = new MonitoringPointAdapter(getContext(), monitoringPointList, new MonitoringPointAdapter.MonitoringPointCallback() {
                        @Override
                        public void click(View view) {
                            final int position = (Integer) view.getTag();
                            /**
                             * 详情按钮的点击事件
                             * @param view
                             */
                            switch (view.getId()) {
                                case R.id.item_devicemonitoring_device_details_ll:
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), DeviceMonitoringDetailsActivity.class);
                                    intent.putExtra("monitoringId", monitoringPointList.get(position).ID);
                                    startActivity(intent);
                                    break;
                            }
                        }
                    });
                    devicelistListview.setAdapter(monitoringPointAdapter);
                }
                // 更新地图上点的数据
                updateNormalMarkers(monitoringPointList);
                // 请求成功后取消刷新框
                isCloseLoad(code);
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(getContext(), error);
                if (searchcode == 0) {
                    fragmentDevicemonitoringReloadLinearlayout.setVisibility(View.VISIBLE);
                    swiperefreshlayout.setVisibility(View.GONE);
                }
                // 请求失败后取消刷新框
                isCloseLoad(code);
            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
    }

    /**
     * 更新地图上点的数据
     */
    private void updateNormalMarkers(List<MonitoringPointBean> monitoringPointList) {
        L.e("monitoringPointList:" + monitoringPointList);
        // 判断上一次更新marker操作的操作类型,若上次显示的是聚合网点,则先清空地图,然后清空网点信息,在刷新地图marker
        aMap.clear();
        markerMap.clear();
        //设置自定义弹窗
        aMap.setInfoWindowAdapter(new MapClickEventsAdapter(getContext(), monitoringPointList));
        // 设置maker点击时的响应
        aMap.setOnMarkerClickListener(new MapClickEventsAdapter(getContext()));
        // 设置点击 地图的点击事件
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.hideInfoWindow();
            }
        });
        loadMarker(monitoringPointList, 0, 0);
    }

    /**
     * 初始化marker数据
     */
    private void loadMarker(List<MonitoringPointBean> monitoringPointBeanList, int code, int postion) {
        LatLng latLng = null;
        if (monitoringPointBeanList == null || monitoringPointBeanList.size() == 0) {
            return;
        }
        L.e("monitoringPointBeanList:" + monitoringPointBeanList.get(postion).NAME);
        if (code == 1) { // code 等于 1 说明是点击的列表后走进这个方法
            L.e("getLATITUDE()：" + monitoringPointBeanList.get(postion).getLATITUDE());
            L.e("getLONGITUDE()" + monitoringPointBeanList.get(postion).getLONGITUDE());
            latLng = new LatLng(monitoringPointBeanList.get(postion).getLATITUDE(), monitoringPointBeanList.get(postion).getLONGITUDE());
            MarkerOptions options = new MarkerOptions();
            options.anchor(0.5f, 1.0f);
            options.position(latLng);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_green_24dp));

            marker = aMap.addMarker(options);
            marker.setObject(monitoringPointBeanList);
            marker.setZIndex(ORGZOON);
            marker.setPeriod(postion);

            // 点击列表后根据经纬度将地图缩放比例调到15
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            marker.showInfoWindow();
        } else {
            for (int i = 0; i < monitoringPointBeanList.size(); i++) {
                MonitoringPointBean monitoringPointBean = monitoringPointBeanList.get(i);
                L.e("getLATITUDE()：" + monitoringPointBean.getLATITUDE());
                L.e("getLONGITUDE():" + monitoringPointBean.getLONGITUDE());
                latLng = new LatLng(monitoringPointBean.getLATITUDE(), monitoringPointBean.getLONGITUDE());
                MarkerOptions options = new MarkerOptions();
                options.anchor(0.5f, 1.0f);
                options.position(latLng);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_green_24dp));

                marker = aMap.addMarker(options);
                marker.setObject(monitoringPointBean);
                marker.setZIndex(ORGZOON);
                marker.setPeriod(postion);
            }
        }
    }

    /**
     * 判断应该关闭下拉刷新框还是上拉加载框
     *
     * @param code 等于 1时，是下拉刷新
     *             等于 2时，是上拉加载
     */
    private void isCloseLoad(int code) {
        if (code == 1) {
            // 下拉刷新
            //为了保险起见可以先判断当前是否在刷新中（旋转的小圈圈在旋转）....
            if (swiperefreshlayout.isRefreshing()) {
                //关闭刷新动画
                swiperefreshlayout.setRefreshing(false);
                // 刷新后将 page改为 1
                page = 1;
            }
        } else if (code == 2) {
            // 上拉加载 后关闭加载框
            monitoringPointAdapter.notifyDataSetChanged();
            devicelistListview.loadComplete();
        }
    }

    @OnClick({R.id.fragment_devicemonitoring_reload_btn, R.id.search_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_devicemonitoring_reload_btn: // 重新加载数据按钮
                page = 1;
                initData(pointName, pointIP, 0, Integer.toString(page));
                break;
            case R.id.search_img: // 搜索按钮
                startActivityForResult(new Intent(getContext(), MonitoringSearchActivity.class), REQUESTCODES);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUESTCODES:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        pointName = intent.getStringExtra("pointName");
                        pointIP = intent.getStringExtra("pointIP");
                        page = 1;
                        initData(pointName, pointIP, 1, Integer.toString(page));
                    }
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        fragmentDevicemonitoringMapview.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        fragmentDevicemonitoringMapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        fragmentDevicemonitoringMapview.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        fragmentDevicemonitoringMapview.onDestroy();
        unbinder.unbind();
    }
}
