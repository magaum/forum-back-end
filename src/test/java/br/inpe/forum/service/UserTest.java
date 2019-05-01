package br.inpe.forum.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.inpe.forum.api.repository.MongoUserRepository;
import br.inpe.forum.api.service.UserService;
import br.inpe.forum.model.User;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

	@Autowired
	private UserService service;
	@Autowired
	private MongoUserRepository repository;
	private User mockuser;

	@Before
	public void setUp() {
		mockuser = new User("testUser", "weslei", "123");
	}

	@After
	public void tearDown() {
		mockuser = null;
	}

	@Test
	public void signupTest() throws NotFoundException {

		User response = service.insert(mockuser);
		assertTrue(Objects.nonNull(response));
		Optional<User> u = repository.findByUsername(mockuser.getUsername());
		service.delete(u.get().getObjectId());
		Optional<User> user = repository.findById(mockuser.getUsername());
		assertFalse(user.isPresent());
	}

	@Test
	public void usersTest() {

		service.insert(mockuser);
		List<User> users = repository.findAll();
		assertTrue(users.size() >= 1);
		repository.delete(mockuser);

	}

	@Test
	public void updateUserTest() throws NotFoundException {

		service.insert(mockuser);
		User actualUser = new User("testUser", "ielsew", "123");
		Optional<User> oldUser = repository.findByUsername(mockuser.getUsername());
		service.update(oldUser.get().getObjectId(), actualUser);
		Optional<User> userFound = repository.findByUsername(actualUser.getUsername());
		assertTrue(userFound.isPresent());
		assertNotEquals("User was not updated", mockuser.getUsername(), userFound.get());
		repository.delete(actualUser);

	}

}
