package com.elink.runkit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.elink.runkit.R;
import com.elink.runkit.bean.MonitoringPointBean;
import com.elink.runkit.log.L;

import java.util.List;

/**
 * @author Evloution
 * @date 2020-01-15
 * @email 15227318030@163.com
 * @description 地图上弹出框的adapter
 */
public class MapClickEventsAdapter implements AMap.InfoWindowAdapter, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener {

    private Context context;
    private static final String TAG = "WindowAdapter";
    private List<MonitoringPointBean> marketDataBeanList;

    public MapClickEventsAdapter(Context context) {
        this.context = context;
    }

    public MapClickEventsAdapter(Context context, List<MonitoringPointBean> marketDataBeanList) {
        this.context = context;
        this.marketDataBeanList = marketDataBeanList;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        L.e("marketDataBeanList:" + marketDataBeanList);
        L.e("marketDataBeanList.get(marker.getPeriod()).NAME:" + marketDataBeanList.get(marker.getPeriod()).NAME);
        //关联布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_devices_map_info, null);
        TextView itemDevicesMapName = view.findViewById(R.id.item_devices_map_name);
        itemDevicesMapName.setText(marketDataBeanList.get(marker.getPeriod()).NAME);
        return view;
    }

    // 如果用自定义的布局，不用管这个方法,返回null即可
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    //绑定信息窗点击事件
    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e(TAG, "InfoWindow被点击了");
    }

    // marker 对象被点击时回调的接口
    // 返回 true 则表示接口已响应事件，否则返回false
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e(TAG, "Marker被点击了");
        //marker.showInfoWindow();
        return true;
    }
}
