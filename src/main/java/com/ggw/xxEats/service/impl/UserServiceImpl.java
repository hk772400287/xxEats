package com.ggw.xxEats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.xxEats.entity.User;
import com.ggw.xxEats.mapper.UserMapper;
import com.ggw.xxEats.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
