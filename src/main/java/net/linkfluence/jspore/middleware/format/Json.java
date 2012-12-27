/*
 */
package net.linkfluence.jspore.middleware.format;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import net.linkfluence.jspore.Method;
import net.linkfluence.jspore.SporeException;
import net.linkfluence.jspore.middleware.Middleware;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Format request/result payload as JSON.
 * JSON parsing is done using Jackson's ObjectMapper in a Tree model.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class Json extends Middleware {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void sendRequest(RequestBuilder request, Object body, Method context) throws SporeException {
        request.setHeader("Content-Type", "application/json");
        String content = null;
        try {
             if(body != null){
                // If we have already a JSon string, we don't have to encode it in JSon !
                if (body instanceof String) {
                    content = (String) body;
                } else {
                    content = mapper.writeValueAsString(body);
                }
                request.setBody(content);
             }
        } catch (Exception ex) {
            throw new SporeException(ex.getMessage());
        }
        next(request, body, context);
    }

    @Override
    public Object receiveRequest(Response response, Object body, Method context) throws SporeException {
        Object tree;
        try {
            tree = mapper.readTree(body.toString());
        } catch (Exception ex){
            // When failing, we can assume that the content is not a JSon document but a String
            tree = body.toString();
        }
        return next(response, tree, context);
    }
    
    @Override
    public String getName() {
        return Json.class.getName();
    }
}
