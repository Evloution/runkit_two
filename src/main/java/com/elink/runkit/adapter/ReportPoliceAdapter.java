package com.elink.runkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elink.runkit.R;
import com.elink.runkit.bean.ReportPoliceBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Evloution
 * @date 2020-01-14
 * @email 15227318030@163.com
 * @description 告警列表的 adapter
 */
public class ReportPoliceAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<ReportPoliceBean> reportPoliceBeans;
    private LayoutInflater mInflater;
    private ReportPoliceCallback reportPoliceCallback;

    public ReportPoliceAdapter(Context context, List<ReportPoliceBean> reportPoliceBeans, ReportPoliceCallback reportPoliceCallback) {
        this.context = context;
        this.reportPoliceBeans = reportPoliceBeans;
        this.mInflater = LayoutInflater.from(context);
        this.reportPoliceCallback = reportPoliceCallback;
    }

    public interface ReportPoliceCallback {
        void click(View view);
    }

    @Override
    public int getCount() {
        return reportPoliceBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return reportPoliceBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fragment_reportpolice_listview, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemFragmentReportpoliceConfirmBtn.setOnClickListener(this);
        viewHolder.itemFragmentReportpoliceDetailsBtn.setOnClickListener(this);
        viewHolder.itemFragmentReportpoliceConfirmBtn.setTag(position);
        viewHolder.itemFragmentReportpoliceDetailsBtn.setTag(position);
        viewHolder.itemFragmentReportpoliceNameTxt.setText(reportPoliceBeans.get(position).NAME); // 名称
        viewHolder.itemFragmentReportpoliceCreatedtimeTxt.setText(reportPoliceBeans.get(position).CREATED_TIME); // 告警时间
        String ackTime = reportPoliceBeans.get(position).CREATED_TIME;
        int ack = reportPoliceBeans.get(position).ACK;
        if (ack == 1) {
            // ack 等于1 说明该告警信息已确认，异常确认按钮就不再显示
            viewHolder.itemFragmentReportpoliceConfirmBtn.setVisibility(View.GONE);
            viewHolder.itemFragmentReportpoliceAckTxt.setText("已确认");
            viewHolder.itemFragmentReportpoliceAckTxt.setTextColor(context.getResources().getColor(R.color.limegreen));
            // 展示确认时间
            viewHolder.itemFragmentReportpoliceAcktimeLl.setVisibility(View.VISIBLE);
            viewHolder.itemFragmentReportpoliceAcktimeTxt.setText(ackTime);
        } else {
            // ack 等于0 说明该告警信息未确认，异常确认按钮正常显示
            viewHolder.itemFragmentReportpoliceConfirmBtn.setVisibility(View.VISIBLE);
            viewHolder.itemFragmentReportpoliceAckTxt.setText("未确认");
            viewHolder.itemFragmentReportpoliceAckTxt.setTextColor(context.getResources().getColor(R.color.red));
            // 隐藏确认时间
            viewHolder.itemFragmentReportpoliceAcktimeLl.setVisibility(View.GONE);
        }
        viewHolder.itemFragmentReportpoliceContentTxt.setText(reportPoliceBeans.get(position).CONTENT); // 告警内容
        return convertView;
    }

    @Override
    public void onClick(View v) {
        reportPoliceCallback.click(v);
    }

    static
    class ViewHolder {
        @BindView(R.id.item_fragment_reportpolice_name_txt)
        TextView itemFragmentReportpoliceNameTxt;
        @BindView(R.id.item_fragment_reportpolice_createdtime_txt)
        TextView itemFragmentReportpoliceCreatedtimeTxt;
        @BindView(R.id.item_fragment_reportpolice_ack_txt)
        TextView itemFragmentReportpoliceAckTxt;
        @BindView(R.id.item_fragment_reportpolice_acktime_txt)
        TextView itemFragmentReportpoliceAcktimeTxt;
        @BindView(R.id.item_fragment_reportpolice_acktime_ll)
        LinearLayout itemFragmentReportpoliceAcktimeLl;
        @BindView(R.id.item_fragment_reportpolice_content_txt)
        TextView itemFragmentReportpoliceContentTxt;
        @BindView(R.id.item_fragment_reportpolice_ip_txt)
        TextView itemFragmentReportpoliceIpTxt;
        @BindView(R.id.item_fragment_reportpolice_details_btn)
        Button itemFragmentReportpoliceDetailsBtn;
        @BindView(R.id.item_fragment_reportpolice_confirm_btn)
        Button itemFragmentReportpoliceConfirmBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
