package com.elink.runkit.echart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class EchartPointsInfoView extends WebView {

    private static final String TAG = EchartPointsInfoView.class.getSimpleName();

    public EchartPointsInfoView(Context context) {
        this(context, null);
    }

    public EchartPointsInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EchartPointsInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        loadUrl("file:///android_asset/pointinfoecharts.html");
    }

    /**
     * 刷新图表
     * java调用js的loadEcharts方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     */

    public void refreshEchartsWithOption(String optionString) {
        Log.e("optionString", optionString);
        String call = "javascript:doCreateChart('" + optionString + "');";
        loadUrl(call);
    }
}
