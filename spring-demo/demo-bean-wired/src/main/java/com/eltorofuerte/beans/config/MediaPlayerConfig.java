package com.eltorofuerte.beans.config;

import com.eltorofuerte.beans.sample.*;
import org.springframework.context.annotation.*;

@Configuration
//@ImportResource("classpath:cd-config.xml")
@Import(value = {CompactDiscConfig.class})
public class MediaPlayerConfig {

    @Bean
    public MediaPlayer mediaPlayer(CompactDisc compactDisc) {
        return new CDPlayer(compactDisc);
    }

}