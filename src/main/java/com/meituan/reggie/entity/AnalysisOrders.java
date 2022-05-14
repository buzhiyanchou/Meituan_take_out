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

    //该用户本周的订单的均价
    private Double averagePriceInWeek;
    //本周订单数量
    private Integer ordersInWeekNum;
    //店铺分数得分(好评+中评-差评)X99
    private Double shopScore;
    //店铺转化率(50-89%)
    private String shopConvertRate;
    //下单转化率(80-99%)
    private String orderConvertRate;
    //复购率(85-94%)
    private String replyOrderRate;
    //购买人数
    private Integer ordersPeopleNum;
    //总收入
    private BigDecimal totalMoney;

    //消费订单  以消费额    订单降序
    private List<Orders> orderBymoneySort;
    //消费用户  以消费额   人员降序
    private List<User> orderByUserId;


}
