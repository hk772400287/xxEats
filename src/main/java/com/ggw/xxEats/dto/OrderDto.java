package com.ggw.xxEats.dto;

import com.ggw.xxEats.entity.OrderDetail;
import com.ggw.xxEats.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;

    private Integer sumNum;
}
