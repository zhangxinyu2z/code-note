package com.eltorofuerte.wired.xml.test;

import com.eltorofuerte.beans.sample.MediaPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author xinyu.zhang
 * @since 2022/11/4 17:54
 */
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
