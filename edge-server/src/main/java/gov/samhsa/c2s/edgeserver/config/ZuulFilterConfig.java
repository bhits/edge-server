package gov.samhsa.c2s.edgeserver.config;

import gov.samhsa.c2s.edgeserver.filter.zuul.post.BasicAuthenticationEntryPointFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFilterConfig {

    @Bean
    public BasicAuthenticationEntryPointFilter basicAuthenticationEntryPointFilter() {
        return new BasicAuthenticationEntryPointFilter();
    }
}
