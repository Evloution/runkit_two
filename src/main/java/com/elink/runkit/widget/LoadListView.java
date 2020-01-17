package com.elink.runkit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.elink.runkit.R;

/**
 * @author Evloution
 * @date 2020-01-14
 * @email 15227318030@163.com
 * @description 上拉加载
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    View footer;// 底部布局；
    int totalItemCount;// 总数量；
    int lastVisibleItem;// 最后一个可见的item；
    boolean isLoading;// 正在加载数据；
    ILoadListener iLoadListener;

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 添加底部加载提示布局到listview
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.pulluploading, null);
        footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
        footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    /**
     * 滑动监听重写的方法
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    /**
     * 滑动监听重写的方法
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // totalItemCount == lastVisibleItem相等时说明滑到了底部
        if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
                footer.findViewById(R.id.load_layout).setVisibility(
                        View.VISIBLE);
                if(iLoadListener!=null) {
                    // 加载更多
                    iLoadListener.onLoad();
                }
            }
        }
    }

    /**
     * 加载完毕
     */
    public void loadComplete(){
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(
                View.GONE);
    }

    public void loadBottom(){
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(
                View.GONE);
        footer.findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
    }

    public void bottomchange(){
        footer.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
    }

    /**
     * 加载更多
     */
    public void setInterface(ILoadListener iLoadListener){
        this.iLoadListener = iLoadListener;
    }

    //加载更多数据的回调接口
    public interface ILoadListener{
        void onLoad();
    }
}
