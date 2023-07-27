package com.ggw.xxEats.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.xxEats.common.R;
import com.ggw.xxEats.entity.Employee;
import com.ggw.xxEats.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
        employeeService.save(employee);
        return R.success("Added new employee successfully");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
        employeeService.updateById(employee);
        return R.success("Updated successfully");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("No employee information");
    }
}
