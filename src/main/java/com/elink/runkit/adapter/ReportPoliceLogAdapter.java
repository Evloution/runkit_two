package com.elink.runkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elink.runkit.R;
import com.elink.runkit.bean.ReportPoliceLogEventBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Evloution
 * @date 2020-01-14
 * @email 15227318030@163.com
 * @description 告警日志的 adapter
 */
public class ReportPoliceLogAdapter extends BaseAdapter {

    private Context context;
    private List<ReportPoliceLogEventBean> reportPoliceLogEventBeanList;
    private LayoutInflater mInflater;
    private String typeFase = null; // 状态为1 时显示的字
    private int fontColor = 0; // 字的颜色
    private int returnStatus = 0; // 返回的状态

    public ReportPoliceLogAdapter(Context context, List<ReportPoliceLogEventBean> reportPoliceLogEventBeanList) {
        this.context = context;
        this.reportPoliceLogEventBeanList = reportPoliceLogEventBeanList;
        this.mInflater = LayoutInflater.from(context);
    }

    public interface ReportPoliceCallback {
        void click(View view);
    }

    @Override
    public int getCount() {
        return reportPoliceLogEventBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return reportPoliceLogEventBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fragment_reportpolicelog_listview, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设备名称
        viewHolder.itemFragmentReportpolicelogNameTxt.setText(reportPoliceLogEventBeanList.get(position).NAME);
        // 事件内容
        viewHolder.itemFragmentReportpolicelogContentTxt.setText(reportPoliceLogEventBeanList.get(position).CONTENT);
        // 设备状态
        returnStatus = reportPoliceLogEventBeanList.get(position).CURRENTSTATUS;
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
        viewHolder.itemFragmentReportpolicelogCurrentstatusTxt.setText(typeFase);
        viewHolder.itemFragmentReportpolicelogCurrentstatusTxt.setTextColor(context.getResources().getColor(fontColor));
        // 设备的告警级别
        returnStatus = reportPoliceLogEventBeanList.get(position).GRADE;
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
        viewHolder.itemFragmentReportpolicelogGradeTxt.setText(typeFase);
        viewHolder.itemFragmentReportpolicelogGradeTxt.setTextColor(context.getResources().getColor(fontColor));
        viewHolder.itemFragmentReportpolicelogCreatedtimeTxt.setText(reportPoliceLogEventBeanList.get(position).CREATED_TIME);
        return convertView;
    }

    static
    class ViewHolder {
        @BindView(R.id.item_fragment_reportpolicelog_name_txt)
        TextView itemFragmentReportpolicelogNameTxt; // 名称
        @BindView(R.id.item_fragment_reportpolicelog_content_txt)
        TextView itemFragmentReportpolicelogContentTxt; // 事件内容
        @BindView(R.id.item_fragment_reportpolicelog_currentstatus_txt)
        TextView itemFragmentReportpolicelogCurrentstatusTxt; // 设备状态
        @BindView(R.id.item_fragment_reportpolicelog_grade_txt)
        TextView itemFragmentReportpolicelogGradeTxt; // 事件级别
        @BindView(R.id.item_fragment_reportpolicelog_createdtime_txt)
        TextView itemFragmentReportpolicelogCreatedtimeTxt; // 事件发生时间

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
