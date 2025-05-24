package com.restapi.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restapi.ecommerce.payload.StripePaymentDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class StripeServiceImpl implements StripeService {
	@Value("${stripe.secret.key}")
	private String stripeApiKey;

	@PostConstruct // the method will run after the bean is created.
	public void init() {
		Stripe.apiKey = this.stripeApiKey;
	}

    public PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDto) throws StripeException {
        PaymentIntentCreateParams params =
	            PaymentIntentCreateParams.builder()
	                .setAmount(stripePaymentDto.getAmount())
	                .setCurrency(stripePaymentDto.getCurrency())
	                .setAutomaticPaymentMethods(
	                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
	                        .setEnabled(true)
	                        .build())
	                .build();
	    return PaymentIntent.create(params);
	}
}
