package br.inpe.forum.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.service.LikeService;
import br.inpe.forum.api.service.LikeTopicService;
import br.inpe.forum.api.service.TopicService;
import br.inpe.forum.gamification.GamificationSetup;
import br.inpe.forum.model.Topic;
import net.sf.esfinge.gamification.user.UserStorage;

/**
 * Defined routes
 * 
 * topics -> all topics tipics\{x} -> specific topic topics\new -> create new
 * topic
 * 
 * @author weslei
 *
 */

@RestController
@RequestMapping(path = "topics", produces = "application/json")
public class TopicController {

	@Autowired
	private TopicService service;
	@Autowired
	private LikeTopicService likeService;
	@Autowired
	private MongoTopicRepository repository;

	/**
	 * find a topic by title
	 * 
	 * @param topicId
	 * @return
	 */
	@GetMapping(path = "{topicId}")
	public ResponseEntity<?> findTopic(@PathVariable String topicId) {
		Topic t = service.findTopic(topicId);
		return new ResponseEntity<Topic>(t, HttpStatus.OK);
	}

	/**
	 * Find topics by username
	 */
	@GetMapping(path = "users/{username}")
	public ResponseEntity<?> findTopicsByUser(@PathVariable String username) {
		List<Topic> t = service.findByUser(username);
		return new ResponseEntity<List<Topic>>(t, HttpStatus.OK);
	}

	/**
	 * find all topics
	 * 
	 * @param title
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> findTopics() {
		List<Topic> topics = service.findTopics();
		return new ResponseEntity<List<Topic>>(topics, HttpStatus.OK);
	}

	/**
	 * Add new topic to topics collections
	 * 
	 * @param topic
	 * @return {@link ResponseEntity}
	 */

	@PostMapping
	public ResponseEntity<?> addTopic(@Valid @RequestBody Topic topic) {
		TopicService s = GamificationSetup.configureGamification(service);
		UserStorage.setUserID(topic.getUser().getUsername());
		Topic t = s.insertTopic(topic);
		return new ResponseEntity<Topic>(t, HttpStatus.CREATED);
	}

	@PostMapping(path = "{topicId}/likes/{username}")
	public ResponseEntity<?> addLikeToTopic(@PathVariable String topicId, @PathVariable String username) {
		LikeTopicService like = GamificationSetup.configureGamification(likeService);
		Topic topic = repository.findById(topicId).get();

		UserStorage.setUserID(topic.getUser().getUsername());
		Topic t = like.addLike(topicId, username);
		return new ResponseEntity<Topic>(t, HttpStatus.OK);

	}

	@DeleteMapping(path = "{topicId}/likes/{username}")
	public ResponseEntity<?> removeLikeOfTopic(@PathVariable String topicId, @PathVariable @NonNull String username) {
		LikeService like = GamificationSetup.configureGamification(likeService);
		UserStorage.setUserID(username);
		like.removeLike(topicId, username);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * send a delete request for topic/{topic title} for remove this topic
	 * 
	 * @param topicId
	 * @return {@link ResponseEntity}
	 */

	@DeleteMapping(path = "{topicId}")
	public ResponseEntity<?> removeTopic(@PathVariable String topicId) {
		TopicService s = GamificationSetup.configureGamification(service);
		Topic topic = repository.findById(topicId).get();
		UserStorage.setUserID(topic.getUser().getUsername());
		s.removeTopic(topicId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping(path = "{topicId}")
	public ResponseEntity<?> updateTopic(@PathVariable String topicId, @RequestBody Topic topic) {

		TopicService s = GamificationSetup.configureGamification(service);

		UserStorage.setUserID(topic.getUser().getUsername());
		Topic t = s.updateTopic(topicId, topic);
		return new ResponseEntity<Topic>(t, HttpStatus.OK);

	}
}
