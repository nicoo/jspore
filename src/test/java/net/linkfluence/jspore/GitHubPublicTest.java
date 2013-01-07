/*
 */
package net.linkfluence.jspore;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test the github spore spec.
 * Tests are only done on the public routes.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 * @author David Pilato <david@pilato.fr>
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
            .put("user", "nicoo")
            .put("keyword", "nicoo")
            .put("repo", "jspore")
            .build();

    @Test
    public void test_get_info() throws SporeException {
        SporeResult<JsonNode> result = spore.call("get_info", publicParams);
        assertEquals("User", result.body.get("type")
                .asText());
    }
    
    @Test
    public void test_user_search() throws SporeException {
        SporeResult<JsonNode> result = spore.call("user_search", publicParams);
        JsonNode node = result.body.get("users");
        
        assertTrue(node instanceof ArrayNode);
        
    	ArrayNode aNode = (ArrayNode) node;
    	String login = aNode.get(0).get("login").asText();
        assertEquals("nicoo", login);
    }
    
    @Test
    public void test_get_repo_info() throws SporeException {
        SporeResult<JsonNode> result = spore.call("get_repo_info", publicParams);
        assertEquals("https://github.com/nicoo/jspore", result.body.get("html_url")
                .asText());
    }
    
    @Test
    public void test_list_following() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_following", publicParams);
        Iterator<JsonNode> users = result.body.getElements();
        boolean hasNgrunwald = false;
        while (users.hasNext()) {
            if (users.next().get("login").asText().equals("ngrunwald")) {
                hasNgrunwald = true;
            }
        }
        assertTrue(hasNgrunwald);
    }

    @Test
    public void test_list_followers() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_followers", publicParams);
        Iterator<JsonNode> users = result.body.getElements();
        boolean hasDocteurZ = false;
        while (users.hasNext()) {
            if (users.next().get("login").asText().equals("docteurZ")) {
                hasDocteurZ = true;
            }
        }
        assertTrue(hasDocteurZ);
    }
    
    @Test
    public void test_list_watched_repos() throws SporeException {
        SporeResult<JsonNode> result = spore.call("list_watched_repos", publicParams);
        Iterator<JsonNode> watchedrepos = result.body.getElements();
        boolean hasCljRome = false;
        while (watchedrepos.hasNext()) {
            if (watchedrepos.next().get("full_name").asText().equals("ngrunwald/clj-rome")) {
                hasCljRome = true;
            }
        }
        assertTrue(hasCljRome);
    }
    

    
    
}
