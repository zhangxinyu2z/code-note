package com.br.zz.wired.xml.test;

import com.br.zz.beans.po.People;
import com.br.zz.beans.sample.CDPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author xinyu.zhang
 * @since 2022/11/4 10:41
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SpringApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("com.eltorofuerte.beans.po.People#0")
    private People people;

    @Autowired
    @Qualifier("p2")
    private People p2;

    @Autowired
    @Qualifier("p4")
    private People p4;

    @Autowired
    @Qualifier("p1_s")
    private People p1s;

    @Autowired
    @Qualifier("p2_s")
    private People p2s;

    @Autowired
    @Qualifier("p1_f")
    private People p1f;

    @Autowired
    @Qualifier("p2_f")
    private People p2f;

    @Test
    public void testWired() {
        System.out.println(p2);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(p4);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(p1s);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(p2s);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(p1f);
        System.out.println("============================");
        System.out.println("============================");
        System.out.println(p2f);
    }

    @Test
    public void testWiredP3() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        People p3 = (People)context.getBean("p3");
        System.out.println(p3);
    }

    @Autowired
    private CDPlayer cdPlayer;

    @Test
    public void testAutoWired() {
        cdPlayer.play();
    }
}
