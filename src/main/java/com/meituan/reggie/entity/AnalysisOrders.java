package com.meituan.reggie.entity;

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
public class AnalysisOrders {

    //消费订单  以消费额    订单降序
    private List<Orders> orderBymoneySort;
    //消费用户  以消费额   人员降序
    private List<User> orderByUserId;
    //热销商品  以价格降序
    private List<Dish> hotCommodityList;
    //整体的订单的均价
    private BigDecimal averagePrice;
    //单量
    private Integer totalOrders;
    //订单总额
    private BigDecimal totalMoeny;


}
