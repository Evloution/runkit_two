package com.elink.runkit.bean;

import java.util.List;

/**
 * @author Evloution
 * @date 2020-01-17
 * @email 15227318030@163.com
 * @description 设备详情中图表的bean
 */
public class IncidentBean {

    /**
     * msg :
     * code : 0
     * data : {
     * "Time":["2020-01-02 18:04:22.0","2020-01-14 20:31:21.0","2020-01-14 21:09:56.0","2020-01-14 21:18:46.0",
     * "2020-01-14 22:13:49.0","2020-01-16 21:02:34.0","2020-01-16 23:36:11.0","2020-01-17 12:01:17"],
     * "CONTENT":["0","1","0","1","0","1","0","0"]}
     */

    public List<String> Time;
    public List<String> CONTENT;

    public List<String> getTime() {
        return Time;
    }

    public void setTime(List<String> Time) {
        this.Time = Time;
    }

    public List<String> getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(List<String> CONTENT) {
        this.CONTENT = CONTENT;
    }
}
