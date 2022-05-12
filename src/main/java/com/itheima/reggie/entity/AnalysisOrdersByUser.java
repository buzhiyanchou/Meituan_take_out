package com.itheima.reggie.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:店铺订单分析
 * @author: zhuming
 * @date 2022-05-13
 */
@Data
@Builder
public class AnalysisOrdersByUser {

    //消费能力 周平均消费达700则为高消费、500中消费、300低消费    该参数灵活配置
    private String costAbility;
    //该用户本周的订单的均价
    private BigDecimal averagePriceInWeek;
    //本周单量
    private Integer OrdersInWeek;
    //本月单量
    private Integer OrdersInMouth;
    //客户热购商品
    private List<Dish> hotDishlist;
}
