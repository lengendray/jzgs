package com.cmeiyuan.hello123.api.v2;

import com.cmeiyuan.hello123.api.BaseApi;
import com.cmeiyuan.hello123.bean.v2.Funds;

/**
 * Created by Administrator on 2015/2/27.
 */
public class FundsApi extends BaseApi<Funds> {

    private static final String URL = "http://www.cmeiyuan.com:8080/fundapi/getfunds";

    @Override
    protected String getUrl() {
        return URL;
    }
}
