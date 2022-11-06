package com.eltorofuerte.ioc.v3;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

// 项目启动时，初始化所有的bean到Map容器中，每个类只有一个实例对象
public class BeanFactory {
    /**
     * 读取资源文件
     */
    private static ResourceBundle beanStrings = ResourceBundle.getBundle("bean");

    /**
     * 定义一个容器
     */
    private static Map<String, Object> beans = new HashMap<>();

    static {
        try {
            Enumeration<String> keys = beanStrings.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String beanPath = beanStrings.getString(key);
                Object bean = Class.forName(beanPath).newInstance();
                beans.put(key, bean);
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError("创建容器失败，程序停止执行");
        }
    }

    public static Object getBean(String beanName) {
        return beans.get(beanName);
    }
}
