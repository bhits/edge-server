package gov.samhsa.c2s.edgeserver;

import gov.samhsa.c2s.edgeserver.config.EdgeServerProperties;
import gov.samhsa.c2s.edgeserver.filters.post.BasicAuthenticationEntryPointFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableZuulProxy
@Controller
public class EdgeServerApplication {

    @Autowired
    private EdgeServerProperties edgeServerProperties;

    public static void main(String[] args) {
        SpringApplication.run(EdgeServerApplication.class, args);
    }

    @RequestMapping("/")
    public String rootRedirection() {
        return edgeServerProperties.getRootRedirectUrl();
    }

    @Bean
    public BasicAuthenticationEntryPointFilter basicAuthenticationEntryPointFilter() {
        return new BasicAuthenticationEntryPointFilter();
    }
}