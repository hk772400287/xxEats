package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.entity.Employee;
import com.ggw.xxEats.mapper.EmployeeMapper;
import com.ggw.xxEats.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
