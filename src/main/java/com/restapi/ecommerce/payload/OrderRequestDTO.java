package com.restapi.ecommerce.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
	private Long shippingAddressId;
	private Long billingAddressId;
	@NotNull
	private CartDTO cartDTO;
	private String pgName;
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
}
