package com.elink.runkit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.elink.runkit.R;
import com.elink.runkit.adapter.ReportPoliceLogAdapter;
import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.ReportPoliceLogEventBean;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.ReportPoliceLogPresenter;
import com.elink.runkit.util.ToastUtil;
import com.elink.runkit.view.DataView;
import com.elink.runkit.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Evloution
 * @date 2020-01-08
 * @email 15227318030@163.com
 * @description
 */
public class ReportPoliceLogFragment extends Fragment {

    @BindView(R.id.fragment_reportpolicelog_reload_btn)
    Button fragmentReportpolicelogReloadBtn; // 重新加载按钮
    @BindView(R.id.fragment_reportpolicelog_reload_linearlayout)
    LinearLayout fragmentReportpolicelogReloadLinearlayout;  // 重新加载按钮的LinearLayout
    @BindView(R.id.fragment_reportpolicelog_ll)
    LinearLayout fragmentReportpolicelogLl; // 主体的LinearLayout
    @BindView(R.id.reportpolicelog_listview)
    LoadListView reportpolicelogListview; // 告警列表
    @BindView(R.id.reportpolicelog_swiperefreshlayout)
    SwipeRefreshLayout reportpolicelogSwiperefreshlayout; // 下拉刷新
    Unbinder unbinder;

    private ReportPoliceLogPresenter reportPoliceLogPresenter = null;
    private ReportPoliceLogAdapter reportPoliceLogAdapter = null;
    private List<ReportPoliceLogEventBean> reportPoliceLogEventBeanList = null;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportpolicelog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(0, "1");
    }

    private void initView() {
        reportPoliceLogPresenter = new ReportPoliceLogPresenter(getContext());
        reportPoliceLogEventBeanList = new ArrayList<>();

        reportPoliceLogPresenter.onCreate();

        // 下拉刷新
        reportpolicelogSwiperefreshlayout.setColorSchemeResources(R.color.colorPrimary, R.color.blueness_two, R.color.blueness_three);
        reportpolicelogSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新", "下拉刷新");
                initData(1, "1");
            }
        });

        // 上拉加载
        reportpolicelogListview.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                L.e("上拉加载");
                page++;
                initData(2, Integer.toString(page));
            }
        });
    }

    private void initData(final int code, final String page) {
        reportPoliceLogPresenter.getEventListPresenter("", "10", page);
        reportPoliceLogPresenter.attachView(new DataView<BaseDataListBean<ReportPoliceLogEventBean>>() {
            @Override
            public void onSuccess(BaseDataListBean<ReportPoliceLogEventBean> TBean) {
                fragmentReportpolicelogReloadLinearlayout.setVisibility(View.GONE);
                fragmentReportpolicelogLl.setVisibility(View.VISIBLE);
                // 如果时下拉刷新和第一次进入时，要重新初始化 reportPoliceBeanList，否则下拉刷新时也会往list中add数据
                if (code == 1 || code == 0) {
                    reportPoliceLogEventBeanList = new ArrayList<>();
                }
                // 将数据放入list中，再传到adapter
                for (int i = 0; i < TBean.getData().size(); i++) {
                    reportPoliceLogEventBeanList.add(TBean.getData().get(i));
                }
                // 第一次加载数据和下拉刷新时走这里
                if ("1".equals(page) || page == "1") {
                    // 这个判断是为了只创建一次 reportPoliceAdapter对象，如果上拉加载时再次走这里的话会直接弹回顶部。
                    reportPoliceLogAdapter = new ReportPoliceLogAdapter(getContext(), reportPoliceLogEventBeanList);
                    reportpolicelogListview.setAdapter(reportPoliceLogAdapter);
                }
                // 请求成功后取消刷新框
                isCloseLoad(code);
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(getContext(), error);
                fragmentReportpolicelogReloadLinearlayout.setVisibility(View.VISIBLE);
                fragmentReportpolicelogLl.setVisibility(View.GONE);
                // 请求失败后取消刷新框
                isCloseLoad(code);
            }

            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        });
    }

    @OnClick(R.id.fragment_reportpolicelog_reload_btn)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_reportpolicelog_reload_btn: // 重新加载按钮
                page = 1;
                initData(0, Integer.toString(page));
                break;
        }
    }

    /**
     * 判断应该关闭下拉刷新框还是上拉加载框
     *
     * @param code 等于 1时，是下拉刷新
     *             等于 2时，是上拉加载
     */
    private void isCloseLoad(int code) {
        if (code == 1) {
            // 下拉刷新
            //为了保险起见可以先判断当前是否在刷新中（旋转的小圈圈在旋转）....
            if (reportpolicelogSwiperefreshlayout.isRefreshing()) {
                //关闭刷新动画
                reportpolicelogSwiperefreshlayout.setRefreshing(false);
                // 刷新后将 page改为 1
                page = 1;
            }
        } else if (code == 2) {
            // 上拉加载 后关闭加载框
            reportPoliceLogAdapter.notifyDataSetChanged();
            reportpolicelogListview.loadComplete();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
