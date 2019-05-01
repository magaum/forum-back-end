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

import br.inpe.forum.api.repository.MongoCommentRepository;
import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.service.CommentService;
import br.inpe.forum.api.service.LikeCommentService;
import br.inpe.forum.api.service.TopicService;
import br.inpe.forum.model.Comment;
import br.inpe.forum.model.Topic;
import br.inpe.forum.model.User;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTest {

	@Autowired
	private MongoCommentRepository repository;
	@Autowired
	private CommentService commentService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private MongoTopicRepository topicRepository;
	@Autowired
	private LikeCommentService likeService;

	private Topic topic;
	private Comment comment;
	private User mockUser;

	@Before
	public void setUp() {
		mockUser = new User("testUser", "weslei", "123");
		comment = new Comment("First comment!", mockUser);
		topic = new Topic("Test topic", "This is a test for insert method", mockUser,
				Arrays.asList("Test", "New", "First insert!"));

	}

	@After
	public void tearDown() {
		topic = null;
		mockUser = null;
		comment = null;
	}

	@Test
	public void topicServiceInsert() throws AuthorizationException, NotFoundException {

		Topic t = topicRepository.insert(topic);
		Comment response = commentService.addComment(t.getObjectId(), comment);
		commentService.addComment(t.getObjectId(), comment);
		assertTrue(Objects.nonNull(response));
		repository.delete(comment);
		topicService.removeTopic(t.getObjectId());

	}

	@Test
	public void updateAndDeleteTest() throws AuthorizationException, NotFoundException {
		Topic t = topicRepository.insert(topic);
		commentService.addComment(t.getObjectId(), comment);
		Comment c = repository.findByDescriptionAndUser(comment.getDescription(), comment.getUser()).get(0);
		mockUser.setUsername("novoUsername");
		Comment actualComment = new Comment("Comment was updated", mockUser);
		Comment response = commentService.updateComment(t.getObjectId(), c.getObjectId(), actualComment);
		assertTrue(Objects.nonNull(response));
		commentService.removeComment(t.getObjectId(), c.getObjectId());
		topicRepository.delete(topic);
	}

	@Test
	public void likeTest() throws NotFoundException {
		Topic t = topicRepository.insert(topic);
		Comment c = repository.insert(comment);
		Comment response = likeService.addLike(t.getObjectId(), c.getObjectId(), "primeiroLiker");
		assertTrue(Objects.nonNull(response));
		repository.deleteById(c.getObjectId());
		topicRepository.delete(t);
	}

}
