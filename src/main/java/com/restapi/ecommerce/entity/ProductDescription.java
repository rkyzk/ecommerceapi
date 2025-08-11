package com.restapi.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_description")
@Getter
@Setter
@NoArgsConstructor
public class ProductDescription {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name="product_id")
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Product product;

	@Size(min=2, max=20)
	private String key;
	@Size(min=2, max=300)
	private String value;

	public ProductDescription(Product product, String key, String value) {
		this.product = product;
		this.key = key;
		this.value = value;
	}
}
