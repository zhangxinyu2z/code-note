package com.eltorofuerte.beans.config;

import com.eltorofuerte.beans.sample.CompactDisc;
import com.eltorofuerte.beans.sample.SgtPeppers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompactDiscConfig {

    @Bean
    public CompactDisc compactDisc() {
        return new SgtPeppers();
    }

}
