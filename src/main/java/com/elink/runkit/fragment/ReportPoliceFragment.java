package com.elink.runkit.fragment;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.elink.runkit.R;
import com.elink.runkit.adapter.ReportPoliceAdapter;
import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.BaseDataListBean;
import com.elink.runkit.bean.ReportPoliceBean;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.ConfirmWarnPresenter;
import com.elink.runkit.presenter.ReportPolicePresenter;
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
public class ReportPoliceFragment extends Fragment {

    @BindView(R.id.fragment_reportpolice_reload_btn)
    Button fragmentReportpoliceReloadBtn; // 重新加载按钮
    @BindView(R.id.fragment_reportpolice_reload_linearlayout)
    LinearLayout fragmentReportpoliceReloadLinearlayout; // 重新加载按钮的LinearLayout
    @BindView(R.id.fragment_reportpolice_linearlayout)
    LinearLayout fragmentReportpoliceLinearlayout; // 主体的LinearLayout
    @BindView(R.id.devicelist_listview)
    LoadListView devicelistListview; // 告警列表
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout; // 下拉刷新
    Unbinder unbinder;

    private ReportPolicePresenter reportPolicePresenter = null;
    private ConfirmWarnPresenter confirmWarnPresenter = null;
    private ReportPoliceAdapter reportPoliceAdapter = null;
    private List<ReportPoliceBean> reportPoliceBeanList = null;
    private AlertDialog alertDialog = null;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportpolice, container, false);
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
        reportPolicePresenter = new ReportPolicePresenter(getContext());
        confirmWarnPresenter = new ConfirmWarnPresenter(getContext());
        reportPoliceBeanList = new ArrayList<>();

        reportPolicePresenter.onCreate();
        confirmWarnPresenter.onCreate();

        // 下拉刷新
        swiperefreshlayout.setColorSchemeResources(R.color.colorPrimary, R.color.blueness_two, R.color.blueness_three);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新", "下拉刷新");
                initData(1, "1");
            }
        });

        // 上拉加载
        devicelistListview.setInterface(new LoadListView.ILoadListener() {
            @Override
            public void onLoad() {
                L.e("上拉加载");
                page++;
                initData(2, Integer.toString(page));
            }
        });
    }

    private void initData(final int code, final String page) {
        reportPolicePresenter.getWarnListPresenter("10", page);
        reportPolicePresenter.attachView(new DataView<BaseDataListBean<ReportPoliceBean>>() {
            @Override
            public void onSuccess(BaseDataListBean<ReportPoliceBean> TBean) {
                fragmentReportpoliceReloadLinearlayout.setVisibility(View.GONE);
                fragmentReportpoliceLinearlayout.setVisibility(View.VISIBLE);
                // 如果时下拉刷新和第一次进入时，要重新初始化 reportPoliceBeanList，否则下拉刷新时也会往list中add数据
                if (code == 1 || code == 0) {
                    reportPoliceBeanList = new ArrayList<>();
                }
                // 将数据放入list中，再传到adapter
                for (int i = 0; i < TBean.getData().size(); i++) {
                    reportPoliceBeanList.add(TBean.getData().get(i));
                }
                // 第一次加载数据和下拉刷新时走这里
                if ("1".equals(page) || page == "1") {
                    // 这个判断是为了只创建一次 reportPoliceAdapter对象，如果上拉加载时再次走这里的话会直接弹回顶部。
                    reportPoliceAdapter = new ReportPoliceAdapter(getContext(), reportPoliceBeanList, new ReportPoliceAdapter.ReportPoliceCallback() {
                        @Override
                        public void click(View view) {
                            final int position = (Integer) view.getTag();
                            switch (view.getId()) {
                                case R.id.item_fragment_reportpolice_details_btn: // 异常详情按钮
                                    ToastUtil.show(getContext(), "异常详情");
                                    break;
                                case R.id.item_fragment_reportpolice_confirm_btn: // 异常确认按钮
                                    showConfirmDialog(reportPoliceBeanList.get(position).ID);
                                    break;
                            }
                        }
                    });
                    devicelistListview.setAdapter(reportPoliceAdapter);
                }
                // 请求成功后取消刷新框
                isCloseLoad(code);
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                ToastUtil.show(getContext(), error);
                fragmentReportpoliceReloadLinearlayout.setVisibility(View.VISIBLE);
                fragmentReportpoliceLinearlayout.setVisibility(View.GONE);
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
            if (swiperefreshlayout.isRefreshing()) {
                //关闭刷新动画
                swiperefreshlayout.setRefreshing(false);
                // 刷新后将 page改为 1
                page = 1;
            }
        } else if (code == 2) {
            // 上拉加载 后关闭加载框
            reportPoliceAdapter.notifyDataSetChanged();
            devicelistListview.loadComplete();
        }
    }

    private void showConfirmDialog(final String id) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.undevice_popup_layout, null, false);
        alertDialog = new AlertDialog.Builder(getContext()).setView(view).show();
        TextView textInfo = view.findViewById(R.id.textview);
        final Button buttonOk = view.findViewById(R.id.btn_ok);
        Button buttonCancel = view.findViewById(R.id.btn_cancel);
        textInfo.setText("是否确认该告警信息");
        // 确定退出按钮
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmWarnPresenter.confirmWarnPresenter(id);
                confirmWarnPresenter.attachView(new DataView<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean TBean) {
                        initData(1, "1");
                        ToastUtil.show(getContext(), "确认成功");
                    }

                    @Override
                    public void onError(String error) {
                        ToastUtil.show(getContext(), "确认失败");
                    }

                    @Override
                    public void showProgress() {

                    }

                    @Override
                    public void hideProgress() {

                    }
                });
                alertDialog.dismiss();
                ToastUtil.show(getContext(), "确认成功");
            }
        });
        // 取消按钮
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        // 点击返回键和外部都不可取消
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @OnClick(R.id.fragment_reportpolice_reload_btn)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_reportpolice_reload_btn: // 重新加载按钮
                page = 1;
                initData(0, Integer.toString(page));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
