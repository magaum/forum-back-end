package br.inpe.forum.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.inpe.forum.model.Topic;
import br.inpe.forum.model.User;
import java.util.List;

/**
 * 
 * update object
 * 
 * db.topics.update( { "_id":"Test topic"}, { $set : {
 * "comments.$[elem].description" : "FUCKING DESCRIPTION"} }, { arrayFilters :
 * [{"elem.description":"First comment!"}], upsert:false } );
 * 
 */
@Repository
public interface MongoTopicRepository extends MongoRepository<Topic, String> {

	Optional<Topic> findByTitle(String title);

	void deleteByTitle(String title);

	boolean existsByTitle(String title);
	
	List<Topic> findByUser(User user);

}
