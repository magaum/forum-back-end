package br.inpe.forum.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.inpe.forum.model.User;
@Repository
public interface MongoUserRepository extends MongoRepository<User, String> {

	@Query(value = "{_id : ?0}", fields = "{'password':0}")
	Optional<User> findByIdWithoutPassword(String id);
	
	Optional<User> findByUsername(String username);

}
