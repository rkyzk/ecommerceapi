package com.restapi.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.entity.Address;
import com.restapi.ecommerce.entity.Order;
import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.exceptions.ResourceNotFoundException;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.repository.AddressRepository;
import com.restapi.ecommerce.repository.OrderRepository;

import jakarta.transaction.Transactional;

/** address service implementation */
@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Value("${msg.address.service001}")
	private String msg001;

	/**
	 * add an address
	 */
	@Override
	public AddressDTO addAddress(AddressDTO addressDTO, User user) {
		if (user != null) addressDTO.setUser(user);
		Address address = modelMapper.map(addressDTO, Address.class);
		address.setUpdateDate(LocalDateTime.now());
		Address savedAddress = addressRepository.save(address);
		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	/**
	 * get a list of given user's addresses
	 */
	@Override
	public List<AddressDTO> getUserAddresses(User user) {
		List<Address> addresses = addressRepository
				.findByUserUserIdOrderByShippingAddressDescDefaultAddressFlgDescUpdateDateDesc(user.getUserId());
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
	@Transactional
	@Override
	public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
		Address addressInDB = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		addressInDB.setFullname(addressDTO.getFullname());
		addressInDB.setDefaultAddressFlg(addressDTO.isDefaultAddressFlg());
		addressInDB.setShippingAddress(addressDTO.isShippingAddress());
		addressInDB.setStreetAddress1(addressDTO.getStreetAddress1());
		addressInDB.setStreetAddress2(addressDTO.getStreetAddress2());
		addressInDB.setStreetAddress3(addressDTO.getStreetAddress3());
		addressInDB.setCity(addressDTO.getCity());
		addressInDB.setPrefecture(addressDTO.getPrefecture());
		addressInDB.setPostalCode(addressDTO.getPostalCode());
		addressInDB.setUpdateDate(LocalDateTime.now());
		Address updatedAddress = addressRepository.save(addressInDB);
		// If defaultAddressFlg is true, update the old default address by setting the flag false.
		if (addressDTO.isDefaultAddressFlg() == true) {
			Long userId = addressDTO.getUser().getUserId();
			Boolean sAddr = addressDTO.isShippingAddress();
			Address oldDefaultAddress = sAddr ?
					addressRepository.findByUserUserIdAndShippingAddressIsTrueAndDefaultAddressFlgIsTrue(userId) :
					addressRepository.findByUserUserIdAndShippingAddressIsFalseAndDefaultAddressFlgIsTrue(userId);
			if (oldDefaultAddress != null) {
				oldDefaultAddress.setDefaultAddressFlg(false);
				addressRepository.save(oldDefaultAddress);
			}
		}
		return modelMapper.map(updatedAddress, AddressDTO.class);
	}

	/**
	 * Delete address.
	 * If the address is present in the orders table,
	 * the address won't be deleted but user id will be set to null
	 * 
	 */
	@Override
	public String deleteAddress(Long addressId) {
		Address address = addressRepository.findByAddressId(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		// check if the address is present in the orders table
		List<Order> orderList = orderRepository
				.findByShippingAddressAddressIdOrBillingAddressAddressId(addressId, addressId);
		if (orderList.size() == 0) {
		    addressRepository.deleteByAddressId(addressId);
		} else {
			address.setUser(null);
			addressRepository.save(address);
		}
		return msg001 + addressId;
	}
}
