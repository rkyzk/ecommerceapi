package com.restapi.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long paymentId;

	@OneToOne
	@JoinColumn(name="order_id")
	private Order order;

	private String pgPaymentId;
	private String pgStatus;
	private String pgResponseMessage;
	private String pgName;

	public Payment(String pgPaymentId,
			String pgStatus, String pgResponseMessage,
			String pgName) {
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
		this.pgName = pgName;
	}
}
