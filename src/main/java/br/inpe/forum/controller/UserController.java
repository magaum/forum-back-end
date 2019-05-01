package br.inpe.forum.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inpe.forum.api.service.UserService;
import br.inpe.forum.model.User;

/**
 * 
 * @author weslei
 *
 */

@Validated
@RestController
@RequestMapping(path = "users", produces = "application/json")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Find users by Id
	 * 
	 * @param id
	 * @return {@link ResponseEntity}
	 */

	@GetMapping(path = "{userId}")
	public ResponseEntity<?> profile(@PathVariable String userId) {
		User user = userService.findUserById(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/**
	 * Find users by Username
	 * 
	 * @param username
	 * @return {@link ResponseEntity}
	 */

	@GetMapping(path = "username/{username}")
	public ResponseEntity<?> username(@PathVariable String username) {
		User user = userService.findUserByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/**
	 * Find users by Id
	 * 
	 * @return {@link ResponseEntity}
	 * 
	 */

	@GetMapping
	public ResponseEntity<?> users() {
		List<User> users = userService.findUsers();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/**
	 * Create users
	 * 
	 * @param {@link User}
	 * @param {@link Errors}
	 * @return {@link ResponseEntity}
	 */
	@PostMapping(path = "signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody User user) {
		User u = userService.insert(user);
		return new ResponseEntity<User>(u, HttpStatus.CREATED);
	}

	/**
	 * Update user
	 * 
	 * @param user
	 * @return
	 */
	@PutMapping(path = "{userId}")
	public ResponseEntity<?> update(@PathVariable String userId, @RequestBody User user) {
		User u = userService.update(userId, user);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}

	/**
	 * Remove user from database
	 * 
	 * @param username
	 * @param password
	 * @return
	 */

	@DeleteMapping(path = "{userId}", produces = "application/json")
	public ResponseEntity<?> deleteUser(@PathVariable String userId) {
		userService.delete(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping(value = "reset/password/{username}")
	public ResponseEntity<?> resetPassword(HttpServletRequest request, @PathVariable("username") String username) {
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(username, token);
		return new ResponseEntity<String>("Email send for password change", HttpStatus.OK);
	}

	@PostMapping(value = "{token}/change/password/{password}")
	public ResponseEntity<?> savePassword(@PathVariable String token,
			@PathVariable String password) {

		userService.changeUserPassword(token, password);
		return new ResponseEntity<String>("Password changed", HttpStatus.OK);
	}

}
