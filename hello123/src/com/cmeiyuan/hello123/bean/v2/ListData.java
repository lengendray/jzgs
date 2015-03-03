package com.cmeiyuan.hello123.bean.v2;

import java.util.List;

/**
 * Created by Administrator on 2015/2/27.
 */
public class ListData<T> {

    private Integer total;
    private List<T> items;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
