package com.restapi.ecommerce.service;

import java.time.LocalDateTime;
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

import jakarta.transaction.Transactional;

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
		// defaultAddressFlgがtrueだったら今までtrueだったアドレスのフラグをfalseに更新する
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
	 * delete address
	 * If orders table has the address, set user id = null
	 * 
	 * 住所を削除。注文テーブルに住所が存在する場合、レコードを物理削除せず
	 * ユーザIDをnullに更新する。
	 */
	@Override
	public String deleteAddress(Long addressId) {
		Address address = addressRepository.findByAddressId(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
		// check if orders table has the address
		List<Order> orderList = orderRepository.findByShippingAddressAddressIdOrBillingAddressAddressId(addressId, addressId);
		if (orderList.size() == 0) {
		    addressRepository.deleteByAddressId(addressId);
		} else {
			address.setUser(null);
			addressRepository.save(address);
		}
		return "Address (address ID: " + addressId + ") was successfully deleted.";
	}
}
