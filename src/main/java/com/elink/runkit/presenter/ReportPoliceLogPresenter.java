package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.ReportPoliceLogEventBean;
import com.elink.runkit.log.L;
import com.elink.runkit.model.DataModel;
import com.elink.runkit.util.ErrorUtil;
import com.elink.runkit.view.DataView;
import com.elink.runkit.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Evloution
 * @date 2020-01-16
 * @email 15227318030@163.com
 * @description 告警信息日志查看
 */
public class ReportPoliceLogPresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseDataListBean<ReportPoliceLogEventBean>> dataView = null;
    private BaseDataListBean<ReportPoliceLogEventBean> reportPoliceLogEventBean = null;

    public ReportPoliceLogPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseDataListBean<ReportPoliceLogEventBean>>) view;
        // 弹加载框框
        dataView.showProgress();
    }

    /**
     * 查看告警日志
     *
     * @param pointname 设备名称
     * @param limit     显示几条数据
     * @param page      请求几页数据
     */
    public void getEventListPresenter(String pointname, String limit, String page) {
        Observable<BaseDataListBean<ReportPoliceLogEventBean>> reportPoliceObservable = dataModel.getEventListObservable(pointname, limit, page);
        reportPoliceObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseDataListBean<ReportPoliceLogEventBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("MonitoringPointPresenter  onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseDataListBean<ReportPoliceLogEventBean> reportPoliceLogEventBeanBaseDataListBean) {
                        L.e("onNext");
                        reportPoliceLogEventBean = reportPoliceLogEventBeanBaseDataListBean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // 关闭加载框
                        dataView.hideProgress();
                        L.e("Presenter onError：" + ErrorUtil.requestHandle(e));
                        dataView.onError(ErrorUtil.requestHandle(e));
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete!");
                        // 关闭加载框
                        dataView.hideProgress();
                        if (dataView != null) {
                            dataView.onSuccess(reportPoliceLogEventBean);
                            L.e("reportPoliceLogEventBean:" + reportPoliceLogEventBean);
                        }
                    }
                });
    }

    @Override
    public void onDestory() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose(); //解除订阅
            compositeDisposable.clear(); //取消所有订阅
            compositeDisposable = null;
        }
    }
}
