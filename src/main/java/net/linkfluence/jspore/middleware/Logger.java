/*
 */
package net.linkfluence.jspore.middleware;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import net.linkfluence.jspore.Method;
import net.linkfluence.jspore.SporeException;
import org.slf4j.LoggerFactory;


/**
 * Log message sent and received.
 * Spore.Builder.setDebug use this middleware as the last element of the processing
 * chain.
 * 
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class Logger extends Middleware {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Logger.class);
    
    @Override
    public void sendRequest(RequestBuilder request, Object body, Method context) throws SporeException {
        LOG.debug("request path: {}",request.build().getUrl());
        if(body != null){
            LOG.debug("request body: {}", body.toString());
        } else {
            LOG.debug("no request body provided");
        }
        next(request, body, context);
    }

    @Override
    public Object receiveRequest(Response response, Object body, Method context) throws SporeException {
        if(body != null){
            LOG.debug("receive data: {}", body.toString());
        } else {
            LOG.debug("receive response, no body");
        }
        return next(response, body, context);
    }
    
}
