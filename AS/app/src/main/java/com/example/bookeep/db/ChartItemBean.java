package com.example.bookeep.db;

public class ChartItemBean {
    int simageId;
    String type;
    float ratio;//所占比例
    float totalMoney;//此项总钱数

    public ChartItemBean() {
    }

    public ChartItemBean(int simageId, String type, float ratio, float totalMoney) {
        this.simageId = simageId;
        this.type = type;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}
