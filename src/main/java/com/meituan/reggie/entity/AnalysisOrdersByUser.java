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
public class AnalysisOrdersByUser {

    //消费能力分数 周平均消费达700则为高消费、500中消费、300低消费    该参数灵活配置
    private Double costAbilityScore;
    //用户本周的订单的均价
    private Double averagePriceInWeek;
    //用户本周单量数量
    private Integer OrdersInWeekNum;

    public Double getCostAbilityScore() {
        return costAbilityScore;
    }

    public void setCostAbilityScore(Double costAbilityScore) {
        this.costAbilityScore = costAbilityScore;
    }

    public Double getAveragePriceInWeek() {
        return averagePriceInWeek;
    }

    public void setAveragePriceInWeek(Double averagePriceInWeek) {
        this.averagePriceInWeek = averagePriceInWeek;
    }

    public Integer getOrdersInWeekNum() {
        return OrdersInWeekNum;
    }

    public void setOrdersInWeekNum(Integer ordersInWeekNum) {
        OrdersInWeekNum = ordersInWeekNum;
    }

    public List<Orders> getOrdersInWeek() {
        return OrdersInWeek;
    }

    public void setOrdersInWeek(List<Orders> ordersInWeek) {
        OrdersInWeek = ordersInWeek;
    }

    //用户本周订单
    private List<Orders> OrdersInWeek;

}
