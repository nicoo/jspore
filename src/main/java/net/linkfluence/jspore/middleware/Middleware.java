/*
 */
package net.linkfluence.jspore.middleware;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import net.linkfluence.jspore.Method;
import net.linkfluence.jspore.SporeException;

/**
 *
 * @author nicolas yzet <nyzet@linkfluence.net>
 */
public abstract class Middleware {
    
    private Middleware next = null;
    private Middleware prev = null;
    
    /**
     * return middleware name
     * 
     * @return middleware name.
     */
    public abstract String getName();
    
    /**
     * Apply before doing the actual request
     * sendRequest can read/change the content of the request data.
     */
    public abstract void sendRequest(RequestBuilder request, Object body, Method context) throws SporeException;
    
    /**
     * apply on result data
     */
    public abstract Object receiveRequest(Response response, Object body, Method context) throws SporeException;
    
    /**
     * add the next middleware in the chain of responsability.
     * @param next
     * @return next middleware in the chain a.k.a the next param
     */
    public Middleware setNext(Middleware next){
        this.next = next;
        return next;
    }
    
    public Middleware setPrev(Middleware prev){
        this.prev = prev;
        return prev;
    }
    
    public Middleware getPrev(){
        return this.prev;
    }
    
    public Middleware getNext(){
        return this.next;
    }
    
    protected void next(RequestBuilder request, Object body, Method context) throws SporeException
    {
        if(next != null){
            next.sendRequest(request, body, context);
        }
    }
    
    protected Object next(Response response, Object body, Method context) throws SporeException{
        if(prev != null){
            return prev.receiveRequest(response, body, context);
        }
        return body;
    }
    
}
