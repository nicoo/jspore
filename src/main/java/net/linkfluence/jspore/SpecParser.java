/*
 */
package net.linkfluence.jspore;

import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Internal spec parser.
 * Read all the needed keys from a JSON tree.
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
class SpecParser {
    /**
     * key names in the JSON tree
     */
    private static final String NAME = "name";
    private static final String VERSION = "version";
    private static final String BASE_URL = "base_url";
    private static final String METHODS = "methods";
    
    private static final Set<String> BASE_KEYS = new ImmutableSet.Builder<String>()
            .add(VERSION)
            .add(BASE_URL)
            .add(NAME)
            .build();
    
    /**
     * Method properties
     */
    public static final String HTTP_METHOD = "method";
    public static final String PATH = "path";
    public static final String REQUIRED_PARAMS = "required_params";
    public static final String OPTIONAL_PARAMS = "optional_params";
    public static final String EXPECTED_STATUS = "expected_status";
    public static final String HEADERS = "headers";
    public static final String AUTH = "authentication";
    
    private static final Set<String> METHOD_KEYS = new ImmutableSet.Builder<String>()
            .add(HTTP_METHOD)
            .add(PATH)
            .add(REQUIRED_PARAMS)
            .add(OPTIONAL_PARAMS)
            .add(EXPECTED_STATUS)
            .add(HEADERS)
            .add(AUTH)
            .build();
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public Model parse(InputStream stream) throws IOException, InvalidSporeSpecException{
        return parse(mapper.readTree(stream));
    }
    
    public Model parse(String content) throws IOException, InvalidSporeSpecException{
        return parse(mapper.readTree(content));
    }
    
    private Model parse(JsonNode root) throws InvalidSporeSpecException, MalformedURLException{
        if(root == null){
            throw new InvalidSporeSpecException("Cannot parse input spec stream");
        }
        Model m = new Model();
        m.version = root.get(VERSION).getTextValue();
        m.baseUrl = new URL(root.get(BASE_URL).getTextValue());
        m.name = root.get(NAME).getTextValue();
        m.expectedStatus.addAll(readIntegerCollection(root.get(EXPECTED_STATUS)));

        // read all object properties to find method
        JsonNode methods = root.get(METHODS);
        Iterator<Entry<String, JsonNode>> nodeIt = methods.getFields();
        while(nodeIt.hasNext()){
            Entry<String, JsonNode> e = nodeIt.next();
            String methodName = e.getKey(); 
            JsonNode n = e.getValue();
            
            String httpMethod = n.get(HTTP_METHOD).getTextValue();
            String path = n.get(PATH).getTextValue();
            Collection<String> requiredParams = readStringCollection(n.get(REQUIRED_PARAMS));
            Collection<Integer> expectedStatuses = readIntegerCollection(n.get(EXPECTED_STATUS));
            // If no status is defined on a method level, we use the default model expected statuses
            if (expectedStatuses.isEmpty()) {
                expectedStatuses.addAll(m.expectedStatus);
            }
            Collection<String> optionalParams = readStringCollection(n.get(OPTIONAL_PARAMS));
            Map<String, String> headers = readStringMap(n.get(HEADERS));
            boolean authentication = false;
            if(n.get(AUTH) != null) {
                authentication = n.get(AUTH).asBoolean(false);
            }            
            Method method = new Method.Builder(methodName, path, httpMethod)
                .addHeaders(headers)
                .addExpectedStatuses(expectedStatuses)
                .addOptionalParams(optionalParams)
                .addRequiredParams(requiredParams)
                .setAuthentication(authentication)
                .build();
            m.addMethod(method);
        }       
        return m;
    } 
    
    /**
     * Read a json array filled with String values.
     * 
     * @param jsonArray
     * @return an empty collection if jsonArray is null or does not contain String elements.
     *         otherwise a Collection of jsonArray elements.
     */
    private Collection<String> readStringCollection(JsonNode jsonArray){
        Collection<String> ret = new ArrayList<String>();
        if(jsonArray != null){
            Iterator<JsonNode> it = jsonArray.getElements();
            while(it.hasNext()){
                ret.add(it.next().getTextValue());
            }
        }
        return ret;
    }
    
    private Collection<Integer> readIntegerCollection(JsonNode jsonArray){
        Collection<Integer> ret = new ArrayList<Integer>();
        if(jsonArray != null){
            Iterator<JsonNode> it = jsonArray.getElements();
            while(it.hasNext()){
                ret.add(it.next().getIntValue());
            }
        }
        return ret;
    }
    
    private Map<String, String> readStringMap(JsonNode jsonMap) {
        Map<String, String> map = new HashMap<String, String>();
        if(jsonMap != null){
            Iterator<Entry<String, JsonNode>> mapIt = jsonMap.getFields();
            while(mapIt.hasNext()){
                Entry<String, JsonNode> e = mapIt.next();
                map.put(e.getKey(), e.getValue().getTextValue());
            }
        }
        return map;
    }
}
