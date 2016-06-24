package com.yaroslav.springboottest.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.yaroslav.springboottest.dao.entity.ServiceResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
public class GetPostCommand extends HystrixCommand<Optional<ServiceResponseEntity>> {

    private final String postId;
    private final String url;

    private static final Logger LOGGER = LoggerFactory.getLogger(GetPostCommand.class);

    public GetPostCommand(String url, int timeout, String postId) {
        super(HystrixCommandGroupKey.Factory.asKey("GetPostCommand"), timeout);
        this.postId = postId;
        this.url = url;
    }

    @Override
    protected Optional<ServiceResponseEntity> getFallback() {
        LOGGER.error("Fallback: " + getExecutionEvents());
        return Optional.ofNullable(null);
    }

    @Override
    protected Optional<ServiceResponseEntity> run() throws Exception {
        LOGGER.debug("Call " + url + postId);
        RestTemplate restTemplate = new RestTemplate();
        return Optional.ofNullable(restTemplate.getForObject(url + postId, ServiceResponseEntity.class));
    }
}
