package com.elink.runkit.bean;

/**
 * @author Evloution
 * @date 2020-01-13
 * @email 15227318030@163.com
 * @description 监测点列表的 Bean
 */
public class MonitoringPointBean {


    /**
     * EVENTTIME : 2020-01-16 11:13:56
     * ID : 796
     * IP : 13.111.18.27
     * ISPRIMARY : 1
     * LATITUDE : null
     * LONGITUDE : null
     * MONIINTERVAL : 3600
     * MONIPAUSE : 0
     * MONITYPE : ping
     * NAME : 中兴大街-守敬路北抓拍2
     * POINTTYPE : 电警
     * PORT : 0
     * ROWNUM_ : 1
     * STATUS : 0
     * WARNGRADE : 4
     */

    public String EVENTTIME;
    public String ID;
    public String IP;
    public int ISPRIMARY;
    public double LATITUDE;
    public double LONGITUDE;
    public int MONIINTERVAL;
    public int MONIPAUSE;
    public String MONITYPE;
    public String NAME;
    public String POINTTYPE;
    public int PORT;
    public int ROWNUM_;
    public int STATUS;
    public int WARNGRADE;

    public String getEVENTTIME() {
        return EVENTTIME;
    }

    public void setEVENTTIME(String EVENTTIME) {
        this.EVENTTIME = EVENTTIME;
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

    public int getISPRIMARY() {
        return ISPRIMARY;
    }

    public void setISPRIMARY(int ISPRIMARY) {
        this.ISPRIMARY = ISPRIMARY;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public int getMONIINTERVAL() {
        return MONIINTERVAL;
    }

    public void setMONIINTERVAL(int MONIINTERVAL) {
        this.MONIINTERVAL = MONIINTERVAL;
    }

    public int getMONIPAUSE() {
        return MONIPAUSE;
    }

    public void setMONIPAUSE(int MONIPAUSE) {
        this.MONIPAUSE = MONIPAUSE;
    }

    public String getMONITYPE() {
        return MONITYPE;
    }

    public void setMONITYPE(String MONITYPE) {
        this.MONITYPE = MONITYPE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPOINTTYPE() {
        return POINTTYPE;
    }

    public void setPOINTTYPE(String POINTTYPE) {
        this.POINTTYPE = POINTTYPE;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
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

    public int getWARNGRADE() {
        return WARNGRADE;
    }

    public void setWARNGRADE(int WARNGRADE) {
        this.WARNGRADE = WARNGRADE;
    }
}
