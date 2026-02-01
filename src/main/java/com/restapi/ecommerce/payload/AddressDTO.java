package com.restapi.ecommerce.payload;

import java.time.LocalDateTime;

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
	private boolean defaultAddressFlg;
	private boolean shippingAddress;
	@NotBlank
	private String streetAddress1;
	@NotBlank
	private String streetAddress2;
	private String streetAddress3;
	@NotBlank
	private String city;
	@NotBlank
	private String prefecture;
	@NotBlank
	private String postalCode;
	private User user;
	private LocalDateTime updateDate;
}
