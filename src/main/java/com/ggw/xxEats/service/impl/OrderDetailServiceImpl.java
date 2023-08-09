package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.entity.OrderDetail;
import com.ggw.xxEats.mapper.OrderDetailMapper;
import com.ggw.xxEats.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
