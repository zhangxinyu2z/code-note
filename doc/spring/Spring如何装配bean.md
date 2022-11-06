[toc]

# 理解概念

IOC：对象的初始化到销毁，对象之间的依赖关系全部交给Spring容器管理

DI（依赖注入）是一种实现控制反转（IOC）且解决依赖性问题的设计模式，自动装配（@autoWired）是一种实现它的机制。

怎么理解呢？在xml中配置一个bean，配置它的属性字段对应的值或者引用，Spring根据这个配置来构建bean，并把这个bean的字段或者依赖的对象注入进去。

# 依赖注入

Spring属性注入有多种方式[^1]，演示构造方法注入、setter注入和实例工厂的方法注入

**People.java**

```java
@Setter@Getter
public class People {
    private String name;

    private int age;

    private Heart heart;

    private String[] hobbies;

    private List<String> loveActress;

    private Map<String,String> loveStarCode;

    private Properties properties;

    public People() {
    }

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public People(String name, int age, Heart heart) {
        this.name = name;
        this.age = age;
        this.heart = heart;
    }

    public People(String name, int age, Heart heart, String[] hobbies, List<String> loveActress, Map<String, String> loveStarCode, Properties properties) {
        this.name = name;
        this.age = age;
        this.heart = heart;
        this.hobbies = hobbies;
        this.loveActress = loveActress;
        this.loveStarCode = loveStarCode;
        this.properties = properties;
    }
}
```

**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:util="http://www.springframework.org/schema/util"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd">

        <!-- bean definitions here -->

</beans>
```

## 构造方法注入

空构造方法：默认id com.eltorofuerte.beans.po.People#0

```xml
<bean class="com.eltorofuerte.beans.po.People"/>
```

构造方法name属性注入：

```xml
<bean class="com.eltorofuerte.beans.po.Heart"/>
<!--constructor 2 -->
<bean id="p2" class="com.eltorofuerte.beans.po.People">
    <constructor-arg name="name" value="El Toro Fuerte"/>
    <constructor-arg name="age" value="27"/>
    <constructor-arg name="heart" ref="com.eltorofuerte.beans.po.Heart#0"/>
    <constructor-arg name="hobbies">
        <null/>
    </constructor-arg>
    <constructor-arg name="loveActress">
        <list><!--set可以去重，取出无序-->
            <value>Lin Qinxia</value>
            <value>Qiu Suzhen</value>
            <value>Wang Zuxian</value>
        </list>
    </constructor-arg>
    <constructor-arg name="loveStarCode">
        <map>
            <entry key="9527" value="Hua An"/>
            <entry key="007" value="James Bond"/>
        </map>
    </constructor-arg>
    <constructor-arg name="properties">
        <props>
            <prop key="driverclass">com.mysql.jdbc.Driver</prop>
            <prop key="username">root</prop>
        </props>
    </constructor-arg>
</bean>
```

构造方法index属性注入：

```xml
<!--constructor 3 -->
<bean id="p3" class="com.eltorofuerte.beans.po.People">
    <constructor-arg index="0" value="El Toro Fuerte"/>
    <constructor-arg index="1" value="27"/>
</bean>
```

c命名空间注入：

```xml
<!-- constructor 4 c命名空间 -->
    <!-- 如果只有一个属性是引用类型，可以用 c:_-ref=     -->
    <bean id="p4" class="com.eltorofuerte.beans.po.People" c:name="El Toro Fuerte" c:_1="27" c:_2-ref="com.eltorofuerte.beans.po.Heart#0"/>
