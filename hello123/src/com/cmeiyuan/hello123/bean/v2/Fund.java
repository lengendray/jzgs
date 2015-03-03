package com.cmeiyuan.hello123.bean.v2;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/2/27.
 */
public class Fund implements Serializable{

    private static final long serialVersionUID = 2L;

    private String fundCode;
    private String fundName;
    private String fundType;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }
}
