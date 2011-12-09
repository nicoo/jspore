package net.linkfluence.jspore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import static org.junit.Assert.* ;

/**
 *
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class SpecParserTest {
    
    @Test
    public void testParseSporeString() throws IOException, InvalidSporeSpecException {
        String spec = "{ "
                + "\"name\" : \"Amazon S3\", "
                + "\"version\" : \"0.1\", "
                + "\"base_url\" : \"http://s3.amazonaws.com\", "
                + "\"methods\" : { "
                + "  \"get_service\" : { "
                + "    \"path\" : \"/\", "
                + "   \"method\" : \"GET\", "
                + "  \"headers\" : { "
                + "    \"Date\" : \"AWS\" "
                + " } "
                + " }, "
                + " \"delete_bucket\" : {"
                + " \"path\" : \"/\","
                + " \"method\" : \"DELETE\","
                + " \"headers\" : {"
                + "    \"Date\" : \"AWS\""
                + " },"
                + " \"optional_params\" : ["
                + "    \"bucket\""
                + "  ],"
                + "   \"expected_status\" : [ 204 ]"
                + " }"
                + "}"
                + "}"
                + "}";
        
        SpecParser parser = new SpecParser();
        Model m = parser.parse(spec);
        
        // check base params
        assertEquals("Amazon S3", m.name);
        assertEquals("0.1", m.version);
        assertEquals("http://s3.amazonaws.com", m.baseUrl.toString());
        
        assertNotNull(m.getMethod("get_service"));
        assertNotNull(m.getMethod("delete_bucket"));
        
        Method getService = m.getMethod("get_service");
        assertEquals("/", getService.path);
        assertEquals("GET", getService.httpMethod);

        Method deleteBucket = m.getMethod("delete_bucket");
        assertEquals("/", deleteBucket.path);
        assertEquals("DELETE", deleteBucket.httpMethod);
    }
    
        
    @Test
    public void testLoadGitHubSpec() throws IOException, InvalidSporeSpecException{
        URL url = this.getClass().getResource("/github.json");
        File spec = new File(url.getFile());
        Spore<String> spore = new Spore.Builder<JsonNode>()
                .addSpecContent(spec)
                .build();
    }
    
}
