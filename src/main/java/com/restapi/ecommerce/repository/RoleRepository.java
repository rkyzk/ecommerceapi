package com.restapi.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Role;
import com.restapi.ecommerce.enums.AppRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(AppRole role);
	Optional<Role> save(AppRole role);
}
