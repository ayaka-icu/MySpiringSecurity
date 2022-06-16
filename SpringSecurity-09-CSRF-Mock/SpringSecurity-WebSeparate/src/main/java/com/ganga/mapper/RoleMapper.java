package com.ganga.mapper;

import com.ganga.domin.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("select r.id,\n" +
            " r.name,\n" +
            " r.name_zh nameZh\n" +
            " from role r,\n" +
            " user_role ur\n" +
            " where r.id = ur.rid\n" +
            " and ur.uid = #{uid}")
    List<Role> getRolesByUid(Integer uid);

}
