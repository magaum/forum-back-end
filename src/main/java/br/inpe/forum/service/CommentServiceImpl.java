package br.inpe.forum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.DBRef;
import com.mongodb.MongoException;

import br.inpe.forum.api.repository.MongoCommentRepository;
import br.inpe.forum.api.repository.MongoTopicRepository;
import br.inpe.forum.api.service.CommentService;
import br.inpe.forum.exception.NotFoundException;
import br.inpe.forum.model.Comment;
import br.inpe.forum.model.Topic;
import net.sf.esfinge.gamification.annotation.auth.points.AllowPointGreaterThan;
import net.sf.esfinge.gamification.annotation.auth.points.DenyPointLessOrEqualsThan;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private MongoTopicRepository topicRepository;
	@Autowired
	private MongoCommentRepository repository;
	@Autowired
	private MongoTemplate template;

	@Override
	@AllowPointGreaterThan(achievementName = "points", quantity = 150)
	public Comment addComment(String topicId, Comment comment) {
		Optional<Topic> optionalTopic = topicRepository.findById(topicId);

		if (optionalTopic.isPresent()) {

			Topic topic = optionalTopic.get();

			Update update;
			Comment c = repository.save(comment);

			if (Objects.isNull(topic.getComments())) {
				topic.setComments(new ArrayList<Comment>());
				update = new Update().push("comments", new DBRef("comments", c.getObjectId()));
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(new ObjectId(topicId)));
			update = new Update().push("comments", c.getObjectId());
			template.updateMulti(query, update, Topic.class);
			return c;

		}

		throw new NotFoundException("Topic doesn't exists");

	}

	@Override
	public Comment findComment(String topicId, String commentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(topicId)));
		query.fields().elemMatch("comments", Criteria.where("$id").is(new ObjectId(commentId)));
		Topic topic = template.findOne(query, Topic.class);
		if (Objects.nonNull(topic)) {
			if (Objects.nonNull(topic.getComments())) {
				Comment comment = topic.getComments().get(0);
				if (comment.getObjectId().equals(commentId))
					return comment;
			} else {
				throw new NotFoundException("Comment " + commentId + " not found");
			}
		}
		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	public List<Comment> findComments(String topicId) {

		Optional<Topic> optionalTopic = topicRepository.findById(topicId);
		if (optionalTopic.isPresent()) {
			List<Comment> topicComments = optionalTopic.get().getComments();
			return topicComments;
		}
		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	@DenyPointLessOrEqualsThan(achievementName = "points", quantity = 350)
	public Comment updateComment(String topicId, String commentId, Comment comment) {

		Optional<Topic> optionalTopic = topicRepository.findById(topicId);
		if (optionalTopic.isPresent()) {
			List<Comment> topicComments = optionalTopic.get().getComments();
			if (Objects.nonNull(topicComments)) {
				comment.setObjectId(commentId);
				Comment actualComment = repository.save(comment);
				return actualComment;
			} else {
				throw new NotFoundException("Comment " + commentId + " not found");
			}

		}
		throw new NotFoundException("Topic " + topicId + " not found");
	}

	@Override
	@AllowPointGreaterThan(achievementName = "points", quantity = 450)
	public void removeComment(String topicId, String commentId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(topicId)));
		Update update = new Update().pull("comments", new DBRef("comments", commentId));
		template.updateMulti(query, update, Topic.class);
		repository.deleteById(commentId);
		Optional<Comment> optionalComment = repository.findById(commentId);
		if (optionalComment.isPresent()) {
			throw new MongoException("Error while delete comment operation was executed");
		}
	}

}
