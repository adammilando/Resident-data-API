package com.challangependuduk.chalangependuduk.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguraion {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
