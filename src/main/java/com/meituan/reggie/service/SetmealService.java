package com.meituan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meituan.reggie.dto.SetmealDto;
import com.meituan.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增优惠券，同时需要保存优惠券和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除优惠券，同时需要删除优惠券和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
