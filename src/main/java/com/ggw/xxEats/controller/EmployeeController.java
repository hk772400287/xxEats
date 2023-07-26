package com.ggw.xxEats.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.xxEats.common.R;
import com.ggw.xxEats.entity.Employee;
import com.ggw.xxEats.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee empDB = employeeService.getOne(lambdaQueryWrapper);
        if (empDB == null) {
            return R.error("Login failed");
        }
        if (!empDB.getPassword().equals(password)) {
            return R.error("Password is not correct");
        }
        if (empDB.getStatus() == 0) {
            return R.error("Invalid account");
        }
        request.getSession().setAttribute("employee", empDB.getId());
        return R.success(empDB);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("Logged out successfully");
    }

    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long creatorId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(creatorId);
        employee.setUpdateUser(creatorId);
        employeeService.save(employee);
        return R.success("Added new employee successfully");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        return null;
    }
}
