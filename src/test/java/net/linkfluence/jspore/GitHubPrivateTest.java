/*
 */
package net.linkfluence.jspore;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import net.linkfluence.jspore.middleware.auth.Basic;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * set the appropriate value to USERNAME and PASS to get these
 * tests to work.
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 * @author David Pilato <david@pilato.fr>
 */
public class GitHubPrivateTest {

	private static Logger logger = LoggerFactory.getLogger(GitHubPrivateTest.class);
	
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
    public void test_get_profile() throws SporeException {
        SporeResult<JsonNode> result = spore.call("get_profile",
                new ImmutableMap.Builder<String, String>()
                .build());
        JsonNode message = result.body.get("message");
        if (message != null) {
            assertEquals("Bad credentials", message.asText());
            logger.warn("You did not set USERNAME and PASS in GitHubPrivateTest class. So Github answers 'Bad credentials'.");
        } else {
            assertEquals("User", result.body.get("type").asText());
        }
    }
}
