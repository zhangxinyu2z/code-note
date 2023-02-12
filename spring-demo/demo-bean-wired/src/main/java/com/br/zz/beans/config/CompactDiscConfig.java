package com.br.zz.beans.config;

import com.br.zz.beans.sample.CompactDisc;
import com.br.zz.beans.sample.SgtPeppers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompactDiscConfig {

    @Bean
    public CompactDisc compactDisc() {
        return new SgtPeppers();
    }

}
