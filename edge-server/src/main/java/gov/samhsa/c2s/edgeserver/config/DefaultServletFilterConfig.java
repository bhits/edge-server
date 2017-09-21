package gov.samhsa.c2s.edgeserver.config;

import gov.samhsa.c2s.edgeserver.filter.servlet.TraceHeaderCleanerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultServletFilterConfig {

    @Bean
    public TraceHeaderCleanerRequestFilter traceHeaderCleanerFilter() {
        return new TraceHeaderCleanerRequestFilter();
    }
}
