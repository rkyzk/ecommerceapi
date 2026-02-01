package com.restapi.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.CompositePrimaryKey;
import com.restapi.ecommerce.entity.SalesCount;

@Repository
public interface SalesCountRepository extends JpaRepository<SalesCount, CompositePrimaryKey>{
	Optional<SalesCount> findById(CompositePrimaryKey compositePrimaryKey);

}
