package gov.samhsa.c2s.edgeserver.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "c2s.edge-server")
@Validated
public class EdgeServerProperties {
    /**
     * The path to redirect to when no path is specified in the URL.
     */
    @NotBlank
    private String rootRedirectUrl;

    /**
     * Whether Trace information is supposed to be included in the response or not.
     * Should be enabled for debugging purposes only for security reasons.
     */
    private boolean exposeTraceHeadersInResponse = false;
}
