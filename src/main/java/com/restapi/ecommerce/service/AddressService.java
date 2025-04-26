package com.restapi.ecommerce.service;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.payload.AddressDTO;

public interface AddressService {
	AddressDTO addAddress(AddressDTO address, User user);
}
