package com.restapi.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByUserUserIdAndBillingAddressIsFalse(Long userId);
	Address findByUserUserIdAndBillingAddressIsTrue(Long userId);
	void deleteById(Long addressId);
}
