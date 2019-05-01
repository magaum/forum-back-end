package br.inpe.forum.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inpe.forum.api.repository.MongoCommentRepository;
import br.inpe.forum.api.service.CommentService;
import br.inpe.forum.api.service.LikeCommentService;
import br.inpe.forum.api.service.LikeService;
import br.inpe.forum.gamification.GamificationSetup;
import br.inpe.forum.model.Comment;
import net.sf.esfinge.gamification.user.UserStorage;

@Validated
@RestController
@RequestMapping(path = "topics/{topicId}/comments", produces = "application/json")
public class CommentController {

	@Autowired
	private CommentService service;

	@Autowired
	private LikeCommentService likeService;

	@Autowired
	private MongoCommentRepository commentRepository;

	/**
	 * find a comment by topic id
	 * 
	 * @param topicId commentId
	 * @return
	 */
	@GetMapping(path = "{commentId}")
	public ResponseEntity<?> findComment(@PathVariable String topicId, @PathVariable String commentId) {
		Comment c = service.findComment(topicId, commentId);
		return new ResponseEntity<Comment>(c, HttpStatus.OK);
	}

	/**
	 * find all comments by topic
	 * 
	 * @param topicId
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> findComments(@PathVariable String topicId) {

		List<Comment> comments = service.findComments(topicId);
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);

	}

	/**
	 * Add new comment to a topic
	 * 
	 * @param topicId comment
	 * @return {@link ResponseEntity}
	 */

	@PostMapping
	public ResponseEntity<?> addComment(@PathVariable String topicId, @Valid @RequestBody Comment comment) {

		CommentService commentService = GamificationSetup.configureGamification(service);
		UserStorage.setUserID(comment.getUser().getUsername());
		Comment c = commentService.addComment(topicId, comment);
		return new ResponseEntity<Comment>(c, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "{commentId}")
	public ResponseEntity<?> removeComment(@PathVariable String topicId, @PathVariable String commentId) {
		CommentService commentService = GamificationSetup.configureGamification(service);
		Comment c = commentRepository.findById(commentId).get();
		UserStorage.setUserID(c.getUser().getUsername());
		commentService.removeComment(topicId, commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping(path = "{commentId}")
	public ResponseEntity<?> updateTopic(@PathVariable String topicId, @PathVariable String commentId,
			@RequestBody Comment comment) {
		CommentService commentService = GamificationSetup.configureGamification(service);
		Comment c = commentRepository.findById(commentId).get();
		UserStorage.setUserID(c.getUser().getUsername());
		Comment updatedComment = commentService.updateComment(topicId, commentId, comment);
		return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
	}

	@PostMapping(path = "{commentId}/likes/{username}")
	public ResponseEntity<?> addLikeToComment(@PathVariable String topicId, @PathVariable String commentId,
			@PathVariable String username) {
		LikeCommentService like = GamificationSetup.configureGamification(likeService);
		Comment c = commentRepository.findById(commentId).get();
		UserStorage.setUserID(c.getUser().getUsername());
		Comment updatedComment = like.addLike(topicId, commentId, username);
		return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
	}

	@DeleteMapping(path = "{commentId}/likes/{username}")
	public ResponseEntity<?> removeLikeOfComment(@PathVariable String topicId, @PathVariable String commentId,
			@PathVariable String username) {
		LikeService like = GamificationSetup.configureGamification(likeService);
		UserStorage.setUserID(username);
		like.removeLike(commentId, username);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
