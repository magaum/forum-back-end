package br.inpe.forum.service;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Objects;

import org.esfinge.guardian.exception.AuthorizationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.service.LikeTopicService;
import br.inpe.forum.api.service.TopicService;
import br.inpe.forum.model.Topic;
import br.inpe.forum.model.User;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

	@Autowired
	private MongoTopicRepository repository;
	@Autowired
	private TopicService topicService;
	@Autowired
	private LikeTopicService likeService;

	private Topic topic;
	private User mockUser;

	@Before
	public void setUp() {
		mockUser = new User("testUser", "weslei", "123");
		topic = new Topic("Test topic", "This is a test for insert method", mockUser,
				Arrays.asList("Test", "New", "First insert!"));

	}

	@After
	public void tearDown() {
		topic = null;
		mockUser = null;
	}

	@Test
	public void topicServiceInsert() {

		Topic response = topicService.insertTopic(topic);
		assertTrue(Objects.nonNull(response));
		repository.delete(topic);

	}

	@Test
	public void updateAndDeleteTest() throws AuthorizationException, NotFoundException {
		Topic t = repository.insert(topic);
		Topic actualTopic = new Topic("Update test", "This is a update test for service update method",
				mockUser, Arrays.asList("Test", "Update", "First update!"));
		Topic response = topicService.updateTopic(t.getObjectId(), actualTopic);
		assertTrue(Objects.nonNull(response) );
		topicService.removeTopic(t.getObjectId());
	}

	@Test
	public void likeTest() throws NotFoundException {
		Topic t = repository.insert(topic);
		Topic response = likeService.addLike(t.getObjectId(), "segundoLiker");
		assertTrue(Objects.nonNull(response ));
		topicService.removeTopic(t.getObjectId());
	}

}
