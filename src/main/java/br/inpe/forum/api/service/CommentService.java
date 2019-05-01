package br.inpe.forum.api.service;

import java.util.List;

import br.inpe.forum.model.Comment;
import net.sf.esfinge.gamification.annotation.auth.points.AllowPointGreaterThan;

public interface CommentService {

	Comment addComment(String topicId, Comment comment);

	void removeComment(String topicId, String commentId);

	Comment findComment(String topicId, String commentId);

	List<Comment> findComments(String topicId);

	Comment updateComment(String topicId, String commentId, Comment comment);

}
