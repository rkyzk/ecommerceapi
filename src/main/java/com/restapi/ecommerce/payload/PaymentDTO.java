package com.restapi.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	private Long paymentId;
	private String pgPaymentId;
	private String pgStatus;
	private String pgResponseMessage;
	private String pgName;

	public PaymentDTO(String pgPaymentId, String pgStatus,
			String pgResponseMessage, String pgName) {
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
		this.pgName = pgName;
	};
}
