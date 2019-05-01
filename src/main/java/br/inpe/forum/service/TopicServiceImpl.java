package br.inpe.forum.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import br.inpe.forum.api.repository.MongoCommentRepository;
import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.repository.MongoUserRepository;
import br.inpe.forum.api.service.TopicService;
import br.inpe.forum.exception.NotFoundException;
import br.inpe.forum.model.Comment;
import br.inpe.forum.model.Topic;
import br.inpe.forum.model.User;
import net.sf.esfinge.gamification.annotation.auth.points.AllowPointGreaterThan;
import net.sf.esfinge.gamification.annotation.auth.points.DenyPointLessOrEqualsThan;

@Service
public class TopicServiceImpl implements TopicService {

	@Autowired
	private MongoTopicRepository mongoTopicRepository;
	@Autowired
	private MongoCommentRepository mongoCommentRepository;
	@Autowired
	private MongoUserRepository mongoUserRepository;

	@Override
	public Topic insertTopic(Topic topic) {

		topic.setObjectId(ObjectId.get().toString());
		Topic savedTopic = mongoTopicRepository.insert(topic);

		if (Objects.nonNull(savedTopic))
			return savedTopic;

		throw new MongoException("Topic was not created");
	}

	@Override
	@DenyPointLessOrEqualsThan(achievementName = "points", quantity = 100)
	public void removeTopic(String topicId) {

		Optional<Topic> optionalTopic = mongoTopicRepository.findById(topicId);
		if (optionalTopic.isPresent()) {
			Topic topic = optionalTopic.get();
			List<Comment> comments = topic.getComments();

			if (Objects.nonNull(comments)) {
				comments.forEach(comment -> mongoCommentRepository.delete(comment));
			}
			mongoTopicRepository.deleteById(topicId);
		} else
			throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	@AllowPointGreaterThan(achievementName = "points", quantity = 50)
	public Topic updateTopic(String topicId, Topic topic) {

		Optional<Topic> optionalTopic = mongoTopicRepository.findById(topicId);

		if (optionalTopic.isPresent()) {
			topic.setObjectId(optionalTopic.get().getObjectId());
			return mongoTopicRepository.save(topic);
		}

		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	public Topic findTopic(String topicId) {

		Optional<Topic> topicFound = mongoTopicRepository.findById(topicId);

		if (topicFound.isPresent()) {
			return topicFound.get();
		}
		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	public List<Topic> findTopics() {
		return mongoTopicRepository.findAll();
	}

	@Override
	public List<Topic> findByUser(String username) {
		Optional<User> optionalUser = mongoUserRepository.findByUsername(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Topic> topics = mongoTopicRepository.findByUser(user);
			return topics;
		}
		throw new NotFoundException("User " + username + " not found");
	}

}
