package com.ggw.xxEats.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.xxEats.common.R;
import com.ggw.xxEats.dto.SetmealDto;
import com.ggw.xxEats.entity.Category;
import com.ggw.xxEats.entity.Setmeal;
import com.ggw.xxEats.service.CategoryService;
import com.ggw.xxEats.service.SetmealDishService;
import com.ggw.xxEats.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @CacheEvict(value = "setMealCache", allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("New Setmeal info saved successfully");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, lambdaQueryWrapper);
        Page<SetmealDto> pageInfoDto = new Page<>();
        BeanUtils.copyProperties(pageInfo, pageInfoDto, "records");
        List<Setmeal> setmeals = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = setmeals.stream().map((setmeal) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        pageInfoDto.setRecords(setmealDtoList);
        return R.success(pageInfoDto);
    }

    @CacheEvict(value = "setMealCache", allEntries = true)
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("Deleted successfully");
    }

    @PostMapping("/status/0")
    public R<String> stopSelling(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Setmeal::getStatus, 0).in(Setmeal::getId, ids);
        setmealService.update(lambdaUpdateWrapper);
        return R.success("Stop selling successfully");
    }

    @PostMapping("/status/1")
    public R<String> startSelling(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Setmeal::getStatus, 1).in(Setmeal::getId, ids);
        setmealService.update(lambdaUpdateWrapper);
        return R.success("Started selling successfully");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getOne(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getWithDish(id);
        return R.success(setmealDto);
    }

    @CacheEvict(value = "setMealCache", allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("Setmeal info updated successfully");
    }

    @Cacheable(value = "setMealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId()).
                eq(Setmeal::getStatus, setmeal.getStatus()).orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(wrapper);
        return R.success(setmealList);
    }
}
