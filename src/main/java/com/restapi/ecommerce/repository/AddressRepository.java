package com.restapi.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByUserUserIdAndShippingAddressIsTrue(Long userId);
	Optional<Address> findByAddressId(Long addressId);
	Address findByUserUserIdAndShippingAddressIsFalse(Long userId);
	Address findByUserUserIdAndShippingAddressIsTrueAndDefaultAddressFlgIsTrue(Long userId);
	Address findByUserUserIdAndShippingAddressIsFalseAndDefaultAddressFlgIsTrue(Long userId);
	List<Address> findByUserUserIdOrderByShippingAddressDescDefaultAddressFlgDescUpdateDateDesc(Long userId);
	void deleteByAddressId(Long addressId);
}
