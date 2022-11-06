# demo-ioc-mock

> 模拟spring ioc容器，项目启动时读取properties文件中的类名通过反射初始化对象，存入Map进行管理

## BeanFactory

```java
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
```

## UserService

```java
public class UserService {
    private UserDao userDaoImpl = (UserDao)BeanFactory.getBean("UserDaoImpl");

    public void learn() {
        System.out.println("v3 class type: " + userDaoImpl.getClass());
        userDaoImpl.learn();
    }
}
```

## bean.properties

```properties
UserDaoImpl=com.eltorofuerte.ioc.v1.dao.UserDaoImpl
```