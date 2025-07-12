package com.restapi.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.payload.AddressDTO;
import com.restapi.ecommerce.service.AddressService;
import com.restapi.ecommerce.utils.AuthUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {
	@Autowired
	AddressService addressService;

	@Autowired
	AuthUtil authUtil;

	/**
	 * add an address in addresses table
	 * and return the address
	 * 
	 * @param addressDTO
	 * @return addressDTO
	 */
	@PostMapping("/addresses")
	public ResponseEntity<AddressDTO> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
		User user = authUtil.loggedinUser();
		AddressDTO addedDTO = addressService.addAddress(addressDTO, user);
		return new ResponseEntity<AddressDTO>(addedDTO, HttpStatus.CREATED);
	}

	/**
	 * add an address in addresses table without user data
	 * and return the address
	 * 
	 * @param addressDTO
	 * @return addressDTO
	 */
	@PostMapping("/addresses/anonym")
	public ResponseEntity<AddressDTO> addAddressWithoutUser(@Valid @RequestBody AddressDTO addressDTO) {
		AddressDTO addedDTO = addressService.addAddress(addressDTO, null);
		return new ResponseEntity<AddressDTO>(addedDTO, HttpStatus.CREATED);
	}
	

	/**
	 * get address list of the logged in user
	 *
	 * @return list of addresses
	 */
	@GetMapping("/user/addresses")
	public ResponseEntity<List<AddressDTO>> getUserAddresses() {
		User user = authUtil.loggedinUser();
		List<AddressDTO> list = addressService.getUserAddresses(user);
		return new ResponseEntity<> (list, HttpStatus.OK);
	}

	/**
	 * get address by id
	 *
	 * @param addressId
	 * @return addressDTO
	 */
	@GetMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable Long addressId) {
		AddressDTO addedDTO = addressService.getAddress(addressId);
		return new ResponseEntity<> (addedDTO, HttpStatus.OK);
	}

	/**
	 * update address with the given id
	 *
	 * @param addressId
	 * @param addressDTO
	 * @return addressDTO
	 */
	@PutMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
			@Valid @RequestBody AddressDTO addressDTO) {
		AddressDTO addedDTO = addressService.updateAddress(addressId, addressDTO);
		return new ResponseEntity<> (addedDTO, HttpStatus.OK);
	}

	/**
	 * delete address with the given id
	 *
	 * @param addressId
	 * @return
	 */
	@DeleteMapping("/addresses/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
		String status = addressService.deleteAddress(addressId);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
