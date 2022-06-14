package com.ganga.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ganga.domin.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
