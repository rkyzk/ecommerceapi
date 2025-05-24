package com.restapi.ecommerce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.repository.AddressRepository;

/** address service implementation */
@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AddressRepository addressRepository;

	/**
	 * add a address
	 */
	@Override
	public AddressDTO addAddress(AddressDTO addressDTO, User user) {
		addressDTO.setUser(user);
		Address address = modelMapper.map(addressDTO, Address.class);
		Address savedAddress = addressRepository.save(address);
		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	/**
	 * get a list of given user's addresses
	 */
	@Override
	public List<AddressDTO> getUserAddresses(User user) {
		List<Address> addresses = user.getAddresses();
		return addresses.stream()
				.map(address -> modelMapper.map(address, AddressDTO.class))
								.toList();
	}

	/**
	 * get address by address id
	 */
	@Override
	public AddressDTO getAddress(Long addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		return modelMapper.map(address, AddressDTO.class);
	}

	/**
	 * update address
	 */
	@Override
	public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
		Address addressInDB = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		addressInDB.setStreetAddress1(addressDTO.getStreetAddress1());
		addressInDB.setStreetAddress2(addressDTO.getStreetAddress2());
		addressInDB.setCity(addressDTO.getCity());
		addressInDB.setProvince(addressDTO.getProvince());
		addressInDB.setCountryCode(addressDTO.getCountryCode());
		addressInDB.setPostalCode(addressDTO.getPostalCode());
		Address updatedAddress = addressRepository.save(addressInDB);
		return modelMapper.map(updatedAddress, AddressDTO.class);
	}

	/**
	 * delete address
	 */
	@Override
	public String deleteAddress(Long addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		addressRepository.delete(address);
		return "Address (address ID: " + addressId + ") was successfully deleted.";
	}
}
