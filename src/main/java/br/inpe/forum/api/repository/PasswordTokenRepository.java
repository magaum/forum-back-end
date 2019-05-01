package br.inpe.forum.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.inpe.forum.model.PasswordResetToken;

@Repository
public interface PasswordTokenRepository extends MongoRepository<PasswordResetToken, String> {

	Optional<PasswordResetToken> findByToken(String token);
}
