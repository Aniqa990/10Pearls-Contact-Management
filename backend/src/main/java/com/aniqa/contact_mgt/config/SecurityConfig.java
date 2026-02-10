package com.aniqa.contact_mgt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    //This tells Spring:

    //“Whenever anyone asks for a PasswordEncoder,
    //give them this exact object.”

    //Without this:

    //Spring has no idea which encoder to use

    //There is no default PasswordEncoder
    //Injection fails

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
