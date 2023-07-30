package com.ggw.xxEats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.xxEats.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
