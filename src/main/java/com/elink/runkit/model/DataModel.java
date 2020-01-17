package com.elink.runkit.model;

import android.content.Context;

import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.HistogramBean;
import com.elink.runkit.bean.IncidentBean;
import com.elink.runkit.bean.MonitoringPointBean;
import com.elink.runkit.bean.MonitoringPointDetailsBean;
import com.elink.runkit.bean.PointsInfoBean;
import com.elink.runkit.bean.ReportPoliceBean;
import com.elink.runkit.bean.ReportPoliceLogEventBean;
import com.elink.runkit.retrofit.RetrofitApi;
import com.elink.runkit.retrofit.RetrofitHelper;

import io.reactivex.Observable;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019-11-29
 * @Email： 15227318030@163.com
 */
public class DataModel {

    private RetrofitApi retrofitApi;

    public DataModel(Context context) {
        this.retrofitApi = RetrofitHelper.getInstance(context).initRetrofit();
    }

    /**
     * 获取首页监测点数据
     *
     * @return
     */
    public Observable<BaseBean<PointsInfoBean>> getPointsInfoObservable() {
        return retrofitApi.getPointsInfo();
    }

    /**
     * 获取首页图表数据
     *
     * @param pointType
     * @return
     */
    public Observable<BaseBean<HistogramBean>> getHistogramListObservable(String pointType) {
        return retrofitApi.getHistogramList(pointType);
    }

    /**
     * 获取监测点列表
     *
     * @param pointname 设备名称
     * @param pointip   设备ip
     * @param limit     显示几条数据
     * @param page      请求几页数据
     * @return
     */
    public Observable<BaseDataListBean<MonitoringPointBean>> getPointListObservable(String pointname, String pointip, String limit, String page) {
        return retrofitApi.getPointList(pointname, pointip, limit, page);
    }

    /**
     * 监测点详情信息
     *
     * @param id 监测点的id
     * @return
     */
    public Observable<BaseBean<MonitoringPointDetailsBean>> getPointByIdObservable(String id) {
        return retrofitApi.getPointById(id);
    }

    /**
     * 获取设备详情界面折线图的数据
     *
     * @param id        监测点的id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public Observable<BaseBean<IncidentBean>> getIncidentObservable(String id, String beginTime, String endTime) {
        return retrofitApi.getIncident(id, beginTime, endTime);
    }

    /**
     * 获取告警信息
     *
     * @param limit 显示几条数据
     * @param page  请求几页数据
     * @return
     */
    public Observable<BaseDataListBean<ReportPoliceBean>> getWarnListObservable(String limit, String page) {
        return retrofitApi.getWarnList(limit, page);
    }

    /**
     * 告警信息确认
     *
     * @param id 告警记录的id
     * @return
     */
    public Observable<BaseBean> confirmWarnObservable(String id) {
        return retrofitApi.confirmWarn(id);
    }

    /**
     * 查看告警日志
     *
     * @param pointname 设备名称
     * @param limit     显示几条数据
     * @param page      请求几页数据
     * @return
     */
    public Observable<BaseDataListBean<ReportPoliceLogEventBean>> getEventListObservable(String pointname, String limit, String page) {
        return retrofitApi.getEventList(pointname, limit, page);
    }
}
