package com.ali.socialblog.Model;

public class Blog {
    public String pTitle;
    public String desc;
    public String pImage;
    public String timeStamp;
    public String userId;

    public Blog() {
    }

    public Blog(String pTitle, String desc, String pImage, String timeStamp, String userId) {
        this.pTitle = pTitle;
        this.desc = desc;
        this.pImage = pImage;
        this.timeStamp = timeStamp;
        this.userId = userId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
