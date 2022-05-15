package com.meituan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meituan.reggie.common.BaseContext;
import com.meituan.reggie.common.R;
import com.meituan.reggie.dto.OrderDto;
import com.meituan.reggie.entity.OrderDetail;
import com.meituan.reggie.entity.Orders;
import com.meituan.reggie.entity.ShoppingCart;
import com.meituan.reggie.entity.User;
import com.meituan.reggie.service.OrderDetailService;
import com.meituan.reggie.service.OrderService;
import com.meituan.reggie.service.ShoppingCartService;
import com.meituan.reggie.service.UserService;
import com.meituan.reggie.utils.SMSUtils;
import com.meituan.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    static HashMap<String, String> codeMap;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        codeMap = objectObjectHashMap;
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("美团外卖","",phone,code);

            //需要将生成的验证码保存到Session
            session.setAttribute(phone, code);
            codeMap.put(phone, code);
            return R.success("手机验证码短信发送成功,您得验证码是:" + code);
        }

        return R.error("短信发送失败");
    }

    /**
     * loginout
     *
     * @return
     */
    @PostMapping("/loginout")
    public R logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        request.getSession().invalidate();
        return R.success("退出成功！");
    }

    /**
     * 注册检测是否已经有存在账号，存在则提示以存在，否则成功注册！
     *
     * @param user
     * @return
     */
    @PostMapping("/checkAccount")
    public R<Boolean> checkAccount(@RequestBody User user) {
        String accountName = user.getName();
        //用QueryWrapper做条件查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(accountName)) {
            queryWrapper.eq("name", accountName);
        }
        List<User> list = userService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return R.success(Boolean.TRUE);
        }
        return R.success(Boolean.FALSE);
    }

//    /**
//     * 注册用户账号
//     *
//     * @param user
//     * @return
//     */
//    @PostMapping("/register")
//    public R<Boolean> register(@RequestBody User user) {
//        boolean result = userService.save(user);
//        return R.success(result);
//    }


    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
//        String phone = map.get("phone").toString();
        if (ObjectUtils.isNull(map.get("name")) || ObjectUtils.isNull(map.get("pwd"))) {
            return R.error("账号或密码有误，请核对后再次登录~");
        }
        //获取账号
        String accountName = map.get("name").toString();
        //获取密码
        String pwd = map.get("pwd").toString();
        //获取验证码
//        String code = map.get("code").toString();

        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
//        if (codeInSession != null && codeInSession.equals(code)) {
        //1、如果能够比对成功，说明数字验证成功

//            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getPhone, phone);

//            User user = userService.getOne(queryWrapper);
//            if (user == null) {
//                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
//                user = new User();
//                user.setPhone(phone);
//                user.setStatus(1);
//                userService.save(user);
//            }
//            session.setAttribute("user", user.getId());
        //用QueryWrapper做条件查询
        if (!ObjectUtils.isEmpty(accountName)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", accountName);
            User one = userService.getOne(queryWrapper);
            try {
                if (accountName.equals(one.getName()) && pwd.equals(one.getPassword())) {
                    session.setAttribute("user", one.getId());
                    return R.success(one);
                }
            } catch (Exception e) {
                return R.error("请确认该账户存在~");
            }
        }
//        }
        return R.error("登录失败");
    }

    /**
     * 移动端用户注册
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody Map map, HttpSession session, User user) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        String password = map.get("password").toString();
        String name = map.get("name").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）

        try {
            if (ObjectUtils.isNotNull(codeMap.get(phone)) && code.equals(codeMap.get(phone))) {
                //如果能够比对成功，说明验证码验证成功
                session.setAttribute("user", user.getId());
                user.setStatus(1);
                user.setPhone(phone);
                user.setName(name);
                user.setPassword(password);
                boolean result = userService.save(user);
                return R.success(result);
            }
        } catch (Exception e) {
            return R.error("注册失败!请获取验证码~");
        }
        return R.error("注册失败!");
    }

    //抽离的一个方法，通过订单id查询订单明细，得到一个订单明细的集合
    //这里之所以抽离出来，那是因为直接写在stream中会出现连续叠加.eq的SQL语句导致后面的数据查询不出来
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return orderDetailList;
    }

    /**
     * 用户端展示自己的订单分页查询
     *
     * @param page
     * @param pageSize
     * @return 遇到的坑：原来分页对象中的records集合存储的对象是分页泛型中的对象，里面有分页泛型对象的数据
     * 开始的时候我以为前端只传过来了分页数据，其他所有的数据都要从本地线程存储的用户id开始查询，
     * 结果就出现了一个用户id查询到 n个订单对象，然后又使用 n个订单对象又去查询 m 个订单明细对象，
     * 结果就出现了评论区老哥出现的bug(嵌套显示数据....)
     * 正确方法:直接从分页对象中获取订单id就行，问题大大简化了......
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> pageDto = new Page<>(page, pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //这里树直接把分页的全部结果查询出来，没有分页条件
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrderDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrderDto> orderDtoList = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = this.getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item, orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //使用dto的分页有点难度.....需要重点掌握
        BeanUtils.copyProperties(pageInfo, pageDto, "records");
        pageDto.setRecords(orderDtoList);
        return R.success(pageDto);
    }
    //客户端点击再来一单
    /**
     * 前端点击再来一单是直接跳转到购物车的，所以为了避免数据有问题，再跳转之前我们需要把购物车的数据给清除
     * ①通过orderId获取订单明细
     * ②把订单明细的数据的数据塞到购物车表中，不过在此之前要先把购物车表中的数据给清除(清除的是当前登录用户的购物车表中的数据)，
     * 不然就会导致再来一单的数据有问题；
     * (这样可能会影响用户体验，但是对于外卖来说，用户体验的影响不是很大，电商项目就不能这么干了)
     */
//    @PostMapping("/again")
//    public R<String> againSubmit(@RequestBody Map<String,String> map){
//        String ids = map.get("id");
//
//        long id = Long.parseLong(ids);
//
//        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrderDetail::getOrderId,id);
//        //获取该订单对应的所有的订单明细表
//        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
//
//        //通过用户id把原来的购物车给清空，这里的clean方法是视频中讲过的,建议抽取到service中,那么这里就可以直接调用了
//        shoppingCartService.clean();
//
//        //获取用户id
//        Long userId = BaseContext.getCurrentId();
//        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map((item) -> {
//            //把从order表中和order_details表中获取到的数据赋值给这个购物车对象
//            ShoppingCart shoppingCart = new ShoppingCart();
//            shoppingCart.setUserId(userId);
//            shoppingCart.setImage(item.getImage());
//            Long dishId = item.getDishId();
//            Long setmealId = item.getSetmealId();
//            if (dishId != null) {
//                //如果是菜品那就添加菜品的查询条件
//                shoppingCart.setDishId(dishId);
//            } else {
//                //添加到购物车的是套餐
//                shoppingCart.setSetmealId(setmealId);
//            }
//            shoppingCart.setName(item.getName());
//            shoppingCart.setDishFlavor(item.getDishFlavor());
//            shoppingCart.setNumber(item.getNumber());
//            shoppingCart.setAmount(item.getAmount());
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            return shoppingCart;
//        }).collect(Collectors.toList());
//
//        //把携带数据的购物车批量插入购物车表  这个批量保存的方法要使用熟练！！！
//        shoppingCartService.saveBatch(shoppingCartList);
//
//        return R.success("操作成功");
//    }
}
