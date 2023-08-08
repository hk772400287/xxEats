package com.ggw.xxEats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.xxEats.dto.SetmealDto;
import com.ggw.xxEats.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> id);

    SetmealDto getWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
