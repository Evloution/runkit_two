package com.elink.runkit.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.elink.runkit.R;
import com.elink.runkit.fragment.DeviceMonitoringFragment;
import com.elink.runkit.fragment.HomePageFragment;
import com.elink.runkit.fragment.MyPageFragment;
import com.elink.runkit.fragment.ReportPoliceFragment;
import com.elink.runkit.fragment.ReportPoliceLogFragment;
import com.elink.runkit.log.L;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment = new Fragment();
    private HomePageFragment homePageFragment;
    private DeviceMonitoringFragment deviceMonitoringFragment;
    private MyPageFragment myPageFragment;
    private ReportPoliceFragment reportPoliceFragment;
    private ReportPoliceLogFragment reportPoliceLogFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(homePageFragment);
                    return true;
                case R.id.navigation_deviceMonitoring:
                    switchFragment(deviceMonitoringFragment);
                    return true;
                case R.id.navigation_reportPolice:
                    switchFragment(reportPoliceFragment);
                    return true;
                case R.id.navigation_reportPoliceLog:
                    switchFragment(reportPoliceLogFragment);
                    return true;
                case R.id.navigation_myPage:
                    switchFragment(myPageFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        L.e("navView.getItemIconSize():" + navView.getItemIconSize());
        // 解决 BottomNavigationView 大于3个menu时文字不显示的问题
        navView.setLabelVisibilityMode(1);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initView();
    }

    private void initView() {
        homePageFragment = new HomePageFragment();
        deviceMonitoringFragment = new DeviceMonitoringFragment();
        myPageFragment = new MyPageFragment();
        reportPoliceFragment = new ReportPoliceFragment();
        reportPoliceLogFragment = new ReportPoliceLogFragment();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        ft.add(R.id.fragment_content, homePageFragment).commit();
        fragment = homePageFragment;

        homePageFragment.setOnReportPoliceClick(new HomePageFragment.OnReportPoliceClick() {
            @Override
            public void onReportPoliceClick(View view) {
                switchFragment(reportPoliceFragment);
            }
        });
    }

    /**
     * fragment 切换方法
     *
     * @param targetFragment
     * @return 对应的fragment
     */
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.hide(fragment).add(R.id.fragment_content, targetFragment).commit();
        } else {
            transaction.hide(fragment).show(targetFragment).commit();
        }
        fragment = targetFragment;
        return transaction;
    }
}
