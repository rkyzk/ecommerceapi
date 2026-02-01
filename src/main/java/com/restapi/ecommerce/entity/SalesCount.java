package com.restapi.ecommerce.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CompositePrimaryKey.class)
@Entity
public class SalesCount {
	@ToString.Exclude
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "product_id")
	@Id
	private Product product;

    @Id
    private LocalDate date;

    private int quantity;
}
