package br.inpe.forum.api.service;

import java.util.List;

import br.inpe.forum.model.Topic;
import net.sf.esfinge.gamification.annotation.PointsToUser;

public interface TopicService {

	@PointsToUser(name = "points", quantity = 10)
	Topic insertTopic(Topic topic);

	void removeTopic(String topicId);

	Topic updateTopic(String topicId, Topic topic);

	Topic findTopic(String title);

	List<Topic> findTopics();
	
	List<Topic> findByUser(String username);

}
