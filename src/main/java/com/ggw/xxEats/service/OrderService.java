package com.ggw.xxEats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.xxEats.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
