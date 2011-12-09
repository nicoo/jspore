/*
 */
package net.linkfluence.jspore;

import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import static org.junit.Assert.* ;

/**
 * Test the github spore spec.
 * Tests are only done on the public routes.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class GitHubPublicTest {
 
    private final Spore<JsonNode> spore;
    
    public GitHubPublicTest() throws FileNotFoundException, InvalidSporeSpecException, IOException {
        URL url = this.getClass().getResource("/github.json");
        File spec = new File(url.getFile());

        spore = new Spore.Builder<JsonNode>()
                .addMiddleware(Spore.JSON)
                .addSpecContent(spec)
                .setDebug(true)
                .build();
    }

    @Test
    public void testGetInfo() throws FileNotFoundException, IOException, InvalidSporeSpecException, SporeException {       
        Map<String, String> params = new ImmutableMap.Builder<String, String>()
                .put("format", "json")
                .put("username", "nicoo")
                .build();
        SporeResult<JsonNode> result = spore.call("get_info", params);
        assertNotNull(result.response);
        assertNotNull(result.body);
        JsonNode user = result.body.get("user");
        JsonNode login = user.get("login");
        assertEquals("nicoo", login.getTextValue());
    }
    
    @Test
    public void testListFollowing() throws FileNotFoundException, IOException, InvalidSporeSpecException, SporeException {
        Map<String, String> params = new ImmutableMap.Builder<String, String>()
                .put("format", "json")
                .put("user", "nicoo")
                .build();
        SporeResult<JsonNode> result = spore.call("list_following", params);
        assertNotNull(result.response);
        assertNotNull(result.body);
        Iterator<JsonNode> users = result.body.get("users").getElements();
        boolean hasNgrunwald = false;
        while(users.hasNext()){
            if(users.next().asText().equals("ngrunwald")){
                hasNgrunwald = true;
            }
        }
        assertTrue(hasNgrunwald);
    }
}
