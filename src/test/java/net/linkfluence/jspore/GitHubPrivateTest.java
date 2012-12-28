/*
 */
package net.linkfluence.jspore;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import net.linkfluence.jspore.middleware.auth.Basic;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

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
    private static final String PASS = "YOUHAVETOCHANGETHISPASSWORD";
    
    private Spore<JsonNode> buildSpore(String login, String password) throws IOException, InvalidSporeSpecException {
        URL url = this.getClass().getResource("/github.json");
        File spec = new File(url.getFile());

        return new Spore.Builder<JsonNode>()
                .addMiddleware(Spore.JSON)
                .addMiddleware(new Basic(login, password))
                .addSpecContent(spec)
                .setDebug(true)
                .build();
    }

    @Test
    public void test_get_profile() throws Exception {
        SporeResult<JsonNode> result = null;
        try {
            result = buildSpore(USERNAME, PASS).call("get_profile",
                    new ImmutableMap.Builder<String, String>()
                            .build());
            assertEquals("User", result.body.get("type").asText());
        } catch (SporeException e) {
            logger.warn("You did not set USERNAME and PASS in GitHubPrivateTest class. So Github answers 'Bad credentials'.", e);
        }
    }

    @Test(expected = SporeException.class)
    public void test_bad_credentials() throws Exception {
        buildSpore(USERNAME, "BADPASSWORD").call("get_profile",
                new ImmutableMap.Builder<String, String>()
                        .build());
    }
}
