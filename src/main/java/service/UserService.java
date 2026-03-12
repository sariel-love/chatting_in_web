package service;

import dao.UserDao;
import entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    UserDao UserDao;
    public LoginUser FindUser_forLogin(String phone_number,String password){
        LoginUser User_forLogin = UserDao.FindUser(phone_number,password);
        return User_forLogin;
    }
}
