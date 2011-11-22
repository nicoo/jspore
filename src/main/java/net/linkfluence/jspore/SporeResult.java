/*
 */
package net.linkfluence.jspore;

import com.google.common.base.Objects;
import com.ning.http.client.Response;

/**
 * Result class returned by any spore call.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class SporeResult<T> {
   
    public final Response response;
    public final T body;
    
    public SporeResult(Response response, T body){
        this.response = response;
        this.body = body;
    }
    
    @Override
    public String toString(){
        return Objects.toStringHelper(this)
                .add("response.getStatusCode()", response.getStatusCode())
                .add("body", body.toString())
                .toString();
    }
}
