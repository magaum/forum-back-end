package br.inpe.forum.api.service;

import java.util.List;

import br.inpe.forum.model.User;

public interface UserService {

	User insert(User user);

	void delete(String userId);

	User findUserById(String id);

	User findUserByUsername(String username);

	List<User> findUsers();

	User update(String userId, User oldUser);

	User changeUserPassword(String token, String password);

	void createPasswordResetTokenForUser(String username, String token);

}
