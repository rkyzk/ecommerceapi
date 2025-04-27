package com.restapi.ecommerce.payload;

import lombok.Data;

@Data
public class StripePaymentDTO {
	private Long amount;
	private String currency;
}
