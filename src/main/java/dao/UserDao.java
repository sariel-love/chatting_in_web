package dao;

import entity.LoginUser;
import entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    public LoginUser FindUser(@Param("phone_number") String phone_number, @Param("password") String password);
}
