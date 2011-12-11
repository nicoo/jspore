/*
 */
package net.linkfluence.jspore;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import net.linkfluence.jspore.middleware.auth.Basic;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * set the appropriate value to USERNAME and PASS to get these
 * tests to work.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class GitHubPrivateTest {

    private static final String USERNAME = "nicoo";
    private static final String PASS = "xxxxxx";
    
    private final Spore<JsonNode> spore;

    public GitHubPrivateTest() throws FileNotFoundException, IOException, InvalidSporeSpecException {
        URL url = this.getClass().getResource("/github.json");
        File spec = new File(url.getFile());

        spore = new Spore.Builder<JsonNode>()
                .addMiddleware(Spore.JSON)
                .addMiddleware(new Basic(USERNAME, PASS))
                .addSpecContent(spec)
                .setDebug(true)
                .build();
    }

    @Test
    public void testGetProfile() throws SporeException {
        SporeResult<JsonNode> result = spore.call("get_profile",
                new ImmutableMap.Builder<String, String>()
                .put("format", "json")
                .build());
        assertEquals("Linkfluence", result.body.get("user")
                .get("company").asText());
    }
}
