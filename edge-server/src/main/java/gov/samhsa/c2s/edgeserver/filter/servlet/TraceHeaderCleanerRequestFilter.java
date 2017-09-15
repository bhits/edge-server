package gov.samhsa.c2s.edgeserver.filter.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.instrument.web.TraceFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static java.util.stream.Collectors.toSet;

@Order(TraceHeaderCleanerRequestFilter.ORDER)
@Slf4j
public class TraceHeaderCleanerRequestFilter extends GenericFilterBean {
    public static final int ORDER = TraceFilter.ORDER - 1;
    public static final Set<String> BANNED_HEADERS = initBannedHeaders();

    private static Set<String> initBannedHeaders() {
        final Set<String> banned = new HashSet(Span.SPAN_HEADERS);
        banned.add(Span.SAMPLED_NAME);
        banned.add(Span.PROCESS_ID_NAME);
        banned.add(Span.PARENT_ID_NAME);
        banned.add(Span.TRACE_ID_NAME);
        banned.add(Span.SPAN_NAME_NAME);
        banned.add(Span.SPAN_ID_NAME);
        banned.add(Span.SPAN_EXPORT_NAME);
        banned.add(Span.SPAN_FLAGS);
        return Collections.unmodifiableSet(banned);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        filterChain.doFilter(new TraceHeaderCleanerRequestWrapper(request, BANNED_HEADERS), servletResponse);
    }

    static class TraceHeaderCleanerRequestWrapper extends HttpServletRequestWrapper {
        private final Set<String> BANNED_HEADERS;

        public TraceHeaderCleanerRequestWrapper(HttpServletRequest request, Set<String> bannedHeaders) {
            super(request);
            this.BANNED_HEADERS = bannedHeaders;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            final Enumeration<String> headerNames = super.getHeaderNames();
            final Set<String> filteredHeaders = Collections.list(headerNames).stream()
                    .filter(headerName -> {
                        final boolean filter = BANNED_HEADERS.stream().noneMatch(headerName::equalsIgnoreCase);
                        if (!filter && log.isTraceEnabled()) {
                            log.trace("filtering out header: {}", headerName);
                        }
                        return filter;
                    })
                    .collect(toSet());
            final Enumeration<String> filteredHeadersEnumeration = new Vector(filteredHeaders).elements();
            return filteredHeadersEnumeration;
        }
    }
}