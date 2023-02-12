package com.br.zz.beans.config;

import com.br.zz.beans.sample.CDPlayer;
import com.br.zz.beans.sample.CompactDisc;
import com.br.zz.beans.sample.MediaPlayer;
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