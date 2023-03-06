package com.wk.xin.ioc.v2;

import com.wk.xin.ioc.v1.dao.UserDao;

// 思考：耦合性是降低了，dao是用来操作数据的工具类，每次都要生成一个对象，是不是有些冗余了？
public class UserService {
    // 扩展性提高，耦合性降低
    private UserDao userDao = (UserDao)BeanFactory.getBean("UserDaoImpl");

    public void learn() {
        userDao.learn();
    }
}
