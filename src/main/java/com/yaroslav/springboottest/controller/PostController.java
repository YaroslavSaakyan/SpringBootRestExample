package com.yaroslav.springboottest.controller;

import com.yaroslav.springboottest.command.CommandFactory;
import com.yaroslav.springboottest.dao.ServiceResponseRepository;
import com.yaroslav.springboottest.dao.entity.ServiceResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private ServiceResponseRepository repository;
    @Autowired
    private CommandFactory commandFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @RequestMapping(value = "{postId}", method = RequestMethod.POST)
    public void createPost(@PathVariable("postId") String postId) {
        LOGGER.debug("Execute createPost with parameter " + postId + " ...");
        Optional<ServiceResponseEntity> entity = commandFactory.createGetPostCommand(postId).execute();
        entity.ifPresent(repository::save);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<ServiceResponseEntity> getPosts() {
        LOGGER.debug("Execute getPosts...");
        return repository.findAll();
    }
}
