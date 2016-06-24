package com.yaroslav.springboottest.controller;

import com.yaroslav.springboottest.command.CommandFactory;
import com.yaroslav.springboottest.command.GetPostCommand;
import com.yaroslav.springboottest.dao.ServiceResponseRepository;
import com.yaroslav.springboottest.dao.entity.ServiceResponseEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

/**
 * Created by yaroslav
 * Date: 24.06.16
 */
@RunWith(MockitoJUnitRunner.class)
public class PostControllerTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostControllerIntegrationTest.class);
    @Mock
    private ServiceResponseRepository repository;
    @Mock
    private CommandFactory commandFactory;
    @InjectMocks
    private PostController postController = new PostController();

    @Test
    public void testCreatePost() throws Exception {
        LOGGER.debug("testCreatePost() started...");
        ServiceResponseEntity entity = new ServiceResponseEntity();
        Optional<ServiceResponseEntity> optional = Optional.of(entity);
        String postId = "1";

        GetPostCommand mockCommand = mock(GetPostCommand.class);
        when(commandFactory.createGetPostCommand(postId)).thenReturn(mockCommand);
        when(mockCommand.execute()).thenReturn(optional);
        postController.createPost(postId);
        verify(repository).save(entity);
        LOGGER.debug("testCreatePost() passed");
    }

    @Test
    public void testGetPosts() throws Exception {
        LOGGER.debug("testGetPosts() started...");
        when(repository.findAll()).thenReturn(createServiceResponseEntities());
        Iterable<ServiceResponseEntity> entities = postController.getPosts();
        Iterator<ServiceResponseEntity> iterator = entities.iterator();
        Assert.assertTrue(iterator.hasNext());
        ServiceResponseEntity entity = iterator.next();
        Assert.assertEquals("0", entity.getId());
        Assert.assertEquals("0", entity.getUserId());
        Assert.assertEquals("Title", entity.getTitle());
        Assert.assertEquals("Body", entity.getBody());
        LOGGER.debug("testGetPosts() passed");
    }

    private Iterable<ServiceResponseEntity> createServiceResponseEntities() {
        List<ServiceResponseEntity> entities = new ArrayList<>();
        ServiceResponseEntity entity0 = new ServiceResponseEntity();
        entity0.setId("0");
        entity0.setUserId("0");
        entity0.setTitle("Title");
        entity0.setBody("Body");
        entities.add(entity0);
        return entities;
    }
}