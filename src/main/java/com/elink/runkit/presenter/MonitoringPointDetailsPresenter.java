package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.MonitoringPointDetailsBean;
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
 * @date 2020-01-15
 * @email 15227318030@163.com
 * @description 监测点详情信息
 */
public class MonitoringPointDetailsPresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseBean<MonitoringPointDetailsBean>> dataView = null;
    private BaseBean<MonitoringPointDetailsBean> monitoringPointDetailsBean = null;

    public MonitoringPointDetailsPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseBean<MonitoringPointDetailsBean>>) view;
        // 弹加载框框
        dataView.showProgress();
    }

    public void getPointByIdPresenter(String id) {
        Observable<BaseBean<MonitoringPointDetailsBean>> monitoringPointObservable = dataModel.getPointByIdObservable(id);
        monitoringPointObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<MonitoringPointDetailsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("MonitoringPointPresenter  onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseBean<MonitoringPointDetailsBean> monitoringPointDetailsBeanBaseBean) {
                        L.e("onNext");
                        monitoringPointDetailsBean = monitoringPointDetailsBeanBaseBean;
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
                            dataView.onSuccess(monitoringPointDetailsBean);
                            L.e("monitoringPointDetailsBean:" + monitoringPointDetailsBean);
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
