package com.restapi.ecommerce.security.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.repository.UserRepository;

/** User details service class */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Value("${msg.userdetails.service001}")
    private String msg001;

    /** get user details by username */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(msg001 + username));
        		// "User is not found. Username: "
        return UserDetailsImpl.build(user);
    }

}
