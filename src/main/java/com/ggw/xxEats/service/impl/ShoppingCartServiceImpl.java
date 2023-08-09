package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.entity.ShoppingCart;
import com.ggw.xxEats.mapper.ShoppingCartMapper;
import com.ggw.xxEats.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
