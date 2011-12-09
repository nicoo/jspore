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
import static org.junit.Assert.*;

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
    private final Map<String, String> publicParams = new ImmutableMap.Builder<String, String>()
            .put("format", "json")
            .put("user", "nicoo")
            .build();

    @Test
    public void testGetInfo() throws SporeException {
        SporeResult<JsonNode> result = spore.call("get_info", publicParams);
        JsonNode user = result.body.get("user");
        JsonNode login = user.get("login");
        assertEquals("nicoo", login.getTextValue());
    }

    @Test
    public void testListFollowing() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_following", publicParams);
        Iterator<JsonNode> users = result.body.get("users").getElements();
        boolean hasNgrunwald = false;
        while (users.hasNext()) {
            if (users.next().asText().equals("ngrunwald")) {
                hasNgrunwald = true;
            }
        }
        assertTrue(hasNgrunwald);
    }

    @Test
    public void testListFollower() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_followers", publicParams);
        Iterator<JsonNode> users = result.body.get("users").getElements();
        boolean hasDocteurZ = false;
        while (users.hasNext()) {
            if (users.next().asText().equals("docteurZ")) {
                hasDocteurZ = true;
            }
        }
        assertTrue(hasDocteurZ);
    }
    
    @Test
    public void testListWatchedRepo() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_watched_repos", publicParams);
        assertNotNull(result.body.get("repositories"));
    }
}
