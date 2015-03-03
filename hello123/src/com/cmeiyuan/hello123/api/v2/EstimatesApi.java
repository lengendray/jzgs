package com.cmeiyuan.hello123.api.v2;

import com.cmeiyuan.hello123.api.BaseApi;
import com.cmeiyuan.hello123.bean.v2.Estimates;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/2/27.
 */
public class EstimatesApi extends BaseApi<Estimates> {

    private static final String URL = "http://www.cmeiyuan.com:8080/fundapi/getestimate";

    @Override
    protected String getUrl() {
        return URL;
    }

    public void get(String... fundCodes) {
        StringBuilder sb = new StringBuilder();
        int size = fundCodes.length;
        for (int i = 0; i < size; i++) {
            sb.append(fundCodes[i]);
            if (i < size - 1) {
                sb.append(",");
            }
        }
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("fundcodes", sb.toString());
        get(param);
    }
}
