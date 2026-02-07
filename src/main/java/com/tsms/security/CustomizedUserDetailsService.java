package com.tsms.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsms.entity.User;
import com.tsms.repository.UserRepository;




@Service
public class CustomizedUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isPresent()) {
			Collection<String> authorities = new ArrayList<>();
			authorities.add(user.get().getRole().name());
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.get().getEmail(),
					user.get().getPassword(), getAuthorities(authorities));
			return userDetails;
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<String> authorities) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String role : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role));
		}
		return grantedAuthorities;
	}

	public Optional<User> getUserDetails() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<User> user = userRepository.findByEmail(auth.getName());
		
			return user;
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}

