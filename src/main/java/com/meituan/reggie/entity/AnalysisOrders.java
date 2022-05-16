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

    public Double getAveragePriceInWeek() {
        return averagePriceInWeek;
    }

    public void setAveragePriceInWeek(Double averagePriceInWeek) {
        this.averagePriceInWeek = averagePriceInWeek;
    }

    public Integer getOrdersInWeekNum() {
        return ordersInWeekNum;
    }

    public void setOrdersInWeekNum(Integer ordersInWeekNum) {
        this.ordersInWeekNum = ordersInWeekNum;
    }

    public Double getShopScore() {
        return shopScore;
    }

    public void setShopScore(Double shopScore) {
        this.shopScore = shopScore;
    }

    public String getShopConvertRate() {
        return shopConvertRate;
    }

    public void setShopConvertRate(String shopConvertRate) {
        this.shopConvertRate = shopConvertRate;
    }

    public String getOrderConvertRate() {
        return orderConvertRate;
    }

    public void setOrderConvertRate(String orderConvertRate) {
        this.orderConvertRate = orderConvertRate;
    }

    public String getReplyOrderRate() {
        return replyOrderRate;
    }

    public void setReplyOrderRate(String replyOrderRate) {
        this.replyOrderRate = replyOrderRate;
    }

    public Integer getOrdersPeopleNum() {
        return ordersPeopleNum;
    }

    public void setOrdersPeopleNum(Integer ordersPeopleNum) {
        this.ordersPeopleNum = ordersPeopleNum;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<Orders> getOrderBymoneySort() {
        return orderBymoneySort;
    }

    public void setOrderBymoneySort(List<Orders> orderBymoneySort) {
        this.orderBymoneySort = orderBymoneySort;
    }

    public List<User> getOrderByUserId() {
        return orderByUserId;
    }

    public void setOrderByUserId(List<User> orderByUserId) {
        this.orderByUserId = orderByUserId;
    }

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
