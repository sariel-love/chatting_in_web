package com.example.chatting_in_web.service;

import com.example.chatting_in_web.dao.UserDao;
import com.example.chatting_in_web.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TestService {
    @Autowired
    UserDao userDao;

    public void findAll() {
        List<User> users = userDao.showAll();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
