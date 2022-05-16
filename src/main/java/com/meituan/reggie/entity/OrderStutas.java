package com.meituan.reggie.entity;

import lombok.Data;

/**
 * @description:
 * @author: zhuming
 * @date 2022-05-15
 * @modified By:
 */
@Data
public class OrderStutas {
    //订单id
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    //订单状态
    private Integer status;
}
