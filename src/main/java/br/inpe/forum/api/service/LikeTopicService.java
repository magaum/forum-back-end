package br.inpe.forum.api.service;

import br.inpe.forum.model.Topic;
import net.sf.esfinge.gamification.annotation.TrophiesToUser;

public interface LikeTopicService extends LikeService {

	@TrophiesToUser(name = "like topic tropy")
	Topic addLike(String topicId, String username);

}
