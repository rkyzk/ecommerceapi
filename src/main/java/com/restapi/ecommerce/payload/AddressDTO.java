package com.restapi.ecommerce.payload;

import com.restapi.ecommerce.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
	private Long addressId;
	private String streetAddress1;
	private String streetAddress2;
	private String city;
	private String province;
	private String countryCode;
	private String postalCode;
	private User user;
}
