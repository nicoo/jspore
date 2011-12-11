/*
 */
package net.linkfluence.jspore.middleware;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import net.linkfluence.jspore.Method;
import net.linkfluence.jspore.SporeException;

/**
 * Thanks to async http client, redirection is made easy.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class Redirection extends Middleware {

    
    @Override
    public void sendRequest(RequestBuilder request, Object body, Method context) throws SporeException {
        request.setFollowRedirects(true);
        next(request, body, context);
    }

    @Override
    public Object receiveRequest(Response response, Object body, Method context) throws SporeException {
        return next(response, body, context);
    }

    @Override
    public String getName() {
        return Redirection.class.getName();
    }

}
