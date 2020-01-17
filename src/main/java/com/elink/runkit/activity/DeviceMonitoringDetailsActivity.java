package com.elink.runkit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.elink.runkit.R;
import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.IncidentBean;
import com.elink.runkit.bean.MonitoringPointDetailsBean;
import com.elink.runkit.datepicker.CustomDatePicker;
import com.elink.runkit.datepicker.DateFormatUtils;
import com.elink.runkit.echart.EchartPointsInfoView;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.IncidentPresenter;
import com.elink.runkit.presenter.MonitoringPointDetailsPresenter;
import com.elink.runkit.util.ToastUtil;
import com.elink.runkit.view.DataView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Evloution
 * @date 2020-01-15
 * @email 15227318030@163.com
 * @description 监测点详情界面
 */
public class DeviceMonitoringDetailsActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg; // 返回上一页按钮
    @BindView(R.id.activity_devicemonitoring_reload_btn)
    Button activityDevicemonitoringReloadBtn; // 重新加载按钮
    @BindView(R.id.activity_devicemonitoring_reload_linearlayout)
    LinearLayout activityDevicemonitoringReloadLinearlayout; // 重新加载按钮背景
    @BindView(R.id.activity_devicemonitoring_linearlayout)
    LinearLayout activityDevicemonitoringLinearlayout; // 主体页面
    @BindView(R.id.devicedetails_name_txt)
    TextView devicedetailsNameTxt; // 名称
    @BindView(R.id.devicedetails_monipause_txt)
    TextView devicedetailsMonipauseTxt; // 是否正在监测
    @BindView(R.id.devicedetails_status_txt)
    TextView devicedetailsStatusTxt; // 状态
    @BindView(R.id.devicedetails_warngrade_txt)
    TextView devicedetailsWarngradeTxt; // 告警级别
    @BindView(R.id.devicedetails_monitype_txt)
    TextView devicedetailsMonitypeTxt; // 监测方式
    @BindView(R.id.devicedetails_moniinterval_txt)
    TextView devicedetailsMoniintervalTxt; // 监测周期
    @BindView(R.id.devicedetails_onlinerate_txt)
    TextView devicedetailsOnlinerateTxt; // 在线率
    @BindView(R.id.from_date)
    TextView fromDate;
    @BindView(R.id.from_linear)
    LinearLayout fromLinear;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.end_linear)
    LinearLayout endLinear;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.devicedetails_echart)
    EchartPointsInfoView devicedetailsEchart;

    private MonitoringPointDetailsPresenter monitoringPointDetailsPresenter = null; // 详情信息的Presenter
    private IncidentPresenter incidentPresenter = null; // 折线图表的Presenter
    private CustomDatePicker mDatePicker, mTimerPicker;
    private String monitoringId = null; // 上个页面传输过来的id
    private String currentTime = null; // 当前时间
    private String startTime = null; // 开始时间
    private String endTime = null; // 结束时间

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemonitoringdetails);
        ButterKnife.bind(this);
        // 获得传过来的id
        monitoringId = getIntent().getStringExtra("monitoringId");
        L.e("monitoringID:" + monitoringId);

        initView();
        initData();
        initIncidentData();
    }

    private void initView() {
        monitoringPointDetailsPresenter = new MonitoringPointDetailsPresenter(this);
        incidentPresenter = new IncidentPresenter(this);

        // 给开始时间赋初始值为本月的第一天
        SimpleDateFormat simpleDateFrom = new SimpleDateFormat("yyyy-MM");
        Date fromdate = new Date(System.currentTimeMillis());
        fromDate.setText("2020-01-15");
        //fromDate.setText(simpleDateFrom.format(fromdate) + "-01");

        // 给结束时间赋值为今天
        SimpleDateFormat simpleDateEnd = new SimpleDateFormat("yyyy-MM-dd");
        Date enddate = new Date(System.currentTimeMillis());
        endDate.setText(simpleDateEnd.format(enddate));

        // 获取进入页面时当前的时间
        SimpleDateFormat simpleCurrentTime = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        currentTime = " " + simpleCurrentTime.format(currentDate);

        startTime = fromDate.getText().toString() + currentTime;
        endTime = endDate.getText().toString() + currentTime;
        initFromDatePicker();
        initEndDatePicker();
    }

    // 请求的是设备详情
    private void initData() {
        monitoringPointDetailsPresenter.onCreate();
        monitoringPointDetailsPresenter.getPointByIdPresenter(monitoringId);
        monitoringPointDetailsPresenter.attachView(new DataView<BaseBean<MonitoringPointDetailsBean>>() {
            @Override
            public void onSuccess(BaseBean<MonitoringPointDetailsBean> TBean) {
                L.e("onSuccess：" + TBean.data.NAME);
                devicedetailsNameTxt.setText(TBean.data.NAME);
                devicedetailsMonipauseTxt.setText(String.valueOf(TBean.data.MONIPAUSE));
                devicedetailsStatusTxt.setText(String.valueOf(TBean.data.STATUS));
                devicedetailsWarngradeTxt.setText(String.valueOf(TBean.data.WARNGRADE));
                devicedetailsMonitypeTxt.setText(TBean.data.MONITYPE);
                devicedetailsMoniintervalTxt.setText(String.valueOf(TBean.data.MONIINTERVAL));
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(DeviceMonitoringDetailsActivity.this, error);
            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
    }

    // 请求的是设备详情图表
    private void initIncidentData() {
        incidentPresenter.onCreate();
        incidentPresenter.getIncidentPresenter(monitoringId, startTime, endTime);
        incidentPresenter.attachView(new DataView<BaseBean<IncidentBean>>() {
            @Override
            public void onSuccess(final BaseBean<IncidentBean> TBean) {
                L.e("onSuccess成功：" + TBean.data.CONTENT);
                devicedetailsEchart.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                        JSONArray statuses = JSONArray.parseArray(JSON.toJSONString(TBean.data.CONTENT));
                        JSONArray times = JSONArray.parseArray(JSON.toJSONString(TBean.data.Time));
                        refreshBarChart(statuses, times, startTime, endTime);
                    }
                });
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
    }

    private void refreshBarChart(JSONArray statuses, JSONArray times, String starTime, String endTimes) {
        // statuses, times, starTime, endTimes
        L.e("statuses:" + statuses + "");
        L.e("times:" + times + "");
        L.e("starTime:" + starTime + "");
        L.e("endTimes:" + endTimes + "");
        devicedetailsEchart.refreshEchartsWithOption(statuses + "," + times + "," + "\"" + starTime + "\"" + "," + "\"" +  endTimes + "\"");
    }

    @OnClick({R.id.back_img, R.id.activity_devicemonitoring_reload_btn, R.id.from_linear, R.id.end_linear, R.id.search_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img: // 返回上一页
                this.finish();
                break;
            case R.id.activity_devicemonitoring_reload_btn: // 重新加载按钮
                break;
            case R.id.from_linear: // 选择开始时间
                mDatePicker.show(fromDate.getText().toString());
                break;
            case R.id.end_linear: // 选择结束时间
                mTimerPicker.show(endDate.getText().toString());
                break;
            case R.id.search_button: // 查询按钮
                L.e("开始时间：" + fromDate.getText().toString() + currentTime + "  结束时间：" + endDate.getText().toString() + currentTime);
                break;
        }
    }

    private void initFromDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        fromDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                fromDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        // mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initEndDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        endDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                endDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        // mDatePicker.setCancelable(false);
        // 不显示时和分
        mTimerPicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mTimerPicker.setScrollLoop(false);
        // 不允许滚动动画
        mTimerPicker.setCanShowAnim(false);
    }
}
