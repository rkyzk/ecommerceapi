package com.restapi.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product_color")
@Getter
@Setter
@NoArgsConstructor
public class ProductColor {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private Long color;
}
