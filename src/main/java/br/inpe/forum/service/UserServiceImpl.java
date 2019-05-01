package br.inpe.forum.service;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.inpe.forum.api.repository.MongoUserRepository;
import br.inpe.forum.api.repository.PasswordTokenRepository;
import br.inpe.forum.api.service.UserService;
import br.inpe.forum.exception.NotFoundException;
import br.inpe.forum.model.PasswordResetToken;
import br.inpe.forum.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private MongoUserRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private PasswordTokenRepository passwordTokenRepository;

	@Override
	public User insert(User user) {

		Optional<User> userOptional = repository.findByUsername(user.getUsername());

		if (userOptional.isPresent())
			throw new ConstraintViolationException("username already exists", null);

		user.setPassword(encoder.encode(user.getPassword()));
		return repository.save(user);

	}

	@Override
	public void delete(String userId) {

		Optional<User> optionalUser = repository.findById(userId);

		if (optionalUser.isPresent()) {
			repository.deleteById(userId);

		} else {
			throw new NotFoundException("User not found");
		}
	}

	@Override
	public User findUserById(String id) {

		Optional<User> optionalUser = repository.findByIdWithoutPassword(id);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user;
		}

		throw new NotFoundException("User not found");
	}

	@Override
	public List<User> findUsers() {
		Query query = new Query();
		query.fields().exclude("password");
		mongoTemplate.find(query, User.class);
		return repository.findAll();
	}

	@Override
	public User update(String userId, User actualUser) {
		Optional<User> olduser = repository.findById(userId);
		if (olduser.isPresent()) {
			actualUser.setObjectId(olduser.get().getObjectId());
			return repository.save(actualUser);
		}
		throw new NotFoundException("User not found");
	}

	@Override
	public User findUserByUsername(String username) {
		Optional<User> optionalUser = repository.findByUsername(username);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}
		throw new NotFoundException("User not found");
	}

//	@Autowired
	private JavaMailSender mailSender;

	public void createPasswordResetTokenForUser(String username, String token) {
		Optional<User> user = repository.findByUsername(username);
		if (!user.isPresent()) {
			throw new NotFoundException();
		}
		PasswordResetToken myToken = new PasswordResetToken(token, user.get());
		myToken.setId(ObjectId.get().toString());
		passwordTokenRepository.save(myToken);

		mailSender.send(constructEmail(token, user.get()));
	}

	private SimpleMailMessage constructEmail(String token, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		StringBuffer buffer = new StringBuffer();
		buffer.append("https://forum-tg.netlify.com/users/");
		buffer.append(user.getUsername());
		buffer.append("/change/password/");
		buffer.append(token);

		email.setSubject("Reset Password");
		email.setText("Clique no link para resetar sua senha: " + buffer.toString());
		email.setTo(user.getEmail());
		return email;
	}

	@Override
	public User changeUserPassword(String token, String password) {

		Optional<PasswordResetToken> optionalToken = passwordTokenRepository.findByToken(token);
		if (!optionalToken.isPresent()) {
			throw new NotFoundException("Invalid token");
		}
		PasswordResetToken requestToken = optionalToken.get();
		User user = requestToken.getUser();
		
		user.setPassword(encoder.encode(password));
		passwordTokenRepository.delete(requestToken);

		return update(user.getObjectId(), user);

	}

}
