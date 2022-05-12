package com.meituan.reggie.entity;

import lombok.Data;

/**
 * @description:
 * @author: zhuming
 * @date 2022-05-12
 * @modified By:
 */
@Data
public class EvaVO {
    //评价
    private String evaluate;

    // 订单评价状态: 1 好评 2 中评 3 差评
    private Integer evaStauts;
}
