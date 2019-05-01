package br.inpe.forum.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.service.LikeTopicService;
import br.inpe.forum.exception.NotFoundException;
import br.inpe.forum.model.Topic;
import net.sf.esfinge.gamification.annotation.auth.trophy.AllowTrophy;

@Service
@Qualifier("topicService")
public class LikeTopicServiceImpl implements LikeTopicService {

	@Autowired
	private MongoTopicRepository repository;

	@Override
	public Topic addLike(String topicId, String username) {
		Optional<Topic> optionalTopic = repository.findById(topicId);
		if (optionalTopic.isPresent()) {

			Topic topic = optionalTopic.get();
			Set<String> likes = topic.getLikes();

			if (Objects.nonNull(likes)) {
				likes.add(username);
			} else {
				likes = new HashSet<>();
				likes.add(username);
				topic.setLikes(likes);
			}

			return repository.save(topic);

		}
		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	@AllowTrophy(achievementName = "like topic tropy")
	public void removeLike(String topicId, String username) {

		Optional<Topic> optionalTopic = repository.findById(topicId);
		if (optionalTopic.isPresent()) {

			Topic topic = optionalTopic.get();
			Set<String> likes = topic.getLikes();

			if (Objects.isNull(likes)) {
				likes = new HashSet<>();
				topic.setLikes(likes);
			} else {
				if (likes.contains(username)) {
					likes.remove(username);
					repository.save(topic);
				} else {
					throw new ConstraintViolationException("User doesn't have add likes to this topic", null);
				}
			}
		} else
			throw new NotFoundException("Topic " + topicId + " not found");
	}

}
