package com.yaroslav.springboottest.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
@Component
public class CommandFactory {
    @Value("${service.url}")
    private String url;
    @Value("${service.call.timeout}")
    private Integer timeout;

    public GetPostCommand createGetPostCommand(String postId) {
        return new GetPostCommand(url + "/posts/", timeout, postId);
    }

}
