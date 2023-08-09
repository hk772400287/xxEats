package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.dto.DishDto;
import com.ggw.xxEats.entity.Dish;
import com.ggw.xxEats.entity.DishFlavor;
import com.ggw.xxEats.mapper.DishMapper;
import com.ggw.xxEats.service.DishFlavorService;
import com.ggw.xxEats.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        dishDtoFlavors = dishDtoFlavors.stream().map((dl) -> {
            dl.setDishId(dishId);
            return dl;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDtoFlavors);
    }

    @Override
    public DishDto getWithFlavorById(Long dishId) {
        Dish dish = this.getById(dishId);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
        List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        dishDtoFlavors = dishDtoFlavors.stream().map((dl) -> {
            dl.setDishId(dishId);
            return dl;
        }).collect(Collectors.toList());
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(lambdaQueryWrapper);
        dishFlavorService.saveBatch(dishDtoFlavors);
    }

    @Override
    public void deleteWithFlavor(Long ids) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(Dish::getIsDeleted, 1);
        updateWrapper.lambda().eq(Dish::getId, ids);
        this.update(updateWrapper);
        UpdateWrapper<DishFlavor> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.lambda().set(DishFlavor::getIsDeleted, 1);
        updateWrapper2.lambda().eq(DishFlavor::getDishId, ids);
        dishFlavorService.update(updateWrapper2);
    }

    @Override
    public void stopSelling(Long dishId) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(Dish::getStatus, 0);
        updateWrapper.lambda().eq(Dish::getId, dishId);
        this.update(updateWrapper);
    }

    @Override
    public void startSelling(Long dishId) {
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(Dish::getStatus, 1);
        updateWrapper.lambda().eq(Dish::getId, dishId);
        this.update(updateWrapper);
    }

    @Override
    public List<DishDto> getListWithFlavor(Dish dish) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = this.list(lambdaQueryWrapper);
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }
}
