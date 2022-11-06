package com.eltorofuerte.wired.xml.test;

import com.eltorofuerte.beans.sample.CDPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author xinyu.zhang
 * @since 2022/11/6 14:59
 */
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
