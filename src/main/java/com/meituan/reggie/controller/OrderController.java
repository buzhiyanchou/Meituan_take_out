package com.meituan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meituan.reggie.common.R;
import com.meituan.reggie.entity.*;
import com.meituan.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${costAblity.high}")
    private Integer high;
    @Value("${costAblity.middle}")
    private Integer middle;
    @Value("${costAblity.low}")
    private Integer low;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 用户订单分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);


        return R.success(pageInfo);
    }

    /**
     * 用户评价
     *
     * @param orders
     * @return
     */
    @GetMapping("/evaluateOrder")
    public R<Boolean> evaluateOrder(@RequestBody Orders orders) {
        String evaluate = orders.getEvaluate();
        //构造器对象
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orders.getId());
        boolean b = orderService.updateById(orders);
        return R.success(b);

    }

    /**
     * 查看评价
     *
     * @param orders
     * @return
     */
    @GetMapping("/selectEvaluateByOrderId")
    public R<EvaVO> selectEvaluateByOrderId(@RequestBody Orders orders) {
        Long id = orders.getId();
        //构造器对象
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(id)) {
            queryWrapper.eq("id", id);
        }
        Orders ordersOne = orderService.getOne(queryWrapper);
        String evaluate = ordersOne.getEvaluate();
        Integer evaStauts = ordersOne.getEvaStutas();
        EvaVO evaVO = new EvaVO();
        evaVO.setEvaluate(evaluate);
        evaVO.setEvaStauts(evaStauts);
        return R.success(evaVO);
    }


    /**
     * 店铺所有订单的量分析，将人群按消费比例，分成三份
     *
     * @param user
     * @return
     */
    @GetMapping("/analysisOrder")
    public R<AnalysisOrders> analysisOrders(@RequestBody User user) {
        //TODO 订单的数据处理封装
        AnalysisOrders analysisOrders = AnalysisOrders.builder()
//                .maxOrders()
//                .minOrders()
//                .midOrders()
//                .maxConsumer()
//                .minConsumer()
//                .midConsumer()
//                .averagePrice()
//                .hotCommodityList()
//                .totalOrders()
                .build();
        return R.success(analysisOrders);
    }

    /**
     * 基于用户分析订单数据
     *
     * @param user
     * @return
     */
    @GetMapping("/analysisOrderByCustomer")
    public R<AnalysisOrdersByUser> analysisOrderByCustomer(@RequestBody User user) {


        //TODO 属于用户的订单数据处理封装
        AnalysisOrdersByUser analysisOrdersByUser = AnalysisOrdersByUser.builder()
//                .costAbility()
//                .averagePriceInWeek()
//                .OrdersInWeek()
//                .OrdersInMouth()
//                .HotDishlist()
                .build();
        return R.success(analysisOrdersByUser);
    }

    //TODO 订单状态的改变  配送完成

}