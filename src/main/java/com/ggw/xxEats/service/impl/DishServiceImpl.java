package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.entity.Dish;
import com.ggw.xxEats.mapper.DishMapper;
import com.ggw.xxEats.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
