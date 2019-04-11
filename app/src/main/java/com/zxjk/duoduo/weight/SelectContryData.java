package com.zxjk.duoduo.weight;

public class SelectContryData {
    private String contry;
    private String code;
    private String Index; // 索引 如：A、B、C

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
