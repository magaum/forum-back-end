package br.inpe.forum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.inpe.forum.api.repository.MongoUserRepository;
import br.inpe.forum.config.UserDetailsImpl;
import br.inpe.forum.model.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private MongoUserRepository mongoUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> userOptional = mongoUserRepository.findByUsername(username);
		UserDetails userDetails;

		if (userOptional.isPresent()) {
			userDetails = new UserDetailsImpl(userOptional.get());
		} else
			userDetails = null;

		return userDetails;
	}

}
