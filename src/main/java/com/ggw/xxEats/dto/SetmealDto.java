package com.ggw.xxEats.dto;

import com.ggw.xxEats.entity.Setmeal;
import com.ggw.xxEats.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
