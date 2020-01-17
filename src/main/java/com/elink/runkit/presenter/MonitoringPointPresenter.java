package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.MonitoringPointBean;
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
 * @description 设备监控中的监测点列表
 */
public class MonitoringPointPresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseDataListBean<MonitoringPointBean>> dataView = null;
    private BaseDataListBean<MonitoringPointBean> monitoringPointBean = null;

    public MonitoringPointPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseDataListBean<MonitoringPointBean>>) view;
        // 弹加载框框
        dataView.showProgress();
    }

    /**
     * @param pointname 设备名称
     * @param pointip   设备ip
     * @param limit     显示几条数据
     * @param page      请求几页数据
     */
    public void getPointListPresenter(String pointname, String pointip, String limit, String page) {
        Observable<BaseDataListBean<MonitoringPointBean>> monitoringPointObservable = dataModel.getPointListObservable(pointname, pointip, limit, page);
        monitoringPointObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseDataListBean<MonitoringPointBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("MonitoringPointPresenter  onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseDataListBean<MonitoringPointBean> monitoringPointBeanBaseDataListBean) {
                        L.e("onNext");
                        monitoringPointBean = monitoringPointBeanBaseDataListBean;
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
                            dataView.onSuccess(monitoringPointBean);
                            L.e("monitoringPointBean:" + monitoringPointBean);
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
