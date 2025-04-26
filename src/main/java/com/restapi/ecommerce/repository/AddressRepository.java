package com.restapi.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
