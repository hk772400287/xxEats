package com.ggw.xxEats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.xxEats.dto.DishDto;
import com.ggw.xxEats.entity.Dish;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getWithFlavorById(Long dishId);

    void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(Long ids);

    void stopSelling(Long dishId);

    void startSelling(Long dishId);
}
