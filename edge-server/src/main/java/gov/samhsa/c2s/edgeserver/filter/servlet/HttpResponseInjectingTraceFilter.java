package gov.samhsa.c2s.edgeserver.filter.servlet;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpResponseInjectingTraceFilter extends GenericFilterBean {
    private final Tracer tracer;
    private final HttpSpanInjector spanInjector;

    public HttpResponseInjectingTraceFilter(Tracer tracer, HttpSpanInjector spanInjector) {
        this.tracer = tracer;
        this.spanInjector = spanInjector;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Span currentSpan = this.tracer.getCurrentSpan();
        this.spanInjector.inject(currentSpan, new HttpServletResponseTextMap(response));
        filterChain.doFilter(request, response);
    }

    class HttpServletResponseTextMap implements SpanTextMap {
        private final HttpServletResponse delegate;

        HttpServletResponseTextMap(HttpServletResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            Map<String, String> map = new HashMap<>();
            for (String header : this.delegate.getHeaderNames()) {
                map.put(header, this.delegate.getHeader(header));
            }
            return map.entrySet().iterator();
        }

        @Override
        public void put(String key, String value) {
            this.delegate.addHeader(key, value);
        }
    }
}