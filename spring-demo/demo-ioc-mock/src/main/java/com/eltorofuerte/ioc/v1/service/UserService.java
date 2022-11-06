package com.eltorofuerte.ioc.v1.service;

import com.eltorofuerte.ioc.v1.dao.UserDao;
import com.eltorofuerte.ioc.v1.dao.UserDaoImpl;

/**
 * @author Arnoer
 * @since 2022/9/27 11:03
 */
// demo 不做接口扩展  思考：耦合性太强，dao只是一个操作数据库的接口，如果dao有其他业务接口呢？需要修改dao的实现
public class UserService {
    // service直接依赖dao
    private UserDao userDao = new UserDaoImpl();

    public void learn() {
        userDao.learn();
    }

}
