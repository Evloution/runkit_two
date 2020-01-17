package com.elink.runkit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elink.runkit.R;
import com.elink.runkit.bean.BaseBean;
import com.elink.runkit.bean.PointsInfoBean;
import com.elink.runkit.constants.Constants;
import com.elink.runkit.log.L;
import com.elink.runkit.presenter.PointsInfoPresenter;
import com.elink.runkit.view.DataView;

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
public class HomePageFragment extends Fragment {

    @BindView(R.id.fragment_homepage_totalnumber_txt)
    TextView fragmentHomepageTotalnumberTxt; // 监测点总数
    @BindView(R.id.fragment_homepage_hostnumber_txt)
    TextView fragmentHomepageHostnumberTxt; // 主监测点数
    @BindView(R.id.fragment_homepage_hostonlinenumber_txt)
    TextView fragmentHomepageHostonlinenumberTxt; // 主监测点在线数
    @BindView(R.id.fragment_homepage_hostofflinenumber_txt)
    TextView fragmentHomepageHostofflinenumberTxt; // 主监测点离线数
    @BindView(R.id.fragment_homepage_warningnumber_txt)
    TextView fragmentHomepageWarningnumberTxt; // 警告数
    @BindView(R.id.fragment_homepage_hostonlineratenumber_txt)
    TextView fragmentHomepageHostonlineratenumberTxt; // 主监测点在线率
    @BindView(R.id.fragment_homepage_reload_linearlayout)
    LinearLayout fragmentHomepageReloadLinearlayout; // 加载的按钮
    @BindView(R.id.fragment_homepage_linearlayout)
    LinearLayout fragmentHomepageLinearlayout; // 整体数据
    @BindView(R.id.fragment_homepage_reload_btn)
    Button fragmentHomepageReloadBtn; // 重新加载按钮
    @BindView(R.id.fragment_homepage_onlinerate_btn)
    Button togglepagesOnLineRateBtn; // 总体在线率饼图
    @BindView(R.id.fragment_homepage_statistics_btn)
    Button togglepagesStatisticsBtn; // 分类统计柱状图
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout; // 下拉刷新
    Unbinder unbinder;
    @BindView(R.id.fragment_homepage_warning_rl)
    RelativeLayout fragmentHomepageWarningRl;


