package com.ggw.xxEats.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.xxEats.common.R;
import com.ggw.xxEats.dto.OrderDto;
import com.ggw.xxEats.entity.OrderDetail;
import com.ggw.xxEats.entity.Orders;
import com.ggw.xxEats.service.OrderDetailService;
import com.ggw.xxEats.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("Submitted successfully");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Page<Orders> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime);
        orderService.page(orderPage, lambdaQueryWrapper);
        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(orderPage, orderDtoPage, "records");
        List<Orders> ordersList = orderPage.getRecords();
        List<OrderDto> orderDtoList = ordersList.stream().map((order) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(wrapper);
            orderDto.setOrderDetails(orderDetailList);
            int sumNum = 0;
            for (OrderDetail orderDetail : orderDetailList) {
                sumNum += orderDetail.getNumber();
            }
            orderDto.setSumNum(sumNum);
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(orderDtoList);
        return R.success(orderDtoPage);
    }

    @GetMapping("/page")
    public R<Page> userPage(int page, int pageSize, Long number, String beginTime, String endTime) {
        log.info("Begin time : {}", beginTime);
        Page<Orders> orderPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime).like(number != null, Orders::getId, number);
        lambdaQueryWrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        orderService.page(orderPage, lambdaQueryWrapper);
        return R.success(orderPage);
    }
}
