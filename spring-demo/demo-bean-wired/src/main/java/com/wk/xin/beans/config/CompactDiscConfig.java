package com.wk.xin.beans.config;

import com.wk.xin.beans.sample.CompactDisc;
import com.wk.xin.beans.sample.SgtPeppers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompactDiscConfig {

    @Bean
    public CompactDisc compactDisc() {
        return new SgtPeppers();
    }

}
