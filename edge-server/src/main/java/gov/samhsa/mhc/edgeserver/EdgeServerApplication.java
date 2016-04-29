package gov.samhsa.mhc.edgeserver;

import gov.samhsa.mhc.edgeserver.filters.post.BasicAuthenticationEntryPointFilter;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mhc.edge-server.root-redirect-url}")
    private String rootRedirectUrl;

    @RequestMapping("/")
    public String rootRedirection() {
        return rootRedirectUrl;
    }

    public static void main(String[] args) {
        SpringApplication.run(EdgeServerApplication.class, args);
    }

    @Bean
    public BasicAuthenticationEntryPointFilter basicAuthenticationEntryPointFilter(){
        return  new BasicAuthenticationEntryPointFilter();
    }
}
