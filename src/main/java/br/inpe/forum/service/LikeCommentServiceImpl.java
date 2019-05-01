package br.inpe.forum.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.inpe.forum.api.repository.MongoCommentRepository;
import br.inpe.forum.api.service.LikeCommentService;
import br.inpe.forum.exception.NotFoundException;
import br.inpe.forum.model.Comment;
import net.sf.esfinge.gamification.annotation.auth.trophy.AllowTrophy;

@Service
@Qualifier("commentService")
public class LikeCommentServiceImpl implements LikeCommentService {

	@Autowired
	private MongoCommentRepository repository;

	@Override
	public Comment addLike(String topicId, String commentId, String username) {
		Optional<Comment> optionalComment = repository.findById(commentId);
		if (optionalComment.isPresent()) {

			Comment comment = optionalComment.get();
			Set<String> likes = comment.getLikes();

			if (Objects.isNull(likes)) {
				likes = new HashSet<>();
				comment.setLikes(likes);
			}

			likes.add(username);
			return repository.save(comment);
		}
		throw new NotFoundException("Comment " + commentId + " not found");
	}

	@Override
	@AllowTrophy(achievementName = "like comment tropy")
	public void removeLike(String commentId, String username) {

		Optional<Comment> optionalComment = repository.findById(commentId);
		if (optionalComment.isPresent()) {

			Comment comment = optionalComment.get();
			Set<String> likes = comment.getLikes();

			if (Objects.isNull(likes)) {
				likes = new HashSet<>();
				comment.setLikes(likes);
			} else {
				if (likes.contains(username)) {
					likes.remove(username);
					repository.save(comment);
				} else {
					throw new ConstraintViolationException("User doesn't have add likes to this comment", null);
				}
			}

		} else
			throw new NotFoundException("Comment " + commentId + " not found");
	}

}
