package com.restapi.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Table(name = "addresses")
@Setter
@Getter
@ToString
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@NotBlank
	@Size(min = 2)
	private String streetAddress1;

	private String streetAddress2;

	@NotBlank
	@Size(min = 2)
	private String city;

	@NotBlank
	@Size(min = 2)
	private String province;

	@NotBlank
	private String countryCode;

	@NotBlank
	@Size(min = 2)
	private String postalCode;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;

	public Address(String streetAddress1, String streetAddress2,
			String city, String province, String countryCode, String postalCode) {
		this.user= user; 
		this.streetAddress1 = streetAddress1;
		this.streetAddress2 = streetAddress2;
		this.city = city;
		this.province = province;
		this.countryCode = countryCode;
		this.postalCode = postalCode;
	}
}
