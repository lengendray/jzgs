package com.cmeiyuan.hello123.bean.v2;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/2/27.
 */
public class Estimate implements Serializable{

    private String fundCode;
    private Float gszzl;
    private Long dateTime;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Float getGszzl() {
        return gszzl;
    }

    public void setGszzl(Float gszzl) {
        this.gszzl = gszzl;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
