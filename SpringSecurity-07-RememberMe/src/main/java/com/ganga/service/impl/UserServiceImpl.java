package com.ganga.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ganga.domain.User;
import com.ganga.mapper.UserMapper;
import com.ganga.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
