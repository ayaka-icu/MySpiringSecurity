package com.ganga.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ganga.domin.User;
import com.ganga.mapper.UserMapper;
import com.ganga.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
}
