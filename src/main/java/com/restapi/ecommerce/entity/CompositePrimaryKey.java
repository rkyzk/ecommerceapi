package com.restapi.ecommerce.entity;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Composite primary key for sales_count table
 * @author reikoyazaki
 *
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CompositePrimaryKey implements Serializable {
	private Product product;
	private LocalDate date;
}
