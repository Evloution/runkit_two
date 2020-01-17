package com.elink.runkit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.elink.runkit.R;
import com.elink.runkit.fragment.DeviceMonitoringFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Evloution
 * @date 2020-01-16
 * @email 15227318030@163.com
 * @description 查询监测点界面
 */
public class MonitoringSearchActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.monitoringsearch_name_edt)
    EditText monitoringsearchNameEdt; // 设备名称输入框
    @BindView(R.id.monitoringsearch_ip_edt)
    EditText monitoringsearchIpEdt; // 设备ip输入框
    @BindView(R.id.monitoringsearch_btn)
    Button monitoringsearchBtn; // 搜索按钮

    private String searchName; // 监测点名称输入框
    private String searchIP; // 监测点ip输入框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoringsearch);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_img, R.id.monitoringsearch_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img: // 页面左上角返回按钮
                finish();
                break;
            case R.id.monitoringsearch_btn: // 搜索按钮
                // 获取输入框的名称
                searchName = monitoringsearchNameEdt.getText().toString().trim();
                // 获取输入框的ip
                searchIP = monitoringsearchIpEdt.getText().toString().trim();
                Intent intent = new Intent(this, DeviceMonitoringFragment.class);
                intent.putExtra("pointName", searchName);
                intent.putExtra("pointIP", searchIP);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
