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
    //订单状态
    private Integer status;
}
