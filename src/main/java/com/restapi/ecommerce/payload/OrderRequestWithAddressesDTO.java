package com.restapi.ecommerce.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestWithAddressesDTO {
	@NotNull
	private AddressDTO shippingAddressDTO;
	private AddressDTO billingAddressDTO;
	@NotNull
	private CartDTO cartDTO;
	@NotNull
	private String pgName;
	@NotNull
    private String pgPaymentId;
	@NotNull
    private String pgStatus;
	@NotNull
    private String pgResponseMessage;
}
