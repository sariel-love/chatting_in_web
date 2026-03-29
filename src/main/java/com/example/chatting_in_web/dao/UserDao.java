package com.example.chatting_in_web.dao;

import com.example.chatting_in_web.entity.LoginUser;
import com.example.chatting_in_web.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Mapper
public interface UserDao {

    List<LoginUser> findUser(@Param("phone_number") String phone_number);
    String findUsername(String phone_number);
    Integer addUser(User user);
}
