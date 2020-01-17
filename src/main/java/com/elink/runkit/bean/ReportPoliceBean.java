package com.elink.runkit.bean;

/**
 * @author Evloution
 * @date 2020-01-14
 * @email 15227318030@163.com
 * @description 告警信息 bean
 */
public class ReportPoliceBean {

    /**
     * ACK : 0
     * ACKTIME : null
     * CONTENT : 在线变离线
     * CREATED_TIME : 2020-01-14 04:01:59
     * ID : cd4c65188dbe473cbff33760b5875b3d
     * IP : 0
     * NAME : 莲池大街桥网络
     * ROWNUM_ : 11
     * STATUS : 1
     */

    public int ACK;
    public Object ACKTIME;
    public String CONTENT;
    public String CREATED_TIME;
    public String ID;
    public String IP;
    public String NAME;
    public int ROWNUM_;
    public int STATUS;

    public int getACK() {
        return ACK;
    }

    public void setACK(int ACK) {
        this.ACK = ACK;
    }

    public Object getACKTIME() {
        return ACKTIME;
    }

    public void setACKTIME(Object ACKTIME) {
        this.ACKTIME = ACKTIME;
    }

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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
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

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
    
}
