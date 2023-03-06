package com.wk.xin.beans.config;

import com.wk.xin.beans.sample.CDPlayer;
import com.wk.xin.beans.sample.CompactDisc;
import com.wk.xin.beans.sample.MediaPlayer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@ImportResource("classpath:cd-config.xml")
@Import(value = {CompactDiscConfig.class})
public class MediaPlayerConfig {

    @Bean
    public MediaPlayer mediaPlayer(CompactDisc compactDisc) {
        return new CDPlayer(compactDisc);
    }

}