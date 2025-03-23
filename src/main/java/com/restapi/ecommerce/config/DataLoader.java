package com.restapi.ecommerce.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.restapi.ecommerce.entity.Role;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.enums.AppRole;
import com.restapi.ecommerce.repository.RoleRepository;
import com.restapi.ecommerce.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
		private final UserRepository userRepository;
		private final RoleRepository roleRepository;
		private final PasswordEncoder passwordEncoder;

		public DataLoader(UserRepository userRepository,
				RoleRepository roleRepository,
				PasswordEncoder passwordEncoder) {
			this.userRepository = userRepository;
			this.roleRepository = roleRepository;
			this.passwordEncoder = passwordEncoder;
		}

	@Override
	public void run(String... args) throws Exception {
    	Role userRole = new Role(AppRole.ROLE_USER);
        roleRepository.save(userRole);

        Role sellerRole = new Role(AppRole.ROLE_SELLER);
        roleRepository.save(sellerRole);

        Role adminRole = new Role(AppRole.ROLE_ADMIN);
        roleRepository.save(adminRole);

        Set<Role> userRoles = Set.of(userRole);
        Set<Role> sellerRoles = Set.of(sellerRole);
        Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

        User user1 = new User("user1", "user1@example.com",
        		passwordEncoder.encode("password1"));
        userRepository.save(user1);

        User seller1 = new User("seller1", "seller1@example.com",
        		passwordEncoder.encode("password2"));
        userRepository.save(seller1);

        User admin = new User("admin", "admin@example.com",
        		passwordEncoder.encode("adminPass"));
        userRepository.save(admin);
        
        user1.setRoles(userRoles);
        userRepository.save(user1);
        seller1.setRoles(sellerRoles);
        userRepository.save(seller1);
        admin.setRoles(adminRoles);
        userRepository.save(admin);
    }
}

