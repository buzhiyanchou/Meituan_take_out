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

    //高消费订单  以价格降序
    private List<Orders> maxOrders;
    //低消费订单  以价格降序
    private List<Orders> minOrders;
    //中消费订单  以价格降序
    private List<Orders> midOrders;
    //高消费用户  以价格降序
    private List<Orders> maxConsumer;
    //低消费用户  以价格降序
    private List<Orders> minConsumer;
    //中消费用户  以价格降序
    private List<Orders> midConsumer;
    //热销商品  以价格降序
    private List<Dish> hotCommodityList;
    //整体的订单的均价
    private BigDecimal averagePrice;
    //单量
    private Integer totalOrders;


}
