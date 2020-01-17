package com.elink.runkit.bean;

import java.util.List;

/**
 * @author Evloution
 * @date 2020-01-09
 * @email 15227318030@163.com
 * @description 首页上图表的信息
 */
public class HistogramBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : {"alive":["86","3","0","0","2","3","27","9","0","0","0","0"],
     * "dropped":["967","212","19","16","100","60","1","408","36","21","5","54"],
     * "label":["电警","卡口","红绿灯条形LED","诱导屏","微卡","礼让行人","借道左转","球机","违停球","公交车道","高点监控","限高架"]}
     */

    public List<String> alive;
    public List<String> dropped;
    public List<String> label;

    public List<String> getAlive() {
        return alive;
    }

    public void setAlive(List<String> alive) {
        this.alive = alive;
    }

    public List<String> getDropped() {
        return dropped;
    }

    public void setDropped(List<String> dropped) {
        this.dropped = dropped;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

}
