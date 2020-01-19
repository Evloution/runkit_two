package com.elink.runkit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.elink.runkit.R;
import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.HistogramBean;
import com.elink.runkit.echart.EchartView;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.HistogramPresenter;
import com.elink.runkit.util.ToastUtil;
import com.elink.runkit.view.DataView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2020-01-08
 * @Email： 15227318030@163.com
 */
public class EchartsDataBarFragment extends Fragment {

    @BindView(R.id.eChart)
    EchartView eChart;
    Unbinder unbinder;

    private HistogramPresenter histogramPresenter = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_echartsdatabar, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
        L.e("EchartsDataBarFragment ****** onStart：");
    }

    public void start() {
        initView();
        initData();
        L.e("EchartsDataBarFragment ****** start：");
    }

    private void initView() {
        histogramPresenter = new HistogramPresenter(getContext());
    }

    private void initData() {
        histogramPresenter.onCreate();
        histogramPresenter.getHistogramList("POINTTYPE");
        histogramPresenter.attachView(new DataView<BaseBean<HistogramBean>>() {
            @Override
            public void onSuccess(final BaseBean<HistogramBean> TBean) {
                L.e("成功：" + TBean.data);
                eChart.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                        JSONArray alive = JSONArray.parseArray(JSON.toJSONString(TBean.data.alive));
                        JSONArray dropped = JSONArray.parseArray(JSON.toJSONString(TBean.data.dropped));
                        JSONArray label = JSONArray.parseArray(JSON.toJSONString(TBean.data.label));
                        refreshBarChart(alive, dropped, label);
                    }
                });
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(getContext(), error);
            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
    }

    /**
     * 加载刷新图标
     *
     * @param alive
     * @param dropped
     * @param label
     */
    private void refreshBarChart(JSONArray alive, JSONArray dropped, JSONArray label) {
        Log.e("aliveArray", alive + "");
        Log.e("droppedArray", dropped + "");
        Log.e("labelArray", label + "");
        eChart.refreshBarEchartsWithOption("'bar'," + label + ", " + dropped + "," + alive);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
