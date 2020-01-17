package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.IncidentBean;
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
 * @Description： 设备详情界面折线图的数据
 * @Author： Evloution_
 * @Date： 2020-01-17
 * @Email： 15227318030@163.com
 */
public class IncidentPresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseBean<IncidentBean>> dataView = null;
    private BaseBean<IncidentBean> incidentBean = null;

    public IncidentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseBean<IncidentBean>>) view;
    }

    public void getIncidentPresenter(String id, String beginTime, String endTime) {
        Observable<BaseBean<IncidentBean>> getIncidentObservable = dataModel.getIncidentObservable(id, beginTime, endTime);
        getIncidentObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<IncidentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseBean<IncidentBean> incidentBeanBaseBean) {
                        L.e("onNext");
                        incidentBean = incidentBeanBaseBean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        L.e("Presenter onError：" + ErrorUtil.requestHandle(e));
                        dataView.onError(ErrorUtil.requestHandle(e));
                    }

                    @Override
                    public void onComplete() {
                        L.e("onComplete!");
                        if (dataView != null) {
                            dataView.onSuccess(incidentBean);
                            L.e("incidentBean:" + incidentBean);
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
