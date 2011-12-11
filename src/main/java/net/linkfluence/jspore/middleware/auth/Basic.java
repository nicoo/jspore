/*
 */
package net.linkfluence.jspore.middleware.auth;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.util.Base64;
import net.linkfluence.jspore.Method;
import net.linkfluence.jspore.SporeException;
import net.linkfluence.jspore.middleware.Middleware;

/**
 * Basic http auth implementation.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class Basic extends Middleware {

    private final String b64Token;
    
    public Basic(String user, String password){
        String token = user + ":" + password;
        b64Token = Base64.encode(token.getBytes());
    }
    
    @Override
    public void sendRequest(RequestBuilder request, Object body, Method context) throws SporeException {
        if(context.authentication){
            request.setHeader("Authorization", "Basic " + b64Token);
        }
        next(request, body, context);
    }

    @Override
    public Object receiveRequest(Response response, Object body, Method context) throws SporeException {
        return next(response, body, context);
    }
    
    @Override
    public String getName() {
        return Basic.class.getName();
    }
    
}
