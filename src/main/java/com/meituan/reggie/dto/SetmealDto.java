package com.meituan.reggie.dto;

import com.meituan.reggie.entity.Setmeal;
import com.meituan.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
