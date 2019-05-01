package br.inpe.forum.api.service;

import br.inpe.forum.model.Comment;
import net.sf.esfinge.gamification.annotation.TrophiesToUser;

public interface LikeCommentService extends LikeService {

	@TrophiesToUser(name = "like comment tropy")
	Comment addLike(String topicId, String commentId, String username);

}
