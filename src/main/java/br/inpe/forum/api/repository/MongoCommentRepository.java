package br.inpe.forum.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.inpe.forum.model.Comment;
import br.inpe.forum.model.User;

@Repository
public interface MongoCommentRepository extends MongoRepository<Comment, String> {

	List<Comment> findByDescriptionAndUser(String description, User user);
}
