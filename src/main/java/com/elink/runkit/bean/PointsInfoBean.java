package com.elink.runkit.bean;

/**
 * @Description：获取监测点数目，首页上的数据
 * @Author： Evloution_
 * @Date： 2020-01-08
 * @Email： 15227318030@163.com
 */

public class PointsInfoBean {

    /**
     * TOTAL : 2029
     * OnLine : 126
     * PERCENT : 6.23
     * TOTAL_PRIMARY : 2023
     * WARN : 7892
     * OffLine : 1897
     */

    public int TOTAL; // 监测点总数
    public int OnLine; // 主监测点在线数
    public String PERCENT; // 主监测点在线率(%)
    public int TOTAL_PRIMARY; // 主监测点数量
    public int WARN; // 警告数
    public int OffLine; // 主监测点离线数

    public int getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }

    public int getOnLine() {
        return OnLine;
    }

    public void setOnLine(int OnLine) {
        this.OnLine = OnLine;
    }

    public String getPERCENT() {
        return PERCENT;
    }

    public void setPERCENT(String PERCENT) {
        this.PERCENT = PERCENT;
    }

    public int getTOTAL_PRIMARY() {
        return TOTAL_PRIMARY;
    }

    public void setTOTAL_PRIMARY(int TOTAL_PRIMARY) {
        this.TOTAL_PRIMARY = TOTAL_PRIMARY;
    }

    public int getWARN() {
        return WARN;
    }

    public void setWARN(int WARN) {
        this.WARN = WARN;
    }

    public int getOffLine() {
        return OffLine;
    }

    public void setOffLine(int OffLine) {
        this.OffLine = OffLine;
    }
}
