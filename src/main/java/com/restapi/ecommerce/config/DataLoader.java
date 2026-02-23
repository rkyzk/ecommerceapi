package com.restapi.ecommerce.config;

//@Component
//public class DataLoader implements CommandLineRunner {
//		private final UserRepository userRepository;
//		private final RoleRepository roleRepository;
//		private final PasswordEncoder passwordEncoder;
//
//		public DataLoader(UserRepository userRepository,
//				RoleRepository roleRepository,
//				PasswordEncoder passwordEncoder) {
//			this.userRepository = userRepository;
//			this.roleRepository = roleRepository;
//			this.passwordEncoder = passwordEncoder;
//		}
//
//	@Override
//	public void run(String... args) throws Exception {
//            // Retrieve or create roles
//            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
//                    .orElseGet(() -> {
//                        Role newUserRole = new Role(AppRole.ROLE_USER);
//                        return roleRepository.save(newUserRole);
//                    });
//
//            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
//                    .orElseGet(() -> {
//                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
//                        return roleRepository.save(newSellerRole);
//                    });
//
//            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
//                    .orElseGet(() -> {
//                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
//                        return roleRepository.save(newAdminRole);
//                    });
//
//            Set<Role> userRoles = Set.of(userRole);
//            Set<Role> sellerRoles = Set.of(sellerRole);
//            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);
//
//
//            // Create users if not already present
//            if (!userRepository.existsByUsername("user1")) {
//                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
//                userRepository.save(user1);
//            }
//
//            if (!userRepository.existsByUsername("seller1")) {
//                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
//                userRepository.save(seller1);
//            }
//
//            if (!userRepository.existsByUsername("admin")) {
//                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
//                userRepository.save(admin);
//            }
//
//            // Update roles for existing users
//            userRepository.findByUsername("user1").ifPresent(user -> {
//                user.setRoles(userRoles);
//                userRepository.save(user);
//            });
//
//            userRepository.findByUsername("seller1").ifPresent(seller -> {
//                seller.setRoles(sellerRoles);
//                userRepository.save(seller);
//            });
//
//            userRepository.findByUsername("admin").ifPresent(admin -> {
//                admin.setRoles(adminRoles);
//                userRepository.save(admin);
//            });
//        };
//}
//
