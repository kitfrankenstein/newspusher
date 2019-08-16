package com.kitfrankenstein.newspusher.dao;

import com.kitfrankenstein.newspusher.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kit
 * @date:  2019/8/1 22:22
 */
@Mapper
public interface UserDao {

    User getUserByPhone(String phoneNumber);

    User getUser(User user);

    int saveUser(User user);

}
