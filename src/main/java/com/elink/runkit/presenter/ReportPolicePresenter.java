package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.ReportPoliceBean;
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
 * @date 2020-01-13
 * @email 15227318030@163.com
 * @description 告警信息的监测点列表
 */
public class ReportPolicePresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseDataListBean<ReportPoliceBean>> dataView = null;
    private BaseDataListBean<ReportPoliceBean> reportPoliceBean = null;

    public ReportPolicePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseDataListBean<ReportPoliceBean>>) view;
        // 弹加载框框
        dataView.showProgress();
    }

    /**
     * @param limit 显示几条数据
     * @param page  请求几页数据
     */
    public void getWarnListPresenter(String limit, String page) {
        Observable<BaseDataListBean<ReportPoliceBean>> reportPoliceObservable = dataModel.getWarnListObservable(limit, page);
        reportPoliceObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseDataListBean<ReportPoliceBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("MonitoringPointPresenter  onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseDataListBean<ReportPoliceBean> reportPoliceBeanBaseDataListBean) {
                        L.e("onNext");
                        reportPoliceBean = reportPoliceBeanBaseDataListBean;
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
                            dataView.onSuccess(reportPoliceBean);
                            L.e("reportPoliceBean:" + reportPoliceBean);
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
