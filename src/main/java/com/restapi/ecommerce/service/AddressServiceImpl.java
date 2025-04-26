package com.restapi.ecommerce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public AddressDTO addAddress(AddressDTO addressDTO, User user) {
		addressDTO.setUser(user);
		Address address = modelMapper.map(addressDTO, Address.class);
		Address savedAddress = addressRepository.save(address);
		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	@Override
	public List<AddressDTO> getUserAddresses(User user) {
		List<Address> addresses = user.getAddresses();
		return addresses.stream()
				.map(address -> modelMapper.map(address, AddressDTO.class))
								.toList();
	}
}
