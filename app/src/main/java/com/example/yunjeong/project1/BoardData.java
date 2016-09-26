package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-25.
 */
public class BoardData {
    String bId;
    String imgUrl;
    String Info;

    public BoardData(String bId, String imgUrl, String info) {
        this.bId = bId;
        this.imgUrl = imgUrl;
        Info = info;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }
}
