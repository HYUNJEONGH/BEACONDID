package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-21.
 */
public class BeaconData {
    // beaconNum | beaconID | beaconName | beaconMajor | beaconMinor | installID  |
    int beaconNum;
    String beaconID;
    String beaconName;
    String beaconMajor;
    String beaconMinor;
    String installID;

    public BeaconData(int beaconNum, String beaconID, String beaconName, String beaconMajor, String beaconMinor, String installID) {
        this.beaconNum = beaconNum;
        this.beaconID = beaconID;
        this.beaconName = beaconName;
        this.beaconMajor = beaconMajor;
        this.beaconMinor = beaconMinor;
        this.installID = installID;
    }

    public int getBeaconNum() {
        return beaconNum;
    }

    public void setBeaconNum(int beaconNum) {
        this.beaconNum = beaconNum;
    }

    public String getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }

    public String getBeaconMajor() {
        return beaconMajor;
    }

    public void setBeaconMajor(String beaconMajor) {
        this.beaconMajor = beaconMajor;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBeaconMinor() {
        return beaconMinor;
    }

    public void setBeaconMinor(String beaconMinor) {
        this.beaconMinor = beaconMinor;
    }

    public String getInstallID() {
        return installID;
    }

    public void setInstallID(String installID) {
        this.installID = installID;
    }
}
