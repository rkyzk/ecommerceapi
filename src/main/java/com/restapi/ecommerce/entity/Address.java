package com.restapi.ecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
	private String fullname;

	private boolean defaultAddressFlg;

	private boolean shippingAddress;

	@NotBlank
	@Size(min = 2)
	private String streetAddress1;

	@NotBlank
	@Size(min = 2)
	private String streetAddress2;

	private String streetAddress3;

	@NotBlank
	@Size(min = 2)
	private String city;

	@NotBlank
	@Size(min = 2)
	private String prefecture;

	@NotBlank
	@Size(min = 2)
	private String postalCode;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDateTime updateDate;

	public Address(User user, String fullname, String streetAddress1, String streetAddress2,
			String streetAddress3, String city, String prefecture, String postalCode) {
		this.user= user;
		this.fullname = fullname;
		this.defaultAddressFlg = defaultAddressFlg;
		this.shippingAddress = shippingAddress;
		this.streetAddress1 = streetAddress1;
		this.streetAddress2 = streetAddress2;
		this.streetAddress3 = streetAddress3;
		this.city = city;
		this.prefecture = prefecture;
		this.postalCode = postalCode;
	}
}
