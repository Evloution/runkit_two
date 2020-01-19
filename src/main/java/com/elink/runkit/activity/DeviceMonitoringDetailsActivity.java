package com.elink.runkit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.elink.runkit.echart.EchartView;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.IncidentPresenter;
import com.elink.runkit.presenter.MonitoringPointDetailsPresenter;
import com.elink.runkit.util.ToastUtil;
import com.elink.runkit.view.DataView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    @BindView(R.id.from_linear)
    LinearLayout fromLinear; // 开始时间的LinearLayout
    @BindView(R.id.from_date)
    TextView fromDates; // 开始时间
    @BindView(R.id.end_date)
    TextView endDate; // 结束时间
    @BindView(R.id.end_linear)
    LinearLayout endLinear; // 结束时间的LinearLayout
    @BindView(R.id.search_button)
    Button searchButton; // 查询按钮
    @BindView(R.id.devicedetails_echart)
    EchartView devicedetailsEchart; // 图表的信息
    @BindView(R.id.reportpolicelog_swiperefreshlayout)
    SwipeRefreshLayout reportpolicelogSwiperefreshlayout; // 下拉刷新
    @BindView(R.id.devicedetails_primary_txt)
    TextView devicedetailsPrimaryTxt; // 是否为主要监测点

    private MonitoringPointDetailsPresenter monitoringPointDetailsPresenter = null; // 详情信息的Presenter
    private IncidentPresenter incidentPresenter = null; // 折线图表的Presenter
    private CustomDatePicker mDatePicker, mTimerPicker;
    private String monitoringId = null; // 上个页面传输过来的id
    private String currentTime = null; // 当前时间
    private String startTime = null; // 开始时间
    private String endTime = null; // 结束时间

    private String typeFase = null; // 状态为1 时显示的字
    private int fontColor = 0; // 字的颜色
    private int returnStatus = 0; // 返回的状态

    private JSONArray statuses;
    private JSONArray times;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemonitoringdetails);
        ButterKnife.bind(this);
        // 获得传过来的id
        monitoringId = getIntent().getStringExtra("monitoringId");
        L.e("monitoringID:" + monitoringId);

        initView();
    }

    private void initView() {
        // 给开始时间赋初始值为本月的第一天
        /*SimpleDateFormat simpleDateFrom = new SimpleDateFormat("yyyy-MM");
        Date fromdate = new Date(System.currentTimeMillis());
        fromDate.setText(simpleDateFrom.format(fromdate) + "-01");*/

        // 给开始时间赋初始值为本日的前3天
        L.e("getCurrentDate:" + getBeforeTime());
        fromDates.setText(getBeforeTime());
        initFromDatePicker();

        // 给结束时间赋值为今天
        SimpleDateFormat simpleDateEnd = new SimpleDateFormat("yyyy-MM-dd");
        Date enddate = new Date(System.currentTimeMillis());
        endDate.setText(simpleDateEnd.format(enddate));
        initEndDatePicker();

        // 获取进入页面时当前的时间
        SimpleDateFormat simpleCurrentTime = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        currentTime = " " + simpleCurrentTime.format(currentDate);

        startTime = fromDates.getText().toString() + currentTime;
        endTime = endDate.getText().toString() + currentTime;

        monitoringPointDetailsPresenter = new MonitoringPointDetailsPresenter(this);
        incidentPresenter = new IncidentPresenter(this);
        monitoringPointDetailsPresenter.onCreate();
        incidentPresenter.onCreate();

        initData();
        initIncidentData(0, startTime, endTime);

        // 下拉刷新
        reportpolicelogSwiperefreshlayout.setColorSchemeResources(R.color.colorPrimary, R.color.blueness_two, R.color.blueness_three);
        reportpolicelogSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新", "下拉刷新");
                initData();
                initIncidentData(0, startTime, endTime);
            }
        });
    }

    // 请求的是设备详情
    private void initData() {
        monitoringPointDetailsPresenter.getPointByIdPresenter(monitoringId);
        monitoringPointDetailsPresenter.attachView(new DataView<BaseBean<MonitoringPointDetailsBean>>() {
            @Override
            public void onSuccess(BaseBean<MonitoringPointDetailsBean> TBean) {
                activityDevicemonitoringReloadLinearlayout.setVisibility(View.GONE);
                reportpolicelogSwiperefreshlayout.setVisibility(View.VISIBLE);
                L.e("onSuccess：" + TBean.data.NAME);
                // 监测点名称
                devicedetailsNameTxt.setText(TBean.data.NAME);
                returnStatus = TBean.data.MONIPAUSE; // 监测点是否还在监测
                if (returnStatus == 1) {
                    typeFase = "暂停监测";
                    fontColor = R.color.red;
                } else if (returnStatus == 0) {
                    typeFase = "正在监测";
                    fontColor = R.color.limegreen;
                }
                devicedetailsMonipauseTxt.setText(typeFase);
                devicedetailsMonipauseTxt.setTextColor(getResources().getColor(fontColor));
                // 是否为主监测点
                returnStatus = TBean.data.ISPRIMARY;
                if (returnStatus == 1) {
                    // 等于1 代表主要监测点
                    typeFase = "主要监测点";
                    fontColor = R.color.blueness_one;
                } else if (returnStatus == 0) {
                    // 等于0 代表辅助监测点
                    typeFase = "辅助监测点";
                    fontColor = R.color.blueness_three;
                }
                devicedetailsPrimaryTxt.setText(typeFase);
                devicedetailsPrimaryTxt.setTextColor(getResources().getColor(fontColor));
                // 监测点的告警级别
                returnStatus = TBean.data.WARNGRADE;
                if (returnStatus == 0) {
                    typeFase = "0· 系统不可用";
                    fontColor = R.color.red;
                } else if (returnStatus == 1) {
                    typeFase = "1· 需要紧急处理";
                    fontColor = R.color.red;
                } else if (returnStatus == 2) {
                    typeFase = "2· 关键的事件";
                    fontColor = R.color.red;
                } else if (returnStatus == 3) {
                    typeFase = "3· 错误事件";
                    fontColor = R.color.red;
                } else if (returnStatus == 4) {
                    typeFase = "4· 警告事件";
                    fontColor = R.color.red;
                } else if (returnStatus == 5) {
                    typeFase = "5· 普通重要事件";
                    fontColor = R.color.gray_one;
                } else if (returnStatus == 6) {
                    typeFase = "6· 有用信息事件";
                    fontColor = R.color.tomato;
                } else if (returnStatus == 7) {
                    typeFase = "7· 调试事件";
                    fontColor = R.color.limegreen;
                } else if (returnStatus == 8) {
                    typeFase = "8· 未知事件";
                    fontColor = R.color.gray_three;
                }
                devicedetailsWarngradeTxt.setText(typeFase);
                devicedetailsWarngradeTxt.setTextColor(getResources().getColor(fontColor));
                returnStatus = TBean.data.STATUS;
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
                devicedetailsStatusTxt.setText(typeFase);
                devicedetailsStatusTxt.setTextColor(getResources().getColor(fontColor));
                devicedetailsMonitypeTxt.setText(TBean.data.MONITYPE);
                devicedetailsMoniintervalTxt.setText(String.valueOf(TBean.data.MONIINTERVAL));
                // 请求失败后取消刷新框
                isCloseLoad();
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(DeviceMonitoringDetailsActivity.this, error);
                // 请求失败后取消刷新框
                isCloseLoad();
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
    private void initIncidentData(final int code, final String startTime, final String endTime) {
        incidentPresenter.getIncidentPresenter(monitoringId, startTime, endTime);
        incidentPresenter.attachView(new DataView<BaseBean<IncidentBean>>() {
            @Override
            public void onSuccess(final BaseBean<IncidentBean> TBean) {
                L.e("onSuccess成功：" + TBean.data.CONTENT);
                statuses = JSONArray.parseArray(JSON.toJSONString(TBean.data.CONTENT));
                times = JSONArray.parseArray(JSON.toJSONString(TBean.data.Time));
                if (code == 1 || code == 0) {
                    refreshBarChart(statuses, times, startTime, endTime);
                }
                devicedetailsEchart.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                        L.e("0000000000000000000000000");
                        refreshBarChart(statuses, times, startTime, endTime);
                    }
                });
                // 请求失败后取消刷新框
                isCloseLoad();
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(DeviceMonitoringDetailsActivity.this, error);
                activityDevicemonitoringReloadLinearlayout.setVisibility(View.VISIBLE);
                reportpolicelogSwiperefreshlayout.setVisibility(View.GONE);
                // 请求失败后取消刷新框
                isCloseLoad();
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
        devicedetailsEchart.refreshBarEchartsWithOption(statuses + "," + times + "," + "\"" + starTime + "\"" + "," + "\"" + endTimes + "\"");
    }

    @OnClick({R.id.back_img, R.id.activity_devicemonitoring_reload_btn, R.id.from_linear, R.id.end_linear, R.id.search_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img: // 返回上一页
                this.finish();
                break;
            case R.id.activity_devicemonitoring_reload_btn: // 重新加载按钮
                initData();
                initIncidentData(0, startTime, endTime);
                break;
            case R.id.from_linear: // 选择开始时间
                mDatePicker.show(fromDates.getText().toString());
                break;
            case R.id.end_linear: // 选择结束时间
                mTimerPicker.show(endDate.getText().toString());
                break;
            case R.id.search_button: // 查询按钮
                L.e("开始时间：" + fromDates.getText().toString() + currentTime + "  结束时间：" + endDate.getText().toString() + currentTime);
                startTime = fromDates.getText().toString() + currentTime;
                endTime = endDate.getText().toString() + currentTime;
                initIncidentData(1, startTime, endTime);
                break;
        }
    }

    private void initFromDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                fromDates.setText(DateFormatUtils.long2Str(timestamp, false));
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

    /**
     * 判断应该关闭下拉刷新框还是上拉加载框
     */
    private void isCloseLoad() {
        // 下拉刷新
        //为了保险起见可以先判断当前是否在刷新中（旋转的小圈圈在旋转）....
        if (reportpolicelogSwiperefreshlayout.isRefreshing()) {
            //关闭刷新动画
            reportpolicelogSwiperefreshlayout.setRefreshing(false);
        }
    }

    // 获取今天的前三天
    private String getBeforeTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3); //向前走一天
        Date date = calendar.getTime();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        return dft.format(date);
    }
}
