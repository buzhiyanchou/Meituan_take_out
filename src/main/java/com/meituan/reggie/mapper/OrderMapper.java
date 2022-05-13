package com.meituan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meituan.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}