    private Fragment fragment = new Fragment();
    private EchartsDataBarFragment echartsDataBarFragment;
    private EchartsDataPieFragment echartsDataPieFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private OnReportPoliceClick onReportPoliceClick = null;
    private PointsInfoPresenter pointsInfoPresenter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this, view);
        L.e("onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.e("onActivityCreated");
        initView();
        initData();
    }

    private void initView() {
        pointsInfoPresenter = new PointsInfoPresenter(getContext());
        echartsDataBarFragment = new EchartsDataBarFragment();
        echartsDataPieFragment = new EchartsDataPieFragment();
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();

        ft.add(R.id.togglepages_framelayout, echartsDataBarFragment).commit();
        fragment = echartsDataBarFragment;

        // 下拉刷新
        swiperefreshlayout.setColorSchemeResources(R.color.colorPrimary, R.color.blueness_two, R.color.blueness_three);
        // swiperefreshlayout.setRefreshing(true);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新", "下拉刷新");
                initView();
                initData();
            }
        });
    }

    private void initData() {
        pointsInfoPresenter.onCreate();
        pointsInfoPresenter.getPointsInfo();
        pointsInfoPresenter.attachView(new DataView<BaseBean<PointsInfoBean>>() {
            @Override
            public void onSuccess(BaseBean<PointsInfoBean> TBean) {
                L.e("成功" + TBean.data.PERCENT);
                L.e("sssssssssssssssssssssssssssssssssssssss");
                fragmentHomepageReloadLinearlayout.setVisibility(View.GONE);
                fragmentHomepageLinearlayout.setVisibility(View.VISIBLE);
                onlineRateDefaultBtn();
                togglepagesOnLineRateBtn.setBackground(getResources().getDrawable(R.drawable.fragment_homepage_onlinerate_btn_pressdown_bg));
                togglepagesOnLineRateBtn.setTextColor(getResources().getColor(R.color.white));
                fragmentHomepageTotalnumberTxt.setText(String.valueOf(TBean.data.TOTAL));
                fragmentHomepageHostnumberTxt.setText(String.valueOf(TBean.data.TOTAL_PRIMARY));
                fragmentHomepageHostonlinenumberTxt.setText(String.valueOf(TBean.data.OnLine));
                fragmentHomepageHostofflinenumberTxt.setText(String.valueOf(TBean.data.OffLine));
                fragmentHomepageWarningnumberTxt.setText(String.valueOf(TBean.data.WARN));
                fragmentHomepageWarningnumberTxt.setTextColor(getResources().getColor(R.color.red));
                fragmentHomepageHostonlineratenumberTxt.setText(String.valueOf(TBean.data.PERCENT));
                Constants.DEVICES_ONLINE = Double.valueOf(TBean.data.PERCENT); // 将在线率赋给常量
                // 请求成功后取消刷新框
                isCloseLoad();
            }

            @Override
            public void onError(String error) {
                L.e("onError：" + error);
                fragmentHomepageReloadLinearlayout.setVisibility(View.VISIBLE);
                fragmentHomepageLinearlayout.setVisibility(View.GONE);
                // 请求成功后取消刷新框
                isCloseLoad();
            }

            @Override
            public void showProgress() {
                L.e("onError：");
            }

            @Override
            public void hideProgress() {

            }
        });
    }

    @OnClick({R.id.fragment_homepage_reload_btn, R.id.fragment_homepage_onlinerate_btn, R.id.fragment_homepage_statistics_btn, R.id.fragment_homepage_warning_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_homepage_reload_btn: // 重新加载数据按钮
                // initView();
                initData();
                echartsDataBarFragment.start();
                break;
            case R.id.fragment_homepage_onlinerate_btn: // 分类统计按钮
                // 点击了这个后背景变为蓝色，字体变为白色。另外一个按钮的背景变为白色，字体变为蓝色
                onlineRateDefaultBtn();
                switchFragment(echartsDataBarFragment);
                break;
            case R.id.fragment_homepage_statistics_btn: // 总体在线率按钮
                togglepagesStatisticsBtn.setBackground(getResources().getDrawable(R.drawable.fragment_homepage_statistics_btn_pressdown_bg));
                togglepagesStatisticsBtn.setTextColor(getResources().getColor(R.color.white));
                togglepagesOnLineRateBtn.setBackground(getResources().getDrawable(R.drawable.fragment_homepage_onlinerate_btn_normal_bg));
                togglepagesOnLineRateBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                switchFragment(echartsDataPieFragment);
                break;
            case R.id.fragment_homepage_warning_rl: // 警告数按钮
                if (onReportPoliceClick != null) {
                    onReportPoliceClick.onReportPoliceClick(fragmentHomepageWarningRl);
                }
                break;
        }
    }

    /**
     * fragment 切换方法
     *
     * @param targetFragment
     * @return
     */
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.hide(fragment).add(R.id.togglepages_framelayout, targetFragment).commit();
        } else {
            transaction.hide(fragment).show(targetFragment).commit();
        }
        fragment = targetFragment;
        return transaction;
    }

    /**
     * 关闭下拉刷新框
     */
    private void isCloseLoad() {
        // 下拉刷新
        //为了保险起见可以先判断当前是否在刷新中（旋转的小圈圈在旋转）....
        if (swiperefreshlayout.isRefreshing()) {
            //关闭刷新动画
            swiperefreshlayout.setRefreshing(false);
        }
    }

    /**
     * 分类统计背景为蓝色，总体在线率按钮为白色
     */
    private void onlineRateDefaultBtn() {
        togglepagesOnLineRateBtn.setBackground(getResources().getDrawable(R.drawable.fragment_homepage_onlinerate_btn_pressdown_bg));
        togglepagesOnLineRateBtn.setTextColor(getResources().getColor(R.color.white));
        togglepagesStatisticsBtn.setBackground(getResources().getDrawable(R.drawable.fragment_homepage_statistics_btn_normal_bg));
        togglepagesStatisticsBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    //定义接口变量的get方法
    public OnReportPoliceClick getOnReportPoliceClick() {
        return onReportPoliceClick;
    }

    //定义接口变量的set方法
    public void setOnReportPoliceClick(OnReportPoliceClick onReportPoliceClick) {
        this.onReportPoliceClick = onReportPoliceClick;
    }

    //1、定义接口，这个接口是跳转页面时使用
    public interface OnReportPoliceClick {
        void onReportPoliceClick(View view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