```

## setter方式注入

name属性输入：

```xml
<bean id="p1_s" class="com.eltorofuerte.beans.po.People">
    <property name="name" value="El Toro Fuerte"/>
    <property name="age" value="27"/>
    <property name="heart" ref="h2"/>

    <!--array-->
    <property name="hobbies">
        <list><!--set可以去重，取出无序-->
            <value>play game</value>
            <value>read book</value>
        </list>
    </property>
    <!--list-->
    <property name="loveActress">
        <list>
            <value>Lin Qinxia</value>
            <value>Qiu Suzhen</value>
            <value>Wang Zuxian</value>
        </list>
    </property>
    <!--map-->
    <property name="loveStarCode">
        <map>
            <entry key="9527" value="Hua An"/>
            <entry key="007" value="James Bond"/>
        </map>
    </property>
    <!--properties-->
    <property name="properties">
        <props>
            <prop key="driverclass">com.mysql.jdbc.Driver</prop>
            <prop key="username">root</prop>
        </props>
    </property>
</bean>
```

p命名标签注入：

```xml
<bean id="p2_s" class="com.eltorofuerte.beans.po.People" p:hobbies-ref="hobbies" p:name="El Toro Fuerte"/>

<util:list id="hobbies">
    <value>play game</value>
    <value>read book</value>
</util:list>
```

## Instance Factory method

```xml
<!--Instance Factory-->
<bean id="instanceFactory" class="com.eltorofuerte.beans.factory.BeanFactory"/>
<bean id="p1_f" factory-bean="instanceFactory" factory-method="getPeople">
    <property name="name" value="El Toro Fuerte"/>
    <property name="age" value="27"/>
</bean>

<!--Static Factory-->
<bean id="p2_f" class="com.eltorofuerte.beans.factory.BeanFactory" factory-method="getPeopleStatic">
    <property name="name" value="El Toro Fuerte"/>
    <property name="age" value="27"/>
</bean>
```

# 自动装配

Spring从两个角度来实现自动化装配：

* 组件扫描(component scanning)：Spring会自动发现应用上下文中需要创建的bean。
* 自动装配(autowiring)：Spring会自动满足bean之间的依赖。

用一个媒体播放器和碟片的例子[^copy][^2][^3]说明：播放器和碟片的关系说明：如果你不将CD插入(注入)到CD播放器中，那么CD播放器其实是没有太大用处的。所以，可以这样说，CD播放器依赖于CD才能完成它的使命。

类的关系说明：
* MediaPlayer：媒体播放器接口
* CDPlayer：媒体播放器的扩展实现
* CompactDisc：碟片接口
* BlankDisc：一张空光盘，可以
* SgtPepper：一款摇滚专辑碟片

## 使用xml配置结合注解

使用`@Component`标识，告知Spring创建该类对象注入到容器中

使用`@Autowired`注解标识，实现自动装配。可以在类的构造器、setter、属性字段、其它任意方法上使用，Spring容器会根据类型去容器中查找对应的bean注入到字段中。属性required=false：没有bean不会注入，解决缺少bean无法启动容器问题，但是会有NPM的情况

在xml中使用组件扫描器指定package，告知Spring扫描哪些包下带有`@Component`等注解的bean进行管理

SgtPeppers
```java
@Component
public class SgtPeppers implements CompactDisc {

  private String title = "Sgt. Pepper's Lonely Hearts Club Band";  
  private String artist = "The Beatles";
  
  @Override
  public void play() {
    System.out.println("Playing " + title + " by " + artist);
  }

}
```

CDPlayer
```java
/**
 * cd播放器
 */
@Component
public class CDPlayer implements MediaPlayer {

    private CompactDisc cd;

    public CDPlayer() {
    }

    @Autowired
    public CDPlayer(CompactDisc cd) {
        this.cd = cd;
    }

    @Override
    public void play() {
        cd.play();
    }
}
```

applicationContext-root.xml

```xml
<context:component-scan base-package="com.eltorofuerte.beans.sample"/>
```

SpringApplicationAutoWiredTest

> @RunWith(SpringJUnit4ClassRunner.class)在测试开始的时候自动创建Spring的应用上下文  
> @ContextConfiguration(locations = {"classpath:applicationContext-root.xml"})告诉Spring加载配置文件

```java
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-root.xml"})
public class SpringApplicationAutoWiredTest {
    @Autowired
    private MediaPlayer mediaPlayer;

