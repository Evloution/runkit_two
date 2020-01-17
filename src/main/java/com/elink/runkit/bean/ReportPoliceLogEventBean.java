package com.elink.runkit.bean;

/**
 * @author Evloution
 * @date 2020-01-14
 * @email 15227318030@163.com
 * @description 告警日志 bean
 */
public class ReportPoliceLogEventBean {


    /**
     * CONTENT : 离线变在线
     * CREATED_TIME : 2020-01-16 11:21:50
     * CURRENTSTATUS : 1
     * GRADE : 5
     * NAME : 测试点2网络
     * ROWNUM_ : 1
     * SOURCEID : null
     */

    public String CONTENT;
    public String CREATED_TIME;
    public int CURRENTSTATUS;
    public int GRADE;
    public String NAME;
    public int ROWNUM_;
    public Object SOURCEID;

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getCREATED_TIME() {
        return CREATED_TIME;
    }

    public void setCREATED_TIME(String CREATED_TIME) {
        this.CREATED_TIME = CREATED_TIME;
    }

    public int getCURRENTSTATUS() {
        return CURRENTSTATUS;
    }

    public void setCURRENTSTATUS(int CURRENTSTATUS) {
        this.CURRENTSTATUS = CURRENTSTATUS;
    }

    public int getGRADE() {
        return GRADE;
    }

    public void setGRADE(int GRADE) {
        this.GRADE = GRADE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getROWNUM_() {
        return ROWNUM_;
    }

    public void setROWNUM_(int ROWNUM_) {
        this.ROWNUM_ = ROWNUM_;
    }

    public Object getSOURCEID() {
        return SOURCEID;
    }

    public void setSOURCEID(Object SOURCEID) {
        this.SOURCEID = SOURCEID;
    }
}
