package com.restapi.ecommerce.service;

import com.restapi.ecommerce.payload.StripePaymentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {
	PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDto) throws StripeException;
}
