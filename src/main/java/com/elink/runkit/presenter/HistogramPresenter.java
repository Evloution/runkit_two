package com.elink.runkit.presenter;

import android.content.Context;

import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.HistogramBean;
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
 * @Description： 首页图表信息
 * @Author： Evloution_
 * @Date： 2020-01-08
 * @Email： 15227318030@163.com
 */
public class HistogramPresenter implements Presenter {

    private Context context;
    private DataModel dataModel = null;
    private CompositeDisposable compositeDisposable = null;
    private DataView<BaseBean<HistogramBean>> dataView = null;
    private BaseBean<HistogramBean> histogramModel = null;

    public HistogramPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dataModel = new DataModel(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(View view) {
        dataView = (DataView<BaseBean<HistogramBean>>) view;
    }

    public void getHistogramList(String pointType) {
        Observable<BaseBean<HistogramBean>> getPointsInfoObservable = dataModel.getHistogramListObservable(pointType);
        getPointsInfoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<HistogramBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.e("onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseBean<HistogramBean> getTokenModel1) {
                        L.e("onNext");
                        histogramModel = getTokenModel1;
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
                            dataView.onSuccess(histogramModel);
                            L.e("appVersion:" + histogramModel);
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
