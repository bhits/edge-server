package gov.samhsa.c2s.edgeserver.web;

import gov.samhsa.c2s.edgeserver.config.EdgeServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootRedirectionController {
    @Autowired
    private EdgeServerProperties edgeServerProperties;

    @RequestMapping("/")
    public String rootRedirection() {
        return edgeServerProperties.getRootRedirectUrl();
    }
}
