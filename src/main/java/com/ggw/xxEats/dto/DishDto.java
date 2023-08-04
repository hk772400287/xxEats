package com.ggw.xxEats.dto;

import com.ggw.xxEats.entity.Dish;
import com.ggw.xxEats.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
