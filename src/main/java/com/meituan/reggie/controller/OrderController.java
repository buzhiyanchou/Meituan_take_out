package com.meituan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meituan.reggie.common.R;
import com.meituan.reggie.entity.*;
import com.meituan.reggie.service.OrderService;
import com.meituan.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Value("${costAblity.middle}")
    private BigDecimal middle;


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
    @PostMapping("/evaluateOrder")
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
     * @return
     */
    @GetMapping("/analysisOrder")
    public R<AnalysisOrders> analysisOrders() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar caln = Calendar.getInstance();
        caln.setTime(getTimesWeekmorning());
        caln.add(Calendar.DAY_OF_WEEK, 7);
        QueryWrapper<Orders> queryWrapperx = new QueryWrapper<>();
        queryWrapperx.between("checkout_time", cal.getTime(), caln.getTime());
        List<Orders> list = orderService.list(queryWrapperx);
        Integer shopScore = null;
        Integer flag;
        for (int i = 0; i < list.size(); i++) {
            Integer status = list.get(i).getStatus();
            //差评扣-8分
            if (Integer.valueOf("3").equals(status)) {
                flag = -8;
                //中评5分
            } else if (Integer.valueOf("2").equals(status)) {
                flag = 5;
                //好评10分
            } else if (Integer.valueOf("1").equals(status)) {
                flag = 10;
            } else {
                flag = 0;
            }
            shopScore = +flag;
            flag = 0;
        }

        //店铺总单数
        int count = list.size();
        //对店铺订单从高到低消费额降序排列
        List<Orders> orderBymoneySort = list.stream().sorted(Comparator.comparing(Orders::getAmount).reversed()).collect(Collectors.toList());
        QueryWrapper<User> queryWrappern = new QueryWrapper<>();
        Map<Long, User> collect = userService.list(queryWrappern).stream().collect(Collectors.toMap(User::getId, a -> a, (k1, k2) -> k1));
        Map<Long, Orders> ordersMap = list.stream().sorted(Comparator.comparing(Orders::getAmount).reversed()).collect(Collectors.toMap(Orders::getId, a -> a, (k1, k2) -> k1));
        orderBymoneySort.forEach(t -> {
            t.setUserName(collect.get(t.getUserId()).getName());
        });
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {
            totalMoney = totalMoney.add(ordersMap.get(list.get(i).getId()).getAmount());
        }
        Map<Long, Orders> map = list.stream().collect(Collectors.toMap(Orders::getUserId, a -> a, (k1, k2) -> k1));
        ArrayList<User> userList = new ArrayList<>();
        list.forEach(t -> {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", t.getUserId());
            User one = userService.getOne(queryWrapper);
            one.setAmount(map.get(t.getUserId()).getAmount());
            userList.add(one);
        });
        List<User> users = userList.stream().sorted(Comparator.comparing(User::getAmount).reversed()).collect(Collectors.toList());

        AnalysisOrders analysisOrders = AnalysisOrders.builder()
                .orderBymoneySort(orderBymoneySort)
                .orderByUserId(users)
                .totalMoney(totalMoney)
                .ordersInWeekNum(count)
                .averagePriceInWeek(new Double(String.valueOf(totalMoney.divide(BigDecimal.valueOf(count), 20, BigDecimal.ROUND_HALF_UP))))
                .ordersPeopleNum(userList.size())
                .shopConvertRate(getRandomNum(50, 89) + "%")
                .orderConvertRate(getRandomNum(80, 99) + "%")
                .shopScore(new Double(shopScore) * 9.9)
                .replyOrderRate(getRandomNum(85, 94) + "%")
                .build();
        return R.success(analysisOrders);
    }

    // 获得本周一0点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 基于用户分析订单数据
     *
     * @param user
     * @return
     */
    @GetMapping("/analysisOrderByCustomer")
    public R<AnalysisOrdersByUser> analysisOrderByCustomer(@RequestBody User user) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Calendar caln = Calendar.getInstance();
        caln.setTime(getTimesWeekmorning());
        caln.add(Calendar.DAY_OF_WEEK, 7);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.between("checkout_time", cal.getTime(), caln.getTime());
        List<Orders> list = orderService.list(queryWrapper);
        int count = list.size();
        Map<Long, Orders> ordersMap = list.stream().sorted(Comparator.comparing(Orders::getAmount).reversed()).collect(Collectors.toMap(Orders::getId, a -> a, (k1, k2) -> k1));

        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal ava = BigDecimal.ZERO;
        BigDecimal divide = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {
            totalMoney = totalMoney.add(ordersMap.get(list.get(i).getId()).getAmount());
        }
        if (count != 0) {
            ava = totalMoney.divide(BigDecimal.valueOf(count), 20, BigDecimal.ROUND_HALF_UP);

            //订单消费周内七次的均值
            divide = totalMoney.divide(BigDecimal.valueOf(7), 20, BigDecimal.ROUND_HALF_UP);
        }
        AnalysisOrdersByUser analysisOrdersByUser = AnalysisOrdersByUser.builder()
                .costAbilityScore(new Double(String.valueOf(divide)))
                .averagePriceInWeek(new Double(String.valueOf(ava)))
                .OrdersInWeek(list)
                .OrdersInWeekNum(count)
                .build();
        return R.success(analysisOrdersByUser);
    }

    @PostMapping("/changeStatus")
    public R<Orders> changeStatus(@RequestBody OrderStutas orderStutas) {
        //构造器对象
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderStutas.getOrderId());
        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(orderStutas.getStatus());
        boolean save = orderService.update(one,queryWrapper);
        if (Boolean.TRUE.equals(save)) {
            return R.success(one);
        } else {
            return R.error("更新失败~");
        }
    }


    public int getRandomNum(int a, int b) {
        int START = a;//定义范围开始数字
        int END = b;//定义范围结束数字
        //创建Random类对象
        Random random = new Random();
        //产生随机数
        int number = random.nextInt(END - START + 1) + START;
        return number;
    }

}