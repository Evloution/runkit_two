package com.elink.runkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elink.runkit.R;
import com.elink.runkit.bean.MonitoringPointBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Evloution
 * @date 2020-01-13
 * @email 15227318030@163.com
 * @description 监测点列表的adapter
 */
public class MonitoringPointAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<MonitoringPointBean> monitoringPointBeans;
    private LayoutInflater mInflater;
    private MonitoringPointCallback monitoringPointCallback;
    private String typeFase = null; // 状态为1 时显示的字
    private int fontColor = 0; // 字的颜色
    private int returnStatus = 0; // 返回的状态


    public interface MonitoringPointCallback {
        void click(View view);
    }

    public MonitoringPointAdapter(Context context, List<MonitoringPointBean> monitoringPointBeans, MonitoringPointCallback monitoringPointCallback) {
        this.context = context;
        this.monitoringPointBeans = monitoringPointBeans;
        this.monitoringPointCallback = monitoringPointCallback;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return monitoringPointBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return monitoringPointBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fragment_devicemonitoring_devicedetails, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemDevicemonitoringDeviceNameTxt.setText(monitoringPointBeans.get(position).NAME); // 监测点名称
        // 监测点状态
        returnStatus = monitoringPointBeans.get(position).STATUS;
        if (returnStatus == 0) {
            // 等于0 代表离线
            typeFase = "离线";
            fontColor = R.color.red;
        } else if (returnStatus == 1) {
            // 等于1 代表在线
            typeFase = "在线";
            fontColor = R.color.limegreen;
        } else if (returnStatus == -1) {
            // 等于-1 代表未知
            typeFase = "未知";
            fontColor = R.color.gray_three;
        }
        viewHolder.itemDevicemonitoringDeviceStatusTxt.setText(typeFase);
        viewHolder.itemDevicemonitoringDeviceStatusTxt.setTextColor(context.getResources().getColor(fontColor));
        // 是否为主监测点
        returnStatus = monitoringPointBeans.get(position).ISPRIMARY;
        if (returnStatus == 1) {
            // 等于1 代表主要监测点
            typeFase = "主要";
            fontColor = R.color.blueness_two;
        } else if (returnStatus == 0) {
            // 等于0 代表辅助监测点
            typeFase = "辅助";
            fontColor = R.color.blueness_three;
        }
        viewHolder.itemDevicemonitoringDevicePrimaryTxt.setText(typeFase);
        viewHolder.itemDevicemonitoringDevicePrimaryTxt.setTextColor(context.getResources().getColor(fontColor));
        // 监测点的告警级别
        returnStatus = monitoringPointBeans.get(position).WARNGRADE;
        if (returnStatus == 0) {
            typeFase = "0:系统不可用";
            fontColor = R.color.red;
        } else if (returnStatus == 1) {
            typeFase = "1：需要紧急处理";
            fontColor = R.color.red;
        } else if (returnStatus == 2) {
            typeFase = "2：关键的事件";
            fontColor = R.color.red;
        } else if (returnStatus == 3) {
            typeFase = "3：错误事件";
            fontColor = R.color.red;
        } else if (returnStatus == 4) {
            typeFase = "4：警告事件";
            fontColor = R.color.red;
        } else if (returnStatus == 5) {
            typeFase = "5：普通重要事件";
            fontColor = R.color.gray_one;
        } else if (returnStatus == 6) {
            typeFase = "6：有用信息事件";
            fontColor = R.color.tomato;
        } else if (returnStatus == 7) {
            typeFase = "7：调试事件";
            fontColor = R.color.limegreen;
        } else if (returnStatus == 8) {
            typeFase = "8：未知事件";
            fontColor = R.color.gray_three;
        }
        viewHolder.itemDevicemonitoringDeviceWarngradeTxt.setText(typeFase);
        viewHolder.itemDevicemonitoringDeviceWarngradeTxt.setTextColor(context.getResources().getColor(fontColor));
        // 监测点的监测方式
        viewHolder.itemDevicemonitoringDeviceMonitypeTxt.setText(monitoringPointBeans.get(position).MONITYPE);
        returnStatus = monitoringPointBeans.get(position).MONIPAUSE; // 监测点是否还在监测
        if (returnStatus == 1) {
            typeFase = "暂停监测";
            fontColor = R.color.red;
        } else if (returnStatus == 0) {
            typeFase = "监测中";
            fontColor = R.color.limegreen;
        }
        viewHolder.itemDevicemonitoringDeviceMonipauseTxt.setText(typeFase);
        viewHolder.itemDevicemonitoringDeviceMonipauseTxt.setTextColor(context.getResources().getColor(fontColor));
        // ip 和 port
        viewHolder.itemDevicemonitoringDeviceIpandportTxt.setText(monitoringPointBeans.get(position).IP + ":" + String.valueOf(monitoringPointBeans.get(position).PORT));
        // 事件的时间
        viewHolder.itemDevicemonitoringDeviceEventtimeTxt.setText(monitoringPointBeans.get(position).EVENTTIME);
        // 详情按钮的点击事件
        viewHolder.itemDevicemonitoringDeviceDetailsLl.setOnClickListener(this);
        // 发送点击详情按钮时的位置
        viewHolder.itemDevicemonitoringDeviceDetailsLl.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        monitoringPointCallback.click(v);
    }

    static class ViewHolder {
        @BindView(R.id.item_devicemonitoring_device_name_txt)
        TextView itemDevicemonitoringDeviceNameTxt; // 监测点名称
        @BindView(R.id.item_devicemonitoring_device_status_txt)
        TextView itemDevicemonitoringDeviceStatusTxt; // 监测点状态
        @BindView(R.id.item_devicemonitoring_device_status_bg_ll)
        LinearLayout itemDevicemonitoringDeviceStatusBgLl; // 监测点状态背景
        @BindView(R.id.item_devicemonitoring_device_primary_txt)
        TextView itemDevicemonitoringDevicePrimaryTxt; // 是否为主监测点
        @BindView(R.id.item_devicemonitoring_device_warngrade_txt)
        TextView itemDevicemonitoringDeviceWarngradeTxt; // 监测点的告警级别
        @BindView(R.id.item_devicemonitoring_device_monitype_txt)
        TextView itemDevicemonitoringDeviceMonitypeTxt; // 监测点的监测方式
        @BindView(R.id.item_devicemonitoring_device_monipause_txt)
        TextView itemDevicemonitoringDeviceMonipauseTxt; // 监测点是否还在监测
        @BindView(R.id.item_devicemonitoring_device_ipandport_txt)
        TextView itemDevicemonitoringDeviceIpandportTxt; // ip 和 port
        @BindView(R.id.item_devicemonitoring_device_eventtime_txt)
        TextView itemDevicemonitoringDeviceEventtimeTxt; // 事件的时间
        @BindView(R.id.item_devicemonitoring_device_details_ll)
        LinearLayout itemDevicemonitoringDeviceDetailsLl; // 详情

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
