package com.wk.xin.beans.factory;

import com.wk.xin.beans.po.People;

/**
 * @author xinyu.zhang
 * @since 2022/11/4 14:53
 */
public class BeanFactory {

    public People getPeople() {
        return new People();
    }

    public static People getPeopleStatic() {
        return new People();
    }
}
