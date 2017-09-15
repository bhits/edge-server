package gov.samhsa.c2s.edgeserver.config;

import gov.samhsa.c2s.edgeserver.filter.servlet.HttpResponseInjectingTraceFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.instrument.web.ZipkinHttpSpanInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "c2s.edge-server.expose-trace-headers-in-response", havingValue = "true")
public class ExposeTraceHeadersInResponseFilterConfig {
    @Bean
    public HttpSpanInjector customHttpServletResponseSpanInjector() {
        return new CustomHttpServletResponseSpanInjector();
    }

    @Bean
    public HttpResponseInjectingTraceFilter responseInjectingTraceFilter(Tracer tracer) {
        return new HttpResponseInjectingTraceFilter(tracer, customHttpServletResponseSpanInjector());
    }

    static class CustomHttpServletResponseSpanInjector extends ZipkinHttpSpanInjector {
        @Override
        public void inject(Span span, SpanTextMap carrier) {
            super.inject(span, carrier);
            carrier.put(Span.TRACE_ID_NAME, span.traceIdString());
            carrier.put(Span.SPAN_ID_NAME, Span.idToHex(span.getSpanId()));
        }
    }
}
