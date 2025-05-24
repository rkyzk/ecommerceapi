package com.restapi.ecommerce.payload;

import com.restapi.ecommerce.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
	private Long addressId;
	@NotBlank
	private String fullname;
	private boolean billingAddress;
	@NotBlank
	private String streetAddress1;
	private String streetAddress2;
	@NotBlank
	private String city;
	@NotBlank
	private String province;
	@NotBlank
	private String countryCode;
	@NotBlank
	private String postalCode;
	private User user;
}