    @Test
    public void testAutoWiredUseXml() {
        System.out.println(mediaPlayer.getClass());
        mediaPlayer.play();
    }
}
```

## 使用code装配

使用`@ComponentScan`注解扫描标识有`@Component`...的类，会创建这些类的bean注册到Spring容器中，使用`@AutoWired`注解处理bean之间的依赖

使用`@Configuration`注解标识该类是一个配置类，然后使用`@Bean`注解配置bean，bean的id是方法的名称，被`@ComponetnScan`扫描后，注入到Spring容器中

示例参考项目代码

# import

## by xml

在xml中可以通过import标签，引入其他配置文件 

cd-config.xml

```xml
<bean id="compactDisc"
    class="com.eltorofuerte.beans.sample.BlankDisc"
    c:_0="Sgt. Pepper's Lonely Hearts Club Band"
    c:_1="The Beatles">
<constructor-arg>
  <list>
    <value>Sgt. Pepper's Lonely Hearts Club Band</value>
    <value>With a Little Help from My Friends</value>
    <value>Lucy in the Sky with Diamonds</value>
    <value>Getting Better</value>
    <value>Fixing a Hole</value>
    <!-- ...other tracks omitted for brevity... -->
  </list>
</constructor-arg>
</bean>
```

cdplayer-config.xml

```xml
<import resource="cd-config.xml"/>
<bean id="cdPlayer" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
```

SpringApplicationImportConfigTest.java

```java
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-root.xml"})
public class SpringApplicationImportConfigTest {
    @Autowired
    private MediaPlayer mediaPlayer;

    @Test
    public void testAutoWiredUseXml() {
        System.out.println(mediaPlayer.getClass());
        mediaPlayer.play();
    }
}
```

## by code

使用@Import注解引入其他@Configuration的配置，使用@ImportResource配置引入xml中的配置

# 自动装配冲突[^4]

cdplayer-config-2.xml

```xml
<import resource="cd-config.xml"/>

<bean id="cdPlayer" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
<bean class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
<bean id="CdPlayer" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
<bean id="cdPlayer2" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
```

SpringApplicationBeanAmbiguityTest.java

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cdplayer-config-2.xml"})
public class SpringApplicationBeanAmbiguityTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CDPlayer cdPlayer;

    @Test
    public void tt() {
        System.out.println("==============");
    }
}
```

经过测试，applicationContext的beanFactory中的beanDefinitionMap，此时有4个cdPlayer类型的bean，id都不相同，因为id是唯一的，`@AutoWired`是byType注入，有多个相同type的bean，就byName注入，如果是下面这种

```xml
<bean id="CdPlayer" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
<bean id="cdPlayer2" class="com.eltorofuerte.beans.sample.CDPlayer" c:cd-ref="compactDisc" />
```

此时，Spring就无法区分该注入哪个bean

有两种解决方式：
1. 使用`@Qualifier`指定注入哪个bean
2. 使用`@Primary`注解，如果有歧义的bean，标识首选的位置，标识在`@Component`或者`@Bean`注解的方法上。


# Reference

[案例代码](../../spring-demo/demo-bean-wired)
[^1]:[SpringDoc-Dependency Injection](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies)
[^2]:[Spring In Action 4 Zh - Wiring Beans](https://potoyang.gitbook.io/spring-in-action-v4/di-2-zhang-zhuang-pei-bean)
[^3]:[Spring In Action 4 En](https://www.manning.com/books/spring-in-action-fourth-edition)
[^copy]:[copy](https://juejin.cn/post/6844903912642707464)
[^4]:[Spring In Action 4 Zh - Addressing ambiguity in autowiring](https://potoyang.gitbook.io/spring-in-action-v4/di-3-zhang-gao-ji-zhuang-pei/untitled-3)
