package com.qluxstory.qingshe.me.entity;

import com.qluxstory.qingshe.common.entity.BaseEntity;

import java.util.List;

/**
 * Created by lenovo on 2016/5/18.
 */
public class MyIncomeResult extends BaseEntity {
    List<MyIncomeEntity> data;
    public List<MyIncomeEntity> getData() {
        return data;
    }

    public void setData(List<MyIncomeEntity> data) {
        this.data = data;
    }


}
