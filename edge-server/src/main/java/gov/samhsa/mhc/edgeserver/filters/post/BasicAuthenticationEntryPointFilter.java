package gov.samhsa.mhc.edgeserver.filters.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import com.netflix.util.Pair;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by tomson.ngassa on 4/28/2016.
 */

public class BasicAuthenticationEntryPointFilter extends ZuulFilter {
    /*
    The WWW-Authenticate key which
    value will be replaced.
    */
    @Value("${response.header.key}")
    private String AUTHENTICATE_KEY;
    /*
    The new value to be set for the WWW-Authenticate key.
    */
    @Value("${response.header.value}")
    private String AUTHENTICATE_VALUE;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        List<Pair<String,String>> currentResponseHeaders = getCurrentResponseHeader();
        for(Pair<String,String> item : currentResponseHeaders){
            if(item.first().equals(AUTHENTICATE_KEY)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() {
        List<Pair<String,String>> currentResponseHeaders = getCurrentResponseHeader();
        for(Pair<String,String> item : currentResponseHeaders){
            if(item.first().equals(AUTHENTICATE_KEY)){
                item.setSecond(AUTHENTICATE_VALUE);
            }
        }
        return null;
    }

    private List<Pair<String,String>> getCurrentResponseHeader(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getZuulResponseHeaders();
    }
}
