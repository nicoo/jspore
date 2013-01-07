package net.linkfluence.jspore;

import java.io.*;
import java.net.URL;
import org.codehaus.jackson.JsonNode;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class SpecParserTest {
    
    @Test
    public void testParseSporeString() throws IOException, InvalidSporeSpecException {
        String spec = readFromClasspath("/amazons3.json");
        
        SpecParser parser = new SpecParser();
        Model m = parser.parse(spec);
        
        // check base params
        assertEquals("Amazon S3", m.name);
        assertEquals("0.1", m.version);
        assertEquals("http://s3.amazonaws.com", m.baseUrl.toString());
        assertEquals(1, m.expectedStatus.size());
        
        assertNotNull(m.getMethod("get_service"));
        assertNotNull(m.getMethod("delete_bucket"));
        
        Method getService = m.getMethod("get_service");
        assertEquals("/", getService.path);
        assertEquals("GET", getService.httpMethod);
        assertEquals(1, getService.expectedStatus.size());
        for (Integer status : getService.expectedStatus) {
            assertEquals(200, status.intValue());
        }

        Method deleteBucket = m.getMethod("delete_bucket");
        assertEquals("/", deleteBucket.path);
        assertEquals("DELETE", deleteBucket.httpMethod);
        assertEquals(1, deleteBucket.expectedStatus.size());
        for (Integer status : deleteBucket.expectedStatus) {
            assertEquals(204, status.intValue());
        }
    }
    
        
    @Test
    public void testLoadGitHubSpec() throws IOException, InvalidSporeSpecException{
        URL url = this.getClass().getResource("/github.json");
        File spec = new File(url.getFile());
        Spore<String> spore = new Spore.Builder<JsonNode>()
                .addSpecContent(spec)
                .build();
    }

    protected static String readFromClasspath(String url) {
        InputStream in = SpecParserTest.class.getResourceAsStream(url);
        return convertStreamToString(in);
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
