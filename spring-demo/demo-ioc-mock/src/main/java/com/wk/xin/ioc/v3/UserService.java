package com.wk.xin.ioc.v3;

import com.wk.xin.ioc.v1.dao.UserDao;

/**
 * @author xinyu.zhang
 * @since 2022/11/3 18:09
 */
public class UserService {
    private UserDao userDaoImpl = (UserDao)BeanFactory.getBean("UserDaoImpl");

    public void learn() {
        System.out.println("v3 class type: " + userDaoImpl.getClass());
        userDaoImpl.learn();
    }

}
