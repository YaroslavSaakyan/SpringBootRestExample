package com.yaroslav.springboottest.controller;

import com.github.restdriver.clientdriver.ClientDriver;
import com.github.restdriver.clientdriver.ClientDriverFactory;
import com.github.restdriver.clientdriver.ClientDriverRequest;
import com.github.restdriver.clientdriver.ClientDriverRule;
import com.yaroslav.springboottest.Application;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.github.restdriver.clientdriver.RestClientDriver.*;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebIntegrationTest({"server.port=0"})
public class PostControllerIntegrationTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostControllerIntegrationTest.class);

    RestTemplate restTemplate = new TestRestTemplate();

    @Rule
    public ClientDriverRule driver = new ClientDriverRule();

    @Rule
    public ClientDriverRule clientDriver = new ClientDriverRule(8081);

    @Value("${local.server.port}")
    private int port;

    private String getBaseUrl() {
        return "http://127.0.0.1:" + port;
    }

    @Test
    public void createAndGetSuccessfully() throws Exception {
        LOGGER.debug("createAndGetSuccessfully() started...");
        String postId = "1";

        clientDriver.addExpectation(onRequestTo("/posts/" + postId).withMethod(ClientDriverRequest.Method.GET),
                giveResponse(successfulResponseBody(), "application/json"));

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("postId", postId);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(mvm, requestHeaders);
        restTemplate.postForObject(getBaseUrl() + "/posts/" + postId, requestEntity, String.class);

        ResponseEntity<Iterable> response = restTemplate.getForEntity(getBaseUrl() + "/posts", Iterable.class);
        Iterator iterator = response.getBody().iterator();
        Assert.assertTrue(iterator.hasNext());

        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!(object instanceof Map)) {
                throw new Exception("Invalid response");
            }
            Map<String, String> map = (Map<String, String>) object;
            Assert.assertTrue(map.get("id") != null);
            Assert.assertTrue(map.get("userId") != null);
            Assert.assertTrue(map.get("title") != null);
            Assert.assertTrue(map.get("body") != null);
        }
        LOGGER.debug("createAndGetSuccessfully() passed");
    }

    private String successfulResponseBody() {
        return "{\n" +
                "  \"userId\": 1,\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                "  \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
                "}";
    }

    @Test
    public void doNotCreateWrongPostId() throws Exception {
        LOGGER.debug("doNotCreateWrongPostId() started...");
        String postId = "9999";

        clientDriver.addExpectation(onRequestTo("/posts/" + postId).withMethod(ClientDriverRequest.Method.GET), giveEmptyResponse());

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("postId", postId);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(mvm, requestHeaders);
        restTemplate.postForObject(getBaseUrl() + "/posts/" + postId, requestEntity, String.class);

        ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl() + "/posts", List.class);

        Assert.assertTrue(response.getBody().isEmpty());
        LOGGER.debug("doNotCreateWrongPostId() passed");
    }
}