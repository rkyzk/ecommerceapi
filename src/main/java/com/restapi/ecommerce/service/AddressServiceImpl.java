package com.restapi.ecommerce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.repository.AddressRepository;
import com.restapi.ecommerce.repository.OrderRepository;

/** address service implementation */
@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	OrderRepository orderRepository;

	/**
	 * add an address
	 */
	@Override
	public AddressDTO addAddress(AddressDTO addressDTO, User user) {
		if (user != null) addressDTO.setUser(user);
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
		addressInDB.setFullname(addressDTO.getFullname());
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
	 * If orders table has the address, set user id = null
	 */
	@Override
	public String deleteAddress(Long addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		// check if orders table has the address
		List<Order> orderList = orderRepository.findByBillingAddressId(addressId);
		if (orderList.size() == 0) {
		    addressRepository.deleteById(addressId);
		} else {
			address.setUser(null);
			addressRepository.save(address);
		}
		return "Address (address ID: " + addressId + ") was successfully deleted.";
	}
}
