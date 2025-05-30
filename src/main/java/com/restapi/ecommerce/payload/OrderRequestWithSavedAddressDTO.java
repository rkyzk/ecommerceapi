package com.restapi.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestWithSavedAddressDTO {
	private Long addressId;
	private String pgName;
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
}
