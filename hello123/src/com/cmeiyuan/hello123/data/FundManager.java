package com.cmeiyuan.hello123.data;

import com.cmeiyuan.hello123.bean.v2.Fund;

import java.util.List;

/**
 * Created by Administrator on 2015/2/27.
 */
public class FundManager {

    private static List<Fund> list;

    public static List<Fund> getList() {
        return list;
    }

    public static void setList(List<Fund> list) {
        FundManager.list = list;
    }
}
