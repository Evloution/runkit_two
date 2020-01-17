package com.elink.runkit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elink.runkit.R;
import com.elink.runkit.constants.Constants;
import com.elink.runkit.echart.EchartView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2020-01-08
 * @Email： 15227318030@163.com
 */
public class EchartsDataPieFragment extends Fragment {

    @BindView(R.id.eChart)
    EchartView eChart;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_echartsdatapie, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eChart.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                refreshBarChart();
            }
        });
    }

    private void refreshBarChart() {
        eChart.refreshBarEchartsWithOption("'pie', " + Constants.DEVICES_ONLINE + "," + 0 + "," + 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
