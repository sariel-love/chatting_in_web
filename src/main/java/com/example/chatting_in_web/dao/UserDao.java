package com.example.chatting_in_web.dao;

import com.example.chatting_in_web.entity.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    LoginUser findUser(@Param("phone_number") String phone_number, @Param("password") String password);
}
