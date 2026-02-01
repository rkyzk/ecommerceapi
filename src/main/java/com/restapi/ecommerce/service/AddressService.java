package com.restapi.ecommerce.service;

import java.util.List;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.payload.AddressDTO;

import jakarta.transaction.Transactional;

/** address service interface */
public interface AddressService {
	AddressDTO addAddress(AddressDTO address, User user);
	List<AddressDTO> getUserAddresses(User user);

	@Transactional
	AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);
	AddressDTO getAddress(Long addressId);
	String deleteAddress(Long addressId);
}
